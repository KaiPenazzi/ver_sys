plugins {
    application
}

application {
    mainClass.set("com.controller.Main")
}

dependencies {
    implementation(project(":common"))
}

// java {
//     sourceCompatibility = JavaVersion.VERSION_24
//     targetCompatibility = JavaVersion.VERSION_24
// }

