package com.simonegiusso.java21.recordpatterns;

import org.junit.jupiter.api.Test;

public class RecordPattern5ExhaustiveSwitchTests {

    Pair<A> p1;
    Pair<I> p2;

    sealed interface I permits C, D {}

    record Pair<T>(T x, T y) {}

    static class A {}

    static class B extends A {}

    static final class C implements I {}

    static final class D implements I {}


    // The switch block must have clauses that deal with all possible values of the selector expression.
    @Test
    void exhaustivenessWithClassInheritance() {
        p1 = new Pair<>(new B(), new B());

        /*
        switch (p1) {                 // Error! Do you know why?
            case Pair<A>(A a, B b) -> System.out.println("AB");
            case Pair<A>(B b, A a) -> System.out.println("BA");
        }
        */

        // This covers all possible combinations. Could we remove some cases or not?
        switch (p1) {
            case Pair<A>(B b1, B b2) -> System.out.println("BB");
            case Pair<A>(A a, B b) -> System.out.println("AB");
            case Pair<A>(B b, A a) -> System.out.println("BA");
            case Pair<A>(A b, A a) -> System.out.println("AA");
        }

        // Order is important:
        switch (p1) {
            case Pair<A>(A a, B b) -> System.out.println("AB");
            case Pair<A>(B b, A a) -> System.out.println("BA");
            case Pair<A>(A b, A a) -> System.out.println("AA");
        }

        switch (p1) {
            case Pair<A>(B b, A a) -> System.out.println("BA");
            case Pair<A>(A a, B b) -> System.out.println("AB");
            case Pair<A>(A b, A a) -> System.out.println("AA");
        }
    }

    @Test
    void exhaustivenessWithInterfaceImplementation() {
        // This covers all possible combinations. Could we remove some cases or not?
        switch (p2) {
            case Pair<I>(C c1, C c2) -> System.out.println("CC");
            case Pair<I>(D d, C c) -> System.out.println("DC");
            case Pair<I>(D d1, D d2) -> System.out.println("DD");
            case Pair<I>(C c, D d) -> System.out.println("CD");
        }

        switch (p2) {
            case Pair<I>(C c, I i) -> System.out.println("CC or CD");
            case Pair<I>(D d, C c) -> System.out.println("DC");
            case Pair<I>(D d1, D d2) -> System.out.println("DD");
        }

        switch (p2) {
            case Pair<I>(I i, C c) -> System.out.println("CC or DC");
            case Pair<I>(I i, D d) -> System.out.println("DD or CD");
            case Pair<I>(I i1, I i2) -> System.out.println("II"); // This is a "dead" case! Do you know why?
        }
    }

}