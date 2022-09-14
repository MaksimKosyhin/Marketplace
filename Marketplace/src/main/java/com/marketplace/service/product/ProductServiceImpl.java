package com.marketplace.service.product;

import com.marketplace.config.exception.AddEntryException;
import com.marketplace.config.exception.ModifyingEntryException;
import com.marketplace.config.exception.NonExistingEntityException;
import com.marketplace.repository.product.*;
import com.marketplace.util.ProductMapper;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;
    private final ProductMapper mapper;

    public ProductServiceImpl(ProductRepository repository, ProductMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public ProductDescription getProduct(long productId) {
        if (!repository.productExists(productId)) {
            throw new NonExistingEntityException(
                    String.format("product with id: %d doesn't exist", productId));
        }

        return mapper.toProductDescription(
                repository.getProduct(productId),
                repository.getShopProducts(productId),
                repository.getProductCharacteristics(productId)
        );
    }

    @Transactional
    @Override
    public void addProduct(ProductInfo productInfo) throws IOException {
        DbProduct dbProduct = mapper.toDbProduct(productInfo);

        long productId = repository.addProduct(dbProduct);

        if (productId == -1) {
            throw new AddEntryException("product was not added");
        }

        for (Long characteristicId : productInfo.getCharacteristics()) {
            repository.addCharacteristicToProduct(productId, characteristicId);
        }
    }

    @Override
    public void removeProduct(long productId) {
        if (!repository.removeProduct(productId)) {
            throw new ModifyingEntryException(
                    String.format("product with id: %d was not removed", productId));
        }
    }

    @Override
    public void addShopProduct(ShopProductInfo shopProductInfo) {
        ShopProduct shopProduct = mapper.toShopProduct(shopProductInfo);

        if (!repository.addShopProduct(shopProduct)) {
            throw new ModifyingEntryException(
                    String.format(
                            "product with id: %d wasn't added to shop with id: %d",
                            shopProductInfo.getProductId(),
                            shopProductInfo.getShopId())
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

    @Override
    public void addShop(Shop shop) {
        if (repository.addShop(shop) == -1) {
            throw new AddEntryException("shop was not added");
        }
    }

    @Override
    public void removeShop(long shopId) {
        if (!repository.removeShop(shopId)) {
            throw new ModifyingEntryException(
                    String.format("shop with id: %d was not removed", shopId));
        }
    }
}
