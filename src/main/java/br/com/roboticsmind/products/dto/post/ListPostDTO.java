package br.com.roboticsmind.products.dto.post;

import java.io.Serial;
import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ListPostDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String media;
    private String mediaMobile;
    private String title;
    private String description;
    private Integer order;

    public ListPostDTO(Long id, String media, String mediaMobile, String title, String description, Integer order) {
        this.id = id;
        this.media = media;
        this.mediaMobile = mediaMobile;
        this.title = title;
        this.description = description;
        this.order = order;
    }
}