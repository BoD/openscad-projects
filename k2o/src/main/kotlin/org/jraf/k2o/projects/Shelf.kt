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
import org.jraf.k2o.math.cos
import org.jraf.k2o.shapes.ExtrudedRoundedSquare
import org.jraf.k2o.shapes.LzLogo
import org.jraf.k2o.shapes.RoundedSquare
import org.jraf.k2o.stdlib.Cube
import org.jraf.k2o.stdlib.Cylinder
import org.jraf.k2o.stdlib.Square
import org.jraf.k2o.stdlib.difference
import org.jraf.k2o.stdlib.linearExtrude
import org.jraf.k2o.stdlib.offset
import org.jraf.k2o.stdlib.rotate
import org.jraf.k2o.stdlib.rotateExtrude
import org.jraf.k2o.stdlib.translate
import org.jraf.k2o.util.cm
import org.jraf.k2o.util.mm

@Composable
private fun Main() {
  val deckLenX = 20.cm
  val deckLenY = 6.5.cm
  val deckLenZ = 4.mm

  val guardrailLenZ = 3.5.cm
  val guardrailThickness = 4.mm
  val backPylonThickness = 2.mm

  val roundedCornerRadius = 1.cm

  val supportLenZ = 1.5.cm
  val supportLenY = 2.mm

  val reinforcementThickness = 2.mm
  val reinforcementRadius = 3.cm

  // Deck
  ExtrudedRoundedSquare(
    x = deckLenX,
    y = deckLenY,
    z = deckLenZ,
    bottomRightRadius = roundedCornerRadius,
    bottomLeftRadius = roundedCornerRadius,
  )

  // Guardrail
  Guardrail(
    deckLenX = deckLenX,
    deckLenY = deckLenY,
    deckLenZ = deckLenZ,
    roundedCornerRadius = roundedCornerRadius,
    guardrailLenZ = guardrailLenZ,
    guardrailThickness = guardrailThickness,
    backPylonThickness = backPylonThickness,
  )

  // Wall support
  WallSupport(
    deckLenX = deckLenX,
    deckLenY = deckLenY,
    supportLenY = supportLenY,
    supportLenZ = supportLenZ,
  )

  // Reinforcement left
  difference {
    translate(y = deckLenY - supportLenY) {
      Reinforcement(reinforcementThickness, reinforcementRadius)
      translate(z = -reinforcementRadius) {
        Cube(
          x = guardrailThickness,
          y = supportLenY,
          z = reinforcementRadius,
        )
      }
    }

    val logoWidth = reinforcementRadius * .9
    translate(
      x = reinforcementThickness / 2,
      y = deckLenY - logoWidth / 2 + reinforcementThickness - 1.mm,
      z = -reinforcementRadius / 2 + deckLenZ / 2,
    ) {
      rotate(x = 90, z = -90) {
        LzLogo(
          width = reinforcementRadius * .75,
          thickness = reinforcementThickness / 2,
        )
      }
    }
  }

  // Reinforcement right
  translate(x = deckLenX - reinforcementThickness, y = deckLenY - supportLenY) {
    Reinforcement(reinforcementThickness, reinforcementRadius)
    translate(
      x = -guardrailThickness + reinforcementThickness,
      z = -reinforcementRadius,
    ) {
      Cube(
        x = guardrailThickness,
        y = supportLenY,
        z = reinforcementRadius,
      )
    }
  }

}

@Composable
private fun WallSupport(
  deckLenX: Double,
  deckLenY: Double,
  supportLenY: Double,
  supportLenZ: Double,
) {
  translate(
    z = -supportLenZ,
    y = deckLenY - supportLenY,
  ) {
    difference {
      Cube(
        x = deckLenX,
        y = supportLenY,
        z = supportLenZ,
      )


//      val logoWidth = (supportLenZ * .90) * 2
//      translate(
//        x = deckLenX / 2,
//        y = supportLenY / 2,
//        z = supportLenZ / 2,
//      ) {
//        rotate(x = 90) {
//          LurezLogo(
//            width = logoWidth,
//            thickness = supportLenY / 2,
//          )
//        }
//      }
    }
  }
}

