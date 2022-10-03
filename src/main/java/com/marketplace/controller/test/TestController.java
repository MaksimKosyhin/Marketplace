package com.marketplace.controller.test;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("testing")
public class TestController {
    @GetMapping
    public String test(Model model) {
//        model.addAttribute("val1", "hello");
//        model.addAttribute("val",
//                new Test(List.of())
//        );
        model.addAttribute("test", new Test(List.of(new TestValue(List.of("hi", "hello"), "none"))));

        return "test";
    }

    @PostMapping
    public String test(@ModelAttribute Test test) {
        System.out.println(test);
        return "redirect:/testing";
    }

//    private final ProductService service;
//
//    public TestController(ProductService service) {
//        this.service = service;
//    }
//
//    @GetMapping("{categoryId}")
//    public String addProduct(@PathVariable long categoryId, Model model) {
//        model.addAttribute("categoryId", categoryId);
//        model.addAttribute("characteristics", List.of());
//        model.addAttribute("product", new ProductInfo());
//
//        return "test";
//    }
//
//    @PostMapping()
//    public String addProduct(@ModelAttribute ProductInfo product) {
//        System.out.println(product);
//
//
//        return "redirect:/testing/12";
//    }
}
