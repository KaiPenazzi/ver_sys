plugins {
    application
}

application {
    mainClass.set("com.controller.Main")
}

dependencies {
    implementation(project(":common"))
}
