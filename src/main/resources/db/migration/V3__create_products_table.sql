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
