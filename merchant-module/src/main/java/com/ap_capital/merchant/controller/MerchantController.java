package com.ap_capital.merchant.controller;

import com.ap_capital.common.model.merchant_module.Merchant;
import com.ap_capital.common.model.merchant_module.Product;
import com.ap_capital.common.req.merchant_module.merchant.AddInventoryRequest;
import com.ap_capital.common.req.merchant_module.merchant.AddMerchantReq;
import com.ap_capital.common.req.merchant_module.merchant.UpdateMerchantReq;
import com.ap_capital.merchant.service.MerchantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/merchants")
public class MerchantController {

    private final MerchantService merchantService;

    public MerchantController(MerchantService merchantService) {
        this.merchantService = merchantService;
    }

    /**
     * 查詢所有商家
     */
    @GetMapping
    public ResponseEntity<List<Merchant>> findAll() {
        List<Merchant> merchants = merchantService.findAll();
        return ResponseEntity.ok(merchants);
    }

    /**
     * 根據商家名稱查詢商家
     */
    @GetMapping("/by-name")
    public ResponseEntity<Merchant> findByName(@RequestParam String name) {
        Merchant merchant = merchantService.findByName(name);
        if (merchant != null) {
            return ResponseEntity.ok(merchant);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 新增商家
     */
    @PostMapping
    public ResponseEntity<?> addMerchant(@RequestBody AddMerchantReq req) {
        merchantService.addMerchant(req);
        return ResponseEntity.ok("Add merchant succeed");
    }

    /**
     * 更新商家
     */
    @PutMapping("/{merchantId}")
    public ResponseEntity<Void> updateMerchant(
            @PathVariable Long merchantId,
            @RequestBody UpdateMerchantReq req
    ) {
        merchantService.updateMerchant(merchantId, req);
        return ResponseEntity.ok().build();
    }

    /**
     * 增加商品庫存
     */
    @PostMapping("/{merchantId}/inventory")
    public ResponseEntity<?> upsertInventory(
            @PathVariable Long merchantId,
            @RequestBody AddInventoryRequest request) {

        // 呼叫 service 方法來處理庫存添加
        merchantService.upsertInventory(
                merchantId,
                request.getProductSku(),
                request.getProductName(),
                request.getPrice(),
                request.getQuantity()
        );

        // 返回成功狀態
        return ResponseEntity.ok("Add/Update inventory succeed");
    }

    /**
     * 查詢商品庫存
     */
    @GetMapping("/{merchantId}/inventory")
    public ResponseEntity<Product> checkInventory(
            @PathVariable Long merchantId,
            @RequestParam String productName
    ) {
        // 呼叫 service 方法來查詢庫存
        Product product = merchantService.checkInventory(merchantId, productName);

        // 返回查詢結果
        return ResponseEntity.ok(product);
    }

    /**
     * 商品賣出增加餘額
     */
    @PutMapping("/{merchantId}/increment-revenue")
    public ResponseEntity<Void> incrementMerchantRevenue(
            @PathVariable("merchantId") Long merchantId,
            @RequestParam("amount") BigDecimal amount
    ) {
        merchantService.incrementMerchantRevenue(merchantId, amount);
        return ResponseEntity.ok().build();
    }

    /**
     * 日常結算
     */
    @PostMapping("/{merchantId}/settlement")
    public void settlement() {
        merchantService.settlement();
    }


}
