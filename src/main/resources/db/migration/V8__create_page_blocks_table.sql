CREATE TABLE page_block (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    landing_page_id BIGINT NOT NULL,
    block_type VARCHAR(50) NOT NULL, -- Ex: 'header', 'paragraph', 'image', 'embed'
    content TEXT, -- Armazena texto, URL da imagem, URL do vídeo, etc.
    metadata JSONB, -- Armazena dados extras, como o nível do header: {"level": 1}
    block_order INTEGER NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_landing_page
        FOREIGN KEY(landing_page_id)
        REFERENCES product_landing_page(id)
        ON DELETE CASCADE
);

CREATE INDEX idx_page_block_landing_page_id ON page_block(landing_page_id);