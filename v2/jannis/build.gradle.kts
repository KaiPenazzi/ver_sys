plugins {
    id("java")
    id("application")
    id("com.google.protobuf") version "0.9.4"
}

group = "com.example"
version = "1.0.0"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.grpc:grpc-netty-shaded:1.71.0")
    implementation("io.grpc:grpc-protobuf:1.71.0")
    implementation("io.grpc:grpc-stub:1.71.0")
    implementation("javax.annotation:javax.annotation-api:1.3.2")
    implementation("com.google.protobuf:protobuf-java-util:3.25.3")

    testImplementation("junit:junit:4.13.2")
}

application {
    mainClass = "org.example.LogClient"
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.25.1"
    }
    plugins {
        create("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.71.0"
        }
    }
    generateProtoTasks {
        all().forEach { task ->
            task.plugins {
                create("grpc") {}
            }
        }
    }
}

tasks.named<ProcessResources>("processResources") {
    exclude("**/*.proto")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.register<JavaExec>("runServer") {
    group = "application"
    description = "Startet den gRPC-Server"
    mainClass.set("org.example.LogServer") // deine Server-Klasse
    classpath = sourceSets["main"].runtimeClasspath
}

tasks.register<JavaExec>("runClient") {
    group = "application"
    description = "Startet den gRPC-Client"
    mainClass.set("org.example.LogClient") // deine Client-Klasse
    classpath = sourceSets["main"].runtimeClasspath
}

sourceSets {
    main {
        proto {
            srcDir("src/main/proto")
        }
        java {
            srcDirs("build/generated/source/proto/main/java", "build/generated/source/proto/main/grpc")
        }
    }
}



