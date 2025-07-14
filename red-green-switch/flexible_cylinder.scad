module flexible_cylinder(
d,
h,
flex,
) {
  segments = 18;
  angle = 360 / segments;
  flex_angle = 55;

  difference() {
    cylinder(
    d = 2 * sqrt(flex^2 + (d / 2)^2 - (2 * flex * (d / 2) * cos(180 - flex_angle))),
    h = h
    );

    for (i = [0 : segments - 1]) {
      rotate([0, 0, i * angle])
        translate([d / 2, 0, 0])
          rotate([0, 0, -flex_angle])
            cube([flex, flex / 3, h]);
    }
  }
}

//$fa = .1;
//$fs = .1;
//
//flexible_cylinder(
//d = 20,
//h = 5,
//flex = 4
//);

