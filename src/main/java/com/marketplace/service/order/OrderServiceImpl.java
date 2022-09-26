package com.marketplace.service.order;

import com.marketplace.config.exception.AddEntryException;
import com.marketplace.config.exception.ModifyingEntryException;
import com.marketplace.config.exception.NonExistingEntityException;
import com.marketplace.repository.order.OrderRepository;
import com.marketplace.repository.order.OrderedProduct;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class OrderServiceImpl implements OrderService {

    private final OrderRepository repository;

    public OrderServiceImpl(OrderRepository repository) {
        this.repository = repository;
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
        long orderId = repository.getCurrentOrderId(username);
        return repository.getOrder(orderId);
    }

    @Override
    public Map<LocalDate, List<OrderedProduct>> getAllOrders(String username) {
        long userId = repository.getUserId(username);

        if(userId == -1) {
            throw new NonExistingEntityException(
                    String.format("user with username: %s doesn't exist", username));
        }

        return repository.getAllOrders(userId)
                .stream()
                .collect(
                        () -> new TreeMap<LocalDate, List<OrderedProduct>>(LocalDate::compareTo),
                        this::addOrderedProductToMap,
                        Map::putAll
                );
    }

    private void addOrderedProductToMap(Map<LocalDate, List<OrderedProduct>> map, OrderedProduct product) {
        map.computeIfAbsent(
                        product.getRegistrationDate(),
                        list -> new ArrayList<OrderedProduct>())
                .add(product);
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
