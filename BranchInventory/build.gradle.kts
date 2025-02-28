plugins {
	java
	id("org.springframework.boot") version "3.4.3"
	id("io.spring.dependency-management") version "1.1.7"
	id("io.freefair.lombok") version "8.6"
}

group = "system.integration"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	// Spring Boot
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-amqp") // RabbitMQ

	// PostgreSQL
	runtimeOnly("org.postgresql:postgresql")

	// Lombok
	compileOnly("org.projectlombok:lombok:1.18.32")
	annotationProcessor("org.projectlombok:lombok:1.18.32")

	// Validación de datos
	implementation("org.springframework.boot:spring-boot-starter-validation")

	// Serialización JSON
	implementation("com.fasterxml.jackson.core:jackson-databind:2.15.3")

	// Testing
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
