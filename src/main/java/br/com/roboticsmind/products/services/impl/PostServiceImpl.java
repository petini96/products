package br.com.roboticsmind.products.services.impl;

import java.io.InputStream;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import br.com.roboticsmind.products.dto.post.CreatePostDTO;
import br.com.roboticsmind.products.dto.post.ListPostDTO;
import br.com.roboticsmind.products.exceptions.ProductNotFoundException;
import br.com.roboticsmind.products.models.Post;
import br.com.roboticsmind.products.repositories.PostRepository;
import br.com.roboticsmind.products.services.IPostService;
import br.com.roboticsmind.products.services.MinioService;

@Service
public class PostServiceImpl implements IPostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MinioService minioService;

    @Transactional
    @Override
    public Post createPost(CreatePostDTO createPostDTO, MultipartFile photo, MultipartFile photoMobile) {
        try {
            Post newPost = new Post();
            newPost.setTitle(createPostDTO.getTitle());
            newPost.setDescription(createPostDTO.getDescription());
            newPost.setOrder(createPostDTO.getOrder());

            String photoName = photo.getOriginalFilename();
            try (InputStream photoStream = photo.getInputStream()) {
                String photoUrl = this.minioService.uploadToBucket("posts", photoName, photoStream, photo.getContentType());
                newPost.setMedia(photoUrl);
            }

            String photoMobileName = photoMobile.getOriginalFilename();
            try (InputStream photoStream = photoMobile.getInputStream()) {
                String photoUrl = this.minioService.uploadToBucket("posts", photoMobileName, photoStream, photoMobile.getContentType());
                newPost.setMediaMobile(photoUrl);
            }

            this.postRepository.save(newPost);

            return newPost;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Erro ao salvar o post e as imagens", e);
        }
    }


    @Override
    public Post getPost(Long postId) {
        Optional<Post> optionalPost = this.postRepository.findById(postId);
        if (!optionalPost.isPresent()) throw new ProductNotFoundException(postId);
        return optionalPost.get();
    }

    @Override
    public Page<ListPostDTO> listPosts(int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        return this.postRepository.findAllPostDTO(pageable);
    }
}
