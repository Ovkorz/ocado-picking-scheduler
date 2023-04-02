plugins {
    id("java")
}

group = "scheduler"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    implementation(platform("com.fasterxml.jackson:jackson-bom:2.13.4.1"))
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.5")
}

tasks.test {
    useJUnitPlatform()
}

tasks.jar {

    manifest {
        attributes["Main-Class"] = "scheduler.Main"
    }
    from(configurations.runtimeClasspath.get().map({ if (it.isDirectory) it else zipTree(it) }))
}
