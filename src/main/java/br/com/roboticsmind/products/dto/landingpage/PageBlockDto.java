package br.com.roboticsmind.products.dto.landingpage;

import com.fasterxml.jackson.databind.JsonNode;

public record PageBlockDto(
        String type,
        JsonNode data
) {}