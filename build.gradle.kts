plugins {
    java
    id("org.springframework.boot") version "3.2.11"
    id("io.spring.dependency-management") version "1.1.6"
    id("maven-publish")
}

group = "kr.or.komca.foundation"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

dependencies {
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-web")

    // JWT
    implementation("io.jsonwebtoken:jjwt-api:0.12.3")
    implementation("io.jsonwebtoken:jjwt-impl:0.12.3")  // runtimeOnly -> implementation
    implementation("io.jsonwebtoken:jjwt-jackson:0.12.3")  // runtimeOnly -> implementation

    // DB
    runtimeOnly("com.oracle.database.jdbc:ojdbc11:21.9.0.0")  // Oracle 21c에 적합한 ojdbc11 버전
    implementation("org.mybatis.spring.boot:mybatis-spring-boot-starter:3.0.3")

    // Spring AOP
    implementation("org.springframework.boot:spring-boot-starter-aop")

    // Swagger
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0")

    implementation("org.springframework.boot:spring-boot-starter-validation")

    // BOM 임포트
    implementation(platform("kr.or.komca:dependencies-bom:0.3.0"))

    // komca 라이브러리
    implementation("kr.or.komca:komca-data-core")

    implementation("kr.or.komca:interface:0.0.1-SNAPSHOT")
}

tasks.withType<Test> {
    useJUnitPlatform()
}


publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            // url은 https://maven.pkg.github.com/YOUR_ORGANIZATION/githun repository 이름
            url = uri("https://maven.pkg.github.com/org-test-hk/komca-jwt-verification")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }

    publications {
        create<MavenPublication>("gpr") {
            from(components["java"])
            version = System.getenv("NEW_VERSION") ?: "0.0.1-SNAPSHOT"
            groupId = "kr.or.komca.foundation"
            artifactId = "verification"

            versionMapping {
                usage("java-api") {
                    fromResolutionOf("runtimeClasspath")
                }
                usage("java-runtime") {
                    fromResolutionResult()
                }
            }
        }
    }
}


repositories {
    mavenLocal()    // Local 테스트 용
    mavenCentral()

    // BOM
    maven {
        name = "GitHubPackages-bom"
        url = uri("https://maven.pkg.github.com/org-test-hk/dependencies-bom")
        credentials {
            username = System.getenv("GITHUB_ACTOR") ?: project.findProperty("gpr.user") as String? ?: ""
            password = System.getenv("GITHUB_TOKEN") ?: project.findProperty("gpr.key") as String? ?: ""
        }
    }

    maven {
        name = "GitHubPackages-data-core"
        url = uri("https://maven.pkg.github.com/org-test-hk/komca-data-core")
        credentials {
            username = System.getenv("GITHUB_ACTOR") ?: project.findProperty("gpr.user") as String? ?: ""
            password = System.getenv("GITHUB_TOKEN") ?: project.findProperty("gpr.key") as String? ?: ""
        }
    }

    maven {
        name = "GitHubPackages-auth-core"
        url = uri("https://maven.pkg.github.com/org-test-hk/komca-common-exception")
        credentials {
            username = System.getenv("GITHUB_ACTOR") ?: project.findProperty("gpr.user") as String? ?: ""
            password = System.getenv("GITHUB_TOKEN") ?: project.findProperty("gpr.key") as String? ?: ""
        }
    }

    maven {
        name = "GitHubPackages-auth-core"
        url = uri("https://maven.pkg.github.com/org-test-hk/komca-common-interface")
        credentials {
            username = System.getenv("GITHUB_ACTOR") ?: project.findProperty("gpr.user") as String? ?: ""
            password = System.getenv("GITHUB_TOKEN") ?: project.findProperty("gpr.key") as String? ?: ""
        }
    }
}