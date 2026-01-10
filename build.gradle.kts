plugins {
  alias(libs.plugins.kotlin.jvm).apply(false)
  alias(libs.plugins.compose).apply(false)
  alias(libs.plugins.kotlin.compose).apply(false)
}

// `./gradlew refreshVersions` to update dependencies
