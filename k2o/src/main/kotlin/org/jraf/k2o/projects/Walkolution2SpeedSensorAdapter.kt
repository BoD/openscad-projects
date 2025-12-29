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
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import org.jraf.k2o.dsl.openScad
import org.jraf.k2o.shapes.BoDLogo
import org.jraf.k2o.stdlib.Cylinder
import org.jraf.k2o.stdlib.difference
import org.jraf.k2o.stdlib.translate

@Composable
private fun Main() {
  val boltDiameter = 15.8
  val boldLength = 9.9
  val adapterDiameter = boltDiameter + 2.0
  val adapterLength = 50.0
  val hexagonFlatToFlat = 8.0
  val hexagonDepth = 7.5

  difference {
    Cylinder(height = adapterLength, diameter = adapterDiameter)
    Cylinder(height = boldLength, diameter = boltDiameter)

    // BoD Logo
    translate(z = adapterLength - 1) {
      BoDLogo(width = adapterDiameter * .8, thickness = 1)
    }
  }
  translate(z = boldLength - hexagonDepth) {
    Cylinder(height = hexagonDepth, diameter = hexagonFlatToFlat, segments = 6)
  }
}

fun main() {
  openScad(
    SystemFileSystem.sink(Path("/Users/bod/Tmp/walkolution2-speed-sensor-adapter.scad")).buffered(),
    fa = .05,
    fs = .05,
  ) {
    Main()
  }
}
