DROP TABLE IF EXISTS merchants;
CREATE TABLE merchants (
    merchant_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(128) NOT NULL,
    account_balance DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    status INT NOT NULL DEFAULT 1,
    created_at DATETIME NOT NULL,
    updated_at DATETIME
);

DROP TABLE IF EXISTS products;
CREATE TABLE products (
    product_sku VARCHAR(64) PRIMARY KEY,
    name VARCHAR(128) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    available_quantity INT NOT NULL DEFAULT 0,
    merchant_id BIGINT NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME
);

DROP TABLE IF EXISTS transactions;
CREATE TABLE transactions (
    transaction_id BIGINT PRIMARY KEY,
    order_id BIGINT NOT NULL,   -- 關聯到訂單ID
    user_id BIGINT NOT NULL,         -- 關聯到用戶ID
    merchant_id BIGINT NOT NULL,     -- 關聯到商戶ID
    product_sku VARCHAR(64) NOT NULL,-- 關聯到產品SKU
    quantity INT NOT NULL,           -- 購買數量
    total_amount DECIMAL(10,2) NOT NULL, -- 交易總金額
    transaction_date DATETIME NOT NULL, -- 交易時間
    transaction_type VARCHAR(20) NOT NULL, -- 交易類型，例如 "purchase", "refund"
    created_at DATETIME NOT NULL,     -- 記錄創建時間
    updated_at DATETIME               -- 記錄更新時間
);

DROP TABLE IF EXISTS merchant_reconciliation;
CREATE TABLE merchant_reconciliation (
     reconciliation_id BIGINT AUTO_INCREMENT PRIMARY KEY,
     merchant_id BIGINT NOT NULL,     -- 關聯到商戶ID
     reconciliation_date DATE NOT NULL, -- 對帳日期
     total_revenue DECIMAL(10,2) NOT NULL, -- 商戶當天總收入
     sold_quantity INT NOT NULL,      -- 當天賣出的產品數量
     product_sku VARCHAR(64),         -- 賣出的產品SKU，可選
     created_at DATETIME NOT NULL,    -- 記錄創建時間
     updated_at DATETIME              -- 記錄更新時間
);