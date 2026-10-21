package com.echo.timeline.domain;

import java.time.LocalDateTime;

/**
 * 피드에 노출되는 항목 하나. 원본 엔티티와 무관한 timeline 자체의 값 객체.
 */
public record FeedItem(Long postId, Long authorId, String content, LocalDateTime createdAt) {
}
