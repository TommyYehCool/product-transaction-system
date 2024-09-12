package com.ap_capital.merchant.controller;

import com.ap_capital.common.model.merchant_module.Product;
import com.ap_capital.merchant.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{productSku}")
    public ResponseEntity<?> getProductBySku(@PathVariable("productSku") String productSku) {
        Product product = productService.getProductBySku(productSku);;
        return ResponseEntity.ok(product);
    }

    @PostMapping("/{productSku}/inventory")
    public ResponseEntity<?> addProductInventory(@PathVariable("productSku") String productSku) {
        return null;
    }
}
