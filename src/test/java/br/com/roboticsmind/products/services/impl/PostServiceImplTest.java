package br.com.roboticsmind.products.services.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import br.com.roboticsmind.products.exceptions.PostSaveException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;
import br.com.roboticsmind.products.dto.post.CreatePostDTO;
import br.com.roboticsmind.products.dto.post.ListPostDTO;
import br.com.roboticsmind.products.exceptions.FileUploadException;
import br.com.roboticsmind.products.exceptions.PostNotFoundException;
import br.com.roboticsmind.products.models.Post;
import br.com.roboticsmind.products.repositories.PostRepository;
import br.com.roboticsmind.products.services.IStorageService;
import br.com.roboticsmind.products.services.impl.helper.FileUploadHandler;

@ExtendWith(MockitoExtension.class)
public class PostServiceImplTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private FileUploadHandler fileUploadHandler;

    @Mock
    private IStorageService storageService;

    @InjectMocks
    private PostServiceImpl postService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void createPost_Success() throws Exception {
        CreatePostDTO dto = new CreatePostDTO("Title", "Description", 1);
        MultipartFile photo = mock(MultipartFile.class);
        MultipartFile photoMobile = mock(MultipartFile.class);
        Post savedPost = new Post();
        savedPost.setId(1L);
        savedPost.setMedia("photo-url");
        savedPost.setMediaMobile("mobile-url");
        savedPost.setTitle("Title");

        when(fileUploadHandler.uploadFile(eq(photo), anyList())).thenReturn("photo-url");
        when(fileUploadHandler.uploadFile(eq(photoMobile), anyList())).thenReturn("mobile-url");
        when(postRepository.save(any(Post.class))).thenReturn(savedPost);

        Post result = postService.createPost(dto, photo, photoMobile);

        assertNotNull(result);
        assertEquals("photo-url", result.getMedia());
        assertEquals("mobile-url", result.getMediaMobile());
        verify(postRepository, times(1)).save(any(Post.class));
        verify(fileUploadHandler, times(1)).uploadFile(eq(photo), anyList());
        verify(fileUploadHandler, times(1)).uploadFile(eq(photoMobile), anyList());
    }

    @Test
    void createPost_FileUploadFailure_CleanupCalled() {
        CreatePostDTO dto = new CreatePostDTO("Title", "Description", 1);
        MultipartFile photo = mock(MultipartFile.class);
        MultipartFile photoMobile = mock(MultipartFile.class);
        List<String> uploadedFiles = new ArrayList<>();

        when(fileUploadHandler.uploadFile(eq(photo), anyList())).thenAnswer(invocation -> {
            List<String> list = invocation.getArgument(1);
            list.add("file1.txt");
            return "photo-url";
        });

        when(fileUploadHandler.uploadFile(eq(photoMobile), anyList())).thenAnswer(invocation -> {
            List<String> list = invocation.getArgument(1);
            list.add("file2.txt");
            throw new FileUploadException("Upload failed", new RuntimeException("Simulated error"));
        });

        assertThrows(PostSaveException.class, () -> postService.createPost(dto, photo, photoMobile));

        verify(fileUploadHandler, times(1)).cleanupFiles(anyList());
    }

    @Test
    void getPost_Success() {
        Post post = new Post();
        post.setId(1L);
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        Post result = postService.getPost(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getPost_NotFound() {
        when(postRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(PostNotFoundException.class, () -> postService.getPost(1L));
    }

    @Test
    void listPosts_Success() {
        ListPostDTO dto = new ListPostDTO();
        Page<ListPostDTO> page = new PageImpl<>(List.of(dto));
        when(postRepository.findAllPostDTO(any(PageRequest.class))).thenReturn(page);

        Page<ListPostDTO> result = postService.listPosts(0, 10);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }
}