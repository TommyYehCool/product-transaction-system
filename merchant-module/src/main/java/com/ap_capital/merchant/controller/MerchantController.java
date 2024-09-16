package com.ap_capital.merchant.controller;

import com.ap_capital.common.model.merchant_module.Merchant;
import com.ap_capital.common.model.merchant_module.Product;
import com.ap_capital.common.req.merchant_module.merchant.*;
import com.ap_capital.common.resp.merchant_module.merchat.CheckProductResp;
import com.ap_capital.merchant.service.MerchantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MerchantController {

    private final MerchantService merchantService;

    public MerchantController(MerchantService merchantService) {
        this.merchantService = merchantService;
    }

    /**
     * 查詢所有商家
     */
    @GetMapping("/merchants")
    public ResponseEntity<List<Merchant>> findAll() {
        List<Merchant> merchants = merchantService.findAll();
        return ResponseEntity.ok(merchants);
    }

    /**
     * 根據商家名稱查詢商家
     */
    @GetMapping("/merchants/by-name")
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
    @PostMapping("/merchants")
    public ResponseEntity<?> addMerchant(@RequestBody AddMerchantReq req) {
        merchantService.addMerchant(req);
        return ResponseEntity.ok("Add merchant succeed");
    }

    /**
     * 更新商家
     */
    @PutMapping("/merchants/{merchantId}")
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
    @PostMapping("/merchants/{merchantId}/inventory")
    public ResponseEntity<?> upsertInventory(
            @PathVariable Long merchantId,
            @RequestBody AddInventoryReq request) {

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
    @GetMapping("/merchants/{merchantId}/inventory")
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
     * 確認產品庫存並計算訂單總金額
     */
    @PostMapping("/merchants/check-product")
    public ResponseEntity<CheckProductResp> checkProduct(
        @RequestBody CheckProductReq req
    ) {
        CheckProductResp resp = merchantService.checkProduct(req);
        return ResponseEntity.ok(resp);
    }

    /**
     * 商品賣出
     */
    @PostMapping("/merchants/product-sold")
    public ResponseEntity<Void> productSold(
            @RequestBody ProductSoldReq req
    ) {
        merchantService.productSold(req);
        return ResponseEntity.ok().build();
    }

    /**
     * 日常結算
     */
    @PostMapping("/merchants/{merchantId}/settlement")
    public void settlement() {
        merchantService.settlement();
    }


}
