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

@file:Suppress("unused")

package org.jraf.k2o.projects.redgreenswitch

import androidx.compose.runtime.Composable
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import org.jraf.k2o.dsl.openScad
import org.jraf.k2o.projects.TMP_FOLDER
import org.jraf.k2o.stdlib.Call
import org.jraf.k2o.stdlib.Cylinder
import org.jraf.k2o.stdlib.Use
import org.jraf.k2o.stdlib.color
import org.jraf.k2o.stdlib.difference
import org.jraf.k2o.stdlib.hull
import org.jraf.k2o.stdlib.translate
import org.jraf.k2o.units.Length
import org.jraf.k2o.units.Length.Companion.mm

@Composable
private fun SupportBase(
  tokenDiameter: Length,
  paddingAroundToken: Length,
  thickness: Length,
  colorIndent: Length,
  pegHoleDiameter: Length,
  pegThicknessRatio: Double,
  magnetWidth: Length,
  magnetHeight: Length,
) {
  color("white") {
    difference {
      translate((tokenDiameter + paddingAroundToken * 2) / 2, (tokenDiameter + paddingAroundToken * 2) / 2) {
        difference {
          hull {
            Cylinder(diameter = tokenDiameter + paddingAroundToken * 2, height = thickness)
            translate(tokenDiameter + paddingAroundToken) {
              Cylinder(diameter = tokenDiameter + paddingAroundToken * 2, height = thickness)
            }
          }

          // Left indent
          translate(z = thickness - colorIndent) {
            Cylinder(diameter = tokenDiameter, height = thickness)
          }

          // Right indent
          translate(tokenDiameter + paddingAroundToken, 0.mm, thickness - colorIndent) {
            Cylinder(diameter = tokenDiameter, height = thickness)
          }

          val magnetHeightAdjusted = magnetHeight * 1.1 // Make the magnet holes slightly taller than the magnets
          // Left magnet hole
          translate(z = thickness - colorIndent - magnetHeightAdjusted) {
            Call(
              "flexible_Cylinder",
              "d" to magnetWidth,
              "h" to magnetHeightAdjusted,
              "flex" to 2,
            )
          }

          // Right magnet hole
          translate(x = tokenDiameter + paddingAroundToken, z = thickness - colorIndent - magnetHeightAdjusted) {
            Call(
              "flexible_Cylinder",
              "d" to magnetWidth,
              "h" to magnetHeightAdjusted,
              "flex" to 2,
            )
          }
        }
      }

      val pegHeight = thickness * pegThicknessRatio * 1.1 // Make the peg holes slightly taller than the pegs
      // Left peg hole
      translate(paddingAroundToken / 2, (tokenDiameter + paddingAroundToken * 2) / 2, thickness - pegHeight) {
        Cylinder(diameter = pegHoleDiameter, height = pegHeight)
      }

      // Right peg hole
      translate(
        tokenDiameter * 2 + paddingAroundToken * 2 + paddingAroundToken / 2,
        (tokenDiameter + paddingAroundToken * 2) / 2,
        thickness - pegHeight,
      ) {
        Cylinder(diameter = pegHoleDiameter, height = pegHeight)
      }

      // Bottom peg hole
      translate(
        tokenDiameter + paddingAroundToken * 2 - paddingAroundToken / 2,
        paddingAroundToken / 2,
        thickness - pegHeight,
      ) {
        Cylinder(diameter = pegHoleDiameter, height = pegHeight)
      }

      // Top peg hole
      translate(
        tokenDiameter + paddingAroundToken * 2 - paddingAroundToken / 2,
        tokenDiameter + paddingAroundToken * 2 - paddingAroundToken / 2,
        thickness - pegHeight,
      ) {
        Cylinder(diameter = pegHoleDiameter, height = pegHeight)
      }
    }
  }
}

@Composable
private fun ColorIndicator(
  tokenDiameter: Length,
  thickness: Length,
  color: String,
) {
  color(color) {
    translate(tokenDiameter / 2, tokenDiameter / 2) {
      Cylinder(diameter = tokenDiameter, height = thickness)
    }
  }
}

