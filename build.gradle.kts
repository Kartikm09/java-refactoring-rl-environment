import com.github.spotbugs.snom.SpotBugsExtension
import com.github.spotbugs.snom.SpotBugsTask
import org.gradle.api.plugins.quality.Checkstyle
import org.gradle.api.tasks.testing.logging.TestExceptionFormat

plugins {
    base
    id("com.github.spotbugs") version "6.5.9" apply false
    id("me.champeau.jmh") version "0.7.3" apply false
}

allprojects {
    group = "dev.kartikm09.rl.java"
    version = "0.1.0"
    repositories { mavenCentral() }
}

subprojects {
    if (name != "performance-tests") {
        apply(plugin = "java-library")
        apply(plugin = "jacoco")
        apply(plugin = "checkstyle")
        apply(plugin = "com.github.spotbugs")

        extensions.configure<JavaPluginExtension> {
            toolchain { languageVersion.set(JavaLanguageVersion.of(25)) }
        }
        dependencies {
            "testImplementation"(platform("org.junit:junit-bom:6.1.2"))
            "testImplementation"("org.junit.jupiter:junit-jupiter")
            "testImplementation"("org.assertj:assertj-core:3.27.7")
            "testRuntimeOnly"("org.junit.platform:junit-platform-launcher")
        }
        tasks.withType<Test>().configureEach {
            useJUnitPlatform()
            jvmArgs("-ea")
            testLogging { exceptionFormat = TestExceptionFormat.FULL }
        }
        extensions.configure<CheckstyleExtension> {
            toolVersion = "11.1.0"
            configFile = rootProject.file("config/checkstyle/checkstyle.xml")
        }
        extensions.configure<SpotBugsExtension> {
            excludeFilter.set(rootProject.file("config/spotbugs/exclude.xml"))
        }
        tasks.withType<Checkstyle>().configureEach {
            exclude("module-info.java")
        }
        tasks.withType<SpotBugsTask>().configureEach {
            reports.create("html") { required.set(true) }
            reports.create("xml") { required.set(false) }
        }
        tasks.withType<JavaCompile>().configureEach {
            options.encoding = "UTF-8"
            options.compilerArgs.addAll(listOf("-Xlint:all", "-Werror"))
        }
    }
}

tasks.register("integrationTest") {
    dependsOn(":app:integrationTest")
}

tasks.register("jacocoTestReport") {
    dependsOn(subprojects.filter { it.name != "performance-tests" }.map { "${it.path}:jacocoTestReport" })
}

tasks.register("spotbugsMain") {
    dependsOn(subprojects.filter { it.name != "performance-tests" }.map { "${it.path}:spotbugsMain" })
}

tasks.register("spotbugsTest") {
    dependsOn(subprojects.filter { it.name != "performance-tests" }.map { "${it.path}:spotbugsTest" })
}

tasks.register("jmh") { dependsOn(":performance-tests:jmh") }
