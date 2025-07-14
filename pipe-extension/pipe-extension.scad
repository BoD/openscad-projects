module pipe_extension(inner_diameter, length, thickness) {
  difference() {
    // Outer tube
    cylinder(h = length, r = inner_diameter / 2 + thickness);

    // Inner tube
    cylinder(h = length, r = inner_diameter / 2);

    // Slant
    cube_side = length;
    translate([-cube_side / sqrt(2) + inner_diameter / 2 + thickness, 0, cube_side / sqrt(2) + (length - cube_side / sqrt(2))])
      rotate([0, 45, 0])
        cube(cube_side, center = true);

    // Slit
    slit_width = thickness * 5;
    translate([-inner_diameter, -slit_width / 2, 0])
      cube([inner_diameter, slit_width, length]);    
  };
}

module main() {
  inner_diameter = 37;
  thickness = 2;
  length = 200;

  pipe_extension(
    inner_diameter = inner_diameter, 
    length = length,
    thickness = thickness,
  );
}

$fa = .1;
$fs = .1;
main();
