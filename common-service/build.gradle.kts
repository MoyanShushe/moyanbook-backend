plugins {
    id("java")
}

group = "com.moyanshushe"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

val aspectVersion = "1.9.21"

dependencies {
    implementation(project(":common"))
    implementation(project(":global-processing"))

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    implementation("org.aspectj:aspectjweaver:$aspectVersion")
    // 202314131130
}

tasks.test {
    useJUnitPlatform()
}