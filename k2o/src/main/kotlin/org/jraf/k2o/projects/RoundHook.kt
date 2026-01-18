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
import org.jraf.k2o.shapes.LurezLogo
import org.jraf.k2o.shapes.RoundedExtrudedRoundedSquare
import org.jraf.k2o.shapes.RoundedHalfCylinder
import org.jraf.k2o.stdlib.difference
import org.jraf.k2o.stdlib.rotate
import org.jraf.k2o.stdlib.translate

@Composable
private fun Main() {
  val lengthX = 25.0
  val thickness = 3.0

  val middlePartZLength = 20.0
  val hookRadius = 22.4 / 2 + thickness

  // Middle part
  translate(
    z = -thickness / 2,
  ) {
    difference {
      RoundedExtrudedRoundedSquare(
        x = lengthX,
        y = thickness,
        z = middlePartZLength + thickness,
        topLeftRadius = 0,
        topRightRadius = 0,
        bottomRightRadius = 0,
        bottomLeftRadius = 0,
        roundingRadius = thickness / 2,
      )

      val logoWidth = lengthX - thickness * 2
      translate(
        x = lengthX / 2,
        y = thickness / 2,
        z = (middlePartZLength + thickness) / 2,
      ) {
        rotate(x = 90) {
          LurezLogo(
            width = logoWidth,
            thickness = thickness / 2,
          )
        }
      }
    }
  }

  // Bottom hook
  translate(
    y = -hookRadius + thickness,
  ) {
    rotate(x = -90) {
      rotate(y = 90) {
        RoundedHalfCylinder(
          height = lengthX,
          radius = hookRadius,
          thickness = thickness,
        )
      }
    }
  }

  // Bottom hook continuation
  translate(
    y = -hookRadius * 2 + thickness * 2,
    z = -thickness / 2,
  ) {
    rotate(x = 90) {
      RoundedExtrudedRoundedSquare(
        x = lengthX,
        y = middlePartZLength / 3 + thickness,
        z = thickness,
        topLeftRadius = (middlePartZLength / 3 + thickness) / 2,
        topRightRadius = (middlePartZLength / 3 + thickness) / 2,
        bottomRightRadius = 0,
        bottomLeftRadius = 0,
        roundingRadius = thickness / 2,
      )
    }
  }

  // Top hook
  translate(
    y = hookRadius,
    z = middlePartZLength,
  ) {
    rotate(x = 90) {
      rotate(y = 90) {
        RoundedHalfCylinder(
          height = lengthX,
          radius = hookRadius,
          thickness = thickness,
        )
      }
    }
  }

  // Top hook continuation
  translate(
    y = hookRadius * 2 - thickness,
    z = middlePartZLength + thickness / 2,
  ) {
    rotate(x = -90) {
      RoundedExtrudedRoundedSquare(
        x = lengthX,
        y = middlePartZLength / 3 + thickness,
        z = thickness,
        topLeftRadius = (middlePartZLength / 3 + thickness) / 2,
        topRightRadius = (middlePartZLength / 3 + thickness) / 2,
        bottomRightRadius = 0,
        bottomLeftRadius = 0,
        roundingRadius = thickness / 2,
      )
    }
  }
}


fun main() {
  openScad(
    SystemFileSystem.sink(Path("/Users/bod/Tmp/round-hook.scad")).buffered(),
    fa = .1,
    fs = .1,
  ) {
    Main()
  }
}
