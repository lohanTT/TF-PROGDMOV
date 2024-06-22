package com.example.albumap.database;

import androidx.room.TypeConverter;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;


public class Converters {

    private static DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @TypeConverter
    public static LocalDateTime fromTimestamp(String value) {
        return value == null ? null : LocalDateTime.parse(value, formatter);
    }

    @TypeConverter
    public static String dateToTimestamp(LocalDateTime date) {
        return date == null ? null : date.format(formatter);
    }
}