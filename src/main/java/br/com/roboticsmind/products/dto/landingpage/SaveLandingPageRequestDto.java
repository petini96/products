
// dto/landingpage/SaveLandingPageRequestDto.java
package br.com.roboticsmind.products.dto.landingpage;

import java.util.List;

public record SaveLandingPageRequestDto(
        String pageTitle,
        String slug,
        String status, // "DRAFT" ou "PUBLISHED"
        List<PageBlockDto> blocks
) {}
