module window_blocker(
  x1,
  x2,
  x3,
  x4,

  y1,
  y2,
  y3,
  
  z1,
  z2,
) {
  cube([x1, y1 + y2, z1 + z2]);
  
  translate([x1, 0, z1])
    cube([x2, y1, z2]);

  translate([x1 + x2, 0, 0])
    cube([x3, y1 + y2, z1 + z2]);

  translate([x1 + x2 + x3, 0, 0])
    cube([x4, y1 + y2 - y3, z1 + z2]);
    
  // Bump
  translate([x1 + x2 + x3 + x4, (y1 + y2 - y3) / 2, 0])
    resize([5, 0, 0], [true, false, false])
      cylinder(h = z1 + z2, r = (y1 + y2 - y3) / 2);
}
