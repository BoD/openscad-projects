module half(
  width,
  height,
) {
  polygon(points = [
      [width / 2, height - 10],
      [0, height],
      [0, 10],
      [width / 2, 0],
    ]);
}

module main() {
  width = 20;
  height = 60;
  thickness = 2;

  difference() {

    linear_extrude(h = thickness) {
      half(
        width = width,
        height = height,
      );
      mirror([1, 0, 0])
        half(
          width = width,
          height = height,
        );
    }

    translate([0, 21, 0])
      cylinder(d = 15, h = thickness);

    translate([0, 40, 0])
      cylinder(d = 15, h = thickness);

  };
}

main();
