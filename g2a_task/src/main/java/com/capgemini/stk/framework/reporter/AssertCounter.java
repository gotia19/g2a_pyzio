package com.capgemini.stk.framework.reporter;

public final class AssertCounter {
    private static Integer counterGlobal = 0;

    private AssertCounter() {
    }

    public static Integer getNumberOfGlobalAsserts() {
        return counterGlobal;
    }

    public static void assertCounterAdder() {
        counterGlobal += 1;
    }
}
