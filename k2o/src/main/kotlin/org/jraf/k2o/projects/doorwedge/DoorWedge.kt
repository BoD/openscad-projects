/*
 * This source is part of the
 *      _____  ___   ____
 *  __ / / _ \/ _ | / __/___  _______ _
 * / // / , _/ __ |/ _/_/ _ \/ __/ _ `/
 * \___/_/|_/_/ |_/_/ (_)___/_/  \_, /
 *                              /___/
 * repository.
 *
 * Copyright (C) 2026-present Benoit 'BoD' Lubek (BoD@JRAF.org)
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

package org.jraf.k2o.projects.doorwedge

import androidx.compose.runtime.Composable
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import org.jraf.k2o.dsl.openScad
import org.jraf.k2o.projects.TMP_FOLDER
import org.jraf.k2o.shapes.LurezLogo
import org.jraf.k2o.stdlib.Color
import org.jraf.k2o.stdlib.Polygon
import org.jraf.k2o.stdlib.color
import org.jraf.k2o.stdlib.difference
import org.jraf.k2o.stdlib.linearExtrude
import org.jraf.k2o.stdlib.rotate
import org.jraf.k2o.stdlib.translate
import org.jraf.k2o.units.Angle.Companion.deg
import org.jraf.k2o.units.Length.Companion.cm
import org.jraf.k2o.units.Length.Companion.mm

@Composable
private fun DoorWedge() {
  val lengthX = 8.cm
  val bigLengthY = 1.cm
  val smallLengthX = 2.mm
  val lengthZ = 3.cm

  difference {
    linearExtrude(height = lengthZ) {
      Polygon(
        0.mm to 0.mm,
        lengthX to 0.mm,
        lengthX to smallLengthX,
        0.mm to bigLengthY,
      )
    }

    val logoWidth = lengthZ * 2 * 0.8
    val logoHeight = logoWidth / 2 // 2:1 aspect ratio
    val logoThickness = 2.mm
    translate(y = bigLengthY, z = (lengthZ - logoHeight) / 2 + logoHeight / 2) {
      rotate(x = -90.deg, z = -5.7.deg) {
        translate(x = (lengthX - logoWidth) / 2 + logoWidth / 2, z = -logoThickness) {
          color(Color.Red) {
            LurezLogo(width = logoWidth, thickness = logoThickness)
          }
        }
      }
    }
  }
}

fun main() {
  openScad(
    SystemFileSystem.sink(Path(TMP_FOLDER, "door-wedge.scad")).buffered(),
  ) {
    DoorWedge()
  }
}
