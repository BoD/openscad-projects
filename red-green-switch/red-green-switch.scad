use <flexible_cylinder.scad>

module support_base(
token_diameter,
padding_around_token,
thickness,
color_indent,
peg_hole_diameter,
peg_thickness_ratio,
magnet_width,
magnet_height,
) {
  color("white")
    difference() {
      translate([(token_diameter + padding_around_token * 2) / 2, (token_diameter + padding_around_token * 2) / 2, 0])
        difference() {
          hull() {
            cylinder(d = token_diameter + padding_around_token * 2, h = thickness);
            translate([token_diameter + padding_around_token, 0, 0])
              cylinder(d = token_diameter + padding_around_token * 2, h = thickness);
          }

          // Left indent
          translate([0, 0, thickness - color_indent])
            cylinder(d = token_diameter, h = thickness);

          // Right indent
          translate([token_diameter + padding_around_token, 0, thickness - color_indent])
            cylinder(d = token_diameter, h = thickness);

          magnet_height_adjusted = magnet_height * 1.1; // Make the magnet holes slightly taller than the magnets
          // Left magnet hole
          translate([0, 0, thickness - color_indent - magnet_height_adjusted])
            flexible_cylinder(d = magnet_width, h = magnet_height_adjusted, flex = 2);

          // Right magnet hole
          translate([token_diameter + padding_around_token, 0, thickness - color_indent - magnet_height_adjusted])
            flexible_cylinder(d = magnet_width, h = magnet_height_adjusted, flex = 2);
        }

      peg_height = thickness * peg_thickness_ratio * 1.1; // Make the peg holes slightly taller than the pegs
      // Left peg hole
      translate([padding_around_token / 2, (token_diameter + padding_around_token * 2) / 2, thickness - peg_height])
        cylinder(d = peg_hole_diameter, h = peg_height);

      // Right peg hole
      translate([token_diameter * 2 + padding_around_token * 2 + padding_around_token / 2, (token_diameter + padding_around_token * 2) / 2, thickness - peg_height])
        cylinder(d = peg_hole_diameter, h = peg_height);

      // Bottom peg hole
      translate([token_diameter + padding_around_token * 2 - padding_around_token / 2, padding_around_token / 2, thickness - peg_height])
        cylinder(d = peg_hole_diameter, h = peg_height);

      // Top peg hole
      translate([token_diameter + padding_around_token * 2 - padding_around_token / 2, token_diameter + padding_around_token * 2 - padding_around_token / 2, thickness - peg_height])
        cylinder(d = peg_hole_diameter, h = peg_height);
    }
}

module color_indicator(
token_diameter,
thickness,
color,
) {
  color(color)
    translate([token_diameter / 2, token_diameter / 2, 0])
      cylinder(d = token_diameter, h = thickness);
}

module support_top(
token_diameter,
padding_around_token,
token_thickness,
thickness,
peg_diameter,
peg_thickness_ratio,
base_thickness,
) {
  color("white") {
    peg_height = base_thickness * peg_thickness_ratio;
    wiggle_room = 1;

    translate([(token_diameter + padding_around_token * 2) / 2, (token_diameter + padding_around_token * 2) / 2, peg_height])
      difference() {
        // Outer
        hull() {
          cylinder(d = token_diameter + padding_around_token * 2, h = token_thickness + thickness + wiggle_room);
          translate([token_diameter + padding_around_token, 0, 0])
            cylinder(d = token_diameter + padding_around_token * 2, h = token_thickness + thickness + wiggle_room);
        }

        // Inner
        hull() {
          cylinder(d = token_diameter, h = token_thickness + wiggle_room);
          translate([token_diameter + padding_around_token, 0, 0])
            cylinder(d = token_diameter, h = token_thickness + wiggle_room);
        }

        // Top opening
        hull() {
          translate([0, 0, token_thickness])
            cylinder(d = token_diameter - padding_around_token * 2, h = thickness + wiggle_room);
          translate([token_diameter + padding_around_token, 0, token_thickness])
            cylinder(d = token_diameter - padding_around_token * 2, h = thickness + wiggle_room);
        }
      }

    // Left peg hole
    translate([padding_around_token / 2, (token_diameter + padding_around_token * 2) / 2, 0])
      cylinder(d = peg_diameter, h = peg_height);

    // Right peg hole
    translate([token_diameter * 2 + padding_around_token * 2 + padding_around_token / 2, (token_diameter + padding_around_token * 2) / 2, 0])
      cylinder(d = peg_diameter, h = peg_height);

    // Bottom peg hole
    translate([token_diameter + padding_around_token * 2 - padding_around_token / 2, padding_around_token / 2, 0])
      cylinder(d = peg_diameter, h = peg_height);

    // Top peg hole
    translate([token_diameter + padding_around_token * 2 - padding_around_token / 2, token_diameter + padding_around_token * 2 - padding_around_token / 2, 0])
      cylinder(d = peg_diameter, h = peg_height);
  }
}

module token(
diameter,
thickness,
magnet_width,
magnet_height,
grab_diameter,
grab_indent,
) {
  color("white")
    translate([diameter / 2, diameter / 2, 0])
      difference() {
        cylinder(d = diameter, h = thickness);

        magnet_height_adjusted = magnet_height * 1.1; // Make the magnet hole slightly taller than the magnet

        // Magnet hole
        flexible_cylinder(d = magnet_width, h = magnet_height_adjusted, flex = 2);

        // Grab indent
        translate([0, 0, thickness - grab_indent])
          cylinder(d = grab_diameter, h = grab_indent);
      }
}

