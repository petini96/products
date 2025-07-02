package br.com.roboticsmind.products.dto.editor;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

@Data
public class EditorBlockDTO {
    private String id;
    private String type;
    private JsonNode data; // JsonNode para manter a estrutura original do editor
}