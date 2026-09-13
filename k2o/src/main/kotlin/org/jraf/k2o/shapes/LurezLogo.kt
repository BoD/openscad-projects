package org.jraf.k2o.shapes

import androidx.compose.runtime.Composable
import org.jraf.k2o.stdlib.Import
import org.jraf.k2o.stdlib.linearExtrude
import org.jraf.k2o.stdlib.resize
import org.jraf.k2o.units.Length

/**
 * Aspect ratio: 2:1
 */
@Composable
fun LurezLogo(
  width: Length,
  thickness: Length,
) {
  linearExtrude(height = thickness) {
    resize(x = width, auto = true) {
      Import("/Users/bod/gitrepo/openscad-projects/k2o/src/main/resources/lurez.svg", center = true)
    }
  }
}
