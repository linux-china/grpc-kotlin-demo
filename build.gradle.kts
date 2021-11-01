import com.google.protobuf.gradle.*

val javaVersion = "1.8"
val protobufVersion = "3.19.1"
val grpcVersion = "1.41.0"
val grpcKotlinVersion = "1.2.0"

plugins {
    kotlin("jvm") version "1.5.31"
    id("com.google.protobuf") version "0.8.17"
    java
    application
}

application {
    group = "org.mvnsearch"
    version = "1.0.0-SNAPSHOT"
    mainClass.value("org.mvnsearch.greeter.GreeterServerKt")
}

repositories {
    google()
    mavenCentral()
    mavenLocal()
}

sourceSets {
    main {
        java {
            setSrcDirs(
                listOf(
                    "build/generated/source/proto/main/grpc",
                    "build/generated/source/proto/main/java"
                )
            )
        }
        withConvention(org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet::class) {
            kotlin.srcDir("src/main/kotlin")
            kotlin.srcDir("build/generated/source/proto/main/grpckt")
            kotlin.srcDir("build/generated/source/proto/main/kotlin")
        }
    }
}


dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(platform("io.netty:netty-bom:4.1.69.Final"))
    implementation(platform("com.google.protobuf:protobuf-bom:${protobufVersion}"))
    implementation(platform("io.grpc:grpc-bom:${grpcVersion}"))
    implementation("javax.annotation:javax.annotation-api:1.3.2")
    implementation("io.grpc:grpc-kotlin-stub:${grpcKotlinVersion}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")
    implementation("com.google.protobuf:protobuf-java")
    implementation("com.google.protobuf:protobuf-kotlin:3.19.1")
    implementation("com.google.protobuf:protobuf-java-util")
    implementation("io.grpc:grpc-netty")
    implementation("io.grpc:grpc-protobuf")
    implementation("io.grpc:grpc-stub")
    implementation("io.grpc:grpc-services")
    implementation("com.google.guava:guava:31.0.1-jre")
    testImplementation(kotlin("test-junit5"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
}

java {
    sourceCompatibility = JavaVersion.toVersion(javaVersion)
    targetCompatibility = JavaVersion.toVersion(javaVersion)
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = javaVersion
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = javaVersion
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions.suppressWarnings = true
}

protobuf {

    protoc {
        // The artifact spec for the Protobuf Compiler
        artifact = "com.google.protobuf:protoc:${protobufVersion}"
    }

    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:${grpcVersion}"
        }
        // Specify protoc to generate using our grpc kotlin plugin
        id("grpckt") {
            artifact = "io.grpc:protoc-gen-grpc-kotlin:${grpcKotlinVersion}:jdk7@jar"
        }
    }

    generateProtoTasks {
        ofSourceSet("main").forEach {
            it.plugins {
                id("grpc")
                id("grpckt")
            }
            it.builtins {
                id("kotlin")
            }
        }
    }
}


