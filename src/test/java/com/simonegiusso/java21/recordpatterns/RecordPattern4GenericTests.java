package com.simonegiusso.java21.recordpatterns;

public class RecordPattern4GenericTests {

    record Box<T>(T t) {}

    static void test2(Box<Box<String>> bbs) {
        if (bbs instanceof Box<Box<String>>(Box<String>(var s))) {
            System.out.println("String " + s);
        }

        // The above can be re-written as
        if (bbs instanceof Box(Box(var s))) {
            System.out.println("String " + s);
        }
    }

}