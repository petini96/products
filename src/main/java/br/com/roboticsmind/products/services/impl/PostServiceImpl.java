package br.com.roboticsmind.products.services.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import br.com.roboticsmind.products.dto.post.CreatePostDTO;
import br.com.roboticsmind.products.dto.post.ListPostDTO;
import br.com.roboticsmind.products.exceptions.FileUploadException;
import br.com.roboticsmind.products.exceptions.PostSaveConstraintViolationException;
import br.com.roboticsmind.products.exceptions.PostSaveException;
import br.com.roboticsmind.products.exceptions.ProductNotFoundException;
import br.com.roboticsmind.products.models.Post;
import br.com.roboticsmind.products.repositories.PostRepository;
import br.com.roboticsmind.products.services.IPostService;
import br.com.roboticsmind.products.services.MinioService;

@Service
public class PostServiceImpl implements IPostService {

    private static final Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MinioService minioService;

   @Transactional
    @Override
    public Post createPost(CreatePostDTO createPostDTO, MultipartFile photo, MultipartFile photoMobile) {
        Post newPost = new Post();
        newPost.setTitle(createPostDTO.getTitle());
        newPost.setDescription(createPostDTO.getDescription());
        newPost.setOrder(createPostDTO.getOrder());

        List<String> uploadedFiles = new ArrayList<>();
        String photoUrl = null;
        String photoMobileUrl = null;

        try {
            String photoName = photo.getOriginalFilename();
            try (InputStream photoStream = photo.getInputStream()) {
                photoUrl = minioService.uploadToBucket("posts", photoName, photoStream, photo.getContentType());
                uploadedFiles.add(photoName);
                logger.info("Uploaded photo: {}", photoName);
            }
            
            String photoMobileName = photoMobile.getOriginalFilename();
            try (InputStream photoMobileStream = photoMobile.getInputStream()) {
                photoMobileUrl = minioService.uploadToBucket("posts", photoMobileName, photoMobileStream, photoMobile.getContentType());
                uploadedFiles.add(photoMobileName);
                logger.info("Uploaded photo mobile: {}", photoMobileName);
            }
            
            newPost.setMedia(photoUrl);
            newPost.setMediaMobile(photoMobileUrl);
            
            postRepository.save(newPost);
            logger.info("Post saved successfully with title: {}", newPost.getTitle());

            return newPost;

        } catch (Exception e) {
            
            String errorMessage;
            RuntimeException exceptionToThrow;

            if (e instanceof DataIntegrityViolationException) {
                errorMessage = "Failed to save post due to constraint violation";
                exceptionToThrow = new PostSaveConstraintViolationException(errorMessage, e);
            } else if (uploadedFiles.size() < 2) {
                errorMessage = "Failed to upload file: " + (uploadedFiles.isEmpty() ? photo.getOriginalFilename() : photoMobile.getOriginalFilename());
                exceptionToThrow = new FileUploadException(errorMessage, e);
            } else {
                errorMessage = "Failed to save post to database";
                exceptionToThrow = new PostSaveException(errorMessage, e);
            }

            logger.error(errorMessage, e);
            cleanupFiles("posts", uploadedFiles);
            throw exceptionToThrow;
        }
    }

    private void cleanupFiles(String bucketName, List<String> fileNames) {
        for (String fileName : fileNames) {
            try {
                minioService.deleteObject(bucketName, fileName);
                logger.info("Cleaned up file: {}", fileName);
            } catch (Exception e) {
                logger.error("Failed to delete file {} during cleanup", fileName, e);
            }
        }
    }
    
    @Override
    public Post getPost(Long postId) {
        Optional<Post> optionalPost = this.postRepository.findById(postId);
        if (!optionalPost.isPresent())
            throw new ProductNotFoundException(postId);
        return optionalPost.get();
    }

    @Override
    public Page<ListPostDTO> listPosts(int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        return this.postRepository.findAllPostDTO(pageable);
    }
}
