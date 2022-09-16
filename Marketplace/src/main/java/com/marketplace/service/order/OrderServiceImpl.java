package com.marketplace.service.order;

import com.marketplace.config.ImageLoader;
import com.marketplace.config.exception.AddEntryException;
import com.marketplace.config.exception.ModifyingEntryException;
import com.marketplace.repository.order.DbOrderedProduct;
import com.marketplace.repository.order.OrderQuery;
import com.marketplace.repository.order.OrderRepository;
import com.marketplace.util.OrderMapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class OrderServiceImpl implements OrderService {

    private final OrderRepository repository;
    private final OrderMapper mapper;
    private final ImageLoader imageLoader;

    public OrderServiceImpl(OrderRepository repository, OrderMapper mapper, ImageLoader imageLoader) {
        this.repository = repository;
        this.mapper = mapper;
        this.imageLoader = imageLoader;
    }


    @Override
    public void addOrder(String username) {
        long userId = repository.getUserId(username);

        if (repository.addOrder(userId) == -1) {
            throw new AddEntryException(
                    String.format("new order for user: %s was not added", username)
            );
        }
    }

    @Override
    public List<OrderedProduct> getCurrentOrder(String username) {
        long userId = repository.getUserId(username);
        long orderId = repository.getCurrentOrderId(username);

        return repository.getOrder(userId, orderId)
                .stream()
                .map(this::toOrderedProduct)
                .collect(Collectors.toList());
    }

    @Override
    public Map<LocalDate, List<OrderedProduct>> getAllOrders(String username) {
        long userId = repository.getUserId(username);

        return repository.getAllOrders(userId)
                .stream()
                .collect(
                        () -> new TreeMap<LocalDate, List<OrderedProduct>>(LocalDate::compareTo),
                        this::addOrderedProductToMap,
                        Map::putAll
                );
    }

    private void addOrderedProductToMap(Map<LocalDate, List<OrderedProduct>> map, DbOrderedProduct dbProduct) {
        map.computeIfAbsent(
                dbProduct.getRegistrationDate(),
                list -> new ArrayList<OrderedProduct>());

        map.get(dbProduct.getRegistrationDate()).add(toOrderedProduct(dbProduct));
    }

    private OrderedProduct toOrderedProduct(DbOrderedProduct dbProduct) {
        OrderedProduct product = mapper.toOrderedProduct(dbProduct);

        product.setProductImgResource(
                imageLoader.toFileSystemResource(dbProduct.getProductImgLocation()));
        product.setShopImgResource(
                imageLoader.toFileSystemResource(dbProduct.getShopImgLocation()));

        return product;
    }

    @Override
    public void addProductToOrder(OrderQuery orderQuery) {
        if (!repository.addProductToOrder(orderQuery)) {
            throw new AddEntryException("new product was not added to order");
        }
    }

    @Override
    public void deleteProductFromOrder(OrderQuery orderQuery) {
        if (!repository.deleteProductFromOrder(orderQuery)) {
            throw new ModifyingEntryException("product was not deleted from order");
        }
    }

    @Override
    public void changeProductAmount(OrderQuery orderQuery) {
        if (!repository.changeProductAmount(orderQuery)) {
            throw new ModifyingEntryException("product amount was not changes");
        }
    }
}
