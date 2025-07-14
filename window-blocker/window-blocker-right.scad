use <window-blocker.scad>

module logo(thickness) {
  linear_extrude(height = thickness)
    import("lurez-logo.svg", center = false);
}


module main() {
  x1 = 10;
  x2 = 24;
  x3 = 20;
  x4 = 12;
  
  y1 = 40;
  y2 = 8;
  y3 = 7;

  z1 = 7;
  z2 = 24;

  difference() {
    window_blocker(
      x1 = x1,
      x2 = x2,
      x3 = x3,
      x4 = x4,

      y1 = y1,
      y2 = y2,
      y3 = y3,

      z1 = z1,
      z2 = z2,
    );
  
    // Logo
    logo_thickness = 1.5;
    logo_height = y1 - 4;
    logo_width = logo_height * 3 / 4;
    translate([logo_width + (x1 + x2 + x3 + x4 - logo_width) / 2, (y1 - logo_height) / 2, z1 + z2 - logo_thickness])
      resize([logo_width, logo_height, 0], true)
        rotate([0, 0, 90])
          logo(thickness = logo_thickness * 2);
  }
}

$fa = .1;
$fs = .1;
main();
