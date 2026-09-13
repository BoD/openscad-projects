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

@file:Suppress("WrapUnaryOperator")

package org.jraf.k2o.projects.freeboxcover

import androidx.compose.runtime.Composable
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import org.jraf.k2o.dsl.openScad
import org.jraf.k2o.math.atan
import org.jraf.k2o.math.cos
import org.jraf.k2o.math.sin
import org.jraf.k2o.projects.TMP_FOLDER
import org.jraf.k2o.stdlib.Call
import org.jraf.k2o.stdlib.Import
import org.jraf.k2o.stdlib.Sphere
import org.jraf.k2o.stdlib.Use
import org.jraf.k2o.stdlib.difference
import org.jraf.k2o.stdlib.hull
import org.jraf.k2o.stdlib.resize
import org.jraf.k2o.stdlib.rotate
import org.jraf.k2o.stdlib.translate
import org.jraf.k2o.units.Angle
import org.jraf.k2o.units.Angle.Companion.deg
import org.jraf.k2o.units.Length
import org.jraf.k2o.units.Length.Companion.mm
import org.jraf.k2o.units.Length.Companion.times

@Composable
private fun FrontPlateLine(
  thickness: Length,
  frontHeight: Length,
  frontBottomWidth: Length,
  frontTopWidth: Length,
  curveRadius: Length,
  curveAngle: Angle,
  steps: Int,
  stepWidth: Length,
  i: Int,
) {
  val roundBevelFraction = 0.2
  val cornerRadius = frontBottomWidth * roundBevelFraction
  val frontTopBottomRatio = frontTopWidth / frontBottomWidth

  // Bottom
  val angleBottom = (curveAngle / steps) * i - curveAngle / 2
  val z = if (i * stepWidth < cornerRadius) {
    (cornerRadius - Length.pythagoreanSide(hypotenuse = cornerRadius, side = cornerRadius - i * stepWidth))
  } else if (i * stepWidth > frontBottomWidth - cornerRadius) {
    (cornerRadius - Length.pythagoreanSide(hypotenuse = cornerRadius, side = cornerRadius - (steps - i) * stepWidth))
  } else {
    0.mm
  }

  translate(curveRadius * sin(angleBottom), curveRadius * cos(angleBottom), z + thickness / 2) {
    Sphere(thickness / 2)
  }

  // Top
  val angleTop = (curveAngle * frontTopBottomRatio / steps) * i - curveAngle * frontTopBottomRatio / 2
  translate(curveRadius * sin(angleTop), curveRadius * cos(angleTop), frontHeight - thickness / 2) {
    Sphere(thickness / 2)
  }
}

@Composable
private fun FrontPlate(
  thickness: Length,
  frontHeight: Length,
  frontBottomWidth: Length,
  frontTopWidth: Length,
  curveRadius: Length,
  curveAngle: Angle,
  steps: Int,
) {
  val stepWidth = frontBottomWidth / steps
  translate(y = curveRadius + thickness / 2) {
    rotate(z = 180.deg) {
      for (i in 0 until steps) {
        hull {
          FrontPlateLine(
            thickness = thickness,
            frontHeight = frontHeight,
            frontBottomWidth = frontBottomWidth,
            frontTopWidth = frontTopWidth,
            curveRadius = curveRadius,
            curveAngle = curveAngle,
            steps = steps,
            stepWidth = stepWidth,
            i = i,
          )
          FrontPlateLine(
            thickness = thickness,
            frontHeight = frontHeight,
            frontBottomWidth = frontBottomWidth,
            frontTopWidth = frontTopWidth,
            curveRadius = curveRadius,
            curveAngle = curveAngle,
            steps = steps,
            stepWidth = stepWidth,
            i = i + 1,
          )
        }
      }
    }
  }
}

@Composable
private fun Logo(height: Length, thickness: Length, curveRadius: Length) {
  translate(y = curveRadius + thickness, z = height / 2) {
    rotate(z = -90.deg) {
      Call("cylinder_extrude", "r_cyl" to curveRadius, "r_delta" to thickness * 2, "h" to height) {
        resize(y = height, auto = true) {
          Import("lurez-full.svg", center = true)
        }
      }
    }
  }
}

@Composable
private fun Front(
  thickness: Length,
  frontHeight: Length,
  frontBottomWidth: Length,
  frontTopWidth: Length,
  logoHeight: Length,
  logoThickness: Length,
  curveRadius: Length,
  curveAngle: Angle,
  steps: Int,
) {
  difference {
    FrontPlate(
      thickness = thickness,
      frontHeight = frontHeight,
      frontBottomWidth = frontBottomWidth,
      frontTopWidth = frontTopWidth,
      curveRadius = curveRadius,
      curveAngle = curveAngle,
      steps = steps,
    )
    translate(z = (frontHeight - logoHeight) / 2) {
      Logo(height = logoHeight, thickness = logoThickness, curveRadius = curveRadius)
    }
  }
}

@Composable
private fun Top(
  thickness: Length,
  frontHeight: Length,
  frontBottomWidth: Length,
  frontTopWidth: Length,
  topBackWidth: Length,
  topDepth: Length,
  curveRadius: Length,
  curveAngle: Angle,
  steps: Int,
) {
  val frontTopBottomRatio = frontTopWidth / frontBottomWidth
  hull {
    translate(y = curveRadius + thickness / 2) {
      rotate(z = 180.deg) {
        for (i in 0..steps) {
          val angleTop = (curveAngle * frontTopBottomRatio / steps) * i - curveAngle * frontTopBottomRatio / 2
          translate(curveRadius * sin(angleTop), curveRadius * cos(angleTop), frontHeight - thickness / 2) {
            Sphere(thickness / 2)
          }
        }
      }
    }

    translate(-topBackWidth / 2, topDepth, frontHeight - thickness / 2) {
      Sphere(thickness / 2)
    }

    translate(topBackWidth / 2, topDepth, frontHeight - thickness / 2) {
      Sphere(thickness / 2)
    }
  }
}

@Composable
private fun FreeboxCover() {
  Use("cylinder_extrude.scad")

  val fbxHeight = 67.mm

  val thickness = 5.mm

  // A bit shorter than the fbx, so it won't touch the floor.
  val frontHeight = fbxHeight - 5.mm
  val frontBottomWidth = 150.0.mm
  val frontTopWidth = 170.mm

  val topBackWidth = frontTopWidth - 10.mm
  val topDepth = 40.mm

  val logoHeight = 30.mm
  val logoThickness = 1.mm

  val curveRadius = 600.mm
  val curveAngle = atan((frontBottomWidth / 2) / Length.pythagoreanSide(hypotenuse = curveRadius, side = frontBottomWidth / 2)) * 2

  val steps = 100

  Front(
    thickness = thickness,
    frontHeight = frontHeight,
    frontBottomWidth = frontBottomWidth,
    frontTopWidth = frontTopWidth,
    logoHeight = logoHeight,
    logoThickness = logoThickness,
    curveRadius = curveRadius,
    curveAngle = curveAngle,
    steps = steps,
  )

  Top(
    thickness = thickness,
    frontHeight = frontHeight,
    frontBottomWidth = frontBottomWidth,
    frontTopWidth = frontTopWidth,
    topBackWidth = topBackWidth,
    topDepth = topDepth,
    curveRadius = curveRadius,
    curveAngle = curveAngle,
    steps = steps,
  )
}


fun main() {
  openScad(SystemFileSystem.sink(Path(TMP_FOLDER, "freebox_cover.scad")).buffered()) {
    FreeboxCover()
  }
}
