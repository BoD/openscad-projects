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
import org.jraf.k2o.stdlib.Color
import org.jraf.k2o.stdlib.Cube
import org.jraf.k2o.stdlib.Cylinder
import org.jraf.k2o.stdlib.color
import org.jraf.k2o.stdlib.difference
import org.jraf.k2o.stdlib.translate
import org.jraf.k2o.util.cm
import org.jraf.k2o.util.mm

@Composable
private fun Main() {
  val frameWallThickness = 1.mm
  val frameSideLength = 12.cm
  val frameHeight = 1.cm

  val displayBoosterHeight = 8.mm
  val displayBoosterTopPartWidth = 5.mm
  val displayBoosterTopPartThickness = 1.mm
  val displayBoosterBottomPartWidth = 8.mm


  // Frame
  difference {
    Cube(
      x = frameSideLength,
      y = frameSideLength,
      z = frameHeight,
    )

    translate(
      x = frameWallThickness,
      y = frameWallThickness,
    ) {
      Cube(
        x = frameSideLength - 2 * frameWallThickness,
        y = frameSideLength - 2 * frameWallThickness,
        z = frameHeight - frameWallThickness,
      )
    }

    // Hole to plug the booster
//    translate(
//      x = frameSideLength / 2 - displayBoosterTopPartWidth / 2,
//      y = frameSideLength / 2 - displayBoosterTopPartWidth / 2,
//      z = frameHeight - displayBoosterTopPartThickness / 2,
//      ) {
//      Cube(
//        x = displayBoosterTopPartWidth,
//        y = displayBoosterTopPartWidth,
//        z = displayBoosterTopPartThickness,
//      )
//    }

    translate(
      x = frameSideLength / 4 - displayBoosterTopPartWidth / 2,
      y = frameSideLength / 2 - displayBoosterTopPartWidth / 2,
      z = frameHeight - displayBoosterTopPartThickness / 2,
    ) {
      Cube(
        x = displayBoosterTopPartWidth,
        y = displayBoosterTopPartWidth,
        z = displayBoosterTopPartThickness,
      )
    }
    translate(
      x = frameSideLength * 3 / 4 - displayBoosterTopPartWidth / 2,
      y = frameSideLength / 2 - displayBoosterTopPartWidth / 2,
      z = frameHeight - displayBoosterTopPartThickness / 2,
    ) {
      Cube(
        x = displayBoosterTopPartWidth,
        y = displayBoosterTopPartWidth,
        z = displayBoosterTopPartThickness,
      )
    }
    translate(
      x = frameSideLength / 2 - displayBoosterTopPartWidth / 2,
      y = frameSideLength / 3 - displayBoosterTopPartWidth / 2,
      z = frameHeight - displayBoosterTopPartThickness / 2,
    ) {
      Cube(
        x = displayBoosterTopPartWidth,
        y = displayBoosterTopPartWidth,
        z = displayBoosterTopPartThickness,
      )
    }

  }

  // Booster
  color(Color.Red) {
    val displayBoosterDiameter = 3.mm
    translate(
      x = frameSideLength * 1.1,
    ) {
      translate(
        x = -displayBoosterBottomPartWidth / 2,
        y = -displayBoosterBottomPartWidth / 2,
      ) {
        Cube(
          x = displayBoosterBottomPartWidth,
          y = displayBoosterBottomPartWidth,
          z = displayBoosterTopPartThickness,
        )
      }

      Cylinder(
        height = displayBoosterHeight,
        diameter = displayBoosterDiameter,
      )

      translate(
        x = -displayBoosterTopPartWidth / 2,
        y = -displayBoosterTopPartWidth / 2,
        z = displayBoosterHeight - displayBoosterTopPartThickness,
      ) {
        Cube(
          x = displayBoosterTopPartWidth,
          y = displayBoosterTopPartWidth,
          z = displayBoosterTopPartThickness,
        )
      }
    }
  }
}

fun main() {
  openScad(
    SystemFileSystem.sink(Path("/Users/bod/Tmp/picture-frame-booster.scad")).buffered(),
  ) {
    Main()
  }
}
