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

package org.jraf.k2o.projects

import androidx.compose.runtime.Composable
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import org.jraf.k2o.dsl.openScad
import org.jraf.k2o.stdlib.Cylinder
import org.jraf.k2o.stdlib.color
import org.jraf.k2o.stdlib.difference
import org.jraf.k2o.stdlib.rotate
import org.jraf.k2o.stdlib.translate
import kotlin.math.PI
import kotlin.math.sqrt

@Composable
private fun Bracelet() {
  val braceletDiameter = 100
  val braceletThickness = 2
  val braceletSegments = 12

  val holeCount = 24
  val holeSpacing = 3
  val holeDiameter = (PI * braceletDiameter / holeCount - holeSpacing) / (sqrt(3.0) / 2)

  val braceletHeight = holeDiameter + holeSpacing * 2
  val holeSegments = 6

  // Holes are hexagonal, so the spacing between holes must be smaller than the spacing between the circles
  // in which they are inscribed.
  val minorDiameter = holeDiameter * sqrt(3.0) / 2
  val diameterRatio = holeDiameter / minorDiameter

  color("red") {
    difference {
      difference {
        Cylinder(
          height = braceletHeight,
          diameter = braceletDiameter,
          segments = braceletSegments,
        )

        Cylinder(
          height = braceletHeight,
          diameter = braceletDiameter - braceletThickness,
          segments = braceletSegments,
        )
      }

      repeat(holeCount) { index ->
        rotate(0, 0, 360.0 / holeCount * index + 360.0 / holeCount / 2) {
          translate(0, 50, braceletHeight / 2) {
            rotate(90, 90, 0) {
              Cylinder(
                height = 20,
                diameter = holeDiameter,
                segments = holeSegments,
              )
            }
          }
        }

        rotate(0, 0, 360.0 / holeCount * index + 360.0 / holeCount) {
          translate(0, 50, braceletHeight / 2 + minorDiameter) {
            rotate(90, 90, 0) {
              Cylinder(
                height = 20,
                diameter = holeDiameter,
                segments = holeSegments,
              )
            }
          }
        }

        rotate(0, 0, 360.0 / holeCount * index + 360.0 / holeCount) {
          translate(0, 50, braceletHeight / 2 - minorDiameter) {
            rotate(90, 90, 0) {
              Cylinder(
                height = 20,
                diameter = holeDiameter,
                segments = holeSegments,
              )
            }
          }
        }

      }
    }
  }
}

fun main() {
  openScad(SystemFileSystem.sink(Path("/Users/bod/Tmp/bracelet.scad")).buffered()) {
    Bracelet()
  }
}
