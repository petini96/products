package br.com.roboticsmind.products.controllers;

import br.com.roboticsmind.products.dto.post.CreatePostDTO;
import br.com.roboticsmind.products.dto.post.ListPostDTO;
import br.com.roboticsmind.products.dto.product.ListAllProductsDTO;
import br.com.roboticsmind.products.models.Post;
import br.com.roboticsmind.products.models.Product;
import br.com.roboticsmind.products.services.IPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@SpringBootApplication
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private IPostService iPostService;

    @GetMapping
    public Page<ListPostDTO> listPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageRequest pageable = PageRequest.of(page, size);
        return this.iPostService.listPosts(page, size);
    }

    @GetMapping("/{postId}")
    public Post getPost(@PathVariable Long postId) {
        return this.iPostService.getPost(postId);
    }

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public Post createPost(
            @RequestPart("post") CreatePostDTO createPostDTO,
            @RequestPart("photo") MultipartFile photo) {

        System.out.println("Recebendo post: " + createPostDTO);
        System.out.println("Recebendo photo: " + photo);

        return this.iPostService.createPost(createPostDTO, photo);
    }
}
