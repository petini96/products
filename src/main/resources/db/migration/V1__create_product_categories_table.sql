CREATE TABLE product_category (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT
);

INSERT INTO
    product_category (id, name, description)
VALUES
    (1, 'camisas', 'e a fada pai'),
    (2, 'shorts', 'vai sentar molinho'),
    (3, 'corta vento', 'vamo vua mlk'),
    (4, 'meião', 'ce nem joga fí'),
    (5, 'kit infantil', 'capetinha mirim'),
    (6, 'baby jersey', 'capetinha nenem');