module everything_assembled(
magnet_width,
magnet_height,
token_diameter,
token_grab_diameter,
token_grab_indent,
token_thickness,
padding_around_token,
support_base_thickness,
support_top_thickness,
color_indent,
peg_diameter,
peg_hole_diameter,
peg_thickness_ratio,
) {
  // Support base
  support_base(
  token_diameter = token_diameter,
  padding_around_token = padding_around_token,
  thickness = support_base_thickness,
  color_indent = color_indent,
  peg_hole_diameter = peg_hole_diameter,
  peg_thickness_ratio = peg_thickness_ratio,
  magnet_width = magnet_width,
  magnet_height = magnet_height
  );

  // Red color indicator
  translate([padding_around_token, padding_around_token, support_base_thickness - color_indent])
    color_indicator(
    token_diameter = token_diameter,
    thickness = color_indent,
    color = "red"
    );

  // Green color indicator
  translate([token_diameter + padding_around_token * 2, padding_around_token, support_base_thickness - color_indent])
    color_indicator(
    token_diameter = token_diameter,
    thickness = color_indent,
    color = "green"
    );

  peg_height = support_base_thickness * peg_thickness_ratio;

  // Support top
  translate([0, 0, support_base_thickness - peg_height])
    support_top(
    token_diameter = token_diameter,
    padding_around_token = padding_around_token,
    token_thickness = token_thickness,
    thickness = support_top_thickness,
    peg_diameter = peg_diameter,
    peg_thickness_ratio = peg_thickness_ratio,
    base_thickness = support_base_thickness
    );

  // Token
  translate([padding_around_token + $t * token_diameter, padding_around_token, support_base_thickness])
    token(
    diameter = token_diameter,
    thickness = token_thickness,
    magnet_width = magnet_width,
    magnet_height = magnet_height,
    grab_diameter = token_grab_diameter,
    grab_indent = token_grab_indent
    );
}

module everything_exploded(
magnet_width,
magnet_height,
token_diameter,
token_grab_diameter,
token_grab_indent,
token_thickness,
padding_around_token,
support_base_thickness,
support_top_thickness,
color_indent,
peg_diameter,
peg_hole_diameter,
peg_thickness_ratio,
) {
  // Support base
  support_base(
  token_diameter = token_diameter,
  padding_around_token = padding_around_token,
  thickness = support_base_thickness,
  color_indent = color_indent,
  peg_hole_diameter = peg_hole_diameter,
  peg_thickness_ratio = peg_thickness_ratio,
  magnet_width = magnet_width,
  magnet_height = magnet_height
  );

  // Red color indicator
  translate([50, 0, 0])
    color_indicator(
    token_diameter = token_diameter,
    thickness = color_indent,
    color = "red"
    );

  // Green color indicator
  translate([75, 0, 0])
    color_indicator(
    token_diameter = token_diameter,
    thickness = color_indent,
    color = "green"
    );

  peg_height = support_base_thickness * peg_thickness_ratio;

  // Support top
  translate([0, 30, 0])
    support_top(
    token_diameter = token_diameter,
    padding_around_token = padding_around_token,
    token_thickness = token_thickness,
    thickness = support_top_thickness,
    peg_diameter = peg_diameter,
    peg_thickness_ratio = peg_thickness_ratio,
    base_thickness = support_base_thickness
    );

  // Token
  translate([100, 0, 0])
    token(
    diameter = token_diameter,
    thickness = token_thickness,
    magnet_width = magnet_width,
    magnet_height = magnet_height,
    grab_diameter = token_grab_diameter,
    grab_indent = token_grab_indent
    );
}


module main() {
  magnet_width = 9.9;
  magnet_height = 6;

  padding_around_token = 3.2;

  token_diameter = 20;
  token_grab_diameter = token_diameter - padding_around_token * 2;
  token_grab_indent = 1;
  token_thickness = magnet_height + 1 + token_grab_indent;

  support_base_thickness = magnet_height + 1.5;
  support_top_thickness = 1;
  color_indent = .8;

  peg_diameter = 2;
  peg_hole_diameter = 2.2;
  peg_thickness_ratio = .5;

  //  everything_assembled(
  //  magnet_width = magnet_width,
  //  magnet_height = magnet_height,
  //  token_diameter = token_diameter,
  //  token_grab_diameter = token_grab_diameter,
  //  token_grab_indent = token_grab_indent,
  //  token_thickness = token_thickness,
  //  padding_around_token = padding_around_token,
  //  support_base_thickness = support_base_thickness,
  //  support_top_thickness = support_top_thickness,
  //  color_indent = color_indent,
  //  peg_diameter = peg_diameter,
  //  peg_hole_diameter = peg_hole_diameter,
  //  peg_thickness_ratio = peg_thickness_ratio
  //  );

  everything_exploded(
  magnet_width = magnet_width,
  magnet_height = magnet_height,
  token_diameter = token_diameter,
  token_grab_diameter = token_grab_diameter,
  token_grab_indent = token_grab_indent,
  token_thickness = token_thickness,
  padding_around_token = padding_around_token,
  support_base_thickness = support_base_thickness,
  support_top_thickness = support_top_thickness,
  color_indent = color_indent,
  peg_diameter = peg_diameter,
  peg_hole_diameter = peg_hole_diameter,
  peg_thickness_ratio = peg_thickness_ratio
  );

}

$fa = .1;
$fs = .1;
main();
