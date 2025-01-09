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
    'http://localhost:9001/api/v1/buckets/products/objects/download?preview=true&prefix=first.png&version_id=null',
    'image/png', 
    204800, 
    CURRENT_TIMESTAMP
),
(
    1, 
    'http://localhost:9001/api/v1/buckets/products/objects/download?preview=true&prefix=second.jpg&version_id=null',
    'image/jpg', 
    204800, 
    CURRENT_TIMESTAMP
),
(
    1, 
    'http://localhost:9001/api/v1/buckets/products/objects/download?preview=true&prefix=third.webp&version_id=null',
    'image/jpeg', 
    204800, 
    CURRENT_TIMESTAMP
),
(
    1, 
    'http://localhost:9001/api/v1/buckets/products/objects/download?preview=true&prefix=fourth.jpg&version_id=null',
    'image/webp', 
    204800, 
    CURRENT_TIMESTAMP
);


