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
import org.jraf.k2o.shapes.RoundedExtrudedRoundedSquare
import org.jraf.k2o.stdlib.translate

@Composable
private fun Main() {
  val lengthZ = 8.5
  val lengthX = 15.0
  val lengthY = 120.0
  val thickness = 6.0

  horizontalPart(lengthX = lengthX, thickness = thickness, lengthY = lengthY)
  translate(z = lengthZ + thickness) {
    horizontalPart(lengthX = lengthX, thickness = thickness, lengthY = lengthY)
  }
  RoundedExtrudedRoundedSquare(
    x = thickness,
    y = lengthY,
    z = lengthZ + thickness * 2,
    topLeftRadius = 0,
    topRightRadius = 0,
    bottomRightRadius = 0,
    bottomLeftRadius = 0,
    roundingRadius = thickness / 2,
  )
}

@Composable
private fun horizontalPart(lengthX: Double, thickness: Double, lengthY: Double) {
  RoundedExtrudedRoundedSquare(
    x = lengthX + thickness,
    y = lengthY,
    z = thickness,
    topLeftRadius = 0,
    topRightRadius = lengthX,
    bottomRightRadius = lengthX,
    bottomLeftRadius = 0,
    roundingRadius = thickness / 2,
  )
}

fun main() {
  openScad(
    SystemFileSystem.sink(Path("/Users/bod/Tmp/walkolution2-mat-holder.scad")).buffered(),
    fa = .25,
    fs = .25,
  ) {
    Main()
  }
}
