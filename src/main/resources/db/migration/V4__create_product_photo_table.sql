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
    'http://localhost:9000/products/first.png',
    'image/png', 
    204800, 
    CURRENT_TIMESTAMP
),
(
    1, 
    'http://localhost:9000/products/second.jpg',
    'image/jpg', 
    204800, 
    CURRENT_TIMESTAMP
),
(
    1, 
    'http://localhost:9000/products/third.webp',
    'image/jpeg', 
    204800, 
    CURRENT_TIMESTAMP
),
(
    1, 
    'http://localhost:9000/products/fourth.jpg',
    'image/webp', 
    204800, 
    CURRENT_TIMESTAMP
);


