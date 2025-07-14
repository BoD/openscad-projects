module leaf(x, y, rotate) {
  translate([x, y, 0])
    rotate([0, 0, rotate])
      linear_extrude(height = 4)
        resize([0, 18, 0], true)
          import("leave.svg", center = true);
}

module plant_pot_support(outer_diameter, width, thickness) {
  difference() {
    // Outer ring
    cylinder(h = thickness, r = outer_diameter / 2);

    // Inner ring
    cylinder(h = thickness, r = outer_diameter / 2 - width);
    
    // Leaves
    leaf_count = 13;
    leaf_radius = outer_diameter / 2 - width / 2 - 2;
    for (i = [0 : leaf_count - 1]) {
      angle = 360 / leaf_count * i;
      leaf(x = cos(angle) * leaf_radius, y = sin(angle) * leaf_radius, rotate = angle + 15);
    }
  };
}

module main() {
  outer_diameter = 150;
  thickness = 4;
  width = 30;

  plant_pot_support(
    outer_diameter = outer_diameter, 
    width = width,
    thickness = thickness,
  );
}

$fa = .1;
$fs = .1;
main();
