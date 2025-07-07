CREATE TABLE "user" (
    id SERIAL PRIMARY KEY,
    keycloak_id VARCHAR(255) NOT NULL UNIQUE, -- <<< ESSA É A COLUNA ESSENCIAL
    name VARCHAR(255),
    nickname VARCHAR(50),
    email VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    is_profile_complete BOOLEAN NOT NULL DEFAULT FALSE
);