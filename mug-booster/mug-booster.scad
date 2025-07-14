use <star.scad>

module base(width, thickness, indent_width) {
  color("green")
    difference() {
      cylinder(h = thickness, r = width / 2);
      linear_extrude(thickness * 2)
        star(5, r1 = width / 2 - indent_width * 2, r2 = (width / 2 - indent_width * 2) / 2.25);
    }
}

module leg(thickness, width, height, curve_radius) {
  translate([0, width, height - curve_radius])
    rotate([90, 0, 0])
      rotate_extrude(90)
        translate([curve_radius, 0, 0])
          square([thickness, width], center = false);

  translate([curve_radius, 0, 0])
    cube([thickness, width, height - curve_radius]);

  translate([-15, 0, height])
    cube([15, width, thickness]);
}

module main() {
  base_width = 100;
  base_thickness = 4;
  base_height = 80;
  base_indent_height = 1.25;
  base_indent_width = 8;

  leg_thickness = base_thickness;

  coffee_machine_base_width = 130;

  // Distance between the coffee machine base and the legs
  coffee_machine_base_margin = .5;

  // Distance on the X axis between the centers of two legs
  leg_X_distance = coffee_machine_base_width + leg_thickness + coffee_machine_base_margin * 2;

  // Distance on the Y axis between the centers of two legs 
  leg_Y_distance = 60;

  leg_width = 8;

  leg_curve_radius = (leg_X_distance - base_thickness) / 2 - sqrt((base_width / 2)^2 - ((leg_Y_distance - leg_width) / 2)^2);

  difference() {
    union() {
      // Base
      translate([0, 0, base_height])
        base(width = base_width, thickness = base_thickness, indent_width = base_indent_width);

      // Right bottom leg
      translate([leg_X_distance / 2 - base_thickness / 2 - leg_curve_radius, -leg_Y_distance / 2 - leg_width / 2, 0])
        leg(thickness = leg_thickness, width = leg_width, height = base_height, curve_radius = leg_curve_radius);

      // Right top leg
      translate([leg_X_distance / 2 - base_thickness / 2 - leg_curve_radius, leg_Y_distance / 2 - leg_width / 2, 0])
        leg(thickness = leg_thickness, width = leg_width, height = base_height, curve_radius = leg_curve_radius);

      // Left top leg
      rotate([0, 0, 180])
        translate([leg_X_distance / 2 - base_thickness / 2 - leg_curve_radius, -leg_Y_distance / 2 - leg_width / 2, 0])
          leg(thickness = leg_thickness, width = leg_width, height = base_height, curve_radius = leg_curve_radius);

      // Left bottom leg
      rotate([0, 0, 180])
        translate([leg_X_distance / 2 - base_thickness / 2 - leg_curve_radius, leg_Y_distance / 2 - leg_width / 2, 0])
          leg(thickness = leg_thickness, width = leg_width, height = base_height, curve_radius = leg_curve_radius);
    }

    // Indent
    translate([0, 0, base_height + base_thickness - base_indent_height])
      cylinder(h = base_indent_height * 2, r = base_width / 2 - base_indent_width);
  }
}

$fa = .1;
$fs = .1;
main();
