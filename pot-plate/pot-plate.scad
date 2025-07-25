use <../lurez-logo/lurez-logo.scad>

module cross_section(
  diameter,
  thickness,
  border_height,
  border_angle,
) {
  square(
      [diameter / 2, thickness]
  );

  translate([diameter / 2, thickness])
    rotate(border_angle)
      translate([0, -thickness])
        square(
            [border_height, thickness]
        );

  translate([diameter / 2, thickness])
    difference() {
      circle(thickness);
      translate([-thickness, 0])
        square(thickness * 2);
    }
}

module main() {
  diameter = 180;
  thickness = 4;
  border_height = 15;
  border_angle = 45;
  logo_thickness = 1;

  difference() {
    rotate_extrude() {
      cross_section(
        diameter = diameter,
        thickness = thickness,
        border_height = border_height,
        border_angle = border_angle,
      );
    }

    translate([0, 0, thickness - logo_thickness])
      lurez_logo(
        width = 140,
        thickness = logo_thickness,
      );
  };
}

$fa = .1;
$fs = .1;
main();
