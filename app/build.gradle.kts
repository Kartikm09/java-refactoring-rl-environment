plugins { application }
dependencies {
    implementation(project(":domain"))
    implementation(project(":application"))
    implementation(project(":infrastructure"))
    implementation(project(":api"))
}
application { mainClass.set("dev.kartikm09.orders.app.Main") }

val integrationTest = sourceSets.create("integrationTest")
configurations[integrationTest.implementationConfigurationName].extendsFrom(configurations.testImplementation.get())
configurations[integrationTest.runtimeOnlyConfigurationName].extendsFrom(configurations.testRuntimeOnly.get())
dependencies { add(integrationTest.implementationConfigurationName, project(":test-fixtures")) }
tasks.register<Test>("integrationTest") {
    description = "Runs service integration and contract tests."
    group = "verification"
    testClassesDirs = integrationTest.output.classesDirs
    classpath = integrationTest.runtimeClasspath
    useJUnitPlatform()
    shouldRunAfter(tasks.test)
}
tasks.check { dependsOn("integrationTest") }
