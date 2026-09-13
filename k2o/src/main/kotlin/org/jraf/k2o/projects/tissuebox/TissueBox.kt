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

package org.jraf.k2o.projects.tissuebox

import androidx.compose.runtime.Composable
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import org.jraf.k2o.dsl.openScad
import org.jraf.k2o.math.cos
import org.jraf.k2o.projects.TMP_FOLDER
import org.jraf.k2o.projects.tissuebox.BoxSize.Big
import org.jraf.k2o.projects.tissuebox.BoxSize.Small
import org.jraf.k2o.shapes.ExtrudedRoundedSquare
import org.jraf.k2o.shapes.HoneycombWall
import org.jraf.k2o.shapes.LzLogo
import org.jraf.k2o.stdlib.Cube
import org.jraf.k2o.stdlib.Cylinder
import org.jraf.k2o.stdlib.color
import org.jraf.k2o.stdlib.difference
import org.jraf.k2o.stdlib.resize
import org.jraf.k2o.stdlib.translate
import org.jraf.k2o.stdlib.union
import org.jraf.k2o.units.Angle.Companion.deg
import org.jraf.k2o.units.Length
import org.jraf.k2o.units.Length.Companion.cm
import org.jraf.k2o.units.Length.Companion.mm

private enum class BoxSize {
  Small, Big,
}

@Composable
private fun TissueBox(boxSize: BoxSize) {
  val boxSizeX = 22.7.cm
  val boxSizeY = 11.4.cm

  val boxSizeZ = when (boxSize) {
    Small -> 5.8.cm
    Big -> 8.2.cm
  }


  val wallThickness = 4.mm

  val slideIndentSize = wallThickness / 2

  val exteriorRoundingRadius = 6.mm
  val interiorRoundingRadius = exteriorRoundingRadius - wallThickness
  val slidingBottomRadius = interiorRoundingRadius + slideIndentSize

  val marginDueToRoundingRadius = interiorRoundingRadius - interiorRoundingRadius * cos(45.deg)
  val marginForSlidingBottom = 1.mm

  val holeSizeX = 14.cm
  val holeSizeY = 4.cm

  val exteriorSizeX = boxSizeX + marginDueToRoundingRadius * 2 + wallThickness * 2
  val exteriorSizeY = boxSizeY + marginDueToRoundingRadius * 2 + wallThickness * 2
  val exteriorSizeZ = boxSizeZ + wallThickness * 2 + marginForSlidingBottom * 2


//  // Fake tissue box
//  translate(
//    x = marginDueToRoundingRadius + wallThickness,
//    y = marginDueToRoundingRadius + wallThickness,
//    z = marginForSlidingBottom * 2 + wallThickness,
//  ) {
//    color("red") {
//      Cube(
//        x = boxSizeX,
//        y = boxSizeY,
//        z = boxSizeZ,
//      )
//    }
//  }

  TissueBox(
    exteriorSizeX = exteriorSizeX,
    wallThickness = wallThickness,
    exteriorSizeY = exteriorSizeY,
    exteriorSizeZ = exteriorSizeZ,
    exteriorRoundingRadius = exteriorRoundingRadius,
    interiorRoundingRadius = interiorRoundingRadius,
    slideIndentSize = slideIndentSize,
    marginForSlidingBottom = marginForSlidingBottom,
    slidingBottomRadius = slidingBottomRadius,
    holeSizeX = holeSizeX,
    holeSizeY = holeSizeY,
  )


  // Sliding bottom
  SlidingBottomPart(
    wallThickness = wallThickness,
    slideIndentSize = slideIndentSize,
    exteriorRoundingRadius = exteriorRoundingRadius,
    marginForSlidingBottom = marginForSlidingBottom,
    boxSizeX = boxSizeX,
    marginDueToRoundingRadius = marginDueToRoundingRadius,
    boxSizeY = boxSizeY,
    slidingBottomRadius = slidingBottomRadius,
  )
}

