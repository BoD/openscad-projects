/*
 * This source is part of the
 *      _____  ___   ____
 *  __ / / _ \/ _ | / __/___  _______ _
 * / // / , _/ __ |/ _/_/ _ \/ __/ _ `/
 * \___/_/|_/_/ |_/_/ (_)___/_/  \_, /
 *                              /___/
 * repository.
 *
 * Copyright (C) 2026-present Benoit 'BoD' Lubek (BoD@JRAF.org)
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

package org.jraf.k2o.projects.plantpotsuspender

import androidx.compose.runtime.Composable
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import org.jraf.k2o.dsl.openScad
import org.jraf.k2o.shapes.ExtrudedRoundedSquare
import org.jraf.k2o.shapes.HoneycombWall
import org.jraf.k2o.stdlib.Comment
import org.jraf.k2o.stdlib.Cube
import org.jraf.k2o.stdlib.Cylinder
import org.jraf.k2o.stdlib.Polygon
import org.jraf.k2o.stdlib.difference
import org.jraf.k2o.stdlib.linearExtrude
import org.jraf.k2o.stdlib.rotate
import org.jraf.k2o.stdlib.translate
import org.jraf.k2o.util.cm
import org.jraf.k2o.util.mm

@Composable
private fun Main() {
  val rampLengthX = 40.3.mm
  val rampLengthZ = 20.7.mm

  val hookThickness = 3.2.mm
  val hookInteriorShortLengthZ = rampLengthZ
  val hookInteriorLongLengthZ = 15.cm
  val hookInteriorLengthX = rampLengthX
  val hookLengthY = 1.cm

  val trayThickness = 3.2.mm
  val trayLengthX = 15.cm
  val trayLengthY = 15.cm

  val reinforcementThickness = hookThickness
  val horizontalReinforcementLengthZ = 1.cm

  val holeDiameter = 14.cm
  val holeBorder = 4.mm

  Comment("Tray", false)
  difference {
    HoneycombWall(
      x = trayLengthX,
      y = trayLengthY,
      z = trayThickness,
      diameter = 1.4.cm,
      spacing = 4.mm,
    )

    // Hole
    translate(
      trayLengthX / 2,
      trayLengthY / 2,
    ) {
      Cylinder(
        diameter = holeDiameter,
        height = trayThickness,
      )
    }
  }

  // Hole border
  translate(
    trayLengthX / 2,
    trayLengthY / 2,
  ) {
    difference {
      Cylinder(
        diameter = holeDiameter + holeBorder * 2,
        height = trayThickness,
      )
      Cylinder(
        diameter = holeDiameter,
        height = trayThickness,
      )
    }
  }

  Comment("Left hook")
  translate(
    x = -hookInteriorLengthX - hookThickness,
    z = trayThickness,
  ) {
    Hook(
      hookThickness = hookThickness,
      hookInteriorShortLengthZ = hookInteriorShortLengthZ,
      hookInteriorLongLengthZ = hookInteriorLongLengthZ,
      hookInteriorLengthX = hookInteriorLengthX,
      hookLengthY = hookLengthY,
    )
  }

  Comment("Right hook")
  translate(
    x = -hookInteriorLengthX - hookThickness,
    y = trayLengthY - hookLengthY,
    z = trayThickness,
  ) {
    Hook(
      hookThickness = hookThickness,
      hookInteriorShortLengthZ = hookInteriorShortLengthZ,
      hookInteriorLongLengthZ = hookInteriorLongLengthZ,
      hookInteriorLengthX = hookInteriorLengthX,
      hookLengthY = hookLengthY,
    )
  }

  Comment("Left reinforcement")
  translate(z = trayThickness) {
    Reinforcement(
      reinforcementThickness = reinforcementThickness,
      trayLengthX = trayLengthX,
      hookInteriorLongLengthZ = hookInteriorLongLengthZ,
    )
  }

  Comment("Right reinforcement")
  translate(
    y = trayLengthY - reinforcementThickness,
    z = trayThickness,
  ) {
    Reinforcement(
      reinforcementThickness = reinforcementThickness,
      trayLengthX = trayLengthX,
      hookInteriorLongLengthZ = hookInteriorLongLengthZ,
    )
  }

  Comment("Horizontal reinforcement")
  Cube(
    x = reinforcementThickness,
    y = trayLengthY,
    z = horizontalReinforcementLengthZ,
  )

}

@Composable
private fun Reinforcement(
  reinforcementThickness: Double,
  trayLengthX: Double,
  hookInteriorLongLengthZ: Double,
) {
  translate(
    y = reinforcementThickness,
  ) {
    rotate(x = 90) {
      linearExtrude(height = reinforcementThickness) {
        Polygon(
          0 to 0,
          2 * (trayLengthX / 3) to 0,
          0 to hookInteriorLongLengthZ / 3,
        )
      }
    }
  }
}

@Composable
private fun Hook(
  hookThickness: Double,
  hookInteriorShortLengthZ: Double,
  hookInteriorLongLengthZ: Double,
  hookInteriorLengthX: Double,
  hookLengthY: Double,
) {
  // Top part
  translate(
    z = hookInteriorLongLengthZ,
  ) {
    Cube(
      x = hookInteriorLengthX + hookThickness * 2,
      y = hookLengthY,
      z = hookThickness,
    )
  }

  // Short part
  translate(
    x = hookThickness,
    z = hookInteriorLongLengthZ - hookInteriorShortLengthZ,
  ) {
    rotate(y = -90) {
      ExtrudedRoundedSquare(
        x = hookInteriorShortLengthZ,
        y = hookLengthY,
        z = hookThickness,
        topLeftRadius = hookLengthY / 2,
        bottomLeftRadius = hookLengthY / 2,
      )
    }
  }

  // Long part
  translate(
    x = hookInteriorLengthX + hookThickness,
  ) {
    Cube(
      x = hookThickness,
      y = hookLengthY,
      z = hookInteriorLongLengthZ,
    )
  }
}

fun main() {
  openScad(
    SystemFileSystem.sink(Path("/Users/bod/Tmp/plant-pot-suspender.scad")).buffered(),
    fa = .2,
    fs = .2,
  ) {
    Main()
  }
}
