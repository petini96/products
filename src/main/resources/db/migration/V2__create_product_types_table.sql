CREATE TABLE product_type (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT
);

INSERT INTO
    product_type (name, description)
VALUES
    ('Digital','Celular dentre outros'),
    ('Outros','outros tipos de produtos');