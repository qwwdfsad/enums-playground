package org.jetbrains;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum TestEnum {
    A, B, C, D, E;

    public static final TestEnum[] VALUES_TO_CAPTURE = values();

    public static List<TestEnum> buildLazyList() {
        return new EnumEntriesList<>(() -> VALUES_TO_CAPTURE);
    }

    public static List<TestEnum> buildVolatileList() {
        return new EnumEntriesVolatileList<>(() -> VALUES_TO_CAPTURE);
    }
}
