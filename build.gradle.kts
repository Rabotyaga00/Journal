plugins {
    id("java")
    id("org.springframework.boot") version "3.1.5"
    id("io.spring.dependency-management") version "1.1.3"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {

    implementation ("org.springframework.boot:spring-boot-starter-web")

    // Spring Boot Thymeleaf
    implementation ("org.springframework.boot:spring-boot-starter-thymeleaf")

    // Spring Boot Data JPA
    implementation ("org.springframework.boot:spring-boot-starter-data-jpa")

    // PostgreSQL Driver
    runtimeOnly ("org.postgresql:postgresql")

    // Spring Boot Validation
    implementation ("org.springframework.boot:spring-boot-starter-validation")

    implementation("org.liquibase:liquibase-core")

    // Spring Boot DevTools
//    developmentOnly ("org.springframework.boot:spring-boot-devtools")

    // Lombok
    compileOnly ("org.projectlombok:lombok")
    annotationProcessor ("org.projectlombok:lombok")

    // Spring Boot Test
    testImplementation ("org.springframework.boot:spring-boot-starter-test")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.withType<org.springframework.boot.gradle.tasks.run.BootRun> {
    mainClass.set("org.example.Main")
}

tasks.getByName<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    mainClass.set("org.example.Main")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

tasks.test {
    useJUnitPlatform()
}