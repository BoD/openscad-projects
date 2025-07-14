module base(
    width,
    height,
    thickness,
) {
  hull() {
    translate([height / 2, height / 2, 0])
      cylinder(h = thickness, r = height / 2);
    translate([width - height / 2, height / 2, 0])
      cylinder(h = thickness, r = height / 2);
  }
}

module hook(
  radius,
  height,
  angle,
) {
  difference() {
    // Height of the elliptical cross-section of a cylinder: 2r cos(a)
    translate([0, -2 * radius * cos(angle), 0])

    rotate([-90 + angle, 0, 0])
      translate([0, radius, 0])
        cylinder(h = height, r = radius);

    translate([-radius, -height / 2, -radius * 2])
      cube([radius * 2, height, radius * 2]);
  }
}

module main() {
  base_width = 30;
  base_height = 24;
  base_thickness = 1;

  hook_radius = 1;
  hook_height = 21;
  hook_angle = 30;
  
  
  base(
    width = base_width,
    height = base_height,
    thickness = base_thickness,
  );
  
  
  translate([base_width / 4, base_height / 4, base_thickness])
    hook(
      radius = hook_radius,
      height = hook_height,
      angle = hook_angle,
    );
    
  translate([base_width / 4 * 3, base_height / 4, base_thickness])
    hook(
      radius = hook_radius,
      height = hook_height,
      angle = hook_angle,
    );

}

$fa = .1;
$fs = .1;
main();
