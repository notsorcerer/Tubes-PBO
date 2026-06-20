package com.liquid.liquidpedia.controller;

import com.liquid.liquidpedia.service.ProdukService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private ProdukService produkService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("bestSellers", produkService.findBestSellers());
        model.addAttribute("newArrivals", produkService.findNewArrivals());
        model.addAttribute("categories", produkService.findAllCategories());
        return "index";
    }
}
