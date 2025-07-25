module lurez_logo(
  width,
  thickness
) {
  height = width * 3 / 4;
  translate([-width / 2, -height / 2, 0])
    linear_extrude(height = thickness)
      resize([width, height, 0], true)
        import("lurez-logo.svg", center = false);
}


module main() {
  logo_thickness = 2;
  logo_width = 400;
  logo_height = logo_height * 3 / 4;
  translate([logo_width / 2, logo_height / 2, logo_thickness / 2])
    resize([logo_width, logo_height, 0], true)
      logo(thickness = logo_thickness * 2);
}

$fa = .1;
$fs = .1;
main();
