package com.bookstore.api.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import static java.time.ZoneOffset.UTC;
import static java.time.format.DateTimeFormatter.ofPattern;

public class DateTimeUtils {

    /**
     * Formatter for ISO 8601 date-time with milliseconds and timezone offset.
     * Example output: "2025-04-30T17:26:52.912Z"
     * Pattern: "yyyy-MM-dd'T'HH:mm:ss.SSSX"
     */
    public static final DateTimeFormatter DATE_TIME_SSS_FORMATTER = ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");

    /**
     * Returns the current date and time as a string, formatted using the DATE_TIME_SSS_FORMATTER.
     *
     * @return Current date-time in ISO 8601 format with milliseconds and 'Z' for UTC offset (e.g., "2025-04-30T17:26:52.912Z")
     */
    public static String getDateTimeNow() {
        OffsetDateTime now = OffsetDateTime.now();

        return DATE_TIME_SSS_FORMATTER.format(now);
    }

    public static LocalDateTime parseDateTime(String dateTimeString, DateTimeFormatter dateTimeFormatter) {
        OffsetDateTime offsetDateTime = OffsetDateTime.parse(dateTimeString, dateTimeFormatter);

        return offsetDateTime.toLocalDateTime();
    }

    public static LocalDate getDateNow() {
        return LocalDate.now(UTC);
    }

    public static LocalDate getFutureDate(Integer daysToAdd) {
        return LocalDate.now(UTC).plusDays(daysToAdd);
    }

    public static LocalDate getPastDate(Integer minusDays) {
        return LocalDate.now(UTC).minusDays(minusDays);
    }
}
