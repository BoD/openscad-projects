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
import org.jraf.k2o.shapes.SMALLEST_LENGTH
import org.jraf.k2o.stdlib.Cylinder
import org.jraf.k2o.stdlib.Sphere
import org.jraf.k2o.stdlib.Square
import org.jraf.k2o.stdlib.difference
import org.jraf.k2o.stdlib.hull
import org.jraf.k2o.stdlib.linearExtrude
import org.jraf.k2o.stdlib.minkowski
import org.jraf.k2o.stdlib.rotate
import org.jraf.k2o.stdlib.translate
import org.jraf.k2o.stdlib.union
import org.jraf.k2o.util.cm
import org.jraf.k2o.util.mm

@Composable
private fun Main() {
  val wallThickness = 3.mm
  val candleDiameter = 38.5.mm
  val candleHeight = 15.mm
  val plateBaseDiameter = 15.cm

  val armLength = plateBaseDiameter / 2
  val armWidth = 1.5.cm

  val armCount = 3

  val candleHolderShiftX = -candleDiameter / 2 - wallThickness * 4

  difference {
    union {
      // Arms
      for (i in 0..<armCount) {
        rotate(z = i * 360.0 / armCount) {
          Arm(
            armWidth = armWidth,
            wallThickness = wallThickness,
            armLength = armLength,
            candleHeight = candleHeight,
          )
        }
      }
    }

    // Middle hole
    Cylinder(
      height = wallThickness,
      diameter = (-candleHolderShiftX - (candleDiameter + wallThickness * 2) / 2) * 3.4,
    )

    // Arm holes
    for (i in 0..<armCount) {
      rotate(z = i * 360.0 / armCount) {
        hull {
          Cylinder(
            height = wallThickness,
            diameter = armWidth / 1.37,
          )

          translate(x = armLength * .9) {
            Cylinder(
              height = wallThickness,
              diameter = armWidth / 3,
            )
          }
        }
      }
    }
  }

  // Candle holder 1
  translate(x = candleHolderShiftX) {
    CandleHolder(
      wallThickness = wallThickness,
      candleDiameter = candleDiameter,
    )
  }

  // Candle holder 2
  rotate(z = 120) {
    translate(x = candleHolderShiftX) {
      CandleHolder(
        wallThickness = wallThickness,
        candleDiameter = candleDiameter,
      )
    }
  }

  // Candle holder 3
  rotate(z = 240) {
    translate(x = candleHolderShiftX) {
      CandleHolder(
        wallThickness = wallThickness,
        candleDiameter = candleDiameter,
      )
    }
  }
}

@Composable
private fun CandleHolder(wallThickness: Double, candleDiameter: Double) {
  difference {
    Cylinder(
      height = wallThickness * 2,
      diameter = candleDiameter + wallThickness * 2,
    )
    translate(z = wallThickness) {
      Cylinder(
        height = wallThickness,
        diameter = candleDiameter,
      )
    }
    Cylinder(
      height = wallThickness,
      diameter = candleDiameter - wallThickness * 2,
    )
  }
}

@Composable
private fun Arm(
  armWidth: Double,
  wallThickness: Double,
  armLength: Double,
  candleHeight: Double,
) {
  translate(
    y = armWidth / 2,
    z = wallThickness / 2 - SMALLEST_LENGTH,
  ) {
    rotate(x = 90) {
      minkowski {
        linearExtrude(armWidth) {
          Square(width = armLength, height = SMALLEST_LENGTH)
          translate(x = armLength) {
            Square(width = SMALLEST_LENGTH, height = candleHeight * 2)
          }
          val armTopLength = armLength / 10
          translate(
            x = armLength - armTopLength + SMALLEST_LENGTH,
            y = candleHeight * 2,
          ) {
            Square(width = armTopLength, height = SMALLEST_LENGTH)
          }
        }

        Sphere(diameter = wallThickness - SMALLEST_LENGTH * 2)
      }
    }
  }
}

fun main() {
  openScad(
    SystemFileSystem.sink(Path("/Users/bod/Tmp/plate-heater.scad")).buffered(),
  ) {
    Main()
  }
}
