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
import org.jraf.k2o.stdlib.Circle
import org.jraf.k2o.stdlib.Square
import org.jraf.k2o.stdlib.difference
import org.jraf.k2o.stdlib.hull
import org.jraf.k2o.stdlib.translate

@Composable
fun RoundedSquare(
  x: Number,
  y: Number,
  radius: Number = 0,
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
  x: Number,
  y: Number,
  topLeftRadius: Number = 0,
  topRightRadius: Number = 0,
  bottomRightRadius: Number = 0,
  bottomLeftRadius: Number = 0,
) {
  val x = x.toDouble()
  val y = y.toDouble()
  val topLeftRadius = topLeftRadius.toDouble()
  val topRightRadius = topRightRadius.toDouble()
  val bottomRightRadius = bottomRightRadius.toDouble()
  val bottomLeftRadius = bottomLeftRadius.toDouble()

  hull {
    if (topLeftRadius > 0) {
      translate(x = topLeftRadius, y = y - topLeftRadius) {
        // Top left quarter circle
        difference {
          Circle(radius = topLeftRadius)
          translate(x = 0, y = -topLeftRadius) {
            Square(width = topLeftRadius, height = topLeftRadius * 2)
          }
          translate(x = -topLeftRadius, y = -topLeftRadius) {
            Square(width = topLeftRadius, height = topLeftRadius)
          }
        }
      }
    } else {
      translate(x = 0, y = y - SMALLEST_SQUARE) {
        Square(SMALLEST_SQUARE)
      }
    }
    if (topRightRadius > 0) {
      translate(x = x - topRightRadius, y = y - topRightRadius) {
        // Top right quarter circle
        difference {
          Circle(radius = topRightRadius)
          translate(x = -topRightRadius, y = -topRightRadius) {
            Square(width = topRightRadius, height = topRightRadius * 2)
          }
          translate(x = 0, y = -topRightRadius) {
            Square(width = topRightRadius, height = topRightRadius)
          }
        }
      }
    } else {
      translate(x = x - SMALLEST_SQUARE, y = y - SMALLEST_SQUARE) {
        Square(SMALLEST_SQUARE)
      }
    }

    if (bottomRightRadius > 0) {
      translate(x = x - bottomRightRadius, y = bottomRightRadius) {
        // Bottom right quarter circle
        difference {
          Circle(radius = bottomRightRadius)
          translate(x = -bottomRightRadius, y = -bottomRightRadius) {
            Square(width = bottomRightRadius, height = bottomRightRadius * 2)
          }
          translate(x = 0, y = 0) {
            Square(width = bottomRightRadius, height = bottomRightRadius)
          }
        }
      }
    } else {
      translate(x = x - SMALLEST_SQUARE, y = 0) {
        Square(SMALLEST_SQUARE)
      }
    }

    if (bottomLeftRadius > 0) {
      translate(x = bottomLeftRadius, y = bottomLeftRadius) {
        // Bottom left quarter circle
        difference {
          Circle(radius = bottomLeftRadius)
          translate(x = 0, y = -bottomLeftRadius) {
            Square(width = bottomLeftRadius, height = bottomLeftRadius * 2)
          }
          translate(x = -bottomLeftRadius, y = 0) {
            Square(width = bottomLeftRadius, height = bottomLeftRadius)
          }
        }
      }
    } else {
      Square(SMALLEST_SQUARE)
    }
  }
}

fun main() {
  openScad(SystemFileSystem.sink(Path("/Users/bod/Tmp/rounded-square.scad")).buffered()) {
    RoundedSquare(
      x = 320,
      y = 200,
      topLeftRadius = 30,
      topRightRadius = 50,
      bottomRightRadius = 70,
      bottomLeftRadius = 80,
    )

    translate(x = 400) {
      RoundedSquare(
        x = 320,
        y = 200,
        topLeftRadius = 0,
        topRightRadius = 0,
        bottomRightRadius = 0,
        bottomLeftRadius = 0,
      )
    }

    translate(x = 800) {
      RoundedSquare(
        x = 320,
        y = 200,
        topLeftRadius = 0,
        topRightRadius = 50,
        bottomRightRadius = 70,
        bottomLeftRadius = 80,
      )
    }

    translate(x = 1200) {
      RoundedSquare(
        x = 320,
        y = 200,
        topLeftRadius = 30,
        topRightRadius = 0,
        bottomRightRadius = 0,
        bottomLeftRadius = 80,
      )
    }

    translate(x = 1600) {
      RoundedSquare(
        x = 320,
        y = 200,
        80,
      )
    }
  }
}
