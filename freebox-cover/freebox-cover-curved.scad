use <cylinder_extrude.scad>

module debug_enclosing() {
  color("blue")
    translate([0, 0, front_height / 2])
      cube([front_bottom_width, thickness, front_height], center = true);

  color("green")
    translate([-front_bottom_width / 2 - thickness / 2, 0, 0])
      cube([thickness, 50, front_height / 2], center = true);
  color("green")
    translate([front_bottom_width / 2 + thickness / 2, 0, 0])
      cube([thickness, 50, front_height / 2], center = true);
}

module debug_curve_center() {
  color("red")
    translate([0, curve_radius, 0])
      sphere(thickness / 2);
}

module front_plate_line(
  thickness,
  front_height,
  front_bottom_width,
  front_top_width,
  top_back_width,
  top_depth,
  curve_radius,
  curve_angle,
  step,
  step_width,
  i, 
) {
  round_bevel_fraction = .2;

  corner_radius = front_bottom_width * round_bevel_fraction;
  front_top_bottom_ratio = front_top_width / front_bottom_width;

  // Bottom
  angle_bottom = (curve_angle / step) * i - curve_angle / 2;
  z =
      i * step_width < corner_radius ?
        corner_radius - sqrt(corner_radius ^ 2 - (corner_radius - i * step_width)^ 2)
      :
      i * step_width > front_bottom_width - corner_radius ?
        corner_radius - sqrt(corner_radius ^ 2 - (corner_radius - (step - i) * step_width)^ 2)
      :
        0;
  translate([
      curve_radius * sin(angle_bottom),
      curve_radius * cos(angle_bottom),
      z + thickness / 2,
    ])
    sphere(thickness / 2);

  // Top
  angle_top = (curve_angle * front_top_bottom_ratio / step) * i - curve_angle * front_top_bottom_ratio / 2;
  translate([
      curve_radius * sin(angle_top),
      curve_radius * cos(angle_top),
      front_height - thickness / 2,
    ])
    sphere(thickness / 2);
}

module front_plate(
  thickness,
  front_height,
  front_bottom_width,
  front_top_width,
  top_back_width,
  top_depth,
  curve_radius,
  curve_angle,
  step,
) {
  step_width = front_bottom_width / step;
  translate([0, curve_radius + thickness / 2, 0])
    rotate([0, 0, 180])
      for (i = [0 : step - 1]) {
        hull() {
          front_plate_line(
            thickness = thickness,
            front_height = front_height,
            front_bottom_width = front_bottom_width,
            front_top_width = front_top_width,
            top_back_width = top_back_width,
            top_depth = top_depth,
            curve_radius = curve_radius,
            curve_angle = curve_angle,
            step = step,
            step_width = step_width,
            i = i,
          );
          front_plate_line(
            thickness = thickness,
            front_height = front_height,
            front_bottom_width = front_bottom_width,
            front_top_width = front_top_width,
            top_back_width = top_back_width,
            top_depth = top_depth,
            curve_radius = curve_radius,
            curve_angle = curve_angle,
            step = step,
            step_width = step_width,
            i = i + 1,
          );
        };
      }
}

module logo(height, thickness, curve_radius) {
  translate([0, curve_radius + thickness, height / 2])
    rotate([0, 0, -90])
      cylinder_extrude(curve_radius, thickness * 2, height)
        resize([0, height, 0], true)
          import("lurez-full.svg", center = true);
}

module front(
  thickness,
  front_height,
  front_bottom_width,
  front_top_width,
  logo_height,
  logo_thickness,
  top_back_width,
  top_depth,
  curve_radius,
  curve_angle,
  step,
) {
  difference() {
    front_plate(
      thickness = thickness,
      front_height = front_height,
      front_bottom_width = front_bottom_width,
      front_top_width = front_top_width,
      top_back_width = top_back_width,
      top_depth = top_depth,
      curve_radius = curve_radius,
      curve_angle = curve_angle,
      step = step,
    );
    translate([0, 0, (front_height - logo_height) / 2])
      logo(height = logo_height, thickness = 1, curve_radius = curve_radius);
  };
}

module top(
  thickness,
  front_height,
  front_bottom_width,
  front_top_width,
  top_back_width,
  top_depth,
  curve_radius,
  curve_angle,
  step,
) {
  front_top_bottom_ratio = front_top_width / front_bottom_width;
  step_width = front_bottom_width / step;
  hull() {
    translate([0, curve_radius + thickness / 2, 0])
      rotate([0, 0, 180])
        for (i = [0 : step]) {
          angle_top = (curve_angle * front_top_bottom_ratio / step) * i - curve_angle * front_top_bottom_ratio / 2;
          translate([
            curve_radius * sin(angle_top),
            curve_radius * cos(angle_top),
            front_height - thickness / 2,
          ])
            sphere(thickness / 2);
        }

    translate([
      -top_back_width / 2,
      top_depth,
      front_height - thickness / 2,
    ])
      sphere(thickness / 2);

    translate([
      top_back_width / 2,
      top_depth,
      front_height - thickness / 2,
    ])
      sphere(thickness / 2);
  };
}

module freebox_cover() {
  fbx_height = 67;

  thickness = 5;

  // A bit shorter than the fbx, so it won't touch the floor.
  front_height = fbx_height - 5;
  front_bottom_width = 150;
  front_top_width = 170;

  top_back_width = front_top_width - 10;
  top_depth = 40;

  logo_height = 30;
  logo_thickness = 1;
  
  curve_radius = 600;
  curve_angle = atan((front_bottom_width / 2) / sqrt(curve_radius ^ 2 - (front_bottom_width / 2) ^ 2)) * 2;

  step = 100;

  front(
    thickness = thickness,
    front_height = front_height,
    front_bottom_width = front_bottom_width,
    front_top_width = front_top_width,
    top_back_width = top_back_width,
    top_depth = top_depth,
    logo_height = logo_height,
    logo_thickness = logo_thickness,
    curve_radius = curve_radius,
    curve_angle = curve_angle,
    step = step,
  );

  top(
    thickness = thickness,
    front_height = front_height,
    front_bottom_width = front_bottom_width,
    front_top_width = front_top_width,
    top_back_width = top_back_width,
    top_depth = top_depth,
    curve_radius = curve_radius,
    curve_angle = curve_angle,
    step = step,
  );
}

$fa = .1;
$fs = .1;
freebox_cover();
