package com.bluelab.util;

import java.sql.Date;
import java.time.LocalDate;

public class DateUtil {
    public static Date toDate(LocalDate date) {
        return Date.valueOf(date);
    }
    
    public static LocalDate toLocalDate(Date date) {
        return date.toLocalDate();
    }
    
    public static Date dateNow() {
        return Date.valueOf(LocalDate.now());
    }
    
    public static LocalDate localDateNow() {
        return LocalDate.now();
    }
}
