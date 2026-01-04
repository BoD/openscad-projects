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

@file:Suppress("SameParameterValue")

package org.jraf.k2o.shapes

import androidx.compose.runtime.Composable
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import org.jraf.k2o.dsl.openScad
import org.jraf.k2o.stdlib.Cube
import org.jraf.k2o.stdlib.Cylinder
import org.jraf.k2o.stdlib.difference
import org.jraf.k2o.stdlib.rotate
import org.jraf.k2o.stdlib.translate
import kotlin.math.sqrt

@Composable
fun HoneycombWall(
  x: Number,
  y: Number,
  z: Number,
  diameter: Number,
  spacing: Number,
) {
  val x = x.toDouble()
  val y = y.toDouble()
  val z = z.toDouble()
  val diameter = diameter.toDouble()
  val spacing = spacing.toDouble()

  difference {
    Cube(
      x = x,
      y = y,
      z = z,
    )

    val radius = diameter / 2.0
    val apothem = (sqrt(3.0) / 2.0) * radius

    val rowCount = ((y - radius / 2.0) / (diameter - radius / 2.0 + spacing)).toInt()
    val columnCount = (x / (apothem * 2.0 + spacing)).toInt()

    val marginY = (y - (rowCount * (diameter - radius / 2.0 + spacing) + radius / 2.0)) / 2.0
    val marginX = (x - columnCount * (apothem * 2.0 + spacing)) / 2.0

    repeat(rowCount) { y ->
      repeat(columnCount - if (y % 2 == 0) 0 else 1) { x ->
        translate(
          x = marginX +
            radius +
            spacing / 2.0 +
            x * (apothem * 2.0 + spacing) +
            (if (y % 2 == 0) 0.0 else apothem + spacing / 2.0) -
            (radius - apothem),
          y = marginY +
            radius +
            spacing / 2.0 +
            y * (diameter - radius / 2.0 + spacing),
        ) {
          rotate(90) {
            Cylinder(
              height = z,
              diameter = diameter,
              segments = 6,
            )
          }
        }
      }
    }
  }
}

fun main() {
  openScad(SystemFileSystem.sink(Path("/Users/bod/Tmp/honeycomb-wall.scad")).buffered()) {
    HoneycombWall(
      x = 9.67,
      y = 11,
      z = 10,
      diameter = 10,
      spacing = 1,
    )
  }
}
