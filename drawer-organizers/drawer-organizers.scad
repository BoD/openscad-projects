module stand_horizontal(
  thickness,
  length,
  height,
) {
  cube([length, thickness, height]);
}

module stand_vertical(
  thickness,
  length,
  height,
) {
  cube([thickness, length, height]);
}

module wall_horizontal(
  thickness,
  length,
  height,
) {
  hole_diameter = 10;
  distance_between_holes = 2;
  difference() {
    cube([length, thickness, height]);
    hole_count_x = length / (hole_diameter + distance_between_holes);
    remainder_x = length % (hole_diameter + distance_between_holes);
    hole_count_z = height / (hole_diameter + distance_between_holes);
    remainder_z = height % (hole_diameter + distance_between_holes);
    for (z = [0 : hole_count_z - 1]) {
      is_even = z % 2 == 0;
      max_x = is_even ? hole_count_x - 1 : hole_count_x - 2;
      for (x = [0 : max_x]) {
        shift_x = is_even ? 0 : (hole_diameter + distance_between_holes) / 2;
        translate(
            [
                    x * (hole_diameter + distance_between_holes) + hole_diameter / 2 + remainder_x / 2 + shift_x,
                thickness / 2 + hole_diameter / 2,
                  z * (hole_diameter + distance_between_holes) + hole_diameter / 2 + remainder_z / 2,
            ]
        )
          rotate([90, 0, 0])
            cylinder(d = hole_diameter, h = thickness * 2);
      }
    }
  };
}

module wall_vertical(
  thickness,
  length,
  height,
) {
  translate([thickness, 0, 0])
    rotate([0, 0, 90])
      wall_horizontal(
        thickness = thickness,
        length = length,
        height = height,
      );
}

module left_part(
  stand_thickness,
  stand_length,
  wall_thickness,
  wall_height,
) {
  // Horizontal wall
  horizontal_wall_length = 187;
  translate([0, -stand_length / 2 + wall_thickness / 2, 0])
    stand_vertical(
      thickness = stand_thickness,
      length = stand_length,
      height = wall_height,
    );
  wall_horizontal(
    thickness = wall_thickness,
    length = horizontal_wall_length,
    height = wall_height,
  );
  translate([horizontal_wall_length - stand_thickness, -stand_length / 2 + wall_thickness / 2, 0])
    stand_vertical(
      thickness = stand_thickness,
      length = stand_length,
      height = wall_height,
    );

  // Vertical wall
  vertical_wall_length = 260;
  vertical_wall_offset = 160;
  translate([vertical_wall_offset, -vertical_wall_length + wall_thickness, 0])
    wall_vertical(
      thickness = wall_thickness,
      length = vertical_wall_length,
      height = wall_height,
    );
  translate([vertical_wall_offset - stand_length / 2 + wall_thickness / 2, -vertical_wall_length + wall_thickness, 0])
    stand_horizontal(
      thickness = stand_thickness,
      length = stand_length,
      height = wall_height,
    );

}


module main() {
  stand_thickness = 2;
  stand_length = 10;

  wall_thickness = 4;
  wall_height = 70;

  left_part(
    stand_thickness = stand_thickness,
    stand_length = stand_length,
    wall_thickness = wall_thickness,
    wall_height = wall_height,
  );
}

$fa = .1;
$fs = .1;
main();
