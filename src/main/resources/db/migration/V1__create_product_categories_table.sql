CREATE TABLE product_category (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT
);

INSERT INTO
    product_category (name, description)
VALUES
    ('Smartphones', 'celulares de qualidade'),
    ('Outros', 'outros tipos de produtos');