package com.ap_capital.merchant.service;

import com.ap_capital.common.model.merchant_module.Product;
import com.ap_capital.merchant.mapper.ProductMapper;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ProductService {

    private final ProductMapper productMapper;

    public ProductService(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    public Product getProductBySky(String productSku) {
        return productMapper.findBySku(productSku);
    }

    public void productSold(String productSku, int quantity) {
        productMapper.productSold(productSku, quantity, new Date());
    }
}
