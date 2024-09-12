package com.ap_capital.merchant.controller;

import com.ap_capital.common.model.merchant_module.Product;
import com.ap_capital.merchant.mapper.ProductMapper;
import com.ap_capital.merchant.service.ProductService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{sku}")
    public Product getProductBySku(@PathVariable String sku) {
        return productService.getProductBySky(sku);
    }

    @PutMapping("/{sku}")
    public void productSold(@PathVariable String sku, @RequestParam int quantity) {
        productService.productSold(sku, quantity);
    }
}
