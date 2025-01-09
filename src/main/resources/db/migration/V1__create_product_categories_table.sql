CREATE TABLE product_category (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT
);

INSERT INTO
    product_category (id, name, description)
VALUES
    (1, 'Smartphones', 'celulares de qualidade'),
    (2, 'Outros', 'outros tipos de produtos');