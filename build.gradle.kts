plugins {
  java
  scala
  id("com.github.johnrengelman.shadow")
  id("com.github.ben-manes.versions")
}

group = "io.jawg"
version = "1.0.0-SNAPSHOT"

allprojects {
  tasks {
    java {
      sourceCompatibility = JavaVersion.VERSION_1_8
      targetCompatibility = JavaVersion.VERSION_1_8
    }
    jar {
      enabled = false
    }

    shadowJar {
      manifest {
        version = ""
        attributes["Main-Class"] = "io.gatling.app.Gatling"
      }
    }

    build { finalizedBy(shadowJar) }
  }
}

fun isNonStable(version: String): Boolean {
  val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.toUpperCase().contains(it) }
  val regex = "^[0-9,\\.v\\-]+(-r)?$".toRegex()
  val isStable = stableKeyword || regex.matches(version)
  return isStable.not()
}

tasks.dependencyUpdates {
  rejectVersionIf {
    isNonStable(candidate.version)
  }
}

repositories {
  mavenLocal()
  mavenCentral()
}

dependencies {
  implementation("org.scala-lang:scala-library:${property("version.scala")}")
  implementation("io.gatling.highcharts:gatling-charts-highcharts:${property("version.gatling")}")
}

