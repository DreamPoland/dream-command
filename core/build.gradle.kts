dependencies {
    // -- dream-utilities --
    implementation(libs.dream.utilties)

    // -- tests --
    testImplementation(platform("org.junit:junit-bom:5.11.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}