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
import org.jraf.k2o.stdlib.Cube
import org.jraf.k2o.stdlib.Cylinder
import org.jraf.k2o.stdlib.Sphere
import org.jraf.k2o.stdlib.difference
import org.jraf.k2o.stdlib.hull
import org.jraf.k2o.stdlib.rotate
import org.jraf.k2o.stdlib.translate
import org.jraf.k2o.stdlib.union

@Composable
private fun Main() {
  val width = 31.0
  val thickness = 3.2

  val prongCount = 5
  val outerRadius = width / 3

  val armDiameter = 5.1
  val wristDiameter = armDiameter + thickness * 2

  val triangleDepth = thickness * 2
  val wristDepth = 10.0

  difference {
    union {
      Prongs(
        width = width,
        thickness = thickness,
        prongCount = prongCount,
        outerRadius = outerRadius,
      )
      translate(x = thickness / 2, y = -outerRadius / 2, z = outerRadius - thickness / 2) {
        Triangle(
          thickness = thickness,
          width = width,
          wristDiameter = wristDiameter,
          triangleDepth = triangleDepth,
        )
      }

      translate(x = thickness / 2, y = -wristDepth / 1.5, z = outerRadius - thickness / 2) {
        Wrist(
          width = width,
          wristDiameter = wristDiameter,
          thickness = thickness,
          triangleDepth = triangleDepth,
          wristDepth = wristDepth,
          armDiameter = armDiameter,
        )
      }
    }

    // Arm hole
    translate(x = width / 2, y = -wristDepth / 1.5 - triangleDepth / 2, z = outerRadius - thickness / 2) {
      rotate(x = 90) {
        Cylinder(height = wristDepth, diameter = armDiameter)
      }
    }
  }
}

@Composable
private fun Prongs(
  width: Double,
  thickness: Double,
  prongCount: Int,
  outerRadius: Double,
) {
  val prongWidth = width / (prongCount * 2 - 1)
  rotate(y = 90) {
    difference {
      Cylinder(height = width, radius = outerRadius)
      Cylinder(height = width, radius = outerRadius - thickness)
      translate(y = -outerRadius) {
        Cube(x = outerRadius * 2, y = outerRadius * 2, z = width)
      }
      translate(x = -outerRadius, y = -outerRadius * 2) {
        Cube(x = outerRadius * 2, y = outerRadius * 2, z = width)
      }
      for (prongIndex in 0..prongCount) {
        val z = prongIndex * prongWidth * 2 + prongWidth
        translate(z = z) {
          Cylinder(height = prongWidth, radius = outerRadius)
        }
      }
      // Cutout to make them sharper
      translate(x = -outerRadius, y = outerRadius - thickness / 1.5) {
        Cube(x = outerRadius * 2, y = 4, z = width)
      }
    }
  }
  // Extension
  translate(y = -outerRadius / 2, z = outerRadius - thickness) {
    Cube(x = width, y = outerRadius / 2, z = thickness)
  }
}

@Composable
private fun Triangle(
  thickness: Double,
  width: Double,
  wristDiameter: Double,
  triangleDepth: Double,
) {
  hull {
    // Front
    Cube(thickness, center = true)
    translate(x = width - thickness) {
      Cube(thickness, center = true)
    }

    // Back
    translate(x = width / 2 - wristDiameter / 2, y = -triangleDepth) {
      Sphere(diameter = thickness)
    }
    translate(x = width / 2 + wristDiameter / 2 - thickness, y = -triangleDepth) {
      Sphere(diameter = thickness)
    }
  }
}

@Composable
private fun Wrist(
  width: Double,
  wristDiameter: Double,
  thickness: Double,
  triangleDepth: Double,
  wristDepth: Double,
  armDiameter: Double,
) {
  hull {
    translate(x = width / 2 - wristDiameter / 2) {
      Sphere(diameter = thickness)
    }
    translate(x = width / 2 + wristDiameter / 2 - thickness) {
      Sphere(diameter = thickness)
    }

    translate(x = width / 2 - thickness / 2, y = -triangleDepth / 2) {
      rotate(x = 90) {
        Cylinder(height = wristDepth, diameter = wristDiameter)
      }
    }
  }
}

fun main() {
  openScad(
    SystemFileSystem.sink(Path("/Users/bod/Tmp/scratcher.scad")).buffered(),
    fa = .1,
    fs = .1,
  ) {
    Main()
  }
}
