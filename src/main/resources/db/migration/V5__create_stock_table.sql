CREATE TABLE stock (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    quantity INT NOT NULL,
    product_id BIGINT NOT NULL,
    FOREIGN KEY (product_id) REFERENCES product(id)
);

INSERT INTO stock (name, quantity, product_id) VALUES ('galpão', 3, 1);
INSERT INTO stock (name, quantity, product_id) VALUES ('armazém', 1, 1);
