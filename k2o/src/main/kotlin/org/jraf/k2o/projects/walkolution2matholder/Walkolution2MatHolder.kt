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

package org.jraf.k2o.projects.walkolution2matholder

import androidx.compose.runtime.Composable
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import org.jraf.k2o.dsl.openScad
import org.jraf.k2o.projects.TMP_FOLDER
import org.jraf.k2o.shapes.RoundedExtrudedRoundedSquare
import org.jraf.k2o.stdlib.translate
import org.jraf.k2o.units.Length
import org.jraf.k2o.units.Length.Companion.mm

@Composable
private fun Walkolution2MatHolder() {
  val lengthZ = 8.5.mm
  val lengthX = 15.mm
  val lengthY = 120.mm
  val thickness = 6.mm

  horizontalPart(lengthX = lengthX, thickness = thickness, lengthY = lengthY)
  translate(z = lengthZ + thickness) {
    horizontalPart(lengthX = lengthX, thickness = thickness, lengthY = lengthY)
  }
  RoundedExtrudedRoundedSquare(
    x = thickness,
    y = lengthY,
    z = lengthZ + thickness * 2,
    roundingRadius = thickness / 2,
  )
}

@Composable
private fun horizontalPart(lengthX: Length, thickness: Length, lengthY: Length) {
  RoundedExtrudedRoundedSquare(
    x = lengthX + thickness,
    y = lengthY,
    z = thickness,
    topRightRadius = lengthX,
    bottomRightRadius = lengthX,
    roundingRadius = thickness / 2,
  )
}

fun main() {
  openScad(
    SystemFileSystem.sink(Path(TMP_FOLDER, "walkolution2-mat-holder.scad")).buffered(),
    fa = .25,
    fs = .25,
  ) {
    Walkolution2MatHolder()
  }
}
