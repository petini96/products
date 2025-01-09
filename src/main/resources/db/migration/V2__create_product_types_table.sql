CREATE TABLE product_type (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT
);

INSERT INTO
    product_type (id, name, description)
VALUES
    (1, 'torcedor', 'versão paia'),
    (2, 'jogador', 'versão ventinho'),
    (3, 'retrô', 'camisas antigas da porra'),
    (4, 'polo', 'quer pagar de putinha agora ?'),
    (
        5,
        'manga longa',
        'ta trabalhando de pedreiro né ?'
    );