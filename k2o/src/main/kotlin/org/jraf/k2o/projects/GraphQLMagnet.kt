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
  // Balls are 1/5 of the diameter, links are 1/20 of the diameter.
  val diameter = ballDiameter * 5
  val linkDiameter = diameter / 20.0
  val cutSize = (ballDiameter - linkDiameter) / 2

  val magnetDiameter = 3.0
  val magnetHeight = 2.0

  // GraphQL magnet
  Comment("GraphQL magnet")
  difference {
    translate(z = ballDiameter / 2 - cutSize) {
      GraphQLMagnet(
        ballDiameter = ballDiameter,
        diameter = diameter,
        linkDiameter = linkDiameter,
        magnetDiameter = magnetDiameter,
        magnetHeight = magnetHeight,
        cutSize = cutSize,
      )
    }

    // Cut it out a bit so it adheres better to the plate
    Comment("Cut out")
    translate(-diameter, -diameter, -ballDiameter) {
      Cube(diameter * 2, diameter * 2, ballDiameter)
    }
  }

  // Stem
  Comment("Stem")
  translate(x = diameter) {
    Stem(
      diameter = diameter,
      magnetDiameter = magnetDiameter,
      magnetHeight = magnetHeight,
    )
  }
}

@Composable
private fun GraphQLMagnet(
  ballDiameter: Double,
  diameter: Double,
  linkDiameter: Double,
  magnetDiameter: Double,
  magnetHeight: Double,
  cutSize: Double,
) {
  val radius = diameter / 2

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
    for (i in 0..5) {
      if (i == 0 || i == 3) {
        rotate(z = i * 360.0 / 6 - (360.0 / 6) * 1.5) {
          translate(x = radius, z = -(ballDiameter / 2 - cutSize)) {
            Cylinder(height = magnetHeight, diameter = magnetDiameter)
          }
        }
      }
    }
  }
}

@Composable
private fun Stem(
  diameter: Double,
  magnetDiameter: Double,
  magnetHeight: Double,
) {
  val holderThickness = .6
  difference {
    union {
      Cylinder(height = magnetHeight + holderThickness, diameter = magnetDiameter + holderThickness * 2)
      translate(x = diameter) {
        Cylinder(height = magnetHeight + holderThickness, diameter = magnetDiameter + holderThickness * 2)
      }

      // Strip
      val width = magnetDiameter + holderThickness * 2
      translate(y = -width / 2) {
        Cube(
          x = diameter,
          y = width,
          z = .6,
        )
      }
    }

    // Magnet holes
    Cylinder(height = magnetHeight, diameter = magnetDiameter)
    translate(x = diameter) {
      Cylinder(height = magnetHeight, diameter = magnetDiameter)
    }
  }
}

fun main() {
  openScad(
    SystemFileSystem.sink(Path("/Users/bod/Tmp/graphql.scad")).buffered(),
    fa = .05,
    fs = .05,
  ) {
    Main(6.0)
  }
}
