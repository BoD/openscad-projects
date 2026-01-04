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

package org.jraf.k2o.projects

import androidx.compose.runtime.Composable
import kotlinx.io.asSink
import kotlinx.io.buffered
import org.jraf.k2o.dsl.openScad
import org.jraf.k2o.stdlib.Cube
import org.jraf.k2o.stdlib.Cylinder
import org.jraf.k2o.stdlib.difference
import org.jraf.k2o.stdlib.rotate
import org.jraf.k2o.stdlib.translate
import org.jraf.k2o.stdlib.union

@Composable
private fun StandHorizontal(
  thickness: Int,
  length: Int,
  height: Int,
) {
  Cube(length, thickness, height)
}

@Composable
private fun StandVertical(
  thickness: Int,
  length: Int,
  height: Int,
) {
  Cube(thickness, length, height)
}

@Composable
private fun WallHorizontal(
  thickness: Int,
  length: Int,
  height: Int,
) {
  val holeDiameter = 12
  val distanceBetweenHoles = 1.2
  difference {
    Cube(length, thickness, height)
    val holeCountX = length / (holeDiameter + distanceBetweenHoles)
    val remainderX = length % (holeDiameter + distanceBetweenHoles)
    val holeCountZ = height / (holeDiameter + distanceBetweenHoles)
    val remainderZ = height % (holeDiameter + distanceBetweenHoles)
    for (z in 0 until holeCountZ.toInt()) {
      val isEven = z % 2 == 0
      val maxX = if (isEven) holeCountX - 1 else holeCountX - 2
      for (x in 0..maxX.toInt()) {
        val shiftX = if (isEven) 0.0 else (holeDiameter + distanceBetweenHoles) / 2
        translate(
          x * (holeDiameter + distanceBetweenHoles) + holeDiameter / 2 + remainderX / 2 + distanceBetweenHoles / 2 + shiftX,
          thickness * 2 - thickness / 2,
          z * (holeDiameter + distanceBetweenHoles) + holeDiameter / 2 + remainderZ / 2 + distanceBetweenHoles / 2,
        ) {
          rotate(90, 90, 0) {
            Cylinder(diameter = holeDiameter, height = thickness * 2, segments = 6)
          }
        }
      }
    }
  }
}

@Composable
private fun WallVertical(
  thickness: Int,
  length: Int,
  height: Int,
) {
  translate(thickness, 0, 0) {
    rotate(0, 0, 90) {
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
  standThickness: Int,
  standLength: Int,
  wallThickness: Int,
  wallHeight: Int,
) {
  // Horizontal wall
  difference {
    val horizontalWallLength = 187
    union {
      translate(0, -standLength / 2 + wallThickness / 2, 0) {
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
      translate(horizontalWallLength - standThickness, -standLength / 2 + wallThickness / 2, 0) {
        StandVertical(
          thickness = standThickness,
          length = standLength,
          height = wallHeight,
        )
      }
    }

    // Indent
    val indentWidth = 10
    translate(horizontalWallLength - indentWidth, -standLength / 2 + wallThickness / 2, 0) {
      Cube(indentWidth, standLength, 6)
    }
  }

  // Vertical wall
  val verticalWallLength = 260
  val verticalWallOffset = 160
  translate(verticalWallOffset, -verticalWallLength + wallThickness, 0) {
    WallVertical(
      thickness = wallThickness,
      length = verticalWallLength,
      height = wallHeight,
    )
  }
  translate(verticalWallOffset - standLength / 2 + wallThickness / 2, -verticalWallLength + wallThickness, 0) {
    StandHorizontal(
      thickness = standThickness,
      length = standLength,
      height = wallHeight,
    )
  }

}

@Composable
private fun RightPart(
  standThickness: Int,
  standLength: Int,
  wallThickness: Int,
  wallHeight: Int,
) {
  val verticalWallOffset = 27
  val horizontalWall1Length = 187

  // Horizontal wall 1
  difference {
    union {
      translate(0, -standLength / 2 + wallThickness / 2, 0) {
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
      translate(horizontalWall1Length - standThickness, -standLength / 2 + wallThickness / 2, 0) {
        StandVertical(
          thickness = standThickness,
          length = standLength,
          height = wallHeight,
        )
      }
    }

    // Indent
    val indentWidth = 10
    translate(0, -standLength / 2 + wallThickness / 2, 0) {
      Cube(indentWidth, standLength, 6)
    }
  }

  // Horizontal wall 2
  val horizontalWall2Offset = 90
  val horizontalWall2Length = horizontalWall1Length - verticalWallOffset
  translate(verticalWallOffset, -horizontalWall2Offset, 0) {
    WallHorizontal(
      thickness = wallThickness,
      length = horizontalWall2Length,
      height = wallHeight,
    )
    translate(horizontalWall2Length - standThickness, -standLength / 2 + wallThickness / 2, 0) {
      StandVertical(
        thickness = standThickness,
        length = standLength,
        height = wallHeight,
      )
    }
  }

  // Vertical wall
  val verticalWallLength = 260
  translate(verticalWallOffset, -verticalWallLength + wallThickness, 0) {
    WallVertical(
      thickness = wallThickness,
      length = verticalWallLength,
      height = wallHeight,
    )
  }
  translate(verticalWallOffset - standLength / 2 + wallThickness / 2, -verticalWallLength + wallThickness, 0) {
    StandHorizontal(
      thickness = standThickness,
      length = standLength,
      height = wallHeight,
    )
  }
}

@Composable
private fun DrawerOrganizers() {
  val standThickness = 2
  val standLength = 10

  val wallThickness = 4
  val wallHeight = 70

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
  openScad(System.out.asSink().buffered()) {
    DrawerOrganizers()
  }
}
