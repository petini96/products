package br.com.roboticsmind.products.controllers;

import br.com.roboticsmind.products.dto.editor.EditorLandingPageDTO;
import br.com.roboticsmind.products.dto.landingpage.ProductLandingPageDTO;
import br.com.roboticsmind.products.dto.landingpage.SaveLandingPageRequestDto;
import br.com.roboticsmind.products.services.ProductLandingPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductLandingPageController {

    private final ProductLandingPageService landingPageService;

    /**
     * Lida com POST para /api/products/{productId}/landing-page (CRIAR/ATUALIZAR)
     */
    @PostMapping("/{productId}/landing-page")
    public ResponseEntity<ProductLandingPageDTO> saveOrUpdateLandingPage(
            @PathVariable Long productId,
            @RequestBody SaveLandingPageRequestDto requestDto) {

        ProductLandingPageService.SaveResult result = landingPageService.saveOrUpdate(productId, requestDto);
        ProductLandingPageDTO responseDto = landingPageService.mapToDto(result.page());

        if (result.wasCreated()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
        } else {
            return ResponseEntity.ok(responseDto);
        }
    }

    @GetMapping("/{productId}/landing-page")
    public ResponseEntity<EditorLandingPageDTO> getLandingPageByProductId(@PathVariable Long productId) {
        // Chama o novo método de serviço específico para o editor
        EditorLandingPageDTO pageDto = landingPageService.findForEditorByProductId(productId);
        return ResponseEntity.ok(pageDto);
    }
}