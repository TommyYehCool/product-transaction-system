package com.ap_capital.common.feign_client.merchant_module;

import com.ap_capital.common.cnst.ServiceName;
import com.ap_capital.common.model.merchant_module.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@FeignClient(
        value = ServiceName.MERCHANT_MODULE,
        contextId = "ProductFeignClient"
)
public interface ProductFeignClient {

    @GetMapping("/products/{productSku}")
    Product getProductBySku(@PathVariable String productSku);

    @PutMapping("/products/{productSku}")
    void productSold(@PathVariable String productSku, @RequestParam int quantity);

}
