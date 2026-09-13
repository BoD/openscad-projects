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

package org.jraf.k2o.shapes

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
import kotlin.math.sqrt

/**
 * This diagram can help understand the placement of hexagons:
 * ```svg
 * <svg width="640" height="640" version="1.1" viewBox="0 0 640 640" xmlns="http://www.w3.org/2000/svg">
 *  <g>
 *   <g fill="none">
 *    <path transform="matrix(1.624 0 0 1.624 118.33 23.164)" d="m141.5 82.07v61.224l-53.021 30.612-53.021-30.612v-61.224l53.021-30.612z" stroke="#000" stroke-width=".61376"/>
 *    <path transform="matrix(1.624 0 0 1.624 357.74 23.164)" d="m141.5 82.07v61.224l-53.021 30.612-53.021-30.612v-61.224l53.021-30.612z" stroke="#000" stroke-width=".61376"/>
 *    <circle cx="501.43" cy="206.15" r="99.472" stroke="#f00" stroke-width="1.0568"/>
 *    <circle cx="262.02" cy="206.15" r="99.472" stroke="#f00" stroke-width="1.0568"/>
 *    <path transform="matrix(1.624 0 0 1.624 -5.1184 240.86)" d="m141.5 82.07v61.224l-53.021 30.612-53.021-30.612v-61.224l53.021-30.612z" stroke="#000" stroke-width=".61376"/>
 *    <path transform="matrix(1.624 0 0 1.624 234.29 240.86)" d="m141.5 82.07v61.224l-53.021 30.612-53.021-30.612v-61.224l53.021-30.612z" stroke="#000" stroke-width=".61376"/>
 *    <circle cx="377.98" cy="423.85" r="99.472" stroke="#f00" stroke-width="1.0568"/>
 *    <circle cx="138.57" cy="423.85" r="99.472" stroke="#f00" stroke-width="1.0568"/>
 *   </g>
 *   <g>
 *    <path d="m348.15 202.1h67.544" fill="#00f" stroke="#00f"/>
 *    <text x="381.9978" y="185.51425" fill="#0000ff" font-size="10.667px" stroke="#0000ff" text-anchor="middle" xml:space="preserve"><tspan x="381.9978" y="185.51425" font-family="Helvetica" font-size="10.667px" font-weight="300">Spacing</tspan></text>
 *    <text x="255.71254" y="349.54388" fill="#ff00ff" font-size="10.667px" stroke="#0000ff" text-anchor="middle" xml:space="preserve"><tspan x="255.71254" y="349.54388" fill="#ff00ff" font-family="Helvetica" font-size="10.667px" font-weight="300" stroke="#ff00ff">r/2</tspan></text>
 *    <path d="m246.04 304.58v67.544" fill="#00f" stroke="#00f"/>
 *   </g>
 *   <path d="m242.94 322.58 0.43788 49.498" fill="#ff0" stroke="#f0f" stroke-width=".49639"/>
 *   <path d="m240.02 322.51v200.72" fill="#ff0" stroke="#f60"/>
 *  </g>
 * </svg>
 * ```
 */
@Composable
fun HoneycombWall(
  x: Length,
  y: Length,
  z: Length,
  diameter: Length,
  spacing: Length,
) {
  val radius = diameter / 2.0
  // Amount to offset hexagons vertically so they fit adjacently together
  val hexagonYOffset = radius / 2.0
  val apothem = (sqrt(3.0) / 2.0) * radius
  val hexagonWidth = apothem * 2.0

  val columnCount = (x / (hexagonWidth + spacing)).toInt() + 1
  // We always want an odd number of rows so that the hexagons on the top and bottom are aligned
  val rowCount = (y / (diameter - hexagonYOffset + spacing)).toInt()
    .let { if (it % 2 == 0) it + 1 else it + 2 }

  val exceedingX = (columnCount * (hexagonWidth + spacing) - spacing - x) / 2.0
  val exceedingY = (rowCount * (diameter - hexagonYOffset + spacing) - spacing + hexagonYOffset - y) / 2.0

  union {
    difference {
      Cube(
        x = x,
        y = y,
        z = z,
      )

      repeat(rowCount) { row ->
        repeat(columnCount + if (row % 2 == 0) 0 else 1) { col ->
          translate(
            x = col * (hexagonWidth + spacing) +
              (if (row % 2 == 0) Length.Zero else -apothem - spacing / 2.0)
              - exceedingX,
            y = row * (diameter - hexagonYOffset + spacing)
              - exceedingY,
          ) {
            translate(
              x = apothem,
              y = radius,
            ) {
              rotate(90.deg) {
                Cylinder(
                  height = z,
                  diameter = diameter,
                  segments = 6,
                )
              }
            }
          }
        }
      }
    }

    // Left edge
    Cube(
      x = spacing,
      y = y,
      z = z,
    )

    // Right edge
    translate(
      x = x - spacing,
    ) {
      Cube(
        x = spacing,
        y = y,
        z = z,
      )
    }

    // Top edge
    translate(
      y = y - spacing,
    ) {
      Cube(
        x = x,
        y = spacing,
        z = z,
      )
    }

    // Bottom edge
    Cube(
      x = x,
      y = spacing,
      z = z,
    )
  }
}

fun main() {
  openScad(SystemFileSystem.sink(Path(TMP_FOLDER, "honeycomb-wall.scad")).buffered()) {
    HoneycombWall(
      x = 320.mm,
      y = 200.mm,
      z = 10.mm,
      diameter = 45.mm,
      spacing = 5.mm,
    )


    translate(
      y = -280.mm,
    ) {
      HoneycombWall(
        x = 320.mm,
        y = 200.mm,
        z = 10.mm,
        diameter = 44.mm,
        spacing = 3.mm,
      )
    }

    translate(
      x = 420.mm,
    ) {
      HoneycombWall(
        x = 320.mm,
        y = 200.mm,
        z = 10.mm,
        diameter = 38.mm,
        spacing = 3.mm,
      )
    }

  }
}
