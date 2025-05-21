plugins {
    `java-library`
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(24))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // Beispiel: JSON-Bibliothek, falls du Jackson nutzen willst
    // implementation("com.fasterxml.jackson.core:jackson-databind:2.17.1")
}

