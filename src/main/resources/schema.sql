DROP TABLE IF EXISTS products;

CREATE TABLE products(
    product_url VARCHAR(300) PRIMARY KEY,
    product_description VARCHAR(1000),
    product_price VARCHAR(1000)
)