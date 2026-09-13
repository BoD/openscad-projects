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

package org.jraf.k2o.projects.plateheater

import androidx.compose.runtime.Composable
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import org.jraf.k2o.dsl.openScad
import org.jraf.k2o.projects.TMP_FOLDER
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
import org.jraf.k2o.units.Angle.Companion.deg
import org.jraf.k2o.units.Angle.Companion.times
import org.jraf.k2o.units.Length
import org.jraf.k2o.units.Length.Companion.cm
import org.jraf.k2o.units.Length.Companion.mm

@Composable
private fun PlateHeater() {
  val wallThickness = 3.mm
  val candleDiameter = 38.5.mm
  val plateBaseDiameter = 13.5.cm

  val candleHolderRadius = 4.cm

  val armCount = 3

  val armLengthX = plateBaseDiameter / 2
  val armLengthY = 1.5.cm
  val armLengthZ = 4.cm


  difference {
    union {
      difference {
        union {
          // Arms
          for (i in 0..<armCount) {
            rotate(z = i * 360.deg / armCount) {
              Arm(
                armLengthX = armLengthX,
                armLengthY = armLengthY,
                armLengthZ = armLengthZ,
                wallThickness = wallThickness,
              )
            }
          }
        }

        // Center circle hole
        Cylinder(
          height = wallThickness,
          radius = candleHolderRadius - wallThickness / 2,
        )
      }

      // Candle holders
      for (i in 0..<armCount) {
        rotate(z = i * 360.deg / armCount + 360.deg / armCount / 2) {
          translate(x = candleHolderRadius) {
            CandleHolder(
              wallThickness = wallThickness,
              candleDiameter = candleDiameter,
            )
          }
        }
      }

      // Center circle
      difference {
        Cylinder(
          height = wallThickness,
          radius = candleHolderRadius + wallThickness / 2,
        )

        Cylinder(
          height = wallThickness,
          radius = candleHolderRadius - wallThickness / 2,
        )
      }
    }

    // Arm holes
    for (i in 0..<armCount) {
      rotate(z = i * 360.deg / armCount) {
        hull {
          Cylinder(
            height = wallThickness,
            diameter = armLengthY,
          )

          translate(x = armLengthX * .9) {
            Cylinder(
              height = wallThickness,
              diameter = armLengthY / 2,
            )
          }
        }
      }
    }

    // Candle holders holes
    for (i in 0..<armCount) {
      rotate(z = i * 360.deg / armCount + 360.deg / armCount / 2) {
        translate(x = candleHolderRadius) {
          Cylinder(
            height = wallThickness,
            diameter = candleDiameter - wallThickness * 2,
          )
        }
      }
    }
  }
}

@Composable
private fun CandleHolder(wallThickness: Length, candleDiameter: Length) {
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
  armLengthX: Length,
  armLengthY: Length,
  armLengthZ: Length,
  wallThickness: Length,
) {
  translate(
    y = armLengthY / 2,
    z = wallThickness / 2 - Length.Smallest,
  ) {
    rotate(x = 90.deg) {
      minkowski {
        linearExtrude(armLengthY) {
          Square(width = armLengthX, height = Length.Smallest)
          translate(x = armLengthX) {
            Square(width = Length.Smallest, height = armLengthZ)
          }
          val armTopLength = armLengthX / 10
          translate(
            x = armLengthX - armTopLength + Length.Smallest,
            y = armLengthZ,
          ) {
            Square(width = armTopLength, height = Length.Smallest)
          }
        }

        Sphere(diameter = wallThickness - Length.Smallest * 2)
      }
    }
  }
}

fun main() {
  openScad(
    SystemFileSystem.sink(Path(TMP_FOLDER, "plate-heater.scad")).buffered(),
  ) {
    PlateHeater()
  }
}
