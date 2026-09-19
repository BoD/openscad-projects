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

package org.jraf.k2o.projects.teabox

import androidx.compose.runtime.Composable
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import org.jraf.k2o.dsl.openScad
import org.jraf.k2o.projects.TMP_FOLDER
import org.jraf.k2o.projects.genericbox.GenericBox
import org.jraf.k2o.stdlib.Import
import org.jraf.k2o.stdlib.difference
import org.jraf.k2o.stdlib.linearExtrude
import org.jraf.k2o.stdlib.resize
import org.jraf.k2o.stdlib.rotate
import org.jraf.k2o.stdlib.translate
import org.jraf.k2o.units.Angle.Companion.deg
import org.jraf.k2o.units.Length
import org.jraf.k2o.units.Length.Companion.cm
import org.jraf.k2o.units.Length.Companion.mm


@Composable
private fun TeaBox() {
  val wallThickness = 2.mm
  val totalLengthZ = 7.5.cm + wallThickness
  val topLengthZ = 2.cm

  val boxLengthX = 15.cm + wallThickness
  val boxLengthY = 7.cm + wallThickness

  val logoMargin = 2.mm
  val logoLengthY = totalLengthZ - topLengthZ * 2 - logoMargin * 2
  val logoLengthX = logoLengthY / 22.0 * 38.0

  difference {
    GenericBox(
      lengthX = boxLengthX,
      lengthY = boxLengthY,
      lengthZBottom = totalLengthZ - topLengthZ,
      lengthZTop = topLengthZ,
      wallThickness = wallThickness,
      radius = 5.mm,
    )

    translate(x = boxLengthX / 2, y = wallThickness / 2, z = logoLengthY / 2 + topLengthZ) {
      rotate(x = 90.deg) {
        TeaLogo(
          width = logoLengthX,
          thickness = wallThickness / 2,
        )
      }
    }


    translate(x = boxLengthX / 2, y = boxLengthY - wallThickness / 2, z = logoLengthY / 2 + topLengthZ) {
      rotate(z = 180.deg) {
        rotate(x = 90.deg) {
          TeaLogo(
            width = logoLengthX,
            thickness = wallThickness / 2,
          )
        }
      }
    }
  }

}

@Composable
private fun TeaLogo(
  width: Length,
  thickness: Length,
) {
  // 38x22
  linearExtrude(height = thickness) {
    resize(x = width, auto = true) {
      Import("/Users/bod/gitrepo/openscad-projects/k2o/src/main/kotlin/org/jraf/k2o/projects/teabox/tea.svg", center = true)
    }
  }
}

fun main() {
  openScad(
    SystemFileSystem.sink(Path(TMP_FOLDER, "tea-box.scad")).buffered(),
    fa = .1,
    fs = .1,
  ) {
    TeaBox()
  }
}
