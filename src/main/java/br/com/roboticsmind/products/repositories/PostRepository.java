package br.com.roboticsmind.products.repositories;

import br.com.roboticsmind.products.dto.post.ListPostDTO;
import br.com.roboticsmind.products.models.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("SELECT new br.com.roboticsmind.products.dto.post.ListPostDTO(p.id, p.media, p.mediaMobile, p.title, p.description, p.order ) FROM Post p")
    Page<ListPostDTO> findAllPostDTO(Pageable pageable);

    List<Post> findByTitle(String title);

    Post findById(long id);
}
