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
import org.jraf.k2o.stdlib.Comment
import org.jraf.k2o.stdlib.Cube
import org.jraf.k2o.stdlib.Cylinder
import org.jraf.k2o.stdlib.Sphere
import org.jraf.k2o.stdlib.difference
import org.jraf.k2o.stdlib.rotate
import org.jraf.k2o.stdlib.translate
import org.jraf.k2o.stdlib.union
import kotlin.math.sqrt

@Composable
private fun Main(ballDiameter: Double) {
  val diameter = ballDiameter * 5
  val linkDiameter = diameter / 20.0
  val cutSize = (ballDiameter - linkDiameter) / 2
  difference {
    translate(z = ballDiameter / 2 - cutSize) {
      GraphQL(ballDiameter)
    }

    // Cut it out a bit so it adheres better to the plate
    Comment("Cut out")
    translate(-20, -20, -ballDiameter) {
      Cube(40, 40, ballDiameter)
    }
  }
}

@Composable
private fun GraphQL(ballDiameter: Double) {
  // Balls are 1/5 of the diameter, links are 1/20 of the diameter.

  val diameter = ballDiameter * 5

  val radius = diameter / 2
  val linkDiameter = diameter / 20.0

  val magnetDiameter = 3.0
  val magnetHeight = 2.0

  difference {
    union {
      // Balls
      Comment("Balls")
      for (i in 0..5) {
        rotate(z = i * 360.0 / 6 - (360.0 / 6) * 1.5) {
          translate(x = radius) {
            Sphere(diameter = ballDiameter)
          }
        }
      }

      // Hexagon
      Comment("Hexagon")
      val hexagonSideLength = radius
      for (i in 0..5) {
        rotate(z = i * 360.0 / 6 - (360.0 / 6) * 1.5) {
          translate(x = radius) {
            rotate(x = 90, z = -30) {
              Cylinder(height = hexagonSideLength, diameter = linkDiameter)
            }
          }
        }
      }

      // Triangle
      Comment("Triangle")
      val triangleSideLength = sqrt(3.0) * radius
      for (i in 0..2) {
        rotate(z = i * 360.0 / 3 - -(360.0 / 6) * 1.5) {
          translate(x = radius) {
            rotate(x = 90, z = -60) {
              Cylinder(height = triangleSideLength, diameter = linkDiameter)
            }
          }
        }
      }
    }

    // Magnet holes (only for top and bottom balls)
    Comment("Magnet holes")
    union {
      for (i in 0..5) {
        if (i == 0 || i == 3) {
          rotate(z = i * 360.0 / 6 - (360.0 / 6) * 1.5) {
            translate(x = radius, z = -8 + magnetHeight) {
              Cylinder(height = 8, diameter = magnetDiameter)
            }
          }
        }
      }
    }
  }
}

fun main() {
  openScad(
    SystemFileSystem.sink(Path("/Users/bod/Tmp/graphql.scad")).buffered(),
    fa = .1,
    fs = .05,
  ) {
    Main(6.0)
  }
}
