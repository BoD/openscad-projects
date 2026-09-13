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

@file:Suppress("SameParameterValue", "DuplicatedCode", "UnnecessaryVariable", "WrapUnaryOperator")

package org.jraf.k2o.projects.graphqltreedecoration

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
private fun GraphQLTreeDecoration(ballDiameter: Length) {
  // Balls are 1/5 of the diameter, links are 1/20 of the diameter.
  val diameter = ballDiameter * 5
  val linkDiameter = diameter / 20

  difference {
    // GraphQL
    Comment("GraphQL")
    GraphQL(
      ballDiameter = ballDiameter,
      diameter = diameter,
      linkDiameter = linkDiameter,
    )

    // Hole for thread
    Comment("Hole for thread")
    translate(x = -ballDiameter / 2, y = diameter / 2 + ballDiameter / 4) {
      rotate(y = 90.deg) {
        Cylinder(height = ballDiameter, diameter = linkDiameter)
      }
    }
  }
}

@Composable
private fun GraphQL(
  ballDiameter: Length,
  diameter: Length,
  linkDiameter: Length,
) {
  val radius = diameter / 2

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

    // Hexagon
    Comment("Hexagon")
    val hexagonSideLength = radius
    for (i in 0..5) {
      rotate(z = i * 360.deg / 6 - (360.deg / 6) * 1.5) {
        translate(x = radius) {
          rotate(x = 90.deg, z = -30.deg) {
            Cylinder(height = hexagonSideLength, diameter = linkDiameter)
          }
        }
      }
    }

    // Triangle
    Comment("Triangle")
    val triangleSideLength = sqrt(3.0) * radius
    for (i in 0..2) {
      rotate(z = i * 360.deg / 3 - -(360.deg / 6) * 1.5) {
        translate(x = radius) {
          rotate(x = 90.deg, z = -60.deg) {
            Cylinder(height = triangleSideLength, diameter = linkDiameter)
          }
        }
      }
    }
  }
}

fun main() {
  openScad(
    SystemFileSystem.sink(Path(TMP_FOLDER, "graphql-tree-decoration.scad")).buffered(),
    fa = .05,
    fs = .05,
  ) {
    GraphQLTreeDecoration(8.mm)
  }
}
