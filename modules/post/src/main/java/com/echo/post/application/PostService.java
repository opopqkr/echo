package com.echo.post.application;

import com.echo.post.domain.Post;
import com.echo.post.infrastructure.PostRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Post 도메인 애플리케이션 서비스. 글 작성·조회 유스케이스.
 */
@Service
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Transactional
    public Post create(Long authorId, String content) {
        Post post = new Post(authorId, content);
        return postRepository.save(post);
    }

    /** 없으면 빈 Optional — 404 같은 HTTP 처리는 호출하는 쪽(컨트롤러)의 몫. */
    public Optional<Post> findById(Long id) {
        return postRepository.findById(id);
    }

    public List<Post> findAllLatest() {
        return postRepository.findAllByOrderByCreatedAtDescIdDesc();
    }
}
