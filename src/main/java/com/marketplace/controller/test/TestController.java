package com.marketplace.controller.test;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("test")
public class TestController {

    @GetMapping
    public String test(Model model) {
        model.addAttribute("test", new Test());
        return "test";
    }

    @PostMapping
    public String test(@ModelAttribute Test test) {
        System.out.println(test.getV1() + " " + test.getV2());
        return "redirect:/test";
    }
}
