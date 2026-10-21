// timeline 모듈 — 최신순 피드 조립 (POJO, Spring Web·JPA 의존 없음)
plugins {
    id("java")
    id("io.spring.dependency-management")
}

apply(plugin = "io.spring.dependency-management")

the<io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension>().apply {
    imports {
        mavenBom("org.springframework.boot:spring-boot-dependencies:3.5.0")
    }
}

dependencies {
    implementation(project(":common"))

    // @Service용 (Spring Web 없음)
    implementation("org.springframework:spring-context")
}
