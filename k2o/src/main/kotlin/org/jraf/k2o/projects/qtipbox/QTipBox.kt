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

package org.jraf.k2o.projects.qtipbox

import androidx.compose.runtime.Composable
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import org.jraf.k2o.dsl.openScad
import org.jraf.k2o.projects.TMP_FOLDER
import org.jraf.k2o.shapes.LzLogo
import org.jraf.k2o.stdlib.Cube
import org.jraf.k2o.stdlib.Import
import org.jraf.k2o.stdlib.difference
import org.jraf.k2o.stdlib.linearExtrude
import org.jraf.k2o.stdlib.offset
import org.jraf.k2o.stdlib.resize
import org.jraf.k2o.stdlib.rotate
import org.jraf.k2o.stdlib.translate
import org.jraf.k2o.stdlib.union
import org.jraf.k2o.units.Angle.Companion.deg
import org.jraf.k2o.units.Length.Companion.cm
import org.jraf.k2o.units.Length.Companion.mm

@Composable
private fun QTipBox() {
  val diameter = 7.cm
  val qTipHeight = 8.cm
  val height = qTipHeight * .7
  val wallWidth = 2.mm
  val twist = 90.deg
  val cutAngle = 10.deg

  difference {

    union {
      linearExtrude(height = height, twist = twist) {
        difference {
          resize(x = diameter, auto = true) {
            RoundedStar12()
          }

          offset(-wallWidth) {
            resize(x = diameter, auto = true) {
              RoundedStar12()
            }
          }
        }
      }

      // Bottom
      difference {
        linearExtrude(height = wallWidth, twist = twist / (height / wallWidth)) {
          resize(x = diameter, auto = true) {
            RoundedStar12()
          }
        }

        // Lz logo
        translate(z = wallWidth / 2) {
          rotate(90.deg) {
            LzLogo(width = diameter * .6, thickness = wallWidth / 2)
          }
        }
      }
    }

    // Cut at an angle
    val cubeSize = diameter * 1.1
    translate(x = -cubeSize / 2, y = -cubeSize / 2, z = height) {
      rotate(y = cutAngle) {
        Cube(x = cubeSize, y = cubeSize, z = height)
      }
    }

  }
}

@Composable
private fun RoundedStar12() {
  Import("/Users/bod/gitrepo/openscad-projects/k2o/src/main/resources/rounded-star-12.svg", center = true)
}

fun main() {
  openScad(
    fa = .2,
    fs = .2,
    sink =
      SystemFileSystem.sink(Path(TMP_FOLDER, "q-tip-box.scad")).buffered(),
  ) {
    QTipBox()
  }
}
