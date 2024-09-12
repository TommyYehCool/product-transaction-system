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