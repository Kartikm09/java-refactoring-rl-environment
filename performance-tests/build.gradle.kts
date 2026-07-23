plugins {
    java
    id("me.champeau.jmh")
}
java { toolchain { languageVersion.set(JavaLanguageVersion.of(25)) } }
dependencies { implementation(project(":application")) }
jmh {
    warmupIterations.set(2)
    iterations.set(3)
    fork.set(1)
    resultFormat.set("JSON")
    resultsFile.set(project.layout.buildDirectory.file("reports/jmh/results.json"))
}
