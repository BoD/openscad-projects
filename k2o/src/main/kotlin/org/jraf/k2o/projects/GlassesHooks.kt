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

package org.jraf.k2o.projects

import androidx.compose.runtime.Composable
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import org.jraf.k2o.dsl.openScad
import org.jraf.k2o.math.cos
import org.jraf.k2o.stdlib.Cube
import org.jraf.k2o.stdlib.Cylinder
import org.jraf.k2o.stdlib.difference
import org.jraf.k2o.stdlib.hull
import org.jraf.k2o.stdlib.rotate
import org.jraf.k2o.stdlib.translate

@Composable
private fun Base(
  width: Int,
  height: Int,
  thickness: Int,
) {
  hull {
    translate(height / 2, height / 2, 0) {
      Cylinder(height = thickness, radius = height / 2)
    }
    translate(width - height / 2, height / 2, 0) {
      Cylinder(height = thickness, radius = height / 2)
    }
  }
}

@Composable
private fun Hook(
  radius: Int,
  height: Int,
  angle: Int,
) {
  difference {
    // Height of the elliptical cross-section of a cylinder: 2r cos(a)
    translate(0, -2 * radius * cos(angle), 0) {
      rotate(-90 + angle, 0, 0) {
        translate(0, radius, 0) {
          Cylinder(height = height, radius = radius)
        }
      }
    }
    translate(-radius, -height / 2, -radius * 2) {
      Cube(radius * 2, height, radius * 2)
    }
  }
}

@Composable
private fun GlassesHooks() {
  val baseWidth = 30
  val baseHeight = 24
  val baseThickness = 1

  val hookRadius = 1
  val hookHeight = 21
  val hookAngle = 30

  Base(
    width = baseWidth,
    height = baseHeight,
    thickness = baseThickness,
  )

  translate(baseWidth / 4, baseHeight / 4, baseThickness) {
    Hook(
      radius = hookRadius,
      height = hookHeight,
      angle = hookAngle,
    )
  }

  translate(baseWidth / 4 * 3, baseHeight / 4, baseThickness) {
    Hook(
      radius = hookRadius,
      height = hookHeight,
      angle = hookAngle,
    )
  }
}

fun main() {
  openScad(SystemFileSystem.sink(Path("/Users/bod/Tmp/tmp.scad")).buffered()) {
    GlassesHooks()
  }
}
