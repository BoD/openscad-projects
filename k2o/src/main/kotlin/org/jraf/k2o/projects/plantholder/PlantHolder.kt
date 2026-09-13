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

package org.jraf.k2o.projects.plantholder

import androidx.compose.runtime.Composable
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import org.jraf.k2o.dsl.openScad
import org.jraf.k2o.projects.TMP_FOLDER
import org.jraf.k2o.stdlib.Cylinder
import org.jraf.k2o.stdlib.Polygon
import org.jraf.k2o.stdlib.difference
import org.jraf.k2o.stdlib.linearExtrude
import org.jraf.k2o.stdlib.mirror
import org.jraf.k2o.stdlib.translate
import org.jraf.k2o.units.Length
import org.jraf.k2o.units.Length.Companion.mm

@Composable
private fun Half(
  width: Length,
  height: Length,
) {
  Polygon(
    width / 2 to (height - 10.mm),
    0.mm to height,
    0.mm to 10.mm,
    width / 2 to 0.mm,
  )
}

@Composable
private fun PlantHolder() {
  val width = 20.mm
  val height = 60.mm
  val thickness = 2.mm

  difference {
    linearExtrude(thickness) {
      Half(
        width = width,
        height = height,
      )
      mirror(1, 0, 0) {
        Half(
          width = width,
          height = height,
        )
      }
    }

    translate(y = 21.mm) {
      Cylinder(diameter = 15.mm, height = thickness)
    }

    translate(y = 40.mm) {
      Cylinder(diameter = 15.mm, height = thickness)
    }
  }
}

fun main() {
  openScad(SystemFileSystem.sink(Path(TMP_FOLDER, "plant-holder.scad")).buffered()) {
    PlantHolder()
  }
}
