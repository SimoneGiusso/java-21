package com.simonegiusso.java21.recordpatterns;

public class RecordPattern2ScalingTests {

    enum Color {RED, GREEN, BLUE}

    // This can scale:
    record Point(int x, int y) {}

    record ColoredPoint(Point p, Color c) {}

    record Rectangle(ColoredPoint upperLeft, ColoredPoint lowerRight) {}

    // As of Java 21
    static void printXCoordOfUpperLeftPointWithPatterns(Rectangle r) {
        if (r instanceof Rectangle(
            ColoredPoint(Point(var x, var y), var c),
            var lr
        )) {
            System.out.println("Upper-left corner: " + x);
            System.out.println("Upper-right corner: " + lr.p.x); // NOTE: using var infers cast to ColoredPoint

        }
    }
}