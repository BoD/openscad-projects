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

package org.jraf.k2o.projects

import androidx.compose.runtime.Composable
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import org.jraf.k2o.dsl.openScad
import org.jraf.k2o.stdlib.Color
import org.jraf.k2o.stdlib.Comment
import org.jraf.k2o.stdlib.Cube
import org.jraf.k2o.stdlib.color
import org.jraf.k2o.stdlib.difference
import org.jraf.k2o.stdlib.translate
import org.jraf.k2o.util.cm
import org.jraf.k2o.util.mm

@Composable
private fun Main() {
  val lengthX = 4.cm
  val lengthY = 5.cm
  val lengthZ = 6.cm


  val wallThickness = 2.mm
  val jointSize = wallThickness * 1.5

  Comment("Bottom part", false)
  BottomPart(
    lengthX = lengthX,
    lengthY = lengthY,
    lengthZ = lengthZ,
    wallThickness = wallThickness,
    jointSize = jointSize,
  )

  Comment("Top part")
  translate(x = lengthX + 1.cm) {
    TopPart(
      lengthX = lengthX,
      lengthY = lengthY,
      lengthZ = lengthZ,
      wallThickness = wallThickness,
      jointSize = jointSize,
    )
  }
}

@Composable
private fun BottomPart(
  lengthX: Double,
  lengthY: Double,
  lengthZ: Double,
  wallThickness: Double,
  jointSize: Double,
) {
  difference {
    Cube(
      x = lengthX,
      y = lengthY,
      z = lengthZ / 2,
    )
    translate(
      x = wallThickness,
      y = wallThickness,
      z = wallThickness,
    ) {
      Cube(
        x = lengthX - wallThickness * 2,
        y = lengthY - wallThickness * 2,
        z = lengthZ / 2,
      )
    }

    Comment("Joint")
    color(Color.Red) {
      translate(
        x = wallThickness / 2,
        y = wallThickness / 2,
        z = lengthZ / 2 - jointSize,
      ) {
        Cube(
          x = lengthX - wallThickness,
          y = lengthY - wallThickness,
          z = jointSize,
        )
      }
    }
  }
}

@Composable
private fun TopPart(
  lengthX: Double,
  lengthY: Double,
  lengthZ: Double,
  wallThickness: Double,
  jointSize: Double,
) {
  difference {
    Cube(
      x = lengthX,
      y = lengthY,
      z = lengthZ / 2,
    )
    translate(
      x = wallThickness,
      y = wallThickness,
      z = wallThickness,
    ) {
      Cube(
        x = lengthX - wallThickness * 2,
        y = lengthY - wallThickness * 2,
        z = lengthZ / 2,
      )
    }
  }

  Comment("Joint")
  color(Color.Red) {
    difference {
      translate(
        x = wallThickness / 2,
        y = wallThickness / 2,
        z = lengthZ / 2,
      ) {
        Cube(
          x = lengthX - wallThickness,
          y = lengthY - wallThickness,
          z = jointSize,
        )
      }

      translate(
        x = wallThickness,
        y = wallThickness,
        z = lengthZ / 2,
      ) {
        Cube(
          x = lengthX - wallThickness * 2,
          y = lengthY - wallThickness * 2,
          z = jointSize,
        )
      }
    }
  }
}


fun main() {
  openScad(
    SystemFileSystem.sink(Path("/Users/bod/Tmp/box.scad")).buffered(),
  ) {
    Main()
  }
}
