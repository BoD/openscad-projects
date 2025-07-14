use <flexible_cylinder.scad>

module main() {
  difference() {
    cube([14 * 10 + 2, 17, 7]);

    holes = 10;
    for (i = [0 : holes - 1]) {
      translate([i * 6 + 4, 15, 1])
        cylinder(d = 2 + i * .1, h = 6);

      diameter = 9 + i * .1;
      translate([14 * i + 2, 2, 1])
          translate([diameter / 2, diameter / 2, 0])
            flexible_cylinder(d = diameter, h = 6, flex = 2);

    }
  }
}

$fa = .1;
$fs = .1;
main();
