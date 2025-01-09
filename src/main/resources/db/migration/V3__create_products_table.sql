CREATE TABLE product (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    brand VARCHAR(255),
    original_price DECIMAL(10, 2) NOT NULL,
    discount_percentage DECIMAL(10, 2) NOT NULL,
    discount_price DECIMAL(10, 2) NOT NULL,
    installment_price DECIMAL(10, 2) NOT NULL,
    installments_count INT NOT NULL,
    additional_info VARCHAR(255),
    description VARCHAR(255),
    link VARCHAR(255),
    product_type_id INT NOT NULL,
    product_category_id INT NOT NULL,
    FOREIGN KEY (product_type_id) REFERENCES product_type(id),
    FOREIGN KEY (product_category_id) REFERENCES product_category(id)
);

INSERT INTO product (
    name,
    brand,
    original_price,
    discount_percentage,
    discount_price,
    installment_price,
    installments_count,
    additional_info,
    description,
    link,
    product_type_id,
    product_category_id
)
VALUES
    (
        'iPhone 16 Pro - Brown', --name
        'Apple', --brand
        7.999, --original_price
        10.0, --discount_percentage
        6.99, --discount_price
        720.0, --installment_price
        12, --installments_count
        'Garanta o seu agora mesmo!', --additional_info
        'iPhone 16 Pro - Brown, design sofisticado e desempenho de ponta.', --description
        'https://iphone.com/16v',
        1, --product_type_id
        1 --product_category_id
    );
