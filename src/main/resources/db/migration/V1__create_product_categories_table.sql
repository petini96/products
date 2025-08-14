CREATE TABLE IF NOT EXISTS product_category (

    id BIGSERIAL PRIMARY KEY,

    name VARCHAR(255) NOT NULL UNIQUE,

    description TEXT,

    image_url TEXT,

    active BOOLEAN NOT NULL DEFAULT true
);