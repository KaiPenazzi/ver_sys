plugins {
    id("java")
    id("com.google.protobuf") version "0.9.4"
}

repositories {
    mavenCentral()
}

dependencies {
    // Falls du gRPC in utils verwenden möchtest, musst du es hier einfügen
    implementation("io.grpc:grpc-netty-shaded:1.71.0")
    implementation("io.grpc:grpc-protobuf:1.71.0")
    implementation("io.grpc:grpc-stub:1.71.0")
    implementation("javax.annotation:javax.annotation-api:1.3.2")

    // Abhängigkeit für die Proto-Generierung, falls utils auch Protobuf verwendet
    implementation("com.google.protobuf:protobuf-java:3.25.1")

    testImplementation("junit:junit:4.13.2")
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

sourceSets {
    main {
        proto {
            srcDir("../proto")  // Hier das Verzeichnis für .proto-Dateien angeben
        }
        java {
            // Stellen sicher, dass die generierten Protobuf-Klassen in den Code eingebunden werden
            srcDirs("build/generated/source/proto/main/java", "build/generated/source/proto/main/grpc")
        }
    }
}

