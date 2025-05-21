plugins {
    java
    application
    id("com.google.protobuf") version "0.9.4"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group="com.example"
version = "1.0.0"
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}


repositories {
    mavenCentral()
}

dependencies {
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

tasks.named<ProcessResources>("processResources") {
    exclude("**/*.proto")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}


//tasks.register<JavaExec>("runServer") {
//    group = "application"
//    description = "Startet den gRPC-Server"
//    mainClass.set("org.example.ServerMain")  // Main-Server-Klasse
//    classpath = sourceSets["main"].runtimeClasspath
//    standardOutput = System.out  // Setze die Standard-Ausgabe
//    errorOutput = System.err  // Setze die Fehler-Ausgabe
//    doFirst {
//        println("Starte den gRPC-Server...")
//    }
//}
//
//tasks.register<JavaExec>("runClient") {
//    group = "application"
//    description = "Startet den gRPC-Client"
//    mainClass.set("org.example.ClientMain")  // Main-Client-Klasse
//    classpath = sourceSets["main"].runtimeClasspath
//    standardOutput = System.out  // Setze die Standard-Ausgabe
//    errorOutput = System.err  // Setze die Fehler-Ausgabe
//    doFirst {
//        println("Starte den gRPC-Client...")
//    }
//}


tasks.shadowJar {
    archiveClassifier.set("client")
    mergeServiceFiles()
    manifest {
        attributes(
            "Main-Class" to "org.example.ClientMain"
        )
    }
}

//tasks.shadowJar {
//    archiveClassifier.set("server")
//    mergeServiceFiles()
//    manifest {
//        attributes(
//            "Main-Class" to "org.example.LogServer"
//        )
//    }
//}


sourceSets.named("main") {
    proto {
        srcDir("src/main/proto")
    }
    java {
        srcDirs("build/generated/source/proto/main/java", "build/generated/source/proto/main/grpc")
    }
}


application {
    mainClass.set("org.example.ClientMain")
}

//application {
//    mainClass.set("org.example.ServerMain")
//}

