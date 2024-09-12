package com.ap_capital.merchant.service;

import com.ap_capital.common.model.merchant_module.Product;
import com.ap_capital.merchant.mapper.ProductMapper;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductMapper productMapper;

    public ProductService(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    public Product getProductBySky(String sku) {
        return productMapper.findBySku(sku);
    }

    public void productSold(String sku, int quantity) {
        // TODO - Product Sold
    }
}
