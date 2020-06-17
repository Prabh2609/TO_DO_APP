package com.saviour.todoapp.dbUtils;

import androidx.room.TypeConverter;

import java.util.Calendar;

public class Converter {
    @TypeConverter
    public static Calendar toCalendar(Long l) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(l);
        return calendar;
    }

    @TypeConverter
    public static Long fromCalendar(Calendar calendar) {
        return calendar == null ? null : calendar.getTime().getTime();
    }
}
