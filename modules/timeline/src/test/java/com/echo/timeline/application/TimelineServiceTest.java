package com.echo.timeline.application;

import com.echo.timeline.domain.FeedItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TimelineServiceTest {

    private static final LocalDateTime BASE = LocalDateTime.of(2026, 1, 1, 12, 0);

    private final TimelineService timelineService = new TimelineService();

    @Test
    @DisplayName("섞인 순서의 후보를 최신순으로 조립한다")
    void assembleLatest_sortsLatestFirst() {
        FeedItem oldest = item(1L, BASE);
        FeedItem middle = item(2L, BASE.plusMinutes(1));
        FeedItem newest = item(3L, BASE.plusMinutes(2));

        List<FeedItem> timeline = timelineService.assembleLatest(List.of(middle, oldest, newest));

        assertEquals(List.of(newest, middle, oldest), timeline);
    }

    @Test
    @DisplayName("작성 시각이 같으면 postId 역순으로 조립한다")
    void assembleLatest_sameCreatedAt_sortsByPostIdDesc() {
        FeedItem first = item(1L, BASE);
        FeedItem second = item(2L, BASE);

        List<FeedItem> timeline = timelineService.assembleLatest(List.of(first, second));

        assertEquals(List.of(second, first), timeline);
    }

    @Test
    @DisplayName("후보가 없으면 빈 타임라인")
    void assembleLatest_empty() {
        assertTrue(timelineService.assembleLatest(List.of()).isEmpty());
    }

    private static FeedItem item(Long postId, LocalDateTime createdAt) {
        return new FeedItem(postId, 1L, "content-" + postId, createdAt);
    }
}
