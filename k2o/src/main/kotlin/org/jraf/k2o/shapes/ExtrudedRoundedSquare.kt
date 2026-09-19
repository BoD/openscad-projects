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

package org.jraf.k2o.shapes

import androidx.compose.runtime.Composable
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import org.jraf.k2o.dsl.openScad
import org.jraf.k2o.projects.TMP_FOLDER
import org.jraf.k2o.stdlib.linearExtrude
import org.jraf.k2o.stdlib.translate
import org.jraf.k2o.units.Length
import org.jraf.k2o.units.Length.Companion.mm

@Composable
fun ExtrudedRoundedSquare(
  x: Length,
  y: Length,
  z: Length,
  radius: Length = Length.Zero,
  offset: Length = Length.Zero,
) {
  ExtrudedRoundedSquare(
    x = x,
    y = y,
    z = z,
    topLeftRadius = radius,
    topRightRadius = radius,
    bottomRightRadius = radius,
    bottomLeftRadius = radius,
    offset = offset,
  )
}

@Composable
fun ExtrudedRoundedSquare(
  x: Length,
  y: Length,
  z: Length,
  topLeftRadius: Length = Length.Zero,
  topRightRadius: Length = Length.Zero,
  bottomRightRadius: Length = Length.Zero,
  bottomLeftRadius: Length = Length.Zero,
  offset: Length = Length.Zero,
) {
  linearExtrude(z) {
    RoundedSquare(
      x = x,
      y = y,
      topLeftRadius = topLeftRadius,
      topRightRadius = topRightRadius,
      bottomRightRadius = bottomRightRadius,
      bottomLeftRadius = bottomLeftRadius,
      offset = offset,
    )
  }
}

fun main() {
  openScad(SystemFileSystem.sink(Path(TMP_FOLDER, "extruded-rounded-square.scad")).buffered()) {
    ExtrudedRoundedSquare(
      x = 320.mm,
      y = 200.mm,
      z = 20.mm,
      topLeftRadius = 30.mm,
      topRightRadius = 50.mm,
      bottomRightRadius = 70.mm,
      bottomLeftRadius = 80.mm,
    )

    translate(x = 400.mm) {
      ExtrudedRoundedSquare(
        x = 320.mm,
        y = 200.mm,
        z = 20.mm,
        topLeftRadius = 0.mm,
        topRightRadius = 0.mm,
        bottomRightRadius = 0.mm,
        bottomLeftRadius = 0.mm,
      )
    }

    translate(x = 800.mm) {
      ExtrudedRoundedSquare(
        x = 320.mm,
        y = 200.mm,
        z = 20.mm,
        topLeftRadius = 0.mm,
        topRightRadius = 50.mm,
        bottomRightRadius = 70.mm,
        bottomLeftRadius = 80.mm,
      )
    }

    translate(x = 1200.mm) {
      ExtrudedRoundedSquare(
        x = 320.mm,
        y = 200.mm,
        z = 20.mm,
        topLeftRadius = 30.mm,
        topRightRadius = 0.mm,
        bottomRightRadius = 0.mm,
        bottomLeftRadius = 80.mm,
      )
    }

    translate(x = 1600.mm) {
      ExtrudedRoundedSquare(
        x = 320.mm,
        y = 200.mm,
        z = 20.mm,
        80.mm,
      )
    }
  }
}
