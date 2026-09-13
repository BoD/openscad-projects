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

@file:Suppress("SameParameterValue", "WrapUnaryOperator")

package org.jraf.k2o.projects.graphqldesktoy

import androidx.compose.runtime.Composable
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import org.jraf.k2o.dsl.openScad
import org.jraf.k2o.projects.TMP_FOLDER
import org.jraf.k2o.stdlib.Comment
import org.jraf.k2o.stdlib.Cylinder
import org.jraf.k2o.stdlib.Sphere
import org.jraf.k2o.stdlib.difference
import org.jraf.k2o.stdlib.rotate
import org.jraf.k2o.stdlib.translate
import org.jraf.k2o.stdlib.union
import org.jraf.k2o.units.Angle.Companion.deg
import org.jraf.k2o.units.Angle.Companion.times
import org.jraf.k2o.units.Length
import org.jraf.k2o.units.Length.Companion.mm
import org.jraf.k2o.units.Length.Companion.times
import kotlin.math.sqrt

@Composable
private fun GraphQLDeskToy(ballDiameter: Length) {
  // Balls are 1/5 of the diameter, links are 1/20 of the diameter.
  val diameter = ballDiameter * 5
  val linkDiameter = diameter / 25.0

  GraphQL(
    ballDiameter = ballDiameter,
    diameter = diameter,
    linkDiameter = linkDiameter,
  )
}

@Suppress("DuplicatedCode")
@Composable
private fun GraphQL(
  ballDiameter: Length,
  diameter: Length,
  linkDiameter: Length,
) {
  val radius = diameter / 2
  val dent = ballDiameter / 7
  val hexagonSideLength = radius - (ballDiameter / 2 - dent) * 2
  val triangleSideLength = sqrt(3.0) * radius - (ballDiameter / 2 - dent) * 2

  difference {
    union {
      // Balls
      Comment("Balls")
      for (i in 0..5) {
        rotate(z = i * 360.deg / 6 - (360.deg / 6) * 1.5) {
          translate(x = radius) {
            Sphere(diameter = ballDiameter)
          }
        }
      }
    }

    // Hexagon
    Comment("Hexagon")
    for (i in 0..5) {
      rotate(z = i * 360.deg / 6 - (360.deg / 6) * 1.5) {
        translate(x = radius) {
          rotate(x = 90.deg, z = -30.deg) {
            translate(z = ballDiameter / 2 - dent) {
              Cylinder(height = hexagonSideLength, diameter = linkDiameter)
            }
          }
        }
      }
    }
    // Triangle
    Comment("Triangle")
    for (i in 0..2) {
      rotate(z = i * 360.deg / 3 - -(360.deg / 6) * 1.5) {
        translate(x = radius) {
          rotate(x = 90.deg, z = -60.deg) {
            translate(z = ballDiameter / 2 - dent) {
              Cylinder(height = triangleSideLength, diameter = linkDiameter)
            }
          }
        }
      }
    }
  }

  // Links
  // Hexagon
  Comment("Hexagon")
  for (i in 0..5) {
    translate(x = i * 6.mm) {
      rotate(x = 90.deg, z = -30.deg) {
        translate(z = ballDiameter / 2 - dent) {
          Cylinder(height = hexagonSideLength, diameter = linkDiameter * .95)
        }
      }
    }
  }
  // Triangle
  Comment("Triangle")
  for (i in 0..2) {
    translate(x = i * 10.mm, y = 30.mm) {
      rotate(x = 90.deg, z = -60.deg) {
        translate(z = ballDiameter / 2 - dent) {
          Cylinder(height = triangleSideLength, diameter = linkDiameter * .95)
        }
      }
    }
  }
}

fun main() {
  openScad(
    SystemFileSystem.sink(Path(TMP_FOLDER, "graphql-desk-toy.scad")).buffered(),
    fa = .1,
    fs = .1,
  ) {
    GraphQLDeskToy(20.mm)
  }
}
