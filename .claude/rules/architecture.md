# 아키텍처 규칙 (항상 적용)

어느 파일을 수정하든 항상 지켜야 하는 구조 규칙.

- **의존성 방향**: `apps/*` → `modules/*`, `common` / `modules/*` → `common`
- **모듈 간 직접 의존 금지**: `modules/post` ↔ `modules/user`처럼 모듈 간의 의존 금지
- **도메인 모듈에 Spring Web 의존 추가 금지**: POJO + JPA만
- **응답 포맷**: 컨트롤러 응답은 `common`의 `ApiResponse<T>`로 Wrapping
