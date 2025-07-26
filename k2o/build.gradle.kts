plugins {
  kotlin("jvm")
}

kotlin {
  jvmToolchain(17)
}

dependencies {
  // k2o
  implementation("org.jraf:k2o:_")
}
