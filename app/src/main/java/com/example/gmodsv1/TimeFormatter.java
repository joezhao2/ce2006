package com.example.gmodsv1;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.Instant;
import java.util.Date;

public class TimeFormatter {
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getStringTimeDelta(Instant timeInstant1, Instant timeInstant2) {
        Date time1 = Date.from(timeInstant1.from(timeInstant1));
        Date time2 = Date.from(timeInstant2.from(timeInstant2));
        long yearDelta = time2.getYear() - time1.getYear();
        if(yearDelta > 0) {
            if(yearDelta < 1.5) {
                return "1 year ago";
            }
            else return Integer.toString((int)Math.round(yearDelta)) + " years ago";
        }
        long monthDelta = time2.getMonth() - time1.getMonth();
        if(monthDelta > 0) {
            if(monthDelta < 1.5) {
                return "1 month ago";
            }
            else return Integer.toString((int)Math.round(monthDelta)) + " months ago";
        }
        long dayDelta = time2.getDay() - time1.getDay();
        if(dayDelta > 0) {
            if(dayDelta < 1.5) {
                return "1 day ago";
            }
            else return Integer.toString((int)Math.round(dayDelta)) + " days ago";
        }
        long hourDelta = time2.getHours() - time1.getHours();
        if(hourDelta > 0) {
            if(hourDelta < 1.5) {
                return "1 hour ago";
            }
            else return Integer.toString((int)Math.round(hourDelta)) + " hours ago";
        }
        long minuteDelta = time2.getMinutes() - time1.getMinutes();
        if(minuteDelta > 0) {
            if(minuteDelta < 1.5) {
                return "1 minute ago";
            }
            else return Integer.toString((int)Math.round(minuteDelta)) + " minutes ago";
        }
        long secondDelta = time2.getSeconds() - time1.getSeconds();
        if(secondDelta > 0) {
            if(secondDelta < 1.5) {
                return "1 second ago";
            }
            else return Integer.toString((int)Math.round(secondDelta)) + " seconds ago";
        }
        return "a few seconds ago";
    }

}
