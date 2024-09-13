INSERT INTO merchants
    (merchant_id, name, account_balance, status, created_at)
VALUES
    (1, 'Apple', 0, 1, NOW());

INSERT INTO products
    (product_sku, name, price, available_quantity, merchant_id, created_at)
VALUES
    ('799A4307AEB3482DBBD48946143DCF8F', 'iPhone16', 29900, 100, 1, NOW());