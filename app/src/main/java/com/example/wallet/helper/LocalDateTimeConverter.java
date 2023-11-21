package com.example.wallet.helper;

import androidx.room.TypeConverter;

import java.time.LocalDateTime;

public class LocalDateTimeConverter {
    @TypeConverter
    public static String timeToString(LocalDateTime createdAt) {
        return createdAt == null ? null : createdAt.toString();
    }

    @TypeConverter
    public static LocalDateTime stringToTime(String createdAt) {
        return createdAt == null ? null : LocalDateTime.parse(createdAt);
    }
}
