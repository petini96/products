package br.com.roboticsmind.products.services.impl.helper;

import br.com.roboticsmind.products.dto.post.CreatePostDTO;
import br.com.roboticsmind.products.models.Post;

public class PostFactory {

    public static Post createPost(CreatePostDTO dto) {
        Post post = new Post();
        post.setTitle(dto.getTitle().trim());
        post.setDescription(dto.getDescription() != null ? dto.getDescription().trim() : null);
        post.setOrder(dto.getOrder());
        return post;
    }
}