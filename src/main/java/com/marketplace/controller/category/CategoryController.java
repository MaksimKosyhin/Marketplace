package com.marketplace.controller.category;

import com.marketplace.config.exception.RequestException;
import com.marketplace.repository.category.Characteristic;
import com.marketplace.service.category.CategoryService;
import com.marketplace.service.category.ProductQuery;
import com.marketplace.service.category.CategoryShop;
import com.marketplace.service.category.SortingOption;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("categories")
public class CategoryController {
    public static final int NUMBER_OF_ENTRIES = 100;
    private final CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }

    @GetMapping(value = {"", "{categoryId}"})
    public String getCategories(@PathVariable(required = false) Long categoryId,
                                 Model model) {
        categoryId = categoryId == null ? 1 : categoryId;

        if(service.isParentCategory(categoryId)) {
            model.addAttribute("categoryId", categoryId);
            model.addAttribute("categories", service.getCategories(categoryId));
            return "categories";
        } else {
            return "redirect:/categories/" + categoryId + "/products";
        }
    }

    @GetMapping("{parentId}/new")
    public String addCategory(@PathVariable long parentId, Model model) {
        model.addAttribute("parentId", parentId);
        model.addAttribute("category", new CategoryInfo());
        return "new-category";
    }

    @PostMapping
    public String addCategory(@ModelAttribute CategoryInfo category) {
        System.out.println(category);

        service.addCategory(category);
        return "redirect:/categories/" + category.getParentId();
    }

    @PutMapping("{categoryId}")
    public String removeCategory(@PathVariable long categoryId) {
        service.removeCategory(categoryId);
        return "redirect:/categories";
    }

    @GetMapping("{categoryId}/products")
    public String getProducts(@PathVariable long categoryId,
                              @ModelAttribute ProductQuery query,
                              @ModelAttribute ProductList list,
                              Model model) {

        if (query.isEmpty() && !list.isEmpty()) {
            model.addAttribute("products", service.getProducts(list.getProductsId()));
            model.addAttribute("list", list);
        } else if (!query.isEmpty() && list.isEmpty()) {
            ProductList newList = service.getProductList(query);
            model.addAttribute("products", service.getProducts(newList.getProductsId()));
            model.addAttribute("list", newList);
        } else if(query.isEmpty() && list.isEmpty()){
            ProductQuery newQuery = service.getProductQuery(categoryId, NUMBER_OF_ENTRIES);
            ProductList newList = service.getProductList(newQuery);

            System.out.println(newQuery);
            System.out.println(newList);

            model.addAttribute("products", service.getProducts(newList.getProductsId()));
            model.addAttribute("list", newList);
        } else {
            throw new RequestException("wrong request parameters");
        }

        model.addAttribute("categoryId", categoryId);
        model.addAttribute("query", new ProductQuery(categoryId, service.getCharacteristics(query.getCategoryId()), NUMBER_OF_ENTRIES));

        return "products";
    }

    @GetMapping("/{categoryId}/shops")
    public String getShops(@PathVariable long categoryId, Model model) {

        Map<Boolean,List<CategoryShop>> shops = service.getShops(categoryId);

        model.addAttribute("included", shops.get(true));
        model.addAttribute("categoryShops", new CategoryShops(categoryId, shops.get(false)));
        return "shops";
    }

    @PostMapping("shops")
    public String addShopsToCategory(@ModelAttribute CategoryShops categoryShops) {
        System.out.println(categoryShops);

        service.addShopsToCategory(categoryShops);
        return "redirect:/categories";
    }

    @GetMapping("{categoryId}/characteristic")
    public String addCharacteristic(@PathVariable long categoryId, Model model) {
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("characteristic", new Characteristic());
        return "new-characteristic";
    }

    @PostMapping("characteristic")
    public String addCharacteristic(@ModelAttribute Characteristic characteristic) {
        System.out.println(characteristic);

        service.addCharacteristic(characteristic);
        return "redirect:/categories/" + characteristic.getCategoryId();
    }
}
