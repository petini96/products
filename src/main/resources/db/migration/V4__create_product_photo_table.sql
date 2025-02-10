CREATE TABLE product_photo (
    id SERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL,
    url VARCHAR(2083) NOT NULL,
    file_type VARCHAR(255),
    file_size BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES product(id)
);

INSERT INTO product_photo (product_id, url, file_type, file_size, created_at) 
VALUES 
(
    1, 
    '${minio.url}/products/pao-de-mel.png',
    'image/png', 
    204800, 
    CURRENT_TIMESTAMP
),
(
    2,
    '${minio.url}/products/bolo-gelado.png',
    'image/jpg', 
    204800, 
    CURRENT_TIMESTAMP
),
(
    3,
    '${minio.url}/products/brownie.png',
    'image/jpeg', 
    204800, 
    CURRENT_TIMESTAMP
),
(
    4,
    '${minio.url}/products/brigadeiro.png',
    'image/webp', 
    204800, 
    CURRENT_TIMESTAMP
);

