plugins {
    application
    id("com.github.johnrengelman.shadow") version "7.1.0"
}

application {
    mainClass.set("com.controller.Main")
}

dependencies {
    implementation(project(":common"))
}

tasks.shadowJar {
    mergeServiceFiles()
    manifest {
        attributes(
            "Main-Class" to "com.controller.Main"
        )
    }
}

