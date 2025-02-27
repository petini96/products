package br.com.roboticsmind.products.services.impl;

import java.util.ArrayList;
import java.util.List;
import br.com.roboticsmind.products.dto.post.CreatePostDTO;
import br.com.roboticsmind.products.dto.post.ListPostDTO;
import br.com.roboticsmind.products.exceptions.FileUploadException;
import br.com.roboticsmind.products.exceptions.PostNotFoundException;
import br.com.roboticsmind.products.exceptions.PostSaveException;
import br.com.roboticsmind.products.models.Post;
import br.com.roboticsmind.products.repositories.PostRepository;
import br.com.roboticsmind.products.services.IPostService;
import br.com.roboticsmind.products.services.IStorageService;
import br.com.roboticsmind.products.services.impl.helper.FileUploadHandler;
import br.com.roboticsmind.products.services.impl.helper.PostFactory;
import br.com.roboticsmind.products.services.impl.helper.PostValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PostServiceImpl implements IPostService {

    private static final Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);

    private final PostRepository postRepository;
    private final FileUploadHandler fileUploadHandler;
    private final String bucketName;

    public PostServiceImpl(PostRepository postRepository,
                           IStorageService storageService,
                           @Value("${storage.bucket.posts:posts}") String bucketName) {
        this.postRepository = postRepository;
        this.fileUploadHandler = new FileUploadHandler(storageService, bucketName);
        this.bucketName = bucketName;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Post createPost(CreatePostDTO createPostDTO, MultipartFile photo, MultipartFile photoMobile) {
        PostValidator.validate(createPostDTO, photo, photoMobile);
        Post post = PostFactory.createPost(createPostDTO);
        List<String> uploadedFiles = new ArrayList<>();

        try {
            String mainPhotoUrl = fileUploadHandler.uploadFile(photo, uploadedFiles);
            String mobilePhotoUrl = fileUploadHandler.uploadFile(photoMobile, uploadedFiles);

            post.setMedia(mainPhotoUrl);
            post.setMediaMobile(mobilePhotoUrl);

            Post savedPost = postRepository.save(post);
            logger.info("Post created successfully with ID: {} and title: {}", savedPost.getId(), savedPost.getTitle());
            return savedPost;
        } catch (FileUploadException e) {
            fileUploadHandler.cleanupFiles(uploadedFiles);
            throw e;
        } catch (Exception e) {
            fileUploadHandler.cleanupFiles(uploadedFiles);
            throw new PostSaveException("Failed to save post with title: " + createPostDTO.getTitle(), e);
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