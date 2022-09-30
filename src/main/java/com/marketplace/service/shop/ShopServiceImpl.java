package com.marketplace.service.shop;

import com.marketplace.config.ImageLoader;
import com.marketplace.config.exception.AddEntryException;
import com.marketplace.config.exception.ModifyingEntryException;
import com.marketplace.controller.shop.ShopInfo;
import com.marketplace.repository.shop.ShopRepository;

import java.util.List;

public class ShopServiceImpl implements ShopService{
    private final ShopRepository repository;
    private final ImageLoader loader;

    public ShopServiceImpl(ShopRepository repository, ImageLoader loader) {
        this.repository = repository;
        this.loader = loader;
    }

    @Override
    public void addShop(ShopInfo info) {
        Shop shop = new Shop();
        shop.setName(info.getName());
        shop.setImgLocation(loader.save(info.getImgFile(), "shops"));

        if (repository.addShop(shop) == -1) {
            throw new AddEntryException("shop was not added");
        }
    }

    @Override
    public List<Shop> getShops() {
        return repository.getShops();
    }

    @Override
    public void removeShop(long shopId) {
        if (!repository.removeShop(shopId)) {
            throw new ModifyingEntryException(
                    String.format("shop with id: %d was not removed", shopId));
        }
    }
}
