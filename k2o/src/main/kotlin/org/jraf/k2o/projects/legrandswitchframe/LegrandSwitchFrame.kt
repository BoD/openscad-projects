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

@file:Suppress("SameParameterValue")

package org.jraf.k2o.projects.legrandswitchframe

import androidx.compose.runtime.Composable
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import org.jraf.k2o.dsl.openScad
import org.jraf.k2o.projects.TMP_FOLDER
import org.jraf.k2o.stdlib.Cube
import org.jraf.k2o.stdlib.difference
import org.jraf.k2o.stdlib.translate
import org.jraf.k2o.units.Length.Companion.cm
import org.jraf.k2o.units.Length.Companion.mm

@Composable
private fun LegrandSwitchFrame() {
  val switchLengthX = 8.5.cm
//  val switchLengthY = switchLengthX
//  val switchLengthY = 156.mm
  val switchLengthY = 227.mm
  val switchLengthZ = 7.mm
  val coverTopLength = 5.mm
  val coverThickness = 1.6.mm

  val coverOutsideLengthX = switchLengthX + coverThickness * 2
  val coverOutsideLengthY = switchLengthY + coverThickness * 2
  difference {
    Cube(
      x = coverOutsideLengthX,
      y = coverOutsideLengthY,
      z = switchLengthZ + coverThickness,
    )

    translate(
      x = coverThickness,
      y = coverThickness,
    ) {
      Cube(
        x = switchLengthX,
        y = switchLengthY,
        z = switchLengthZ,
      )
    }

    translate(
      x = coverTopLength,
      y = coverTopLength,
    ) {
      Cube(
        x = coverOutsideLengthX - coverTopLength * 2,
        y = coverOutsideLengthY - coverTopLength * 2,
        z = switchLengthZ + coverThickness,
      )
    }
  }
}

fun main() {
  openScad(
    SystemFileSystem.sink(Path(TMP_FOLDER, "legrand-switch-frame.scad")).buffered(),
    fa = .1,
    fs = .1,
  ) {
    LegrandSwitchFrame()
  }
}
