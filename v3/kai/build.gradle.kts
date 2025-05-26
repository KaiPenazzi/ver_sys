subprojects {
    apply(plugin = "java")

    repositories {
        mavenCentral()
    }

    dependencies {
        // "testImplementation"("org.junit.jupiter:junit-jupiter-api:5.13.0-RC1")
        // "testRuntimeOnly"("org.junit.jupiter:junit-jupiter-engine:5.13.0-RC1")
        "testImplementation"("org.junit.jupiter:junit-jupiter-api:5.10.0")
        "testRuntimeOnly"("org.junit.jupiter:junit-jupiter-engine:5.10.0")
    }

    plugins.withType<JavaPlugin> {
        extensions.configure<JavaPluginExtension> {
            toolchain {
                languageVersion.set(JavaLanguageVersion.of(21))
            }
        }

        tasks.withType<Test> {
            useJUnitPlatform()
        }
    }
}

