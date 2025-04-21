package com.bookstore.api.utils;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class UniqueIdGenerator {

    /**
     * Generates a new unique integer ID by finding the maximum existing ID in the list
     * and incrementing it by 1. Returns 1 if the list is empty or only contains null IDs.
     *
     * @param list        the list of items (e.g., DTOs)
     * @param idExtractor a lambda or method reference to extract the ID from each item
     * @param <T>         the type of items in the list
     * @return a new unique ID
     */
    public static <T> Integer createUniqueId(List<T> list, Function<T, Integer> idExtractor) {
        return list.stream()
                .map(idExtractor)
                .filter(Objects::nonNull)
                .max(Integer::compareTo)
                .orElse(0) + 1;
    }
}
