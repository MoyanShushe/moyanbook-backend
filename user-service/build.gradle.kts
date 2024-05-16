plugins {
    id("java")
}

group = "com.moyanshushe"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

val aspectVersion = "1.9.21"
val openFeignVersion = "4.1.1"
val loadBalancerVersion = "4.1.2"

dependencies {
    implementation(project(":common"))
    implementation(project(":global-processing"))

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    implementation("org.aspectj:aspectjweaver:$aspectVersion")
    // https://mvnrepository.com/artifact/org.springframework.cloud/spring-cloud-starter-openfeign
//    implementation("org.springframework.cloud:spring-cloud-starter-openfeign:$openFeignVersion")
// https://mvnrepository.com/artifact/org.springframework.cloud/spring-cloud-starter-loadbalancer
//    implementation("org.springframework.cloud:spring-cloud-starter-loadbalancer:$loadBalancerVersion")

}

tasks.test {
    useJUnitPlatform()
}