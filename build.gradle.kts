plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.5.7"
    id("io.spring.dependency-management") version "1.1.7"
    kotlin("plugin.jpa") version "1.9.25"
    id("org.openapi.generator") version "7.10.0"
}

group = "com.odeng"
version = "0.0.1-SNAPSHOT"
description = "OdengFinance"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.liquibase:liquibase-core")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.hibernate.orm:hibernate-envers")
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.5")

    // JWT dependencies
    implementation("io.jsonwebtoken:jjwt-api:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6")

    // OpenAPI/Swagger dependencies for generated code
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.7.0")
    implementation("io.swagger.core.v3:swagger-annotations:2.2.27")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    runtimeOnly("org.postgresql:postgresql")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.springframework.security:spring-security-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

// ============================================================================
// OpenAPI Code Generation Configuration
// ============================================================================

// Define the generated source directory
val generatedSourcesDir = layout.buildDirectory.dir("generated/openapi/src/main/kotlin").get().asFile

// Add generated sources to the main source set
sourceSets {
    main {
        kotlin {
            srcDir(generatedSourcesDir)
        }
    }
}

// Dynamic OpenAPI code generation - automatically discovers all YAML files in api-specs directory
val apiSpecsDir = file("$rootDir/src/main/resources/rest")
val generatedApiTasks = mutableListOf<String>()

// Common configuration for all OpenAPI generation tasks
fun org.openapitools.generator.gradle.plugin.tasks.GenerateTask.configureOpenApiGeneration() {
    generatorName.set("kotlin-spring")
    outputDir.set(layout.buildDirectory.dir("generated/openapi").get().asFile.absolutePath)
    apiPackage.set("com.odeng.finance.interfaces.rest.api")
    modelPackage.set("com.odeng.finance.interfaces.rest.api.model")

    configOptions.set(mapOf(
        "dateLibrary" to "java8",
        "interfaceOnly" to "true",              // Generate only interfaces, not implementations
        "skipDefaultInterface" to "true",       // Don't generate default methods
        "useTags" to "true",                    // Use tags for API grouping
        "useSpringBoot3" to "true",             // Use Spring Boot 3 annotations
        "useBeanValidation" to "true",          // Add Bean Validation annotations
        "performBeanValidation" to "true",      // Enable validation
        "reactive" to "false",                  // Use traditional Spring MVC (not WebFlux)
        "serializationLibrary" to "jackson",    // Use Jackson for JSON
        "enumPropertyNaming" to "UPPERCASE",    // Enum naming convention
        "documentationProvider" to "springdoc"  // Use SpringDoc for API docs
    ))

    typeMappings.set(mapOf(
        "DateTime" to "java.time.Instant",
        "Date" to "java.time.LocalDate"
    ))

    importMappings.set(mapOf(
        "java.time.Instant" to "java.time.Instant",
        "java.time.LocalDate" to "java.time.LocalDate"
    ))
}

// Dynamically create generation tasks for each OpenAPI spec file
if (apiSpecsDir.exists()) {
    apiSpecsDir.listFiles { file -> file.extension == "yaml" || file.extension == "yml" }?.forEach { specFile ->
        // Skip common-components.yaml as it's only used for references
        if (specFile.name == "common-components.yml") {
            return@forEach
        }

        // Convert filename to task name (e.g., "accounts-api.yaml" -> "generateAccountsApi")
        val taskName = "generate" + specFile.nameWithoutExtension
            .split("-")
            .joinToString("") { it.replaceFirstChar { char -> char.uppercase() } }
            .replace("Api", "") + "Api"

        tasks.register<org.openapitools.generator.gradle.plugin.tasks.GenerateTask>(taskName) {
            inputSpec.set(specFile.absolutePath)
            configureOpenApiGeneration()
            group = "openapi"
            description = "Generate API from ${specFile.name}"
        }

        generatedApiTasks.add(taskName)
    }
}

// Aggregate task to generate all APIs
tasks.register("generateApis") {
    dependsOn(generatedApiTasks)
    group = "openapi"
    description = "Generate all API interfaces and models from OpenAPI specifications"
}

// Make compileKotlin depend on API generation
tasks.named("compileKotlin") {
    dependsOn("generateApis")
}
