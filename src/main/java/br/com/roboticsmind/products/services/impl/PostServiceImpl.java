package br.com.roboticsmind.products.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.roboticsmind.products.dto.post.CreatePostDTO;
import br.com.roboticsmind.products.dto.post.ListPostDTO;
import br.com.roboticsmind.products.exceptions.FileUploadException;
import br.com.roboticsmind.products.exceptions.PostNotFoundException;
import br.com.roboticsmind.products.exceptions.PostSaveException;
import br.com.roboticsmind.products.models.Post;
import br.com.roboticsmind.products.repositories.PostRepository;
import br.com.roboticsmind.products.services.IPostService;
import br.com.roboticsmind.products.services.impl.helper.FileUploadHandler;
import br.com.roboticsmind.products.services.impl.helper.PostFactory;
import br.com.roboticsmind.products.services.impl.helper.PostValidator;

@Service
public class PostServiceImpl implements IPostService {

    private static final Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);

    private final PostRepository postRepository;
    private final FileUploadHandler fileUploadHandler;

    @Autowired
    public PostServiceImpl(PostRepository postRepository,
            FileUploadHandler fileUploadHandler) {
        this.postRepository = postRepository;
        this.fileUploadHandler = fileUploadHandler;
    }

    @Override
    public Post createPost(CreatePostDTO dto, MultipartFile photo, MultipartFile photoMobile) throws PostSaveException {
        List<String> uploadedFiles = new ArrayList<>();

        try {
            PostValidator.validate(dto, photo, photoMobile);

            Post post = PostFactory.createPost(dto);

            String photoUrl = fileUploadHandler.uploadFile(photo, uploadedFiles);
            String mobileUrl = fileUploadHandler.uploadFile(photoMobile, uploadedFiles);

            post.setMedia(photoUrl);
            post.setMediaMobile(mobileUrl);

            Post savedPost = postRepository.save(post);
            logger.info("Post created successfully with ID: {}", savedPost.getId());
            return savedPost;

        } catch (FileUploadException e) {
            fileUploadHandler.cleanupFiles(uploadedFiles);
            logger.error("Failed to upload files for post creation", e);
            throw new PostSaveException("Failed to save post due to file upload error", e);
        } catch (IllegalArgumentException e) {
            fileUploadHandler.cleanupFiles(uploadedFiles);
            logger.error("Validation error during post creation", e);
            throw new PostSaveException("Invalid post data or files: " + e.getMessage(), e);
        } catch (Exception e) {
            fileUploadHandler.cleanupFiles(uploadedFiles);
            logger.error("Unexpected error during post creation", e);
            throw new PostSaveException("Failed to create post due to an unexpected error", e);
        }
    }

    @Override
    public Post getPost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> {
                    logger.warn("Post not found for ID: {}", postId);
                    return new PostNotFoundException(postId);
                });
    }

    @Override
    public Page<ListPostDTO> listPosts(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<ListPostDTO> posts = postRepository.findAllPostDTO(pageRequest);
        logger.debug("Retrieved {} posts for page {} with size {}", posts.getTotalElements(), page, size);
        return posts;
    }
}