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

package org.jraf.k2o.projects.glasseshooks

import androidx.compose.runtime.Composable
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import org.jraf.k2o.dsl.openScad
import org.jraf.k2o.math.cos
import org.jraf.k2o.projects.TMP_FOLDER
import org.jraf.k2o.stdlib.Cube
import org.jraf.k2o.stdlib.Cylinder
import org.jraf.k2o.stdlib.difference
import org.jraf.k2o.stdlib.hull
import org.jraf.k2o.stdlib.rotate
import org.jraf.k2o.stdlib.translate
import org.jraf.k2o.units.Angle
import org.jraf.k2o.units.Angle.Companion.deg
import org.jraf.k2o.units.Length
import org.jraf.k2o.units.Length.Companion.mm
import org.jraf.k2o.units.Length.Companion.times

@Composable
private fun Base(
  width: Length,
  height: Length,
  thickness: Length,
) {
  hull {
    translate(height / 2, height / 2) {
      Cylinder(height = thickness, radius = height / 2)
    }
    translate(width - height / 2, height / 2) {
      Cylinder(height = thickness, radius = height / 2)
    }
  }
}

@Composable
private fun Hook(
  radius: Length,
  height: Length,
  angle: Angle,
) {
  difference {
    // Height of the elliptical cross-section of a cylinder: 2r cos(a)
    translate(y = -2 * radius * cos(angle)) {
      rotate(x = -90.deg + angle) {
        translate(y = radius) {
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
  val baseWidth = 30.mm
  val baseHeight = 24.mm
  val baseThickness = 1.mm

  val hookRadius = 1.mm
  val hookHeight = 21.mm
  val hookAngle = 30.deg

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
  openScad(SystemFileSystem.sink(Path(TMP_FOLDER, "glasses_hooks.scad")).buffered()) {
    GlassesHooks()
  }
}
