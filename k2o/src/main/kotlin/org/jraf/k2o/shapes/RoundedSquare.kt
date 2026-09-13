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

package org.jraf.k2o.shapes

import androidx.compose.runtime.Composable
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import org.jraf.k2o.dsl.openScad
import org.jraf.k2o.projects.TMP_FOLDER
import org.jraf.k2o.stdlib.Circle
import org.jraf.k2o.stdlib.Square
import org.jraf.k2o.stdlib.difference
import org.jraf.k2o.stdlib.hull
import org.jraf.k2o.stdlib.translate
import org.jraf.k2o.units.Length
import org.jraf.k2o.units.Length.Companion.mm

@Composable
fun RoundedSquare(
  x: Length,
  y: Length,
  radius: Length = Length.Zero,
) {
  RoundedSquare(
    x = x,
    y = y,
    topLeftRadius = radius,
    topRightRadius = radius,
    bottomRightRadius = radius,
    bottomLeftRadius = radius,
  )
}

@Composable
fun RoundedSquare(
  x: Length,
  y: Length,
  topLeftRadius: Length = Length.Zero,
  topRightRadius: Length = Length.Zero,
  bottomRightRadius: Length = Length.Zero,
  bottomLeftRadius: Length = Length.Zero,
) {
  hull {
    if (topLeftRadius > Length.Zero) {
      translate(x = topLeftRadius, y = y - topLeftRadius) {
        // Top left quarter circle
        difference {
          Circle(radius = topLeftRadius)
          translate(y = -topLeftRadius) {
            Square(width = topLeftRadius, height = topLeftRadius * 2)
          }
          translate(x = -topLeftRadius, y = -topLeftRadius) {
            Square(width = topLeftRadius, height = topLeftRadius)
          }
        }
      }
    } else {
      translate(y = y - Length.Smallest) {
        Square(Length.Smallest)
      }
    }
    if (topRightRadius > Length.Zero) {
      translate(x = x - topRightRadius, y = y - topRightRadius) {
        // Top right quarter circle
        difference {
          Circle(radius = topRightRadius)
          translate(x = -topRightRadius, y = -topRightRadius) {
            Square(width = topRightRadius, height = topRightRadius * 2)
          }
          translate(y = -topRightRadius) {
            Square(width = topRightRadius, height = topRightRadius)
          }
        }
      }
    } else {
      translate(x = x - Length.Smallest, y = y - Length.Smallest) {
        Square(Length.Smallest)
      }
    }

    if (bottomRightRadius > Length.Zero) {
      translate(x = x - bottomRightRadius, y = bottomRightRadius) {
        // Bottom right quarter circle
        difference {
          Circle(radius = bottomRightRadius)
          translate(x = -bottomRightRadius, y = -bottomRightRadius) {
            Square(width = bottomRightRadius, height = bottomRightRadius * 2)
          }
          Square(width = bottomRightRadius, height = bottomRightRadius)
        }
      }
    } else {
      translate(x = x - Length.Smallest) {
        Square(Length.Smallest)
      }
    }

    if (bottomLeftRadius > Length.Zero) {
      translate(x = bottomLeftRadius, y = bottomLeftRadius) {
        // Bottom left quarter circle
        difference {
          Circle(radius = bottomLeftRadius)
          translate(y = -bottomLeftRadius) {
            Square(width = bottomLeftRadius, height = bottomLeftRadius * 2)
          }
          translate(x = -bottomLeftRadius) {
            Square(width = bottomLeftRadius, height = bottomLeftRadius)
          }
        }
      }
    } else {
      Square(Length.Smallest)
    }
  }
}

fun main() {
  openScad(SystemFileSystem.sink(Path(TMP_FOLDER, "rounded-square.scad")).buffered()) {
    RoundedSquare(
      x = 320.mm,
      y = 200.mm,
      topLeftRadius = 30.mm,
      topRightRadius = 50.mm,
      bottomRightRadius = 70.mm,
      bottomLeftRadius = 80.mm,
    )

    translate(x = 400.mm) {
      RoundedSquare(
        x = 320.mm,
        y = 200.mm,
        topLeftRadius = 0.mm,
        topRightRadius = 0.mm,
        bottomRightRadius = 0.mm,
        bottomLeftRadius = 0.mm,
      )
    }

    translate(x = 800.mm) {
      RoundedSquare(
        x = 320.mm,
        y = 200.mm,
        topLeftRadius = 0.mm,
        topRightRadius = 50.mm,
        bottomRightRadius = 70.mm,
        bottomLeftRadius = 80.mm,
      )
    }

    translate(x = 1200.mm) {
      RoundedSquare(
        x = 320.mm,
        y = 200.mm,
        topLeftRadius = 30.mm,
        topRightRadius = 0.mm,
        bottomRightRadius = 0.mm,
        bottomLeftRadius = 80.mm,
      )
    }

    translate(x = 1600.mm) {
      RoundedSquare(
        x = 320.mm,
        y = 200.mm,
        80.mm,
      )
    }
  }
}
