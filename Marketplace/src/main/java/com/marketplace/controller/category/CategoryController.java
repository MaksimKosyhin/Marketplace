package com.marketplace.controller.category;

import com.marketplace.service.category.ProductQuery;
import com.marketplace.service.category.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("categories")
public class CategoryController {
    private static final int NUMBER_OF_ENTRIES = 100;
    private final CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }

    @GetMapping("{parentId}/new")
    public String addCategory(@PathVariable long parentId, Model model) {
//        model.addAttribute("category", new CategoryInfo(parentId));
        return "new-category";
    }

    @PostMapping
    public String addCategory(@ModelAttribute CategoryInfo categoryInfo) {
        service.addCategory(categoryInfo);
        return "redirect:/categories" + categoryInfo.getParentId();
    }

    @PutMapping("{categoryId}")
    public String removeCategory(@PathVariable long categoryId) {
        service.removeCategory(categoryId);
        return "redirect:/categories";
    }

    @GetMapping(value = {"", "{categoryId}"})
    public String getCategories(@PathVariable(required = false) Long categoryId,
                                RedirectAttributes attributes, Model model) {
        categoryId = categoryId == null ? 1 : categoryId;

        if(service.isParentCategory(categoryId)) {
            model.addAttribute("categoryId", categoryId);
            model.addAttribute("categories", service.getCategories(categoryId));
            return "categories";
        } else {
            ProductQuery query = new ProductQuery(
                    categoryId,
                    NUMBER_OF_ENTRIES,
                    service.getCharacteristicsId(categoryId)
            );
            attributes.addAttribute(query);

            return "redirect:/categories/products";
        }
    }

    @GetMapping("/products")
    public String getProducts(@RequestParam ProductQuery query, Model model) {
        model.addAttribute("products", service.getProducts(query));
        model.addAttribute("characteristics", service.getCharacteristics(query.getCategoryId()));

        return "products";
    }

    @GetMapping("shops")
    public String getShops(@RequestParam long categoryId, Model model) {
        model.addAttribute("shops", service.getShops());
        return "shops";
    }

    @PostMapping("shops")
    public String addShopToCategory(@RequestParam CategoryShops shops) {
        service.addShopsToCategory(shops);
        return "redirect:/";
    }
}
