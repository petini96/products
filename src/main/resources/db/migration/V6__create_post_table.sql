CREATE TABLE Post (
    id SERIAL PRIMARY KEY,
    media VARCHAR(2083) NOT NULL,
    media_mobile VARCHAR(2083) NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    "order" INT NOT NULL
);