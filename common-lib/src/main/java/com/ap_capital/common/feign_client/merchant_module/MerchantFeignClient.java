package com.ap_capital.common.feign_client.merchant_module;

import com.ap_capital.common.cnst.ServiceName;
import com.ap_capital.common.req.merchant_module.merchant.CheckProductReq;
import com.ap_capital.common.req.merchant_module.merchant.ProductSoldReq;
import com.ap_capital.common.resp.merchant_module.merchat.CheckProductResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Service
@FeignClient(
        value = ServiceName.MERCHANT_MODULE,
        contextId = "MerchantFeignClient"
)
public interface MerchantFeignClient {
    @PutMapping("/merchants/{merchantId}/increment-revenue")
    ResponseEntity<Void> incrementMerchantRevenue(
            @PathVariable("merchantId") Long merchantId,
            @RequestParam("amount") BigDecimal amount
    );

    @PostMapping("/merchants/check-product")
    ResponseEntity<CheckProductResp> checkProduct(
            @RequestBody CheckProductReq req
    );

    @PostMapping("/merchants/product-sold")
    ResponseEntity<Void> productSold(
            @RequestBody ProductSoldReq req
    );
}
