package br.com.roboticsmind.products.dto.post;

import br.com.roboticsmind.products.models.Post;

@lombok.Data
@lombok.NoArgsConstructor
public class GetPostDTO {

    private Long id;
    private String media;
    private String mediaMobile;
    private String title;
    private String description;
    private Integer order;

    public GetPostDTO(Post post){
         this.media = post.getMedia();
         this.mediaMobile = post.getMediaMobile();
         this.title = post.getTitle();
         this.description = post.getDescription();
         this.order = post.getOrder();
    }   
}
