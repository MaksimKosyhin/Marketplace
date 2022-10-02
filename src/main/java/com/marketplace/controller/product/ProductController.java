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
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("characteristics", service.getCategoryCharacteristic(categoryId));
        model.addAttribute("product", new ProductInfo());

        return "new-product";
    }

    @PostMapping()
    public String addProduct(@ModelAttribute ProductInfo info) {
        System.out.println(info);

        long productId = service.addProduct(info);

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
    public String addShopProduct(@ModelAttribute ShopProductInfo shopProduct) {
        System.out.println(shopProduct);

        service.addShopProduct(shopProduct);

        return "redirect:/products/" + shopProduct.getProductId();
    }

    @PutMapping("/{productId}/{shopId}")
    public String removeShopProduct(@PathVariable long productId, @PathVariable long shopId) {
        service.removeShopProduct(productId, shopId);

        return "redirect:/products/" + productId;
    }
}
