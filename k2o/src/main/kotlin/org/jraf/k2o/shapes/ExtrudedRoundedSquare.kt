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
import org.jraf.k2o.stdlib.linearExtrude
import org.jraf.k2o.stdlib.translate

@Composable
fun ExtrudedRoundedSquare(
  x: Number,
  y: Number,
  z: Number,
  radius: Number = 0,
) {
  ExtrudedRoundedSquare(
    x = x,
    y = y,
    z = z,
    topLeftRadius = radius,
    topRightRadius = radius,
    bottomRightRadius = radius,
    bottomLeftRadius = radius,
  )
}

@Composable
fun ExtrudedRoundedSquare(
  x: Number,
  y: Number,
  z: Number,
  topLeftRadius: Number = 0,
  topRightRadius: Number = 0,
  bottomRightRadius: Number = 0,
  bottomLeftRadius: Number = 0,
) {
  linearExtrude(z) {
    RoundedSquare(
      x = x,
      y = y,
      topLeftRadius = topLeftRadius,
      topRightRadius = topRightRadius,
      bottomRightRadius = bottomRightRadius,
      bottomLeftRadius = bottomLeftRadius,
    )
  }
}

fun main() {
  openScad(SystemFileSystem.sink(Path("/Users/bod/Tmp/extruded-rounded-square.scad")).buffered()) {
    ExtrudedRoundedSquare(
      x = 320,
      y = 200,
      z = 20,
      topLeftRadius = 30,
      topRightRadius = 50,
      bottomRightRadius = 70,
      bottomLeftRadius = 80,
    )

    translate(x = 400) {
      ExtrudedRoundedSquare(
        x = 320,
        y = 200,
        z = 20,
        topLeftRadius = 0,
        topRightRadius = 0,
        bottomRightRadius = 0,
        bottomLeftRadius = 0,
      )
    }

    translate(x = 800) {
      ExtrudedRoundedSquare(
        x = 320,
        y = 200,
        z = 20,
        topLeftRadius = 0,
        topRightRadius = 50,
        bottomRightRadius = 70,
        bottomLeftRadius = 80,
      )
    }

    translate(x = 1200) {
      ExtrudedRoundedSquare(
        x = 320,
        y = 200,
        z = 20,
        topLeftRadius = 30,
        topRightRadius = 0,
        bottomRightRadius = 0,
        bottomLeftRadius = 80,
      )
    }

    translate(x = 1600) {
      ExtrudedRoundedSquare(
        x = 320,
        y = 200,
        z = 20,
        80,
      )
    }
  }
}
