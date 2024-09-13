package com.ap_capital.merchant.controller;

import com.ap_capital.common.model.merchant_module.Product;
import com.ap_capital.merchant.service.ProductService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{productSku}")
    public Product getProductBySku(@PathVariable String productSku) {
        return productService.getProductBySky(productSku);
    }

    @PutMapping("/{productSku}")
    public void productSold(@PathVariable String productSku, @RequestParam int quantity) {
        productService.productSold(productSku, quantity);
    }
}
