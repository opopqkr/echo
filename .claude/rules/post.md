---
paths:
  - "modules/post/**"
  - "apps/customer-api/src/**/post/**"
---

# Post 모듈 규칙

## 도메인 불변식

- **content**: null·빈 값 금지, 280자 초과 금지(`Post.MAX_CONTENT_LENGTH`), 도메인 생성자에서 검증
- **createdAt**: 생성 시 고정, 수정 불가
- **setter 금지**: getter만 노출. 상태 변경이 필요하면 의도가 드러나는 도메인 메서드로 (예: `edit(content)`), 메서드 안에서 불변식 재검증

## 계층

- **domain**: JPA 엔티티 + 불변식, Spring 의존 없음
- **application**: `@Service`, 클래스 `@Transactional(readOnly = true)` + 쓰기 메서드 `@Transactional`
- **infrastructure**: `JpaRepository` 상속 인터페이스

## HTTP 관심사

- **Controller·DTO 위치**: post 모듈이 아닌 `apps/customer-api` (`com.echo.api.post`)
- **DTO**: record
- **요청 DTO 검증**: Bean Validation(`@NotBlank`, `@Size(max = 280)`) 적용. 도메인 예외(500)까지 가기 전에 400으로 차단
