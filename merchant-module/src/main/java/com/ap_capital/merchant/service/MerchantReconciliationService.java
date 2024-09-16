package com.ap_capital.merchant.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class MerchantReconciliationService {

    private final MerchantService merchantService;

    public MerchantReconciliationService(MerchantService merchantService) {
        this.merchantService = merchantService;
    }

    // 每天凌晨執行對帳
    @Scheduled(cron = "0 0 0 * * ?")
    public void reconcileMerchants() {
        merchantService.settlement();
    }
}
