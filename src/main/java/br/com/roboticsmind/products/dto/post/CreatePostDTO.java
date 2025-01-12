package br.com.roboticsmind.products.dto.post;

import br.com.roboticsmind.products.models.Post;

@lombok.Data
@lombok.NoArgsConstructor
public class CreatePostDTO {
    private String title;
    private String description;
    private Integer order;

    public CreatePostDTO(Post post){
         this.title = post.getTitle();
         this.description = post.getDescription();
         this.order = post.getOrder();
    }   
}
