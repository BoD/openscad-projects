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
import org.jraf.k2o.stdlib.Circle
import org.jraf.k2o.stdlib.Square
import org.jraf.k2o.stdlib.difference
import org.jraf.k2o.stdlib.rotate
import org.jraf.k2o.stdlib.rotateExtrude
import org.jraf.k2o.stdlib.translate

@Composable
private fun crossSection(
  diameter: Int,
  thickness: Int,
  borderHeight: Int,
  borderAngle: Int,
) {
  Square(diameter / 2, thickness)

  translate(diameter / 2, thickness) {
    rotate(borderAngle) {
      translate(0, -thickness) {
        Square(
          borderHeight,
          thickness,
        )
      }
    }
  }

  translate(diameter / 2, thickness) {
    difference {
      Circle(thickness)
      translate(-thickness, 0) {
        Square(thickness * 2)
      }
    }
  }
}

@Composable
private fun PotPlate() {
  val diameter = 140
  val thickness = 4
  val borderHeight = 25
  val borderAngle = 45
  val logoThickness = 1

  difference {
    rotateExtrude {
      crossSection(
        diameter = diameter,
        thickness = thickness,
        borderHeight = borderHeight,
        borderAngle = borderAngle,
      )
    }

    translate(0, 0, thickness - logoThickness) {
      LurezLogo(
        width = 120,
        thickness = logoThickness,
      )
    }
  }
}

fun main() {
  openScad(SystemFileSystem.sink(Path("/Users/bod/Tmp/pot-plate.scad")).buffered()) {
    PotPlate()
  }
}
