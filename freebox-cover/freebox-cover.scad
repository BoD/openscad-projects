use <wheel.scad>


fbx_height = 67;

thickness = 5;

// A bit shorter than the fbx, so it won't touch the floor.
front_height = fbx_height - 5;

front_bottom_width = 150;
front_top_width = 170;

top_front_width = front_top_width;
top_back_width = top_front_width - 10;
top_depth = 30;

logo_height = 30;

module front() {
  difference() {
    rounded_trapezoid(
      front_bottom_width, 
      front_top_width, 
      front_height, 
      thickness
    );
  
    translate([(front_top_width + front_bottom_width) / 4 - front_height / 2 , 0, 0])
      bevel();

    translate([-(front_top_width + front_bottom_width) / 4 + front_height / 2 , thickness, 0])
      rotate([0, 0, 180])
        bevel();        
   
    translate([0, 0, (front_height - logo_height) / 2])
      logo(height = logo_height, thickness = .33);
   }
}

module bevel() {
  difference() {
    cube([front_height / 2, thickness, front_height / 2]);

    translate([0, thickness / 2, front_height / 2])
      rotate([90, 90])
        wheel(radius = front_height / 2, thickness = thickness, angle = 90);
  }
}

module top() {
  translate([0, 0, front_height])
  rotate([-90, 0, 0])
  rounded_trapezoid(
    top_front_width, 
    top_back_width, 
    top_depth, 
    thickness
  );  
}

module rounded_trapezoid(
  bottom_width,
  top_width,
  height,
  thickness
) {
  radius = thickness / 2;
  hull() {
    // Bottom
    translate([-bottom_width / 2 + radius, radius, radius])
      sphere(radius);
    translate([bottom_width / 2 - radius, radius, radius])
      sphere(radius);

    // Top
    translate([-top_width / 2 + radius, radius, height - radius])
      sphere(radius);
    translate([top_width / 2 - radius, radius, height - radius])
      sphere(radius);
  }
}

module logo(height = 30, thickness = 1) {
  translate([0, thickness, height / 2])
    rotate([90, 0, 0])
      linear_extrude(thickness)
        resize([0, height, 0], true)
          import("lurez-full.svg", center = true);
}

$fa = .1;
$fs = .1;

union() {
  front();
  top();
}
