DROP TABLE IF EXISTS products;

CREATE TABLE products(
    productUrl VARCHAR(100) PRIMARY KEY,
    productDescription VARCHAR(100),
    productPrice VARCHAR(100)
)