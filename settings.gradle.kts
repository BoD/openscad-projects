rootProject.name = "k2o-root"

pluginManagement {
  repositories {
    mavenCentral()
    gradlePluginPortal()
  }
}

dependencyResolutionManagement {
  @Suppress("UnstableApiUsage")
  repositories {
    mavenCentral()
    mavenLocal()
  }
}

plugins {
  // See https://splitties.github.io/refreshVersions/
  id("de.fayard.refreshVersions") version "0.60.5"
}

include(
  ":k2o",
)
