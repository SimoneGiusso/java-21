package com.simonegiusso.java21.recordpatterns;

public class RecordPattern1Tests {

    record Point(int x, int y) {} // x and y are the components and x() and y() the accessors

    // Record pattern
    static void printSum(Object obj) {
        if (obj instanceof Point(int x, int y)) {
            System.out.println(x + y);
        }
    }

    // Instead of
    static void printSumOld(Object obj) {
        if (obj instanceof Point p) {
            int x = p.x();
            int y = p.y();
            System.out.println(x + y);
        }
    }
}