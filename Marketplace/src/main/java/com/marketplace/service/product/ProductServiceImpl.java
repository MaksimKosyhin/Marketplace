package com.marketplace.service.product;

import com.marketplace.config.ImageLoader;
import com.marketplace.config.exception.AddEntryException;
import com.marketplace.config.exception.ModifyingEntryException;
import com.marketplace.config.exception.NonExistingEntityException;
import com.marketplace.repository.product.*;
import com.marketplace.util.ProductMapper;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
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
    public ProductDescription getProduct(long productId) {
        if (!repository.productExists(productId)) {
            throw new NonExistingEntityException(
                    String.format("product with id: %d doesn't exist", productId));
        }

        DbProduct product = repository.getProduct(productId);

        ProductDescription description = mapper.toProductDescription(
                product,
                repository.getProductCharacteristics(productId)
        );

        description.setImgResource(
                imageLoader.findInFileSystem(product.getImgLocation()));

        List<ShopProductDescription> shops = repository.getShopProducts(productId)
                .stream()
                .map(this::toShopProductDescription)
                .collect(Collectors.toList());

        description.setShops(shops);

        return description;
    }

    private ShopProductDescription toShopProductDescription(ShopProduct shopProduct) {
        ShopProductDescription description = mapper.toShopProductDescription(shopProduct);

        description.setResource(
                imageLoader.findInFileSystem(shopProduct.getImgLocation()));

        return description;
    }

    @Transactional
    @Override
    public void addProduct(ProductInfo productInfo) {
        DbProduct dbProduct = mapper.toDbProduct(productInfo);

        String imgLocation;
        try {
            imgLocation = imageLoader.save(productInfo.getImgData(), "product", productInfo.getName());
        } catch (IOException ex) {
            throw new ModifyingEntryException(ex.getMessage());
        }
        dbProduct.setImgLocation(imgLocation);

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
    public void addShopProduct(ShopProduct shopProduct) {
        if (!repository.addShopProduct(shopProduct)) {
            throw new ModifyingEntryException(
                    String.format(
                            "product with id: %d wasn't added to shop with id: %d",
                            shopProduct.getProductId(),
                            shopProduct.getShopId())
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
