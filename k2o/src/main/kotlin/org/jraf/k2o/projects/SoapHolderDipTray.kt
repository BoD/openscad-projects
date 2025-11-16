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
import org.jraf.k2o.shapes.RoundedCuboid
import org.jraf.k2o.stdlib.Cube
import org.jraf.k2o.stdlib.difference
import org.jraf.k2o.stdlib.translate

@Composable
private fun Main() {
  val soapHolderLengthX = 131.4
  val soapHolderLengthY = 83.4
  val soapHolderLengthZ = 24.4

  val wallThickness = 1.0
  val wallHeight = soapHolderLengthZ / 4

  val tolerance = 1.0

  val lengthX = soapHolderLengthX + wallThickness * 2 + tolerance
  val lengthY = soapHolderLengthY + wallThickness * 2 + tolerance
  difference {
    RoundedCuboid(
      x = lengthX,
      y = lengthY,
      z = wallHeight + wallThickness,
      radius = 1,
    )

    translate(
      x = wallThickness,
      y = wallThickness,
      z = wallThickness,
    ) {
      Cube(
        x = soapHolderLengthX + tolerance,
        y = soapHolderLengthY + tolerance,
        z = wallHeight,
      )
    }

    // Lurez logo
    translate(
      x = lengthX / 4 + lengthX / 2 - (lengthX / 2) / 2,
      y = lengthY / 4 + lengthY / 2 - (lengthY / 2) / 2,
      z = wallThickness - wallThickness / 3,
    ) {
      LurezLogo(
        width = lengthX / 2,
        thickness = 1,
      )
    }
  }
}

fun main() {
  openScad(
    SystemFileSystem.sink(Path("/Users/bod/Tmp/soap-holder-dip-tray.scad")).buffered(),
    fa = .1,
    fs = .1,
  ) {
    Main()
  }
}