@Composable
private fun SupportTop(
  tokenDiameter: Length,
  paddingAroundToken: Length,
  tokenThickness: Length,
  thickness: Length,
  pegDiameter: Length,
  pegThicknessRatio: Double,
  baseThickness: Length,
) {
  color("white") {
    val pegHeight = baseThickness * pegThicknessRatio
    val wiggleRoom = 1.mm

    translate((tokenDiameter + paddingAroundToken * 2) / 2, (tokenDiameter + paddingAroundToken * 2) / 2, pegHeight) {
      difference {
        // Outer
        hull {
          Cylinder(diameter = tokenDiameter + paddingAroundToken * 2, height = tokenThickness + thickness + wiggleRoom)
          translate(tokenDiameter + paddingAroundToken) {
            Cylinder(diameter = tokenDiameter + paddingAroundToken * 2, height = tokenThickness + thickness + wiggleRoom)
          }
        }

        // Inner
        hull {
          Cylinder(diameter = tokenDiameter, height = tokenThickness + wiggleRoom)
          translate(tokenDiameter + paddingAroundToken) {
            Cylinder(diameter = tokenDiameter, height = tokenThickness + wiggleRoom)
          }
        }

        // Top opening
        hull {
          translate(z = tokenThickness) {
            Cylinder(diameter = tokenDiameter - paddingAroundToken * 2, height = thickness + wiggleRoom)
          }
          translate(x = tokenDiameter + paddingAroundToken, z = tokenThickness) {
            Cylinder(diameter = tokenDiameter - paddingAroundToken * 2, height = thickness + wiggleRoom)
          }
        }
      }
    }

    // Left peg hole
    translate(paddingAroundToken / 2, (tokenDiameter + paddingAroundToken * 2) / 2) {
      Cylinder(diameter = pegDiameter, height = pegHeight)
    }

    // Right peg hole
    translate(
      tokenDiameter * 2 + paddingAroundToken * 2 + paddingAroundToken / 2,
      (tokenDiameter + paddingAroundToken * 2) / 2,
    ) {
      Cylinder(diameter = pegDiameter, height = pegHeight)
    }

    // Bottom peg hole
    translate(
      tokenDiameter + paddingAroundToken * 2 - paddingAroundToken / 2,
      paddingAroundToken / 2,
    ) {
      Cylinder(diameter = pegDiameter, height = pegHeight)
    }

    // Top peg hole
    translate(
      tokenDiameter + paddingAroundToken * 2 - paddingAroundToken / 2,
      tokenDiameter + paddingAroundToken * 2 - paddingAroundToken / 2,
    ) {
      Cylinder(diameter = pegDiameter, height = pegHeight)
    }
  }
}

@Composable
private fun Token(
  diameter: Length,
  thickness: Length,
  magnetWidth: Length,
  magnetHeight: Length,
  grabDiameter: Length,
  grabIndent: Length,
) {
  color("white") {
    translate(diameter / 2, diameter / 2) {
      difference {
        Cylinder(diameter = diameter, height = thickness)

        val magnetHeightAdjusted = magnetHeight * 1.1 // Make the magnet hole slightly taller than the magnet
        // Magnet hole
        Call(
          "flexible_Cylinder",
          "d" to magnetWidth,
          "h" to magnetHeightAdjusted,
          "flex" to 2,
        )

        // Grab indent
        translate(z = thickness - grabIndent) {
          Cylinder(diameter = grabDiameter, height = grabIndent)
        }
      }
    }
  }
}

@Composable
private fun EverythingAssembled(
  magnetWidth: Length,
  magnetHeight: Length,
  tokenDiameter: Length,
  tokenGrabDiameter: Length,
  tokenGrabIndent: Length,
  tokenThickness: Length,
  paddingAroundToken: Length,
  supportBaseThickness: Length,
  supportTopThickness: Length,
  colorIndent: Length,
  pegDiameter: Length,
  pegHoleDiameter: Length,
  pegThicknessRatio: Double,
) {
  // Support base
  SupportBase(
    tokenDiameter = tokenDiameter,
    paddingAroundToken = paddingAroundToken,
    thickness = supportBaseThickness,
    colorIndent = colorIndent,
    pegHoleDiameter = pegHoleDiameter,
    pegThicknessRatio = pegThicknessRatio,
    magnetWidth = magnetWidth,
    magnetHeight = magnetHeight,
  )

  // Red color indicator
  translate(paddingAroundToken, paddingAroundToken, supportBaseThickness - colorIndent) {
    ColorIndicator(
      tokenDiameter = tokenDiameter,
      thickness = colorIndent,
      color = "red",
    )
  }

  // Green color indicator
  translate(tokenDiameter + paddingAroundToken * 2, paddingAroundToken, supportBaseThickness - colorIndent) {
    ColorIndicator(
      tokenDiameter = tokenDiameter,
      thickness = colorIndent,
      color = "green",
    )
  }

  val pegHeight = supportBaseThickness * pegThicknessRatio

  // Support top
  translate(z = supportBaseThickness - pegHeight) {
    SupportTop(
      tokenDiameter = tokenDiameter,
      paddingAroundToken = paddingAroundToken,
      tokenThickness = tokenThickness,
      thickness = supportTopThickness,
      pegDiameter = pegDiameter,
      pegThicknessRatio = pegThicknessRatio,
      baseThickness = supportBaseThickness,
    )
  }

  // Token
  translate(paddingAroundToken + tokenDiameter, paddingAroundToken, supportBaseThickness) {
    Token(
      diameter = tokenDiameter,
      thickness = tokenThickness,
      magnetWidth = magnetWidth,
      magnetHeight = magnetHeight,
      grabDiameter = tokenGrabDiameter,
      grabIndent = tokenGrabIndent,
    )
  }
}

