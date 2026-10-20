package com.echo.api;

import com.echo.api.post.CreatePostRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * POST /posts 통합 테스트.
 * Web + JPA + H2 + 크로스-모듈 의존성이 전부 연결되는지 검증한다.
 */
@SpringBootTest
@AutoConfigureMockMvc
class PostIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /posts — 정상 글 작성 시 201 반환")
    void createPost_success() throws Exception {
        CreatePostRequest request = new CreatePostRequest("안녕, echo!");

        mockMvc.perform(post("/posts")
                .header("X-User-Id", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content").value("안녕, echo!"));
    }

    @Test
    @DisplayName("POST /posts — 280자 초과 시 400 반환")
    void createPost_contentTooLong_400() throws Exception {
        // 281자 content
        String longContent = "a".repeat(281);
        CreatePostRequest request = new CreatePostRequest(longContent);

        mockMvc.perform(post("/posts")
                .header("X-User-Id", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /posts/{id} — 존재하는 글 조회 시 200 반환")
    void getPost_success() throws Exception {
        long id = createPost("단건 조회용 글");

        mockMvc.perform(get("/posts/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(id))
                .andExpect(jsonPath("$.data.content").value("단건 조회용 글"));
    }

    @Test
    @DisplayName("GET /posts/{id} — 없는 글 조회 시 404 반환")
    void getPost_notFound_404() throws Exception {
        mockMvc.perform(get("/posts/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /posts — 최신 글부터 정렬")
    void getPosts_latestFirst() throws Exception {
        long olderId = createPost("먼저 쓴 글");
        long newerId = createPost("나중에 쓴 글");

        mockMvc.perform(get("/posts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].id").value(newerId))
                .andExpect(jsonPath("$.data[1].id").value(olderId));
    }

    private long createPost(String content) throws Exception {
        String body = mockMvc.perform(post("/posts")
                .header("X-User-Id", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new CreatePostRequest(content))))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return objectMapper.readTree(body).path("data").path("id").asLong();
    }
}
