/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.util;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author qiuyukun
 *  a useful class to compare the date.
 */
public class DateOperator {
    public static boolean isBeforeDate(Date d1, Date d2){
        LocalDate ld1 = ZonedDateTime.ofInstant(d1.toInstant(), ZoneId.systemDefault()).toLocalDate();
        LocalDate ld2 = ZonedDateTime.ofInstant(d2.toInstant(), ZoneId.systemDefault()).toLocalDate();
        return ld1.isBefore(ld2);
    }
    public static boolean isAfterDate(Date d1, Date d2){
        LocalDate ld1 = ZonedDateTime.ofInstant(d1.toInstant(), ZoneId.systemDefault()).toLocalDate();
        LocalDate ld2 = ZonedDateTime.ofInstant(d2.toInstant(), ZoneId.systemDefault()).toLocalDate();
        return ld1.isAfter(ld2);
    }
    public static boolean isSameDate(Date d1, Date d2){
        LocalDate ld1 = ZonedDateTime.ofInstant(d1.toInstant(), ZoneId.systemDefault()).toLocalDate();
        LocalDate ld2 = ZonedDateTime.ofInstant(d2.toInstant(), ZoneId.systemDefault()).toLocalDate();
        return ld1.isEqual(ld2);
    }
    public static Date addDay(Date d1,int day){
        Calendar c = Calendar.getInstance();
        c.setTime(d1);
        c.add(Calendar.DATE, day);
        return c.getTime();
    }
}
