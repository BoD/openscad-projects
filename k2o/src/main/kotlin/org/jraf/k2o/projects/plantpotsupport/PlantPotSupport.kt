/*
 * This source is part of the
 *      _____  ___   ____
 *  __ / / _ \/ _ | / __/___  _______ _
 * / // / , _/ __ |/ _/_/ _ \/ __/ _ `/
 * \___/_/|_/_/ |_/_/ (_)___/_/  \_, /
 *                              /___/
 * repository.
 *
 * Copyright (C) 2025-present Benoit 'BoD' Lubek (BoD@JRAF.org)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.jraf.k2o.projects.plantpotsupport

import androidx.compose.runtime.Composable
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import org.jraf.k2o.dsl.openScad
import org.jraf.k2o.math.cos
import org.jraf.k2o.math.sin
import org.jraf.k2o.projects.TMP_FOLDER
import org.jraf.k2o.stdlib.Cylinder
import org.jraf.k2o.stdlib.Import
import org.jraf.k2o.stdlib.difference
import org.jraf.k2o.stdlib.linearExtrude
import org.jraf.k2o.stdlib.resize
import org.jraf.k2o.stdlib.rotate
import org.jraf.k2o.stdlib.translate
import org.jraf.k2o.units.Angle
import org.jraf.k2o.units.Angle.Companion.deg
import org.jraf.k2o.units.Length
import org.jraf.k2o.units.Length.Companion.mm

@Composable
private fun Leaf(
  x: Length,
  y: Length,
  rotate: Angle,
) {
  translate(x, y) {
    rotate(z = rotate) {
      linearExtrude(height = 4.mm) {
        resize(y = 18.mm, auto = true) {
          Import("/Users/bod/gitrepo/openscad-projects/k2o/src/main/resources/leave.svg", center = true)
        }
      }
    }
  }
}

@Composable
private fun PlantPotSupport(
  outerDiameter: Length,
  width: Length,
  thickness: Length,
) {
  difference {
    // Outer ring
    Cylinder(height = thickness, radius = outerDiameter / 2)

    // Inner ring
    Cylinder(height = thickness, radius = outerDiameter / 2 - width)

    // Leaves
    val leafCount = 13
    val leafRadius = outerDiameter / 2 - width / 2 - 2.mm
    for (i in 0 until leafCount) {
      val angle = 360.deg / leafCount * i
      Leaf(
        x = leafRadius * cos(angle),
        y = leafRadius * sin(angle),
        rotate = angle + 15.deg,
      )
    }
  }
}


fun main() {
  val outerDiameter = 150.mm
  val thickness = 4.mm
  val width = 30.mm
  openScad(SystemFileSystem.sink(Path(TMP_FOLDER, "plant-pot-support.scad")).buffered()) {
    PlantPotSupport(
      outerDiameter = outerDiameter,
      width = width,
      thickness = thickness,
    )
  }
}
