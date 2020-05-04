val app = "mp"

buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath("org.jetbrains.kotlin", "kotlin-gradle-plugin", kotlin_version)
        classpath("com.github.jengelman.gradle.plugins", "shadow", "4.0.3")
    }
}

plugins {
    java
    kotlin("jvm") version kotlin_version
    application
    id("com.github.johnrengelman.shadow") version shadow_version
}
group = "cc.hiroga"
version = "1.0-SNAPSHOT"

application.mainClassName = "cc.hiroga.mp.ApplicationKt"

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("com.github.ajalt", "clikt", clikt_version)
    implementation("com.github.kittinunf.fuel", "fuel-gson", fuel_version)
    testCompile("junit", "junit", "4.12")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}
tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }

    register<Exec>("native-image") {
        dependsOn("build")
        workingDir(".")
        commandLine(
                "${System.getenv("GRAALVM_BIN")}/native-image",
                "--report-unsupported-elements-at-runtime",
                "-jar",
                "build/libs/${app}-${version}-all.jar",
                "bin/$app",
                "--enable-https",
                "--no-server"
        )
    }
}