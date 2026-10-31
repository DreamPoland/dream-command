repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://maven.reposilite.com/maven-central")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

dependencies {
    api(project(":core"))

    // -- bungee api -- (base)
    compileOnly(libs.bungeecord.api)

    // -- tests --
    testImplementation(platform("org.junit:junit-bom:5.11.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation(libs.bungeecord.api)
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}
