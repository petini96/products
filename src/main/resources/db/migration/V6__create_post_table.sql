CREATE TABLE Post (
    id SERIAL PRIMARY KEY,
    media VARCHAR(2083) NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    "order" INT NOT NULL
);

INSERT INTO Post (media, title, description, "order")
VALUES
    ('http://localhost:9001/api/v1/buckets/products/objects/download?preview=true&prefix=post1.jpg&version_id=null', 'Post Title 1', 'Description for the first post.', 1),
    ('http://localhost:9001/api/v1/buckets/products/objects/download?preview=true&prefix=post2.jpg&version_id=null', 'Post Title 2', 'Description for the second post.', 2),
    ('http://localhost:9001/api/v1/buckets/products/objects/download?preview=true&prefix=post3.jpg&version_id=null', 'Post Title 3', 'Description for the third post.', 3);
