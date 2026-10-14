# Echo

Threads류 텍스트 SNS 백엔드.

> AI에 대한 지식 베이스와 실무에서의 하네스 구성에 대한 전문성을 더 높이기 위해,</br>강의를 따라 학습하며 작성한 **개인 학습용 프로젝트**입니다.

### 📌 출처

- **강의**: [네카라쿠배 시니어의 실무 하네스 엔지니어링 : 실전 Spring Boot 환경에서 사용하는 클로드 코드](https://www.inflearn.com/course/the-standard-of-clau) (인프런)
- **지식공유자**: 실용주의개발

<ins>프로젝트의 설계와 구성은 위 강의를 바탕으로 하며, 문제가 있을 경우 즉시 삭제하겠습니다.</ins>

## 기술 스택

- Java 25
- Spring Boot 3.5.x (Web, Data JPA, Validation, Actuator)
- Gradle 멀티프로젝트 (Kotlin DSL)
- H2 (인메모리)
- JUnit 5
- springdoc-openapi (Swagger UI)

## 모듈 구조

```
echo
├── apps
│   └── customer-api    HTTP API 진입점 (Spring Boot 애플리케이션)
├── modules
│   ├── post            Post 도메인
│   └── user            User 도메인
└── common              공통 응답·오류 포맷
```

- **의존성 방향**: `apps → modules, common` / `modules → common`
- **모듈 간 직접 의존 금지**
- **도메인 모듈 계층**: `domain` / `application` / `infrastructure`

## 시작하기

### 요구 사항

- JDK 25 (없으면 Gradle toolchain이 자동으로 내려받음)

### 빌드·실행·테스트

```bash
./gradlew build                          # 전체 빌드 + 테스트
./gradlew :apps:customer-api:bootRun     # 실행 → http://localhost:8080
./gradlew test                           # 전체 테스트
```

Windows PowerShell에서는 `./gradlew` 대신 `.\gradlew.bat`을 사용한다.

### 실행 후 확인

| 용도 | URL |
|---|---|
| Swagger UI | http://localhost:8080/swagger-ui.html |
| H2 콘솔 | http://localhost:8080/h2-console (`jdbc:h2:mem:echo`, `sa`) |
| Health Check | http://localhost:8080/actuator/health |

## API

### 글 작성

```http
POST /posts
X-User-Id: 1
Content-Type: application/json

{ "content": "안녕, echo!" }
```

```json
{
  "success": true,
  "data": {
    "id": 1,
    "authorId": 1,
    "content": "안녕, echo!",
    "createdAt": "2026-10-14T19:30:00"
  },
  "message": "글이 작성되었습니다."
}
```

- `content`: 필수, 최대 280자 (초과 시 `400 Bad Request`)
- `X-User-Id`: 작성자 ID (생략 시 `1`)
