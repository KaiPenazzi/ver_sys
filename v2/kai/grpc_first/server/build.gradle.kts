plugins {
    id("java")
    id("com.google.protobuf") version "0.9.4"
    id("com.github.johnrengelman.shadow") version "7.1.0"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":utils"))  // Das utils-Modul einbinden
    implementation("io.grpc:grpc-netty-shaded:1.71.0")
    implementation("io.grpc:grpc-protobuf:1.71.0")
    implementation("io.grpc:grpc-stub:1.71.0")
    implementation("javax.annotation:javax.annotation-api:1.3.2")

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
            srcDir("../proto")  // Verzeichnis für .proto-Dateien
        }
        java {
            srcDirs("build/generated/source/proto/main/java", "build/generated/source/proto/main/grpc")
        }
    }
}

tasks.shadowJar {
    mergeServiceFiles()
    manifest {
        attributes(
            "Main-Class" to "org.server.Main"  // Die Main-Class für das JAR
        )
    }
}

tasks.register<JavaExec>("runServer") {

    group = "application"
    mainClass.set("org.server.Main")
    classpath = sourceSets["main"].runtimeClasspath
}

