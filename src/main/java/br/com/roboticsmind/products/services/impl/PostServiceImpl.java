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
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public PostServiceImpl(PostRepository postRepository,
                           FileUploadHandler fileUploadHandler,
                           @Value("${storage.bucket.posts:posts}") String bucketName) {
        this.postRepository = postRepository;
        this.fileUploadHandler = fileUploadHandler;
        this.bucketName = bucketName;
    }

    public Post createPost(CreatePostDTO dto, MultipartFile photo, MultipartFile photoMobile) throws PostSaveException {
        List<String> uploadedFiles = new ArrayList<>();
        try {
            String photoUrl = fileUploadHandler.uploadFile(photo, uploadedFiles);
            String mobileUrl = fileUploadHandler.uploadFile(photoMobile, uploadedFiles);

            Post post = new Post();
            post.setTitle(dto.getTitle());
            post.setDescription(dto.getDescription());
            post.setMedia(photoUrl);
            post.setMediaMobile(mobileUrl);

            return postRepository.save(post);
        } catch (FileUploadException e) {
            fileUploadHandler.cleanupFiles(uploadedFiles);
            throw new PostSaveException("Failed to save post due to file upload error", e);
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