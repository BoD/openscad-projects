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

  difference {
    // GraphQL
    Comment("GraphQL")
    GraphQLMagnet(
      ballDiameter = ballDiameter,
      diameter = diameter,
      linkDiameter = linkDiameter,
    )

    // Hole for thread
    Comment("Hole for thread")
    translate(x = -ballDiameter / 2, y = diameter / 2 + ballDiameter / 4) {
      rotate(y = 90) {
        Cylinder(height = ballDiameter, diameter = linkDiameter)
      }
    }
  }
}

@Composable
private fun GraphQLMagnet(
  ballDiameter: Double,
  diameter: Double,
  linkDiameter: Double,
) {
  val radius = diameter / 2

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
}

fun main() {
  openScad(
    SystemFileSystem.sink(Path("/Users/bod/Tmp/graphql-tree-decoration.scad")).buffered(),
    fa = .05,
    fs = .05,
  ) {
    Main(8.0)
  }
}
