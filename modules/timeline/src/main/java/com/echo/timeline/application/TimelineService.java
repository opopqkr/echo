package com.echo.timeline.application;

import com.echo.timeline.domain.FeedItem;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Timeline 애플리케이션 서비스. 호출자가 넘긴 피드 후보를 타임라인으로 조립한다.
 * 후보를 어디서 가져오는지는 모른다 — 모듈 간 직접 의존 금지.
 */
@Service
public class TimelineService {

    private static final Comparator<FeedItem> LATEST_FIRST =
        Comparator.comparing(FeedItem::createdAt)
            .thenComparing(FeedItem::postId)
            .reversed();

    /** 최신순(createdAt 내림차순, 같으면 postId 내림차순)으로 정렬한 불변 리스트를 반환한다. */
    public List<FeedItem> assembleLatest(List<FeedItem> candidates) {
        return candidates.stream()
            .sorted(LATEST_FIRST)
            .toList();
    }
}
