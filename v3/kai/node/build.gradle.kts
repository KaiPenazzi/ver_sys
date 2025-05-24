plugins {
    application
}

application {
    mainClass.set("com.node.Main")
}

dependencies {
    implementation(project(":common"))
}

// java {
//     sourceCompatibility = JavaVersion.VERSION_24
//     targetCompatibility = JavaVersion.VERSION_24
// }

