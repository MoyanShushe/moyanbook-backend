plugins {
    java
    id("org.springframework.boot") version "3.2.5"
    id("io.spring.dependency-management") version "1.1.4"
}

group = "com.moyanshushe"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()

}

val springVersion = "3.2.5"
val jimmerVersion = "0.8.104"
val druidVersion = "1.2.21"
val jwtVersion = "0.9.1"
val jaxbVersion = "4.0.2"
val jaxbRuntimeVersion = "2.3.1"
val lombokVersion = "1.18.32"
val aspectVersion = "1.9.21"
val junitPlatformLauncherVersion = "1.10.2"
val javaMailVersion = "1.6.2"
val commonsPoolVersion = "2.11.1"
val nacosVersion = "2023.0.1.0"

allprojects {
    apply(plugin = "java")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "org.springframework.boot")

    dependencies {

        implementation("org.springframework.boot:spring-boot-starter-web")

        // Data
        implementation("org.springframework.boot:spring-boot-starter-data-redis")
        implementation("org.babyfish.jimmer:jimmer-spring-boot-starter:${jimmerVersion}")
        implementation("org.babyfish.jimmer:jimmer-sql:${jimmerVersion}")
        implementation("com.alibaba:druid:${druidVersion}")
        runtimeOnly("com.mysql:mysql-connector-j")

        // Utils
        implementation("io.jsonwebtoken:jjwt:${jwtVersion}")
        implementation("org.springframework.boot:spring-boot-starter-mail")
        compileOnly("org.projectlombok:lombok")
        annotationProcessor("org.projectlombok:lombok")
        annotationProcessor("org.babyfish.jimmer:jimmer-apt:${jimmerVersion}")

        // https://mvnrepository.com/artifact/com.alibaba.cloud/spring-cloud-starter-alibaba-nacos-discovery
        implementation("com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-discovery:${nacosVersion}")


        // test
        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    }
}


tasks.withType<Test> {
    useJUnitPlatform()
}
