CREATE TABLE Post (
    id SERIAL PRIMARY KEY,
    media VARCHAR(2083) NOT NULL,
    media_mobile VARCHAR(2083) NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    "order" INT NOT NULL
);

INSERT INTO Post (media, media_mobile, title, description, "order")
VALUES
    ('${minio.url}/posts/post1.svg', '${minio.url}/posts/post1_mobile.svg', 'Post Title 1', 'Description for the first post.', 1),
    ('${minio.url}/posts/post2.svg', '${minio.url}/posts/post1_mobile.svg', 'Post Title 2', 'Description for the second post.', 2);
