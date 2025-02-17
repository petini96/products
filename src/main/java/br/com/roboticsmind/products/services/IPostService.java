package br.com.roboticsmind.products.services;

import br.com.roboticsmind.products.dto.post.CreatePostDTO;
import br.com.roboticsmind.products.dto.post.ListPostDTO;
import br.com.roboticsmind.products.dto.product.ListAllProductsDTO;
import br.com.roboticsmind.products.models.Post;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface IPostService {
    public Post createPost(CreatePostDTO createPostDTO, MultipartFile photo, MultipartFile photoMobile);
    public Post getPost(Long postId);
    public Page<ListPostDTO> listPosts(int page, int size);
}
