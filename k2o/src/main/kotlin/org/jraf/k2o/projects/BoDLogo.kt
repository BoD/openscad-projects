package org.jraf.k2o.projects

import androidx.compose.runtime.Composable
import org.jraf.k2o.stdlib.Import
import org.jraf.k2o.stdlib.linearExtrude
import org.jraf.k2o.stdlib.resize

@Composable
fun BoDLogo(
  width: Double,
  thickness: Number,
) {
  linearExtrude(height = thickness) {
    resize(x = width, auto = true) {
      Import("/Users/bod/gitrepo/openscad-projects/k2o/src/main/resources/BoD.svg", center = true)
    }
  }
}
