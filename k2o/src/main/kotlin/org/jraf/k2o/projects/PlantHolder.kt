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
import kotlinx.io.asSink
import kotlinx.io.buffered
import org.jraf.k2o.dsl.openScad
import org.jraf.k2o.stdlib.Cylinder
import org.jraf.k2o.stdlib.Polygon
import org.jraf.k2o.stdlib.difference
import org.jraf.k2o.stdlib.linearExtrude
import org.jraf.k2o.stdlib.mirror
import org.jraf.k2o.stdlib.translate

@Composable
private fun Half(
  width: Int,
  height: Int,
) {
  Polygon(
    width / 2 to (height - 10),
    0 to height,
    0 to 10,
    width / 2 to 0,
  )
}

@Composable
private fun PlantHolder() {
  val width = 20
  val height = 60
  val thickness = 2

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

    translate(0, 21, 0) {
      Cylinder(diameter = 15, height = thickness)
    }

    translate(0, 40, 0) {
      Cylinder(diameter = 15, height = thickness)
    }
  }
}

fun main() {
  openScad(System.out.asSink().buffered()) {
    PlantHolder()
  }
}
