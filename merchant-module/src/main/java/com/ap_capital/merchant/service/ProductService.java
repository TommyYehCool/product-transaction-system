package com.ap_capital.merchant.service;

import com.ap_capital.common.model.merchant_module.Product;
import com.ap_capital.merchant.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired
    private ProductMapper productMapper;

    public Product getProductBySku(String productSku) {
        return null;
    }
}
