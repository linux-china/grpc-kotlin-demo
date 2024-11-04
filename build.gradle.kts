import com.google.protobuf.gradle.id
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val protobufVersion = "4.28.3"
val grpcVersion = "1.68.1"
val grpcKotlinVersion = "1.4.1"

plugins {
  kotlin("jvm") version "2.0.21"
  id("com.google.protobuf") version "0.9.4"
  java
  application
}

application {
  group = "org.mvnsearch"
  version = "1.0.0-SNAPSHOT"
  mainClass.value("org.mvnsearch.greeter.GreeterServerKt")
}

repositories {
  mavenLocal()
  mavenCentral()
  google()
}

dependencies {
  implementation(platform("io.netty:netty-bom:4.1.114.Final"))
  implementation(platform("com.google.protobuf:protobuf-bom:${protobufVersion}"))
  implementation(platform("io.grpc:grpc-bom:${grpcVersion}"))
  implementation("javax.annotation:javax.annotation-api:1.3.2")
  implementation("io.grpc:grpc-kotlin-stub:${grpcKotlinVersion}")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
  implementation("com.google.protobuf:protobuf-java")
  implementation("com.google.protobuf:protobuf-kotlin:${protobufVersion}")
  implementation("com.google.protobuf:protobuf-java-util")
  implementation("io.grpc:grpc-netty")
  implementation("io.grpc:grpc-protobuf")
  implementation("io.grpc:grpc-stub")
  implementation("io.grpc:grpc-services")
  implementation("com.google.guava:guava:33.3.1-jre")
  testImplementation(kotlin("test-junit5"))
  testImplementation("org.junit.jupiter:junit-jupiter-api:5.11.3")
}

java {
  sourceCompatibility = JavaVersion.VERSION_21
  targetCompatibility = JavaVersion.VERSION_21
}

tasks {
  compileKotlin {
    compilerOptions {
      jvmTarget = JvmTarget.JVM_21
      suppressWarnings = true
      freeCompilerArgs = listOf("-opt-in=kotlin.RequiresOptIn")
    }
  }
  compileTestKotlin {
    compilerOptions {
      jvmTarget = JvmTarget.JVM_21
    }
  }
}

kotlin {
  jvmToolchain(21)
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
      artifact = "io.grpc:protoc-gen-grpc-kotlin:${grpcKotlinVersion}:jdk8@jar"
    }
  }

  generateProtoTasks {
    all().forEach {
      it.plugins {
        create("grpc")
        create("grpckt")
      }
      it.builtins {
        create("kotlin")
      }
    }
  }
}


