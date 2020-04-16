import com.google.protobuf.gradle.*

val protobufVersion = "3.11.4"
val grpcVersion = "1.28.1"
val grpcKotlinVersion = "0.1.1"

plugins {
  kotlin("jvm") version "1.3.72"
  id("com.google.protobuf") version "0.8.12"
}

group = "org.mvnsearch"
version = "1.0.0-SNAPSHOT"


repositories {
  maven("https://dl.bintray.com/kotlin/kotlin-eap")
  google()
  jcenter()
  mavenCentral()
  mavenLocal()
}


sourceSets {
  val main by getting {
    java.srcDirs("build/generated/source/proto/main/grpc")
    java.srcDir("build/generated/source/proto/main/java")
    withConvention(org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet::class) {
      kotlin.srcDir("src/main/kotlin")
      kotlin.srcDir("build/generated/source/proto/main/grpckt")
    }
  }
}


dependencies {
  implementation(kotlin("stdlib-jdk8"))
  implementation("io.grpc:grpc-kotlin-stub:${grpcKotlinVersion}")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.5")
  implementation("com.google.protobuf:protobuf-java:${protobufVersion}")
  implementation("com.google.protobuf:protobuf-java-util:${protobufVersion}")
  implementation("io.grpc:grpc-netty-shaded:${grpcVersion}")
  implementation("io.grpc:grpc-protobuf:${grpcVersion}")
  implementation("io.grpc:grpc-stub:${grpcVersion}")
  implementation("com.google.guava:guava:29.0-jre")
  testImplementation(kotlin("test-junit5"))
  testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.2")
}

tasks {
  compileKotlin {
    kotlinOptions.jvmTarget = "1.8"
  }
  compileTestKotlin {
    kotlinOptions.jvmTarget = "1.8"
  }
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
      artifact = "io.grpc:protoc-gen-grpc-kotlin:${grpcKotlinVersion}"
    }
  }

  generateProtoTasks {
    all().forEach {
      it.plugins {
        id("grpc")
        id("grpckt")
      }
    }
  }
}