@Composable
private fun Reinforcement(reinforcementThickness: Double, reinforcementRadius: Double) {
  translate(x = reinforcementThickness) {
    rotate(y = 90, x = 180) {
      rotateExtrude(degrees = 90) {
        Square(width = reinforcementRadius, height = reinforcementThickness)
      }
    }
  }
}

@Composable
private fun Guardrail(
  deckLenX: Double,
  deckLenY: Double,
  deckLenZ: Double,
  roundedCornerRadius: Double,
  guardrailLenZ: Double,
  guardrailThickness: Double,
  backPylonThickness: Double,
) {
  translate(z = deckLenZ + guardrailLenZ - guardrailThickness) {
    difference {
      ExtrudedRoundedSquare(
        x = deckLenX,
        y = deckLenY,
        z = guardrailThickness,
        bottomRightRadius = roundedCornerRadius,
        bottomLeftRadius = roundedCornerRadius,
      )

      linearExtrude(height = guardrailThickness) {
        offset(radius = -guardrailThickness) {
          RoundedSquare(
            x = deckLenX,
            y = deckLenY,
            bottomRightRadius = roundedCornerRadius,
            bottomLeftRadius = roundedCornerRadius,
          )
        }
      }

      translate(
        x = guardrailThickness,
        y = deckLenY - guardrailThickness,
      ) {
        Cube(
          x = deckLenX - guardrailThickness * 2,
          y = guardrailThickness,
          z = guardrailThickness,
        )
      }
    }
  }

  // Guardrail pylon top left
  translate(
    y = deckLenY - backPylonThickness,
  ) {
    SquarePylon(
      guardrailThickness = guardrailThickness,
      deckLenZ = deckLenZ,
      guardrailLenZ = guardrailLenZ,
      backPylonThickness = backPylonThickness,
    )
  }

  // Guardrail pylon bottom left
  translate(
    x = roundedCornerRadius - roundedCornerRadius * cos(45) + guardrailThickness / 2 * cos(45),
    y = roundedCornerRadius - roundedCornerRadius * cos(45) + guardrailThickness / 2 * cos(45),
  ) {
    RoundPylon(guardrailThickness = guardrailThickness, deckLenZ = deckLenZ, guardrailLenZ = guardrailLenZ)
  }

  // Guardrail pylon bottom right
  translate(
    x = deckLenX - roundedCornerRadius + roundedCornerRadius * cos(45) - guardrailThickness / 2 * cos(45),
    y = roundedCornerRadius - roundedCornerRadius * cos(45) + guardrailThickness / 2 * cos(45),
  ) {
    RoundPylon(guardrailThickness = guardrailThickness, deckLenZ = deckLenZ, guardrailLenZ = guardrailLenZ)
  }

  // Guardrail pylon top right
  translate(
    x = deckLenX - guardrailThickness,
    y = deckLenY - backPylonThickness,
  ) {
    SquarePylon(
      guardrailThickness = guardrailThickness,
      deckLenZ = deckLenZ,
      guardrailLenZ = guardrailLenZ,
      backPylonThickness = backPylonThickness,
    )
  }
}

@Composable
private fun RoundPylon(guardrailThickness: Double, deckLenZ: Double, guardrailLenZ: Double) {
  translate(
    z = deckLenZ,
  ) {
    Cylinder(
      height = guardrailLenZ - guardrailThickness,
      diameter = guardrailThickness,
    )
  }
}

@Composable
private fun SquarePylon(guardrailThickness: Double, deckLenZ: Double, guardrailLenZ: Double, backPylonThickness: Double) {
  translate(
    z = deckLenZ,
  ) {
    Cube(
      x = guardrailThickness,
      y = backPylonThickness,
      z = guardrailLenZ - guardrailThickness,
    )
  }
}

fun main() {
  openScad(
//    fa = 0.5,
//    fs = 0.5,
    sink =
      SystemFileSystem.sink(Path("/Users/bod/Tmp/shelf.scad")).buffered(),
  ) {
    Main()
  }
}
