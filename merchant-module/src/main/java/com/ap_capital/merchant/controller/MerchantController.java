package com.ap_capital.merchant.controller;

import com.ap_capital.merchant.service.MerchantService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/merchants")
public class MerchantController {

    private final MerchantService merchantService;

    public MerchantController(MerchantService merchantService) {
        this.merchantService = merchantService;
    }

    /**
     * 增加商品庫存
     */
    @PostMapping("/{merchantId}/inventory")
    public void addInventory() {
        merchantService.addInventory();
    }

    /**
     * 日常結算
     */
    @PostMapping("/{merchantId}/settlement")
    public void settlement() {
        merchantService.settlement();
    }


}
