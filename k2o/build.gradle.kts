plugins {
  alias(libs.plugins.kotlin.jvm)
  alias(libs.plugins.compose)
  alias(libs.plugins.kotlin.compose)
}

kotlin {
  jvmToolchain(25)

  compilerOptions {
    // See https://kotlinlang.org/docs/whatsnew-eap.html#support-for-collection-literals
    freeCompilerArgs.add("-Xcollection-literals")
  }
}

dependencies {
  // k2o
  implementation(libs.k2o)
}
