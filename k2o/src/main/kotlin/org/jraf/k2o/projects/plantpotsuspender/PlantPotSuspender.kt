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

@file:Suppress("SameParameterValue", "WrapUnaryOperator", "UnnecessaryVariable")

package org.jraf.k2o.projects.plantpotsuspender

import androidx.compose.runtime.Composable
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import org.jraf.k2o.dsl.openScad
import org.jraf.k2o.projects.TMP_FOLDER
import org.jraf.k2o.shapes.ExtrudedRoundedSquare
import org.jraf.k2o.shapes.HoneycombWall
import org.jraf.k2o.stdlib.Comment
import org.jraf.k2o.stdlib.Cube
import org.jraf.k2o.stdlib.Cylinder
import org.jraf.k2o.stdlib.Polygon
import org.jraf.k2o.stdlib.difference
import org.jraf.k2o.stdlib.linearExtrude
import org.jraf.k2o.stdlib.offset
import org.jraf.k2o.stdlib.rotate
import org.jraf.k2o.stdlib.translate
import org.jraf.k2o.units.Angle.Companion.deg
import org.jraf.k2o.units.Length
import org.jraf.k2o.units.Length.Companion.cm
import org.jraf.k2o.units.Length.Companion.mm
import org.jraf.k2o.units.Length.Companion.times

@Composable
private fun PlantPotSuspender() {
  val rampLengthX = 40.3.mm
  val rampLengthZ = 20.7.mm

  val thickness = 3.2.mm

  val hookThickness = thickness
  val hookInteriorShortLengthZ = rampLengthZ
  val hookInteriorLongLengthZ = 15.cm
  val hookInteriorLengthX = rampLengthX
  val hookLengthY = 1.cm

  val potThickness = thickness
  val potDiameterBottomInterior = 8.3.cm
  val potDiameterBottomExterior = potDiameterBottomInterior + 2 * potThickness
  val potDiameterTopInterior = 9.cm
  val potDiameterTopExterior = potDiameterTopInterior + 2 * potThickness
  val potHeight = 3.cm

  val holeDiameter = potDiameterTopExterior

  val backPadding = .8.cm

  val trayThickness = thickness
  val trayLengthX = potDiameterTopExterior / 2 + backPadding
  val trayLengthY = potDiameterTopExterior

  val reinforcementThickness = thickness
  val horizontalReinforcementLengthZ = 1.cm


  Comment("Tray", false)
  difference {
    HoneycombWall(
      x = trayLengthX,
      y = trayLengthY,
      z = trayThickness,
      diameter = 1.6.cm,
      spacing = thickness,
    )

    // Hole
    translate(
      x = holeDiameter / 2 + backPadding,
      y = holeDiameter / 2,
    ) {
      Cylinder(
        diameter = holeDiameter,
        height = trayThickness,
      )
    }
  }

  Comment("Pot")
  translate(
    x = potDiameterTopExterior / 2 + backPadding,
    y = potDiameterTopExterior / 2,
    z = -potHeight + potThickness,
  ) {
    Pot(
      potDiameterBottom = potDiameterBottomExterior,
      potDiameterTop = potDiameterTopExterior,
      potHeight = potHeight,
      potThickness = potThickness,
    )
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
private fun Pot(
  potDiameterBottom: Length,
  potDiameterTop: Length,
  potHeight: Length,
  potThickness: Length,
) {
  difference {
    Cylinder(
      diameter = potDiameterBottom,
      topDiameter = potDiameterTop,
      height = potHeight,
    )

    difference {
      Cylinder(
        diameter = potDiameterBottom - potThickness * 2,
        topDiameter = potDiameterTop - potThickness * 2,
        height = potHeight,
      )
      Cylinder(
        diameter = potDiameterBottom - potThickness * 2,
        height = potThickness,
      )
    }
  }
}

@Composable
private fun Reinforcement(
  reinforcementThickness: Length,
  trayLengthX: Length,
  hookInteriorLongLengthZ: Length,
) {
  translate(
    y = reinforcementThickness,
  ) {
    rotate(x = 90.deg) {
      linearExtrude(height = reinforcementThickness) {
        difference {
          Polygon(
            0.mm to 0.mm,
            trayLengthX to 0.mm,
            0.mm to hookInteriorLongLengthZ / 3,
          )
          offset(-reinforcementThickness) {
            Polygon(
              0.mm to 0.mm,
              trayLengthX to 0.mm,
              0.mm to hookInteriorLongLengthZ / 3,
            )
          }
        }
      }
    }
  }

  translate(x = reinforcementThickness) {
    Cube(
      x = reinforcementThickness,
      y = reinforcementThickness,
      z = hookInteriorLongLengthZ + reinforcementThickness,
    )
  }
}

@Composable
private fun Hook(
  hookThickness: Length,
  hookInteriorShortLengthZ: Length,
  hookInteriorLongLengthZ: Length,
  hookInteriorLengthX: Length,
  hookLengthY: Length,
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
    rotate(y = -90.deg) {
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
    SystemFileSystem.sink(Path(TMP_FOLDER, "plant-pot-suspender.scad")).buffered(),
    fa = .2,
    fs = .2,
  ) {
    PlantPotSuspender()
  }
}
