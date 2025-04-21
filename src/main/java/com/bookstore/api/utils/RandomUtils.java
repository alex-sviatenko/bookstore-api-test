package com.bookstore.api.utils;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.concurrent.ThreadLocalRandom;

public class RandomUtils {

    public static Integer getRandomNumber(int length) {
        Integer randomNumber = ThreadLocalRandom.current().nextInt(length + 1);

        return randomNumber;
    }

    public static String getRandomString(int length) {
        return RandomStringUtils.randomAlphabetic(length);
    }
}
