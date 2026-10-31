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

package org.jraf.k2o.projects.shelfbracket

import androidx.compose.runtime.Composable
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import org.jraf.k2o.dsl.openScad
import org.jraf.k2o.projects.TMP_FOLDER
import org.jraf.k2o.stdlib.Circle
import org.jraf.k2o.stdlib.hull
import org.jraf.k2o.stdlib.linearExtrude
import org.jraf.k2o.stdlib.translate
import org.jraf.k2o.stdlib.union
import org.jraf.k2o.units.Length
import org.jraf.k2o.units.Length.Companion.cm
import org.jraf.k2o.units.Length.Companion.mm

@Composable
private fun ShelfBracket() {
  val lengthX = 140.mm
  val lengthY = 130.mm
  val beamThickness = 1.cm
  val lengthZ = 2.4.cm

  linearExtrude(height = lengthZ) {
    union {
      Beam2D(
        x1 = beamThickness / 2, y1 = beamThickness / 2,
        x2 = beamThickness / 2, y2 = lengthY - beamThickness / 2,
        thickness = beamThickness,
      )

      Beam2D(
        x1 = beamThickness / 2, y1 = lengthY - beamThickness / 2,
        x2 = lengthX - beamThickness / 2, y2 = lengthY - beamThickness / 2,
        thickness = beamThickness,
      )

      translate(
        x = beamThickness,
        y = lengthY - beamThickness,
      ) {
        Circle(radius = beamThickness)
      }

      Beam2D(
        x1 = beamThickness / 2, y1 = beamThickness / 2,
        x2 = lengthX - beamThickness / 2, y2 = lengthY - beamThickness / 2,
        thickness = beamThickness,
      )
    }
  }
}

@Composable
private fun Beam2D(
  x1: Length, y1: Length,
  x2: Length, y2: Length,
  thickness: Length,
) {
  hull {
    translate(x = x1, y = y1) {
      Circle(diameter = thickness)
    }
    translate(x = x2, y = y2) {
      Circle(diameter = thickness)
    }
  }
}


fun main() {
  openScad(
    SystemFileSystem.sink(Path(TMP_FOLDER, "shelf-bracket.scad")).buffered(),
    fa = .1,
    fs = .1,
  ) {
    ShelfBracket()
  }
}
