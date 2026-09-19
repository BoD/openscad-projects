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

package org.jraf.k2o.projects.genericbox

import androidx.compose.runtime.Composable
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import org.jraf.k2o.dsl.openScad
import org.jraf.k2o.projects.TMP_FOLDER
import org.jraf.k2o.shapes.ExtrudedRoundedSquare
import org.jraf.k2o.stdlib.Color
import org.jraf.k2o.stdlib.Comment
import org.jraf.k2o.stdlib.color
import org.jraf.k2o.stdlib.difference
import org.jraf.k2o.stdlib.translate
import org.jraf.k2o.stdlib.union
import org.jraf.k2o.units.Length
import org.jraf.k2o.units.Length.Companion.cm
import org.jraf.k2o.units.Length.Companion.mm

@Composable
fun GenericBox(
  lengthX: Length,
  lengthY: Length,
  lengthZ: Length,
  wallThickness: Length,
  radius: Length,
  jointLength: Length = wallThickness * 1.5,
) {
  GenericBox(
    lengthX = lengthX,
    lengthY = lengthY,
    lengthZBottom = lengthZ / 2,
    lengthZTop = lengthZ / 2,
    wallThickness = wallThickness,
    radius = radius,
    jointLength = jointLength,
  )
}

@Composable
fun GenericBox(
  lengthX: Length,
  lengthY: Length,
  lengthZBottom: Length,
  lengthZTop: Length,
  wallThickness: Length,
  radius: Length,
  jointLength: Length = wallThickness * 1.5,
) {
  union {
    Comment("Bottom part", false)
    BottomPart(
      lengthX = lengthX,
      lengthY = lengthY,
      lengthZ = lengthZBottom,
      wallThickness = wallThickness,
      jointLength = jointLength,
      radius = radius,
    )

    Comment("Top part")
    translate(x = lengthX + 1.cm) {
      TopPart(
        lengthX = lengthX,
        lengthY = lengthY,
        lengthZ = lengthZTop,
        wallThickness = wallThickness,
        jointLength = jointLength,
        radius = radius,
      )
    }
  }
}

@Composable
private fun BottomPart(
  lengthX: Length,
  lengthY: Length,
  lengthZ: Length,
  wallThickness: Length,
  jointLength: Length,
  radius: Length,
) {
  difference {
    ExtrudedRoundedSquare(
      x = lengthX,
      y = lengthY,
      z = lengthZ,
      radius = radius,
    )
    translate(
      x = wallThickness,
      y = wallThickness,
      z = wallThickness,
    ) {
      ExtrudedRoundedSquare(
        x = lengthX,
        y = lengthY,
        z = lengthZ,
        radius = radius,
        offset = -wallThickness,
      )
    }

    Comment("Joint")
    color(Color.Red) {
      translate(
        x = wallThickness / 2,
        y = wallThickness / 2,
        z = lengthZ - jointLength,
      ) {
        ExtrudedRoundedSquare(
          x = lengthX,
          y = lengthY,
          z = jointLength,
          radius = radius,
          offset = -wallThickness / 2,
        )
      }
    }
  }
}

@Composable
private fun TopPart(
  lengthX: Length,
  lengthY: Length,
  lengthZ: Length,
  wallThickness: Length,
  jointLength: Length,
  radius: Length,
) {
  difference {
    ExtrudedRoundedSquare(
      x = lengthX,
      y = lengthY,
      z = lengthZ,
      radius = radius,
    )
    translate(
      x = wallThickness,
      y = wallThickness,
      z = wallThickness,
    ) {
      ExtrudedRoundedSquare(
        x = lengthX,
        y = lengthY,
        z = lengthZ,
        radius = radius,
        offset = -wallThickness,
      )
    }
  }

  Comment("Joint")
  color(Color.Red) {
    difference {
      translate(
        x = wallThickness / 2,
        y = wallThickness / 2,
        z = lengthZ,
      ) {
        ExtrudedRoundedSquare(
          x = lengthX,
          y = lengthY,
          z = jointLength,
          radius = radius,
          offset = -wallThickness / 2,
        )
      }

      translate(
        x = wallThickness,
        y = wallThickness,
        z = lengthZ,
      ) {
        ExtrudedRoundedSquare(
          x = lengthX,
          y = lengthY,
          z = jointLength,
          radius = radius,
          offset = -wallThickness,
        )
      }
    }
  }
}

fun main() {
  openScad(
    SystemFileSystem.sink(Path(TMP_FOLDER, "generic-box.scad")).buffered(),
    fa = .1,
    fs = .1,
  ) {
    val wallThickness = 2.mm
    val totalLengthZ = 7.5.cm + wallThickness
    val topLengthZ = 2.cm
    GenericBox(
      lengthX = 15.cm + wallThickness,
      lengthY = 7.cm + wallThickness,
      lengthZBottom = totalLengthZ - topLengthZ,
      lengthZTop = topLengthZ,
      wallThickness = wallThickness,
      radius = 5.mm,
    )
  }
}
