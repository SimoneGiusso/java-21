package com.simonegiusso.java21.recordpatterns;

import org.junit.jupiter.api.Test;

public class RecordPattern3FailingTests {

    Pair p = new Pair(42, 42);

    // Nested patterns can, of course, fail to match:
    record Pair(Object x, Object y) {}

    @Test
    void testFailCast() {
        if (p instanceof Pair(String s, String t)) {
            System.out.println(s + ", " + t);
        } else {
            System.out.println("Not a pair of strings");
        }
    }

    @Test
    void testFailCast2() {
        if (p instanceof Pair(var s, var t)) {
            System.out.println(s + ", " + t);
        } else {
            System.out.println("Not a pair of strings");
        }
    }

}