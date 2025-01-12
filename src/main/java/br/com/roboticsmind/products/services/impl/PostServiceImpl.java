package br.com.roboticsmind.products.services.impl;

import br.com.roboticsmind.products.dto.post.CreatePostDTO;
import br.com.roboticsmind.products.dto.post.ListPostDTO;
import br.com.roboticsmind.products.models.Post;
import br.com.roboticsmind.products.repositories.PostRepository;
import br.com.roboticsmind.products.services.IPostService;
import br.com.roboticsmind.products.services.MinioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
public class PostServiceImpl implements IPostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MinioService minioService;

    @Override
    public Post createPost(CreatePostDTO createPostDTO, MultipartFile photo) {
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

            this.postRepository.save(newPost);

            return newPost;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar o post e as imagens", e);
        }
    }

    @Override
    public Post getPost(Long postId) {
        return this.postRepository.findById(postId).get();
    }

    @Override
    public Page<ListPostDTO> listPosts(int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        return this.postRepository.findAllPostDTO(pageable);
    }
}
