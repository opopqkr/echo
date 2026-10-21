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
 * GET /timeline 통합 테스트.
 * post 조회 결과가 컨트롤러에서 FeedItem으로 조립되어 최신순으로 나오는지 검증한다.
 */
@SpringBootTest
@AutoConfigureMockMvc
class TimelineIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /timeline — 최신 글부터 반환")
    void getTimeline_latestFirst() throws Exception {
        long olderId = createPost("타임라인 먼저 쓴 글");
        long newerId = createPost("타임라인 나중에 쓴 글");

        mockMvc.perform(get("/timeline"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].postId").value(newerId))
                .andExpect(jsonPath("$.data[0].content").value("타임라인 나중에 쓴 글"))
                .andExpect(jsonPath("$.data[1].postId").value(olderId));
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
