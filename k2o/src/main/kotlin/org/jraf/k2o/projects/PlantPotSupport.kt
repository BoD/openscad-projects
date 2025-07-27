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

package org.jraf.k2o.projects

import androidx.compose.runtime.Composable
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import org.jraf.k2o.dsl.openScad
import org.jraf.k2o.math.cos
import org.jraf.k2o.math.sin
import org.jraf.k2o.stdlib.Cylinder
import org.jraf.k2o.stdlib.Import
import org.jraf.k2o.stdlib.difference
import org.jraf.k2o.stdlib.linearExtrude
import org.jraf.k2o.stdlib.resize
import org.jraf.k2o.stdlib.rotate
import org.jraf.k2o.stdlib.translate

@Composable
private fun Leaf(
  x: Number,
  y: Number,
  rotate: Number,
) {
  translate(x, y, 0) {
    rotate(0, 0, rotate) {
      linearExtrude(height = 4) {
        resize(0, 18, 0, true) {
          Import("leave.svg", center = true)
        }
      }
    }
  }
}

@Composable
private fun PlantPotSupport(
  outerDiameter: Int,
  width: Int,
  thickness: Int,
) {
  difference {
    // Outer ring
    Cylinder(height = thickness, radius = outerDiameter / 2)

    // Inner ring
    Cylinder(height = thickness, radius = outerDiameter / 2 - width)

    // Leaves
    val leafCount = 13
    val leafRadius = outerDiameter / 2 - width / 2 - 2
    for (i in 0 until leafCount) {
      val angle = 360.0 / leafCount * i
      Leaf(
        x = leafRadius * cos(angle),
        y = leafRadius * sin(angle),
        rotate = angle + 15,
      )
    }
  }
}


fun main() {
  val outerDiameter = 150
  val thickness = 4
  val width = 30
  openScad(SystemFileSystem.sink(Path("/Users/bod/gitrepo/openscad-projects/plant-pot-support/tmp.scad")).buffered()) {
    PlantPotSupport(
      outerDiameter = outerDiameter,
      width = width,
      thickness = thickness,
    )
  }
}
