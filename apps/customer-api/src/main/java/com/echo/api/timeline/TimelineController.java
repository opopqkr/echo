package com.echo.api.timeline;

import com.echo.common.ApiResponse;
import com.echo.post.application.PostService;
import com.echo.timeline.application.TimelineService;
import com.echo.timeline.domain.FeedItem;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.web.bind.annotation.*;

/**
 * 타임라인 HTTP API 컨트롤러.
 * 모듈 간 직접 의존이 금지되어 있어, post에서 피드 후보를 가져와 timeline에 넘기는 연결을 여기(apps)서 한다.
 */
@RestController
@RequestMapping("/timeline")
@Tag(name = "Timeline", description = "타임라인 조회 API")
public class TimelineController {

    private final PostService postService;
    private final TimelineService timelineService;

    public TimelineController(PostService postService, TimelineService timelineService) {
        this.postService = postService;
        this.timelineService = timelineService;
    }

    @GetMapping
    @Operation(summary = "타임라인 조회", description = "전체 글을 최신순으로 조립한 피드를 조회합니다.")
    public ApiResponse<List<TimelineItemResponse>> getTimeline() {
        List<FeedItem> candidates = postService.findAllLatest().stream()
            .map(post -> new FeedItem(
                post.getId(),
                post.getAuthorId(),
                post.getContent(),
                post.getCreatedAt()
            ))
            .toList();

        List<TimelineItemResponse> items = timelineService.assembleLatest(candidates).stream()
            .map(TimelineItemResponse::from)
            .toList();
        return ApiResponse.ok(items);
    }
}
