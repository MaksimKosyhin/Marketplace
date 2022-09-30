package com.marketplace.controller.shop;

import com.marketplace.service.shop.ShopService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("shops")
public class ShopController {
    private final ShopService service;

    public ShopController(ShopService service) {
        this.service = service;
    }

    @GetMapping
    public String getShops(Model model) {
        model.addAttribute("shops", service.getShops());
        return "shops";
    }

    @PostMapping
    public String addShop(@ModelAttribute ShopInfo shop) {
        service.addShop(shop);

        return "redirect:/categories";
    }

    @PutMapping("{shopId}")
    public String removeShop(@PathVariable long shopId) {
        service.removeShop(shopId);

        return "redirect:/categories";
    }
}
