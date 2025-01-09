CREATE TABLE product_type (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT
);

INSERT INTO
    product_type (id, name, description)
VALUES
    (1,'Digital','Celular dentre outros'),
    (5,'Outros','outros tipos de produtos');