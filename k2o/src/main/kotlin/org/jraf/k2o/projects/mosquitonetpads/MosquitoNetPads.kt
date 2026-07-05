/*
 * This source is part of the
 *      _____  ___   ____
 *  __ / / _ \/ _ | / __/___  _______ _
 * / // / , _/ __ |/ _/_/ _ \/ __/ _ `/
 * \___/_/|_/_/ |_/_/ (_)___/_/  \_, /
 *                              /___/
 * repository.
 *
 * Copyright (C) 2026-present Benoit 'BoD' Lubek (BoD@JRAF.org)
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

package org.jraf.k2o.projects.mosquitonetpads

import androidx.compose.runtime.Composable
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import org.jraf.k2o.dsl.openScad
import org.jraf.k2o.stdlib.Cube
import org.jraf.k2o.stdlib.Cylinder
import org.jraf.k2o.stdlib.difference
import org.jraf.k2o.stdlib.hull
import org.jraf.k2o.stdlib.rotate
import org.jraf.k2o.stdlib.translate
import org.jraf.k2o.stdlib.union
import org.jraf.k2o.util.cm
import org.jraf.k2o.util.mm

@Composable
private fun Main() {
  val diameterLarge = 13.1.mm
  val diameterSmall = 8.8.mm

  val thickness = 1.4.mm
  val height = 1.cm
  val baseDiameter = 2.cm

  // Large pad
  MosquitoNetPad(
    height = height,
    thickness = thickness,
    baseDiameter = baseDiameter,
    diameter = diameterLarge,
  )

  // Small pad
  translate(
    x = baseDiameter * 1.2,
  ) {
    MosquitoNetPad(
      height = height,
      thickness = thickness,
      baseDiameter = baseDiameter,
      diameter = diameterSmall,
    )
  }
}

@Composable
private fun MosquitoNetPad(
  height: Double,
  thickness: Double,
  baseDiameter: Double,
  diameter: Double,
) {
  union {
    difference {
      Cylinder(height = height + thickness, diameter = diameter + thickness * 2)
      Cylinder(height = height + thickness, diameter = diameter)
      hull {
        Cube(x = diameter / 2 + thickness * 2, y = .1, z = height + thickness)
        rotate(z = 45) {
          Cube(x = diameter / 2 + thickness * 2, y = .1, z = height + thickness)
        }
      }
    }

    // Base
    Cylinder(height = thickness, diameter = baseDiameter)
  }
}

fun main() {
  openScad(
    SystemFileSystem.sink(Path("/Users/bod/Tmp/mosquito-net-pads.scad")).buffered(),
    fa = .1,
    fs = .1,
  ) {
    Main()
  }
}
