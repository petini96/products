package br.com.roboticsmind.products.dto.block;

import lombok.Data;
import java.util.Map; // Importe a classe Map

@Data
public class BlockMetadataDTO {
    private Integer level; // Para blocos do tipo 'header'
    private String caption; // Para blocos do tipo 'image' ou 'embed'
    private String style; // Para blocos do tipo 'list'

    /**
     * Campo adicionado para metadados de links.
     * Um Map flexível para receber o objeto "meta" com título, descrição, etc.
     */
    private Map<String, Object> meta;
}