package br.com.roboticsmind.products.dto.post;

@lombok.Data
@lombok.NoArgsConstructor
public class ListPostDTO {

    private Long id;
    private String media;
    private String title;
    private String description;
    private Integer order;

    public ListPostDTO(Long id, String media, String title, String description, Integer order) {
        this.id = id;
        this.media = media;
        this.title = title;
        this.description = description;
        this.order = order;
    }
}