@Composable
private fun TissueBox(
  exteriorSizeX: Length,
  wallThickness: Length,
  exteriorSizeY: Length,
  exteriorSizeZ: Length,
  exteriorRoundingRadius: Length,
  interiorRoundingRadius: Length,
  slideIndentSize: Length,
  marginForSlidingBottom: Length,
  slidingBottomRadius: Length,
  holeSizeX: Length,
  holeSizeY: Length,
) {
  val interiorSizeX = exteriorSizeX - wallThickness * 2
  val interiorSizeY = exteriorSizeY - wallThickness * 2
  val interiorSizeZ = exteriorSizeZ - wallThickness
  difference {
    // Exterior
    ExtrudedRoundedSquare(
      x = exteriorSizeX,
      y = exteriorSizeY,
      z = exteriorSizeZ,
      radius = exteriorRoundingRadius,
    )

    // Interior
    translate(
      x = wallThickness,
      y = wallThickness,
    ) {
      ExtrudedRoundedSquare(
        x = interiorSizeX,
        y = interiorSizeY,
        z = interiorSizeZ,
        radius = interiorRoundingRadius,
      )
    }

    // Slide indent
    translate(
      x = wallThickness - slideIndentSize,
      y = wallThickness - slideIndentSize,
      z = marginForSlidingBottom,
    ) {
      ExtrudedRoundedSquare(
        x = interiorSizeX + slideIndentSize * 2,
        y = interiorSizeY + slideIndentSize * 3,
        z = wallThickness,
        radius = slidingBottomRadius,
      )
    }

    // Cut hanging bit
    translate(
      y = exteriorSizeY - exteriorRoundingRadius,
    ) {
      Cube(
        x = exteriorSizeX,
        y = wallThickness * 4,
        z = wallThickness + marginForSlidingBottom,
      )
    }

    // Hole
    translate(
      x = (exteriorSizeX - holeSizeX) / 2,
      y = (exteriorSizeY - holeSizeY) / 2,
      z = exteriorSizeZ - wallThickness * 2,
    ) {
      Hole(x = holeSizeX, y = holeSizeY, z = wallThickness * 2)
    }

    // Lz logo
    val logoSizeX = 3.cm
    val logoMargin = 6.mm
    val logoSizeY = logoSizeX * 3 / 4
    translate(
      x = exteriorSizeX - logoMargin - logoSizeX / 2,
      y = logoMargin + logoSizeY / 2,
      z = exteriorSizeZ - wallThickness / 2,
    ) {
      LzLogo(width = logoSizeX, thickness = wallThickness)
    }
  }
}

@Composable
private fun SlidingBottomPart(
  wallThickness: Length,
  slideIndentSize: Length,
  exteriorRoundingRadius: Length,
  marginForSlidingBottom: Length,
  boxSizeX: Length,
  marginDueToRoundingRadius: Length,
  boxSizeY: Length,
  slidingBottomRadius: Length,
) {
  val someMargin = 2.mm
  val tolerance = 0.4.mm
  val bottomPartThickness = wallThickness - tolerance
  translate(
    x = slideIndentSize + 30.cm,
    y = slideIndentSize,
    z = exteriorRoundingRadius + marginForSlidingBottom,
  ) {
    union {
      color("blue") {
        difference {
          ExtrudedRoundedSquare(
            x = boxSizeX + marginDueToRoundingRadius * 2 + slideIndentSize * 2 - tolerance * 2,
            y = boxSizeY + marginDueToRoundingRadius * 2 + slideIndentSize * 3 - tolerance * 2,
            z = bottomPartThickness,
            radius = slidingBottomRadius,
          )
          translate(
            x = someMargin,
            y = someMargin,
          ) {
            Cube(
              x = boxSizeX + marginDueToRoundingRadius * 2 + slideIndentSize * 2 - someMargin * 2 - tolerance * 2,
              y = boxSizeY + marginDueToRoundingRadius * 2 + slideIndentSize * 3 - someMargin * 2 - tolerance * 2,
              z = bottomPartThickness,
            )
          }
        }
      }

      color("cyan") {
        translate(
          x = someMargin,
          y = someMargin,
        ) {
          HoneycombWall(
            x = boxSizeX + marginDueToRoundingRadius * 2 + slideIndentSize * 2 - someMargin * 2 - tolerance * 2,
            y = boxSizeY + marginDueToRoundingRadius * 2 + slideIndentSize * 3 - someMargin * 2 - tolerance * 2,
            z = bottomPartThickness,
            diameter = 2.5.cm,
            spacing = 1.mm,
          )
        }
      }
    }
  }
}

@Composable
private fun Hole(x: Length, y: Length, z: Length) {
  translate(x = x / 2, y = y / 2) {
    resize(x = x, auto = false) {
      Cylinder(height = z, diameter = y)
    }
  }
}

fun main() {
  openScad(
    SystemFileSystem.sink(Path(TMP_FOLDER, "tissue-box.scad")).buffered(),
  ) {
    TissueBox(Small)
  }
}
