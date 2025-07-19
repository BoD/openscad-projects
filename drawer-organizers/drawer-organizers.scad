module stand_horizontal(
  stand_thickness,
  stand_width,
  wall_height,
) {
  cube([stand_width, stand_thickness, wall_height]);
}

module stand_vertical(
  stand_thickness,
  stand_width,
  wall_height,
) {
  cube([stand_thickness, stand_width, wall_height]);
}

module left_part(
  stand_thickness,
  stand_width,
  wall_thickness,
  wall_height,
) {

  // Horizontal wall
  horizontal_wall_length = 200;
  translate([0, -stand_width / 2 + wall_thickness / 2, 0])
    stand_vertical(
      stand_thickness = stand_thickness,
      stand_width = stand_width,
      wall_height = wall_height,
    );
  cube([horizontal_wall_length, wall_thickness, wall_height]);
  translate([horizontal_wall_length - stand_thickness, -stand_width / 2 + wall_thickness / 2, 0])
    stand_vertical(
      stand_thickness = stand_thickness,
      stand_width = stand_width,
      wall_height = wall_height,
    );

  // Vertical wall
  vertical_wall_length = 250;
  vertical_wall_offset = 180;
  translate([vertical_wall_offset, -vertical_wall_length, 0])
    cube([wall_thickness, vertical_wall_length, wall_height]);

  translate([vertical_wall_offset - stand_width / 2 + wall_thickness / 2, -vertical_wall_length - wall_thickness, 0])
    stand_horizontal(
      stand_thickness = stand_thickness,
      stand_width = stand_width,
      wall_height = wall_height,
    );

}


module main() {
  stand_thickness = 2;
  stand_width = 10;

  wall_thickness = 5;
  wall_height = 150;

  left_part(
    stand_thickness = stand_thickness,
    stand_width = stand_width,
    wall_thickness = wall_thickness,
    wall_height = wall_height,
  );
}

$fa = .1;
$fs = .1;
main();
