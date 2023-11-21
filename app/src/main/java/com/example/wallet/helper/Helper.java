package com.example.wallet.helper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Helper {
    public static String formatDate(LocalDateTime dateTime) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return dateTime.format(dateFormatter);
    }
    public static String formatTime(LocalDateTime dateTime) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        return dateTime.format(timeFormatter);
    }

    public static String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");
        return dateTime.format(dateTimeFormatter);
    }
}
