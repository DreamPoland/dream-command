repositories {
    maven("https://repo.codemc.io/repository/nms")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    implementation(project(":core"))

    // -- bungee api -- (base)
    compileOnly("net.md-5:bungeecord-api:1.20-R0.1-SNAPSHOT")

    // -- dream-utilities --
    implementation("cc.dreamcode:utilities:1.3.0")
    implementation("cc.dreamcode:utilities-bungee:1.3.0")
}