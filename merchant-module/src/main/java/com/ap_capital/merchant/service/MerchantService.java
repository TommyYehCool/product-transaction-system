package com.ap_capital.merchant.service;

import com.ap_capital.common.cnst.CommonStatus;
import com.ap_capital.common.cnst.TransactionType;
import com.ap_capital.common.model.merchant_module.Merchant;
import com.ap_capital.common.model.merchant_module.MerchantReconciliation;
import com.ap_capital.common.model.merchant_module.Product;
import com.ap_capital.common.model.merchant_module.Transaction;
import com.ap_capital.common.req.merchant_module.merchant.AddMerchantReq;
import com.ap_capital.common.req.merchant_module.merchant.CheckProductReq;
import com.ap_capital.common.req.merchant_module.merchant.ProductSoldReq;
import com.ap_capital.common.req.merchant_module.merchant.UpdateMerchantReq;
import com.ap_capital.common.resp.merchant_module.merchat.CheckProductResp;
import com.ap_capital.common.utils.DateUtils;
import com.ap_capital.common.utils.IDGenerator;
import com.ap_capital.common.utils.UUIDUtils;
import com.ap_capital.merchant.mapper.MerchantMapper;
import com.ap_capital.merchant.mapper.MerchantReconciliationMapper;
import com.ap_capital.merchant.mapper.ProductMapper;
import com.ap_capital.merchant.mapper.TransactionMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class MerchantService {

    private final MerchantMapper merchantMapper;
    private final ProductMapper productMapper;
    private final TransactionMapper transactionMapper;
    private MerchantReconciliationMapper merchantReconciliationMapper;

    public MerchantService(MerchantMapper merchantMapper, ProductMapper productMapper, TransactionMapper transactionMapper, MerchantReconciliationMapper merchantReconciliationMapper) {
        this.merchantMapper = merchantMapper;
        this.productMapper = productMapper;
        this.transactionMapper = transactionMapper;
        this.merchantReconciliationMapper = merchantReconciliationMapper;
    }

    public List<Merchant> findAll() {
        return merchantMapper.findAll();
    }

    public Merchant findByName(String name) {
        return merchantMapper.findByName(name);
    }

    public void addMerchant(AddMerchantReq req) {
        // 檢查是否有同名商家
        Merchant existingMerchant = merchantMapper.findByName(req.getName());
        if (existingMerchant != null) {
            throw new RuntimeException("Merchant with name " + req.getName() + " already exists.");
        }

        // 新增商家
        Merchant merchant = new Merchant();
        BeanUtils.copyProperties(req, merchant);
        merchant.setStatus(CommonStatus.Active.getValue());
        merchant.setCreatedAt(new Date());
        merchantMapper.insert(merchant);
    }

    public void updateMerchant(Long merchantId, UpdateMerchantReq req) {
        // 檢查商家是否存在
        Merchant existingMerchant = merchantMapper.findById(merchantId);
        if (existingMerchant == null) {
            throw new RuntimeException("Merchant with ID " + merchantId + " does not exist.");
        }

        // 根據現有商家對象更新資料
        if (req.getName() != null && !req.getName().equals(existingMerchant.getName())) {
            existingMerchant.setName(req.getName());
        }
        if (req.getAccountBalance() != null && !req.getAccountBalance().equals(existingMerchant.getAccountBalance())) {
            existingMerchant.setAccountBalance(req.getAccountBalance());
        }
        if (req.getStatus() != null && req.getStatus().getValue() != existingMerchant.getStatus()) {
            existingMerchant.setStatus(req.getStatus().getValue());
        }
        existingMerchant.setUpdatedAt(new Date());

        // 更新商家資料
        merchantMapper.update(existingMerchant);
    }

    public void upsertInventory(Long merchantId, String productSku, String productName, BigDecimal price, int quantity) {
        // 驗證商家是否存在
        Merchant merchant = merchantMapper.findById(merchantId);
        if (merchant == null) {
            throw new RuntimeException("Merchant not found with ID: " + merchantId);
        }

        if (StringUtils.isEmpty(productSku)) {
            productSku = UUIDUtils.getUUID();
        }

        // 查詢產品是否已存在
        Product product = productMapper.findBySku(productSku);
        if (product != null) {
            // 如果產品已存在，更新庫存數量
            int newQuantity = product.getAvailableQuantity() + quantity;
            product.setAvailableQuantity(newQuantity);
            product.setUpdatedAt(new Date());
            productMapper.update(product);
        } else {
            // 如果產品不存在，創建新產品並添加到庫存
            Product newProduct = new Product();
            newProduct.setProductSku(productSku);
            newProduct.setName(productName);
            newProduct.setPrice(price);
            newProduct.setAvailableQuantity(quantity);
            newProduct.setMerchantId(merchantId);
            newProduct.setCreatedAt(new Date());
            productMapper.insert(newProduct);
        }
    }

    public Product checkInventory(Long merchantId, String productName) {
        // 查詢產品是否已存在
        Product product = productMapper.findByMerchantIdAndName(merchantId, productName);
        if (product == null) {
            throw new RuntimeException("Product with name: " + productName + " not found for merchant with merchantId: " + merchantId);
        }
        return product;
    }

    public CheckProductResp checkProduct(CheckProductReq req) {
        Product product = productMapper.findBySku(req.getProductSku());
        if (product == null) {
            return CheckProductResp.builder()
                    .result(false)
                    .message("Product not found with SKU: " + req.getProductSku())
                    .build();
        }

        if (product.getAvailableQuantity() < req.getQuantity()) {
            return CheckProductResp.builder()
                    .result(false)
                    .message("Insufficient stock for product: " + product.getName())
                    .build();
        }

        BigDecimal totalAmount = product.getPrice().multiply(BigDecimal.valueOf(req.getQuantity()));

        return CheckProductResp.builder()
                .result(true)
                .message("ok to sell")
                .merchantId(product.getMerchantId())
                .totalAmount(totalAmount)
                .build();
    }

    @Transactional
    public void productSold(ProductSoldReq req) {
        productMapper.productSold(req.getProductSku(), req.getQuantity(), new Date());

        Transaction transaction
                = Transaction.builder()
                    .transactionId(IDGenerator.getTransactionId())
                    .orderId(req.getOrderId())
                    .userId(req.getUserId())
                    .merchantId(req.getMerchantId())
                    .productSku(req.getProductSku())
                    .quantity(req.getQuantity())
                    .totalAmount(req.getAmount())
                    .transactionDate(new Date())
                    .transactionType(TransactionType.purchase.name())
                    .createdAt(new Date())
                    .build();
        transactionMapper.insertTransaction(transaction);
    }

    @Transactional
    public void settlement() {
        // 1. 查詢所有活躍的商戶
        List<Merchant> merchants = merchantMapper.findAll();

        for (Merchant merchant : merchants) {
            // 2. 查詢該商戶自上次對賬以來賣出的產品
            Date startOfDay = DateUtils.getStartOfDay(); // 當天 00:00:00
            Date endOfDay = DateUtils.getEndOfDay();     // 當天 23:59:59

            List<Transaction> transactions
                    = transactionMapper.findTransactionsByMerchantIdAndDateRange(merchant.getMerchantId(), startOfDay, endOfDay);

            // 3. 計算總收入和售出產品數量
            BigDecimal totalRevenue
                    = transactions.stream()
                        .map(Transaction::getTotalAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

            int soldQuantity
                    = transactions.stream()
                        .mapToInt(Transaction::getQuantity)
                        .sum();

            // 4. 更新商戶的賬戶餘額
            if (totalRevenue.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal newBalance = merchant.getAccountBalance().add(totalRevenue);
                merchant.setAccountBalance(newBalance);
                merchant.setUpdatedAt(new Date());
                merchantMapper.update(merchant);

                // 5. 添加對賬的交易記錄
                Transaction reconciliationTransaction
                        = Transaction.builder()
                            .transactionId(IDGenerator.getTransactionId())
                            .orderId(null)
                            .userId(null)
                            .merchantId(merchant.getMerchantId())
                            .productSku(null)
                            .quantity(soldQuantity)
                            .totalAmount(totalRevenue)
                            .transactionDate(new Date())
                            .transactionType(TransactionType.reconciliation.name())
                            .createdAt(new Date())
                            .build();

                transactionMapper.insertTransaction(reconciliationTransaction);

                // 6. 插入對賬記錄到 merchant_reconciliation 表
                MerchantReconciliation reconciliation
                        = MerchantReconciliation.builder()
                            .reconciliationId(IDGenerator.getReconciliationId())
                            .merchantId(merchant.getMerchantId())
                            .reconciliationDate(new Date())
                            .totalRevenue(totalRevenue)
                            .soldQuantity(soldQuantity)
                            .createdAt(new Date())
                            .build();

                // 插入對賬記錄
                merchantReconciliationMapper.insertReconciliation(reconciliation);
            }
        }
    }

}
