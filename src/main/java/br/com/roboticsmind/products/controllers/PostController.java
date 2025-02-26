package br.com.roboticsmind.products.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.roboticsmind.products.annotations.LogExecutionTime;
import br.com.roboticsmind.products.dto.post.CreatePostDTO;
import br.com.roboticsmind.products.dto.post.ListPostDTO;
import br.com.roboticsmind.products.models.Post;
import br.com.roboticsmind.products.services.IPostService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@SpringBootApplication
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private IPostService iPostService;

    @LogExecutionTime
    @GetMapping
    public Page<ListPostDTO> listPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return this.iPostService.listPosts(page, size);
    }

    @LogExecutionTime
    @GetMapping("/{postId}")
    public Post getPost(@PathVariable Long postId) {
        return this.iPostService.getPost(postId);
    }

    @LogExecutionTime
    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public Post createPost(
            @Valid @RequestPart("post") CreatePostDTO createPostDTO,
            @Valid @RequestPart("photo") MultipartFile photo,
            @Valid @RequestPart("photo_mobile") MultipartFile photoMobile) {
        return this.iPostService.createPost(createPostDTO, photo, photoMobile);
    }
}
