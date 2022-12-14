package com.marketplace.controller.product;

import com.marketplace.service.product.ProductService;
import com.marketplace.service.product.ShopProductInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
        model.addAttribute("product",
                new ProductForm(categoryId, service.getCategoryCharacteristics(categoryId)));

        return "new-product";
    }

//    @PostMapping()
//    public String addProduct(@ModelAttribute ProductInfo product) {
//        System.out.println(product);
//
//        long productId = service.addProduct(product);
//
//        return "redirect:/products/" + productId;
//    }

    @PostMapping()
    public String addProduct(@ModelAttribute ProductForm product) {
        System.out.println(product);


        return "redirect:/categories";
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
