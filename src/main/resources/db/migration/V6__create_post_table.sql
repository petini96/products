CREATE TABLE Post (
    id SERIAL PRIMARY KEY,
    media VARCHAR(2083) NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    "order" INT NOT NULL
);

INSERT INTO Post (media, title, description, "order")
VALUES
    ('http://localhost:9000/posts/post1.jpg', 'Post Title 1', 'Description for the first post.', 1),
    ('http://localhost:9000/posts/post2.jpg', 'Post Title 2', 'Description for the second post.', 2);
