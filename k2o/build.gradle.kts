plugins {
  kotlin("jvm")
  id("org.jetbrains.compose")
  kotlin("plugin.compose")
}

kotlin {
  jvmToolchain(17)
}

dependencies {
  // k2o
  implementation("org.jraf.k2o:k2o:_")
}
