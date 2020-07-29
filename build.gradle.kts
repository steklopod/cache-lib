import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    val kotlin = "1.4.0-rc"
    `java-library`
    kotlin("jvm") version kotlin
    kotlin("plugin.spring") version kotlin
    id("org.springframework.boot") version "2.3.2.RELEASE"
    id("io.spring.dependency-management") version "1.0.9.RELEASE"
    id("com.github.ben-manes.versions") version "0.29.0"
}

repositories {
    mavenCentral(); mavenLocal(); maven("https://dl.bintray.com/kotlin/kotlin-eap")
}

dependencies {
    implementation("org.springframework.boot", "spring-boot-starter-data-redis")
    implementation("org.springframework.boot", "spring-boot-starter-cache")
//    implementation("org.jetbrains.kotlin", "kotlin-stdlib-jdk8")
//    implementation("com.fasterxml.jackson.module", "jackson-module-kotlin")
    compileOnly("org.projectlombok:lombok")
    testImplementation("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
}


tasks {
    val java = "11"
    withType<KotlinCompile> {
        kotlinOptions { jvmTarget = java }; sourceCompatibility = java; targetCompatibility = java
    }
    test { useJUnitPlatform() }
    bootJar { mainClassName = "NONE" }
}
