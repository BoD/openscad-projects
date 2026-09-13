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

package org.jraf.k2o.projects.pipeextension

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
import org.jraf.k2o.units.Angle.Companion.deg
import org.jraf.k2o.units.Length
import org.jraf.k2o.units.Length.Companion.cm
import org.jraf.k2o.units.Length.Companion.mm
import kotlin.math.sqrt

@Composable
private fun PipeExtension(
  innerDiameter: Length,
  length: Length,
  thickness: Length,
) {
  difference {
    // Outer tube
    Cylinder(height = length, radius = innerDiameter / 2 + thickness)

    // Inner tube
    Cylinder(height = length, radius = innerDiameter / 2)

    // Slant
    val cubeSide = length
    translate(x = -cubeSide / sqrt(2.0) + innerDiameter / 2 + thickness, z = cubeSide / sqrt(2.0) + (length - cubeSide / sqrt(2.0))) {
      rotate(y = 45.deg) {
        Cube(cubeSide, center = true)
      }
    }

    // Slit
    val slitWidth = thickness * 5
    translate(-innerDiameter, -slitWidth / 2) {
      Cube(innerDiameter, slitWidth, length)
    }
  }
}

fun main() {
  openScad(SystemFileSystem.sink(Path(TMP_FOLDER, "pipe-extension.scad")).buffered()) {
    val innerDiameter = 37.mm
    val thickness = 2.mm
    val length = 20.cm

    PipeExtension(
      innerDiameter = innerDiameter,
      length = length,
      thickness = thickness,
    )
  }
}
