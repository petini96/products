
INSERT INTO product_category (
    name,
    description,
    image_url,
    active
)
VALUES
    (
        'Bolos', -- name
        'Nossos bolos fofinhos e recheados, perfeitos para qualquer ocasião.', -- description
        'https://www.sabornamesa.com.br/media/k2/items/cache/814d386f5c4138112e5fa70430be6661_XL.jpg', -- image_url
        true -- active
    ),
    (
        'Doces Finos', -- name
        'Brigadeiros, beijinhos e outros doces especiais para festas e eventos.', -- description
        'https://www.receitasnestle.com.br/sites/default/files/styles/recipe_detail_desktop_new/public/srh_recipes/8d6a0e43fea8d543981a3eb7d870f3b0.jpeg?itok=azWUsQbD', -- image_url
        true -- active
    ),
    (
        'Sobremesas', -- name
        'Deliciosas sobremesas individuais como brownies, pães de mel e bolos no pote.', -- description
        'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQKf8_VaHduEDF2XvIX9eKsivA3uSS2P4c6jw&s', -- image_url
        true -- active
    ),
    (
        'Salgados', -- name
        'Opções de salgados para festas e lanches, feitos com a mesma qualidade dos nossos doces.', -- description
        'https://www.sabornamesa.com.br/media/k2/items/cache/98401d211546397e2b8c04cfd4ec5a4d_XL.jpg', -- image_url
        true -- active
    ),
    (
        'Bebidas', -- name
        'Sucos, refrigerantes e outras bebidas para acompanhar seu pedido.', -- description
        'https://paroquiasantaclaradf.com.br/wp-content/uploads/2021/07/Design-sem-nome-10.png', -- image_url
        false -- active
    )
ON CONFLICT (name) DO NOTHING;