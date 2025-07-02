package br.com.roboticsmind.products.controllers;

import br.com.roboticsmind.products.dto.landingpage.ProductLandingPageDTO;
import br.com.roboticsmind.products.services.ProductLandingPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public/pages")
@RequiredArgsConstructor
public class PublicPageController {

    private final ProductLandingPageService landingPageService;

    @GetMapping("/{slug}")
    public ResponseEntity<ProductLandingPageDTO> getPageBySlug(@PathVariable String slug) {
        ProductLandingPageDTO pageDto = landingPageService.findDtoBySlug(slug);
        return ResponseEntity.ok(pageDto);
    }
}