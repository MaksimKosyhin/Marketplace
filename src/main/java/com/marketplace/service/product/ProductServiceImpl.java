package com.marketplace.service.product;

import com.marketplace.config.ImageLoader;
import com.marketplace.config.exception.AddEntryException;
import com.marketplace.config.exception.ModifyingEntryException;
import com.marketplace.config.exception.NonExistingEntityException;
import com.marketplace.controller.product.ProductInfo;
import com.marketplace.repository.product.Product;
import com.marketplace.repository.product.ProductCharacteristic;
import com.marketplace.repository.product.ProductRepository;
import com.marketplace.service.category.CategoryShop;
import com.marketplace.util.ProductMapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;
    private final ProductMapper mapper;
    private final ImageLoader imageLoader;

    public ProductServiceImpl(ProductRepository repository, ProductMapper mapper, ImageLoader imageLoader) {
        this.repository = repository;
        this.mapper = mapper;
        this.imageLoader = imageLoader;
    }


    @Override
    public long getCategoryId(long productId) {
        if(repository.productExists(productId)) {
            return repository.getCategoryId(productId);
        } else {
            throw new NonExistingEntityException(
                    String.format("product with id: %d does not exist", productId));
        }
    }

    @Override
    public ProductDescription getProduct(long productId) {
        if (!repository.productExists(productId)) {
            throw new NonExistingEntityException(
                    String.format("product with id: %d doesn't exist", productId));
        }

        Product product = repository.getProduct(productId);

        ProductDescription description = mapper.toProductDescription(product);

        description.setCharacteristics(repository.getProductCharacteristics(productId));
        description.setShops(repository.getShopProducts(productId));

        return description;
    }

    @Transactional
    @Override
    public long addProduct(ProductInfo info) {
        Product product = mapper.toProduct(info);
        product.setImgLocation(imageLoader.save(info.getImgFile(), "product", info.getName()));

        long productId = repository.addProduct(product);
        if (productId == -1) {
            throw new AddEntryException("product was not added");
        }

        for (ProductCharacteristicMap characteristic: info.getCharacteristics()) {
            repository.addCharacteristicToProduct(productId, characteristic.getIncludedCharacteristic());
        }

        return productId;
    }

    @Override
    public List<ProductCharacteristicMap> getCategoryCharacteristic(long categoryId) {
        return repository.getCategoryCharacteristics(categoryId)
                .stream()
                .collect(
                        Collectors.collectingAndThen(
                                Collectors.groupingBy(ProductCharacteristic::getName),
                                this::mapValues
                        )
                );
    }

    @Override
    public List<CategoryShop> getShops() {
        return repository.getShops();
    }

    private List<ProductCharacteristicMap> mapValues(Map<String, List<ProductCharacteristic>> map) {
        List<ProductCharacteristicMap> characteristics = new ArrayList<>();

        for(Map.Entry<String, List<ProductCharacteristic>> entry: map.entrySet()) {
            characteristics.add(new ProductCharacteristicMap(entry.getValue()));
        }

        return characteristics;
    }

    @Override
    public void removeProduct(long productId) {
        if (!repository.removeProduct(productId)) {
            throw new ModifyingEntryException(
                    String.format("product with id: %d was not removed", productId));
        }
    }

    @Override
    public void addShopProduct(ShopProductInfo info) {
        if (!repository.addShopProduct(info)) {
            throw new ModifyingEntryException(
                    String.format(
                            "product with id: %d wasn't added to shop with id: %d",
                            info.getProductId(),
                            info.getShopId())
            );
        }
    }

    @Override
    public void removeShopProduct(long productId, long shopId) {
        if (repository.removeShopProduct(productId, shopId)) {
            throw new ModifyingEntryException(
                    String.format(
                            "product with id: %d was not removed from shop with id: %d",
                            productId,
                            shopId)
            );
        }
    }
}
