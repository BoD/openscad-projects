module wheel(radius, thickness, angle = 360) {
  rotate_extrude(angle) {
  translate([0, -thickness / 2])
    square([radius - thickness / 2, thickness]);
  translate([radius - thickness / 2, 0])
    circle(thickness / 2);
  }
}

wheel(30, 5, 90);