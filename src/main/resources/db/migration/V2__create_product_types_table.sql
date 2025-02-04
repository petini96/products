CREATE TABLE product_type (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT
);

INSERT INTO
    product_type (name, description)
VALUES
    ('Pão de mel','Delicioso pão de mel'),
    ('Bolo de congelado','outros tipos de produtos'),
    ('Brownie','Delicioso brownie'),
    ('Brigadeiro','Delicioso brigadeiro');
