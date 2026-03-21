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

package org.jraf.k2o.projects

import androidx.compose.runtime.Composable
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import org.jraf.k2o.dsl.openScad
import org.jraf.k2o.math.cos
import org.jraf.k2o.math.sin
import org.jraf.k2o.stdlib.Cylinder
import org.jraf.k2o.stdlib.difference
import org.jraf.k2o.stdlib.translate
import org.jraf.k2o.util.cm
import org.jraf.k2o.util.mm

@Composable
private fun Main() {
  val diameter = 10.4.cm
  val thickness = 1.2.mm
  val holeDiameter = 2.4.mm
  val indentWidth = 8.mm

  difference {
    Cylinder(
      diameter = diameter,
      height = thickness,
    )

    translate(z = -thickness / 2) {
      difference {
        Cylinder(
          diameter = diameter,
          height = thickness,
        )

        Cylinder(
          diameter = diameter - indentWidth * 2,
          height = thickness,
        )
      }
    }

    val holeCount = 16
    repeat(holeCount) { i ->
      val angle = i * 360.0 / holeCount
      translate(
        x = (diameter / 2 * .7 * (i * (1.0 / holeCount)) + diameter / 2 * .1) * cos(angle * 3),
        y = (diameter / 2 * .7 * (i * (1.0 / holeCount)) + diameter / 2 * .1) * sin(angle * 3),
      ) {
        Cylinder(
          diameter = holeDiameter,
          height = thickness,
        )
      }
    }
  }
}

fun main() {
  openScad(
    SystemFileSystem.sink(Path("/Users/bod/Tmp/bug-trap.scad")).buffered(),
  ) {
    Main()
  }
}
