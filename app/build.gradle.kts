plugins {
    application
}

repositories {
    mavenCentral()
}

dependencies {
    annotationProcessor("org.projectlombok:lombok:1.18.24")
    implementation("com.google.guava:guava:30.1.1-jre")
    compileOnly("org.projectlombok:lombok:1.18.24")

    testAnnotationProcessor("org.projectlombok:lombok:1.18.24")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
    testCompileOnly("org.projectlombok:lombok:1.18.24")
}

application {
    mainClass.set("gradlectl.process.viewer.Main")
    applicationName = "gctl"
    applicationDefaultJvmArgs = listOf("--add-exports=jdk.internal.jvmstat/sun.jvmstat.monitor=ALL-UNNAMED")
}

val javaVersion = 11
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(javaVersion))
    }
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

tasks.withType<JavaCompile>().configureEach {
    // Required so we can set --add-exports with toolchains, issue: https://github.com/gradle/gradle/issues/18824
    sourceCompatibility = "$javaVersion"
    options.compilerArgs = listOf("--add-exports=jdk.internal.jvmstat/sun.jvmstat.monitor=ALL-UNNAMED")
}

val installDist = tasks.named<Sync>("installDist")
tasks.register<Sync>("install") {
    val installDirPropertyName = "gctl.install.dir"
    val installDir = when {
        providers.gradleProperty(installDirPropertyName).isPresent -> providers.gradleProperty(installDirPropertyName).get()
        providers.systemProperty(installDirPropertyName).isPresent -> providers.systemProperty(installDirPropertyName).get()
        else -> "$rootDir/distribution"
    }
    from(installDist.map { it.destinationDir })
    into(installDir)
}