@Composable
private fun EverythingExploded(
  magnetWidth: Length,
  magnetHeight: Length,
  tokenDiameter: Length,
  tokenGrabDiameter: Length,
  tokenGrabIndent: Length,
  tokenThickness: Length,
  paddingAroundToken: Length,
  supportBaseThickness: Length,
  supportTopThickness: Length,
  colorIndent: Length,
  pegDiameter: Length,
  pegHoleDiameter: Length,
  pegThicknessRatio: Double,
) {
  // Support base
  SupportBase(
    tokenDiameter = tokenDiameter,
    paddingAroundToken = paddingAroundToken,
    thickness = supportBaseThickness,
    colorIndent = colorIndent,
    pegHoleDiameter = pegHoleDiameter,
    pegThicknessRatio = pegThicknessRatio,
    magnetWidth = magnetWidth,
    magnetHeight = magnetHeight,
  )

  // Red color indicator
  translate(50.mm) {
    ColorIndicator(
      tokenDiameter = tokenDiameter,
      thickness = colorIndent,
      color = "red",
    )
  }

  // Green color indicator
  translate(75.mm) {
    ColorIndicator(
      tokenDiameter = tokenDiameter,
      thickness = colorIndent,
      color = "green",
    )
  }

  // Support top
  translate(y = 30.mm) {
    SupportTop(
      tokenDiameter = tokenDiameter,
      paddingAroundToken = paddingAroundToken,
      tokenThickness = tokenThickness,
      thickness = supportTopThickness,
      pegDiameter = pegDiameter,
      pegThicknessRatio = pegThicknessRatio,
      baseThickness = supportBaseThickness,
    )
  }

  // Token
  translate(100.mm) {
    Token(
      diameter = tokenDiameter,
      thickness = tokenThickness,
      magnetWidth = magnetWidth,
      magnetHeight = magnetHeight,
      grabDiameter = tokenGrabDiameter,
      grabIndent = tokenGrabIndent,
    )
  }
}

@Composable
private fun RedGreenSwitch() {
  Use("flexible_Cylinder.scad")
  val magnetWidth = 9.9.mm
  val magnetHeight = 6.mm
  val paddingAroundToken = 3.2.mm
  val tokenDiameter = 20.mm
  val tokenGrabDiameter = tokenDiameter - paddingAroundToken * 2
  val tokenGrabIndent = 1.mm
  val tokenThickness = magnetHeight + 1.mm + tokenGrabIndent
  val supportBaseThickness = magnetHeight + 1.5.mm
  val supportTopThickness = 1.mm
  val colorIndent = 0.8.mm
  val pegDiameter = 2.mm
  val pegHoleDiameter = 2.2.mm
  val pegThicknessRatio = 0.5
  EverythingExploded(
    magnetWidth = magnetWidth,
    magnetHeight = magnetHeight,
    tokenDiameter = tokenDiameter,
    tokenGrabDiameter = tokenGrabDiameter,
    tokenGrabIndent = tokenGrabIndent,
    tokenThickness = tokenThickness,
    paddingAroundToken = paddingAroundToken,
    supportBaseThickness = supportBaseThickness,
    supportTopThickness = supportTopThickness,
    colorIndent = colorIndent,
    pegDiameter = pegDiameter,
    pegHoleDiameter = pegHoleDiameter,
    pegThicknessRatio = pegThicknessRatio,
  )
}

fun main() {
  openScad(SystemFileSystem.sink(Path(TMP_FOLDER, "red-green-switch.scad")).buffered()) {
    RedGreenSwitch()
  }
}
