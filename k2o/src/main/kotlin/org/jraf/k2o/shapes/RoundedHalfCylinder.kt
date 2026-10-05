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
import org.jraf.k2o.math.cos
import org.jraf.k2o.math.sin
import org.jraf.k2o.projects.TMP_FOLDER
import org.jraf.k2o.stdlib.Circle
import org.jraf.k2o.stdlib.Cylinder
import org.jraf.k2o.stdlib.Polygon
import org.jraf.k2o.stdlib.Sphere
import org.jraf.k2o.stdlib.difference
import org.jraf.k2o.stdlib.intersection
import org.jraf.k2o.stdlib.linearExtrude
import org.jraf.k2o.stdlib.minkowski
import org.jraf.k2o.stdlib.rotate
import org.jraf.k2o.stdlib.translate
import org.jraf.k2o.units.Angle
import org.jraf.k2o.units.Angle.Companion.deg
import org.jraf.k2o.units.Length
import org.jraf.k2o.units.Length.Companion.mm

@Composable
fun RoundedHollowCylinder(
  height: Length,
  radius: Length,
  thickness: Length,
  cut: Angle,
) {
  translate(
    z = thickness / 2,
  ) {
    minkowski {
      difference {
        Cylinder(height = height - thickness, radius = radius - thickness / 2)
        Cylinder(height = height - thickness, radius = radius - thickness / 2 - Length.Smallest)
        // Cut
        rotate(z = 180.deg) {
          PieSlice(radius = radius, angle = Angle.FullCircle - cut, height = height)
        }
      }
      Sphere(thickness / 2)
    }
  }
}

@Composable
private fun PieSlice(radius: Length, angle: Angle, height: Length) {
  linearExtrude(height = height) {
    intersection {
      Circle(radius = radius)
      val resolution = (angle / 5.deg).toInt()
      val points = Array(resolution + 1) { i ->
        val a = angle / resolution * i
        radius * 2 * cos(a) to radius * 2 * sin(a)
      }
      Polygon(
        *arrayOf(Length.Zero to Length.Zero) + points,
      )
    }
  }
}

fun main() {
  openScad(
    SystemFileSystem.sink(Path(TMP_FOLDER, "rounded-hollow-cylinder.scad")).buffered(),
    fa = 1.0,
    fs = 1.0,
  ) {
    RoundedHollowCylinder(
      height = 100.mm,
      radius = 50.mm,
      thickness = 5.mm,
      cut = 90.deg,
    )
  }
}
