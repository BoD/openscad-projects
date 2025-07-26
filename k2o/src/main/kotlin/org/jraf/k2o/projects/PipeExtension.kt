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

@file:Suppress("FunctionName")

package org.jraf.k2o.projects

import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import org.jraf.k2o.dsl.OpenScad
import org.jraf.k2o.dsl.writeOpenScad
import org.jraf.k2o.stdlib.cube
import org.jraf.k2o.stdlib.cylinder
import org.jraf.k2o.stdlib.difference
import org.jraf.k2o.stdlib.rotate
import org.jraf.k2o.stdlib.translate
import kotlin.math.sqrt

private fun OpenScad.PipeExtension(
  innerDiameter: Double,
  length: Double,
  thickness: Double,
) {
  difference {
    // Outer tube
    cylinder(height = length, radius = innerDiameter / 2 + thickness)

    // Inner tube
    cylinder(height = length, radius = innerDiameter / 2)

    // Slant
    val cubeSide = length
    translate(-cubeSide / sqrt(2.0) + innerDiameter / 2 + thickness, 0, cubeSide / sqrt(2.0) + (length - cubeSide / sqrt(2.0))) {
      rotate(0, 45, 0) {
        cube(cubeSide, center = true)
      }
    }

    // Slit
    val slitWidth = thickness * 5
    translate(-innerDiameter, -slitWidth / 2, 0) {
      cube(innerDiameter, slitWidth, length)
    }
  }
}

fun main() {
  writeOpenScad(SystemFileSystem.sink(Path("/Users/bod/gitrepo/openscad-projects/pipe-extension/tmp.scad")).buffered()) {
    val innerDiameter = 37.0
    val thickness = 2.0
    val length = 200.0

    PipeExtension(
      innerDiameter = innerDiameter,
      length = length,
      thickness = thickness,
    )
  }
}
