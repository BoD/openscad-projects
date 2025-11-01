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
import org.jraf.k2o.stdlib.Cube
import org.jraf.k2o.stdlib.color
import org.jraf.k2o.stdlib.difference
import org.jraf.k2o.stdlib.translate
import org.jraf.k2o.stdlib.union

@Composable
private fun Main() {
  // Coordinates system assumes the phone is flat on a table, screen up, top of the phone is +Y, right side is +X

  val phoneSizeX = 78.7
  val phoneSizeZ = 10.3
  val phoneSizeWithKickstandZ = 14.9

  val gamepadSizeX = 72.1
  val gamepadSizeY = 41.8
  val gamepadSizeZ = 12.1

  val phoneGamepadSeparatorSizeY = 1.0

  val holderThickness = 1.0

  val holderSizeX = maxOf(phoneSizeX, gamepadSizeX) + holderThickness * 2
  val holderSizeY = gamepadSizeY + phoneGamepadSeparatorSizeY + 44.0 + holderThickness
  val holderSizeZ = maxOf(phoneSizeWithKickstandZ, gamepadSizeZ) + holderThickness * 2

  val gamepadZPaddingSizeZ = holderSizeZ - holderThickness * 2 - gamepadSizeZ

  difference {
    union {
      difference {
        // Outer shell
        Cube(
          x = holderSizeX,
          y = holderSizeY,
          z = holderSizeZ,
        )

        // Carve out space for phone and gamepad
        translate(
          x = -holderThickness,
          y = holderThickness,
          z = holderThickness,
        ) {
          Cube(
            x = holderSizeX,
            y = holderSizeY,
            z = holderSizeZ - holderThickness * 2,
          )
        }

        // Gamepad window
        val gamepadWindowBorderRight = 4.8
        val gamepadWindowBorderTop = 8.0
        val gamepadWindowBorderBottom = 8.0
        translate(
          x = 0,
          y = gamepadWindowBorderBottom,
          z = holderSizeZ - holderThickness * 2,
        ) {
          Cube(
            x = gamepadSizeX - gamepadWindowBorderRight + (holderSizeX - gamepadSizeX) / 2,
            y = gamepadSizeY - gamepadWindowBorderTop - gamepadWindowBorderBottom,
            z = holderThickness * 4,
          )
        }

        // Phone window
        val phoneWindowBorderRight = 4.0
        val phoneWindowBorderBottom = 4.0
        translate(
          x = 0,
          y = gamepadSizeY + phoneGamepadSeparatorSizeY + phoneWindowBorderBottom,
          z = holderSizeZ - holderThickness * 2,
        ) {
          Cube(
            x = phoneSizeX - phoneWindowBorderRight,
            y = holderSizeY - gamepadSizeY - phoneGamepadSeparatorSizeY - phoneWindowBorderBottom,
            z = holderThickness * 4,
          )
        }
      }

      // Separator between phone and gamepad
      translate(
        x = 0,
        y = gamepadSizeY,
        z = holderThickness,
      ) {
        Cube(
          x = holderSizeX - holderThickness,
          y = phoneGamepadSeparatorSizeY,
          z = holderSizeZ - holderThickness * 2,
        )
      }

      // Gamepad Z padding
      translate(
        x = 0,
        y = holderThickness,
        z = holderThickness,
      ) {
        color("red") {
          Cube(
            x = holderSizeX - holderThickness,
            y = gamepadSizeY - holderThickness,
            z = gamepadZPaddingSizeZ,
          )
        }
      }

      // Gamepad X padding
      val gamepadXPaddingSizeX = (holderSizeX - gamepadSizeX) / 2
      translate(
        x = holderSizeX - gamepadXPaddingSizeX - holderThickness,
        y = holderThickness,
        z = holderThickness,
      ) {
        color("blue") {
          Cube(
            x = gamepadXPaddingSizeX,
            y = gamepadSizeY - holderThickness,
            z = holderSizeZ - holderThickness * 2,
          )
        }
      }

      // Gamepad rail A
      val buttonSize = 2.4
      val gamepadRailSizeZ = (gamepadSizeZ - buttonSize) / 2.0
      translate(
        x = 0,
        y = holderThickness,
        z = holderThickness + gamepadZPaddingSizeZ,
      ) {
        color("cyan") {
          Cube(
            x = holderSizeX - holderThickness,
            y = 1.0,
            z = gamepadRailSizeZ,
          )
        }
      }

      // Gamepad rail B
      translate(
        x = 0,
        y = holderThickness,
        z = holderThickness + gamepadZPaddingSizeZ + gamepadRailSizeZ + buttonSize,
      ) {
        color("cyan") {
          Cube(
            x = holderSizeX - holderThickness,
            y = 1.0,
            z = gamepadRailSizeZ,
          )
        }
      }


      // Phone Z padding A
      val kickstandSizeZ = phoneSizeWithKickstandZ - phoneSizeZ
      val phoneZPaddingASizeX = holderSizeX - holderThickness
      val phoneZPaddingASizeZ = kickstandSizeZ
      val kickstandBorderYBottom = 10.8
      translate(
        x = 0,
        y = gamepadSizeY + phoneGamepadSeparatorSizeY,
        z = holderThickness,
      ) {
        color("green") {
          Cube(
            x = phoneZPaddingASizeX,
            y = kickstandBorderYBottom,
            z = phoneZPaddingASizeZ,
          )
        }
      }

      // Phone Z padding B
      val kickstandSizeY = 16.0
      val phoneZPaddingBSizeX = phoneZPaddingASizeX
      val phoneZPaddingBSizeZ = phoneZPaddingASizeZ
      val phoneZPaddingBSizeY = holderSizeY - gamepadSizeY - phoneGamepadSeparatorSizeY - kickstandBorderYBottom - kickstandSizeY
      translate(
        x = 0,
        y = gamepadSizeY + phoneGamepadSeparatorSizeY + kickstandBorderYBottom + kickstandSizeY,
        z = holderThickness,
      ) {
        color("yellow") {
          Cube(
            x = phoneZPaddingBSizeX,
            y = phoneZPaddingBSizeY,
            z = phoneZPaddingBSizeZ,
          )
        }
      }
    }

    // Gamepad access cutout
    val gamepadAccessCutoutSizeY = 11.0
    val gamepadAccessCutoutSizeZ = 7.0
    translate(
      x = 0,
      y = (gamepadSizeY - gamepadAccessCutoutSizeY) / 2,
      z = holderThickness + gamepadZPaddingSizeZ + (gamepadSizeZ - gamepadAccessCutoutSizeZ) / 2,
    ) {
      Cube(
        x = holderSizeX,
        y = gamepadAccessCutoutSizeY,
        z = gamepadAccessCutoutSizeZ,
      )
    }
  }
}

fun main() {
  openScad(SystemFileSystem.sink(Path("/Users/bod/Tmp/PhoneGamepadHolder.scad")).buffered()) {
    Main()
  }
}
