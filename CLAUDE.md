# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

> **응답 언어 : 한국어**

## 프로젝트 소개

**Echo** — Threads류 텍스트 SNS 백엔드.

## 기술 스택

Java 25 · Spring Boot 3.5.x · Gradle 멀티프로젝트(Kotlin DSL) · H2 · JUnit 5

## 멀티모듈 구조

- `apps/customer-api` - HTTP 진입점 — Controller, 요청/응답 DTO (유일한 Boot 앱)
- `modules/post` - 도메인 모듈 — 엔티티, 서비스, 리포지토리
- `modules/user` - 도메인 모듈
- `common` - 공통 응답·오류 포맷 (ApiResponse, ApiError) — 순수 Java

## 핵심 규칙

- **각 모듈은 hexagonal**: `domain` / `application` / `infrastructure` 계층
- **DTO는 record**
- **패키지 규칙**: `com.echo.{module}.{layer}`

## 빌드/실행/테스트

```bash
./gradlew build                                        # 전체 빌드 + 테스트
./gradlew :apps:customer-api:bootRun                   # 실행 → :8080
./gradlew test                                         # 전체 테스트
```