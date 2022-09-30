package com.marketplace.controller.product;

import com.marketplace.service.product.ProductCharacteristicMap;
import com.marketplace.service.product.ProductService;
import com.marketplace.service.product.ShopProductInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("products")
public class ProductController {
    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping("{productId}")
    public String getProduct(@PathVariable long productId, Model model) {
        model.addAttribute("product", service.getProduct(productId));
        return "product";
    }

    @GetMapping("{categoryId}/new")
    public String addProduct(@PathVariable long categoryId, Model model) {

        ProductInfo info = new ProductInfo();
        info.setCategoryId(categoryId);
        info.setCharacteristics(service.getCategoryCharacteristic(categoryId));

        model.addAttribute("product", info);

        return "new-product";

//        model.addAttribute("categoryId", categoryId);
//        model.addAttribute("product", new ProductInfo());
//        model.addAttribute("characteristics", service.getCategoryCharacteristic(categoryId));
//
//        return "new-product";
    }

    @PostMapping()
    public String addProduct(
            @ModelAttribute ProductInfo product) {

        long productId = service.addProduct(product);

        return "redirect:/products/" + productId;
    }

    @PutMapping("{productId}")
    public String removeProduct(@PathVariable long productId) {
        service.removeProduct(productId);

        return "redirect:/categories/" + service.getCategoryId(productId);
    }

    @GetMapping("/{productId}/shop-products")
    public String getShops(@PathVariable long productId, Model model) {
        model.addAttribute("productId");
        model.addAttribute("shops", service.getShops());
        model.addAttribute("shopProduct", new ShopProductInfo());

        return "new-shop-product";
    }

    @PostMapping("shop-products")
    public String addShop(@ModelAttribute ShopProductInfo shopProduct) {
        service.addShopProduct(shopProduct);

        return "redirect:/products/" + shopProduct.getProductId();
    }

    @PutMapping("/{productId}/{shopId}")
    public String removeShopProduct(@PathVariable long productId, @PathVariable long shopId) {
        service.removeShopProduct(productId, shopId);

        return "redirect:/products/" + productId;
    }
}
