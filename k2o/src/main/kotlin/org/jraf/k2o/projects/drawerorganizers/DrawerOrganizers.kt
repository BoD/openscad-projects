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

@file:Suppress("SameParameterValue", "unused")

package org.jraf.k2o.projects.drawerorganizers

import androidx.compose.runtime.Composable
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import org.jraf.k2o.dsl.openScad
import org.jraf.k2o.projects.TMP_FOLDER
import org.jraf.k2o.stdlib.Cube
import org.jraf.k2o.stdlib.Cylinder
import org.jraf.k2o.stdlib.difference
import org.jraf.k2o.stdlib.rotate
import org.jraf.k2o.stdlib.translate
import org.jraf.k2o.stdlib.union
import org.jraf.k2o.units.Angle.Companion.deg
import org.jraf.k2o.units.Length
import org.jraf.k2o.units.Length.Companion.mm
import org.jraf.k2o.units.Length.Companion.times

@Composable
private fun StandHorizontal(
  thickness: Length,
  length: Length,
  height: Length,
) {
  Cube(length, thickness, height)
}

@Composable
private fun StandVertical(
  thickness: Length,
  length: Length,
  height: Length,
) {
  Cube(thickness, length, height)
}

@Composable
private fun WallHorizontal(
  thickness: Length,
  length: Length,
  height: Length,
) {
  val holeDiameter = 12.mm
  val distanceBetweenHoles = 1.2.mm
  difference {
    Cube(length, thickness, height)
    val holeCountX = (length / (holeDiameter + distanceBetweenHoles)).toInt()
    val remainderX = length % (holeDiameter + distanceBetweenHoles)
    val holeCountZ = (height / (holeDiameter + distanceBetweenHoles)).toInt()
    val remainderZ = height % (holeDiameter + distanceBetweenHoles)
    for (z in 0 until holeCountZ) {
      val isEven = z % 2 == 0
      val maxX = if (isEven) holeCountX - 1 else holeCountX - 2
      for (x in 0..maxX) {
        val shiftX = if (isEven) Length.Zero else (holeDiameter + distanceBetweenHoles) / 2
        translate(
          x * (holeDiameter + distanceBetweenHoles) + holeDiameter / 2 + remainderX / 2 + distanceBetweenHoles / 2 + shiftX,
          thickness * 2 - thickness / 2,
          z * (holeDiameter + distanceBetweenHoles) + holeDiameter / 2 + remainderZ / 2 + distanceBetweenHoles / 2,
        ) {
          rotate(90.deg, 90.deg) {
            Cylinder(diameter = holeDiameter, height = thickness * 2, segments = 6)
          }
        }
      }
    }
  }
}

@Composable
private fun WallVertical(
  thickness: Length,
  length: Length,
  height: Length,
) {
  translate(thickness) {
    rotate(z = 90.deg) {
      WallHorizontal(
        thickness = thickness,
        length = length,
        height = height,
      )
    }
  }
}

@Composable
private fun LeftPart(
  standThickness: Length,
  standLength: Length,
  wallThickness: Length,
  wallHeight: Length,
) {
  // Horizontal wall
  difference {
    val horizontalWallLength = 187.mm
    union {
      translate(y = -standLength / 2 + wallThickness / 2) {
        StandVertical(
          thickness = standThickness,
          length = standLength,
          height = wallHeight,
        )
      }
      WallHorizontal(
        thickness = wallThickness,
        length = horizontalWallLength,
        height = wallHeight,
      )
      translate(horizontalWallLength - standThickness, -standLength / 2 + wallThickness / 2) {
        StandVertical(
          thickness = standThickness,
          length = standLength,
          height = wallHeight,
        )
      }
    }

    // Indent
    val indentWidth = 10.mm
    translate(horizontalWallLength - indentWidth, -standLength / 2 + wallThickness / 2) {
      Cube(indentWidth, standLength, 6.mm)
    }
  }

  // Vertical wall
  val verticalWallLength = 260.mm
  val verticalWallOffset = 160.mm
  translate(verticalWallOffset, -verticalWallLength + wallThickness) {
    WallVertical(
      thickness = wallThickness,
      length = verticalWallLength,
      height = wallHeight,
    )
  }
  translate(verticalWallOffset - standLength / 2 + wallThickness / 2, -verticalWallLength + wallThickness) {
    StandHorizontal(
      thickness = standThickness,
      length = standLength,
      height = wallHeight,
    )
  }

}

@Composable
private fun RightPart(
  standThickness: Length,
  standLength: Length,
  wallThickness: Length,
  wallHeight: Length,
) {
  val verticalWallOffset = 27.mm
  val horizontalWall1Length = 187.mm

  // Horizontal wall 1
  difference {
    union {
      translate(y = -standLength / 2 + wallThickness / 2) {
        StandVertical(
          thickness = standThickness,
          length = standLength,
          height = wallHeight,
        )
      }
      WallHorizontal(
        thickness = wallThickness,
        length = horizontalWall1Length,
        height = wallHeight,
      )
      translate(x = horizontalWall1Length - standThickness, y = -standLength / 2 + wallThickness / 2) {
        StandVertical(
          thickness = standThickness,
          length = standLength,
          height = wallHeight,
        )
      }
    }

    // Indent
    val indentWidth = 10.mm
    translate(y = -standLength / 2 + wallThickness / 2) {
      Cube(indentWidth, standLength, 6.mm)
    }
  }

  // Horizontal wall 2
  val horizontalWall2Offset = 90.mm
  val horizontalWall2Length = horizontalWall1Length - verticalWallOffset
  translate(verticalWallOffset, -horizontalWall2Offset) {
    WallHorizontal(
      thickness = wallThickness,
      length = horizontalWall2Length,
      height = wallHeight,
    )
    translate(x = horizontalWall2Length - standThickness, y = -standLength / 2 + wallThickness / 2) {
      StandVertical(
        thickness = standThickness,
        length = standLength,
        height = wallHeight,
      )
    }
  }

  // Vertical wall
  val verticalWallLength = 260.mm
  translate(verticalWallOffset, -verticalWallLength + wallThickness) {
    WallVertical(
      thickness = wallThickness,
      length = verticalWallLength,
      height = wallHeight,
    )
  }
  translate(x = verticalWallOffset - standLength / 2 + wallThickness / 2, y = -verticalWallLength + wallThickness) {
    StandHorizontal(
      thickness = standThickness,
      length = standLength,
      height = wallHeight,
    )
  }
}

@Composable
private fun DrawerOrganizers() {
  val standThickness = 2.mm
  val standLength = 10.mm

  val wallThickness = 4.mm
  val wallHeight = 70.mm

//    RightPart(
//      standThickness = standThickness,
//      standLength = standLength,
//      wallThickness = wallThickness,
//      wallHeight = wallHeight,
//    )

  LeftPart(
    standThickness = standThickness,
    standLength = standLength,
    wallThickness = wallThickness,
    wallHeight = wallHeight,
  )
}

fun main() {
  openScad(SystemFileSystem.sink(Path(TMP_FOLDER, "drawer-organizers.scad")).buffered()) {
    DrawerOrganizers()
  }
}
