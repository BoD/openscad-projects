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

package org.jraf.k2o.shapes

import androidx.compose.runtime.Composable
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import org.jraf.k2o.dsl.openScad
import org.jraf.k2o.stdlib.Cube
import org.jraf.k2o.stdlib.Cylinder
import org.jraf.k2o.stdlib.Sphere
import org.jraf.k2o.stdlib.difference
import org.jraf.k2o.stdlib.minkowski
import org.jraf.k2o.stdlib.translate

@Composable
fun RoundedHalfCylinder(
  height: Number,
  radius: Number,
  thickness: Number,
) {
  val height = height.toDouble()
  val radius = radius.toDouble()
  val thickness = thickness.toDouble()

  translate(
    z = thickness / 2,
  ) {
    minkowski {
      difference {
        Cylinder(height = height - thickness, radius = radius - thickness / 2)
        Cylinder(height = height - thickness, radius = radius - thickness / 2 - SMALLEST_LENGTH)
        // Cut half
        translate(x = -radius, y = -radius) {
          Cube(x = radius * 2, y = radius, z = height)
        }
      }
      Sphere(thickness / 2)
    }
  }
}

fun main() {
  openScad(
    SystemFileSystem.sink(Path("/Users/bod/Tmp/rounded-half-cylinder.scad")).buffered(),
    fa = 1.0,
    fs = 1.0,
  ) {
    RoundedHalfCylinder(
      height = 100,
      radius = 50,
      thickness = 5,
    )
  }
}
