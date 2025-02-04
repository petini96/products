CREATE TABLE product_category (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT
);

INSERT INTO
    product_category (name, description)
VALUES
    ('Comum', 'doces do dia a dia'),
    ('Eventual', 'para datas específicas');