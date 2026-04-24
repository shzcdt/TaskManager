plugins {
    java
    jacoco
}

group = "com.github.shzcdt"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:${property("JUnitVersion")}")
    testImplementation("org.junit.jupiter:junit-jupiter-params:${property("JUnitVersion")}")

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${property("JUnitVersion")}")
    compileOnly("org.projectlombok:lombok:1.18.46")
    annotationProcessor("org.projectlombok:lombok:1.18.46")

    testCompileOnly("org.projectlombok:lombok:1.18.46")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.46")
}

jacoco {
    toolVersion = "0.8.12"
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required = true
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

