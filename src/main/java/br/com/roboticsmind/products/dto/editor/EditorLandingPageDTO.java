package br.com.roboticsmind.products.dto.editor;

import lombok.Data;
import java.util.List;

@Data
public class EditorLandingPageDTO {
    private String pageTitle;
    private String slug;
    private String status;
    private List<EditorBlockDTO> blocks;
}