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

INSERT INTO product (
    name,
    brand,
    original_price,
    discount_percentage,
    discount_price,
    installment_price,
    installments_count,
    additional_info,
    description,
    link,
    product_type_id,
    product_category_id
)
VALUES
    (
        'Pão de mel', --name
        'Doceria', --brand
        10.00, --original_price
        10.0, --discount_percentage
        0.99, --discount_price
        10.00, --installment_price
        1, --installments_count
        'Produzido em casa', --additional_info
        'Feito com ingredientes selecionados e um toque especial de carinho, nosso pão de mel caseiro é macio, aromático e irresistivelmente saboroso. 🍯🍫', --description
        'https://doceria.roboticsmind.com.br/pao-de-mel',
        1, --product_type_id
        1 --product_category_id
    ),
    (
        'Bolo Gelado', --name
        'Doceria', --brand
        8.00, --original_price
        8.0, --discount_percentage
        0.99, --discount_price
        10.00, --installment_price
        1, --installments_count
        'Produzido em casa', --additional_info
        'Feito com ingredientes selecionados e um toque especial de carinho, nosso bolo gelado caseiro é fofinho, refrescante e irresistivelmente delicioso. ❄️🍰', --description
        'https://doceria.roboticsmind.com.br/bolo-congelado',
        2, --product_type_id
        1 --product_category_id
    ),
    (
        'Brownie', --name
        'Doceria', --brand
        10.00, --original_price
        10.0, --discount_percentage
        0.99, --discount_price
        10.00, --installment_price
        1, --installments_count
        'Produzido em casa', --additional_info
        'Feito com ingredientes selecionados e um toque especial de carinho, nosso brownie caseiro é denso, chocolatudo e simplesmente viciante. 🍫💖', --description
        'https://doceria.roboticsmind.com.br/brownie',
        3, --product_type_id
        1 --product_category_id
    ),
    (
        'Brigadeiro', --name
        'Doceria', --brand
        12.00, --original_price
        12.0, --discount_percentage
        0.99, --discount_price
        12.00, --installment_price
        1, --installments_count
        'Produzido em casa', --additional_info
        'Feito com ingredientes selecionados e um toque especial de carinho, nosso brigadeiro caseiro é macio, cremoso e cheio de sabor. 🎉🍬', --description
        'https://doceria.roboticsmind.com.br/brownie',
        3, --product_type_id
        1 --product_category_id
    );
