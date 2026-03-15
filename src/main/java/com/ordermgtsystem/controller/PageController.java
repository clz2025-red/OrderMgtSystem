package com.ordermgtsystem.controller;

import com.ordermgtsystem.service.ProductCatalogService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    private final ProductCatalogService productCatalogService;

    public PageController(ProductCatalogService productCatalogService) {
        this.productCatalogService = productCatalogService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("products", productCatalogService.getProductList());
        return "index";
    }
}
