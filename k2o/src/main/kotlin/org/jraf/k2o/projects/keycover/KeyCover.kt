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

@file:Suppress("SameParameterValue", "WrapUnaryOperator")

package org.jraf.k2o.projects.keycover

import androidx.compose.runtime.Composable
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import org.jraf.k2o.dsl.openScad
import org.jraf.k2o.projects.TMP_FOLDER
import org.jraf.k2o.shapes.BoDLogo
import org.jraf.k2o.stdlib.Cube
import org.jraf.k2o.stdlib.Import
import org.jraf.k2o.stdlib.difference
import org.jraf.k2o.stdlib.linearExtrude
import org.jraf.k2o.stdlib.resize
import org.jraf.k2o.stdlib.rotate
import org.jraf.k2o.stdlib.translate
import org.jraf.k2o.units.Angle.Companion.deg
import org.jraf.k2o.units.Length
import org.jraf.k2o.units.Length.Companion.mm

@Composable
private fun KeyCoverShape(
  width: Length,
  height: Length,
  thickness: Length,
) {
  translate(-width / 2, -height / 2) {
    linearExtrude(height = thickness) {
      resize(width, height, auto = true) {
        Import("/Users/bod/gitrepo/openscad-projects/k2o/src/main/resources/key.svg", center = false)
      }
    }
  }
}

@Composable
private fun MainPart(
  keyHeadWidth: Length,
  keyHeadHeight: Length,
  keyThickness: Length,
  wallWidth: Length,
  cutOutWidth: Length,
) {
  val width = keyHeadWidth + wallWidth * 2
  val height = keyHeadHeight + wallWidth * 2
  val thickness = keyThickness + wallWidth * 2
  difference {
    // Outside
    KeyCoverShape(
      width = width,
      height = height,
      thickness = thickness,
    )

    // Inside carving (make it hollow)
    translate(z = wallWidth) {
      KeyCoverShape(
        width = keyHeadWidth,
        height = keyHeadHeight,
        thickness = keyThickness,
      )
    }

    // Fix inside carving (the ring hole)
    translate(
      x = 4.mm,
      y = -5.mm,
      z = wallWidth,
    ) {
      Cube(
        x = 8.mm,
        y = 10.mm,
        z = keyThickness,
      )
    }

    // Cutout because the key head needs to be very close to the lock
    translate(
      x = -width / 2,
      y = -height / 2,
    ) {
      Cube(
        x = cutOutWidth,
        y = height,
        z = thickness,
      )
    }

    // Bottom logo
    translate(x = -1.mm, z = wallWidth / 2) {
      rotate(z = 90.deg, x = 180.deg) {
        BoDLogo(
          width = 20.mm,
          thickness = wallWidth,
        )
      }
    }

    // Top logo
    translate(x = -1.mm, z = thickness - wallWidth / 2) {
      rotate(z = -90.deg) {
        BoDLogo(
          width = 20.mm,
          thickness = wallWidth,
        )
      }
    }
  }
}

@Composable
private fun KeyCover() {
  val keyHeadWidth = 26.1.mm
  val keyHeadHeight = 26.9.mm
  val keyThickness = 3.15.mm

  val wallWidth = 1.mm

  val cutOutWidth = 3.mm

  MainPart(
    keyHeadWidth = keyHeadWidth,
    keyHeadHeight = keyHeadHeight,
    keyThickness = keyThickness,
    wallWidth = wallWidth,
    cutOutWidth = cutOutWidth,
  )

  // Support
  val supportHeight = 14.mm
  val supportWidth = 18.mm
  translate(
    x = -30.mm,
    y = -supportHeight / 2,
  ) {
    Cube(
      x = supportWidth,
      y = supportHeight,
      z = wallWidth,
    )
  }
}

fun main() {
  openScad(SystemFileSystem.sink(Path(TMP_FOLDER, "key-cover.scad")).buffered()) {
    KeyCover()
  }
}
