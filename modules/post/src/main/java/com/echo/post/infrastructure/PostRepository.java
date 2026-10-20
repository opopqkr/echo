package com.echo.post.infrastructure;

import com.echo.post.domain.Post;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Post JPA 리포지토리. infrastructure 계층.
 */
public interface PostRepository extends JpaRepository<Post, Long> {

    /** 최신 글부터. createdAt이 같으면 id 역순으로 순서를 고정한다. */
    List<Post> findAllByOrderByCreatedAtDescIdDesc();
}
