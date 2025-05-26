plugins {
    application
}

application {
    mainClass.set("com.node.Main")
}

dependencies {
    implementation(project(":common"))
}
