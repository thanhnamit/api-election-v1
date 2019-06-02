import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.hidetake.gradle.swagger.generator.GenerateSwaggerCode
import kotlin.collections.mapOf

plugins {
	id("org.springframework.boot") version "2.1.5.RELEASE"
	id("io.spring.dependency-management") version "1.0.7.RELEASE"
	id("org.hidetake.swagger.generator") version "2.18.1"
	id("com.google.cloud.tools.jib") version "1.2.0"
	kotlin("jvm") version "1.2.71"
	kotlin("plugin.spring") version "1.2.71"
}

group = "my.gov.election"
version = "0.0.1-SNAPSHOT"
extra["springCloudVersion"] = "Greenwich.SR1"

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.springframework.cloud:spring-cloud-starter-sleuth")
	implementation("org.springframework.cloud:spring-cloud-starter-zipkin")
	implementation("org.drools:drools-core:7.19.0.Final")
	implementation("org.kie:kie-spring:7.19.0.Final")

	// swagger
	implementation("io.swagger:swagger-annotations:1.5.22")
	swaggerCodegen("io.swagger.codegen.v3:swagger-codegen-cli:3.0.8")
	swaggerUI("org.webjars:swagger-ui:3.10.0")

	// test
	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(group = "junit", module = "junit")
	}
	testImplementation("org.junit.jupiter:junit-jupiter-api:5.4.2")
	testImplementation("org.junit.jupiter:junit-jupiter-engine:5.4.2")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
	}
}

val basePkg = "my.gov.election"
val apiSpec = "src/main/resources/api/api.yaml"
val srcDir = "src/main/java/my/gov/election"

swaggerSources {
	create("api") {
		code(closureOf<GenerateSwaggerCode> {
			inputFile = file(apiSpec)
			language = "spring"
			additionalProperties = mapOf(
					"modelPackage" to "$basePkg.models",
					"apiPackage" to "$basePkg.apis",
					"java8" to "true",
					"dateLibrary" to "java8",
					"interfaceOnly" to "true",
					"delegatePattern" to "false",
					"useTags" to "true"
			)
			dependsOn(validation)
		})
	}
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "1.8"
	}
	dependsOn(swaggerSources["api"].code)
}

sourceSets {
	main {
		java.srcDir("${swaggerSources["api"].code.outputDir}/src/main/java")
	}
}

jib {
	to {
		image = "api-election-v1:$version"
	}
}
