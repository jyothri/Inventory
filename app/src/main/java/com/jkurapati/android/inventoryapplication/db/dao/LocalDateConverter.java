package com.jkurapati.android.inventoryapplication.db.dao;

import androidx.room.TypeConverter;

import java.time.LocalDate;

public class LocalDateConverter {

    @TypeConverter
    static LocalDate toDate(String dateString) {
        if (dateString == null) {
            return null;
        } else {
            return LocalDate.parse(dateString);
        }
    }

    @TypeConverter
    static String toDateString(LocalDate date) {
        if (date == null) {
            return null;
        } else {
            return date.toString();
        }
    }
}
