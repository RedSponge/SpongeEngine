package com.redsponge.sponge.util;

import text.formic.Stringf;

import java.util.Date;

public class DateFormatter {
    private static final String[] days = {"sun", "mon", "tue", "wed", "thu", "fri", "sat"};
    private static final String[] months = {"jan", "feb", "mar", "Apr", "may", "jun", "jul", "aug", "sep", "oct", "nov", "dec"};

    private static <T> int find(T[] arr, T val) {
        for (int i = 0; i < arr.length; i++) {
            if(arr[i].equals(val)) return i;
        }
        return -1;
    }

    public static String formatDate(Date date, String format) {
        String[] parts = date.toString().split(" ");
        String dow = Stringf.format("%02d", 1 + find(days, parts[0].toLowerCase()));//Integer.toString(find(days, parts[0]));
        String month = Stringf.format("%02d", 1 + find(months, parts[1].toLowerCase()));
        String dom = parts[2];
        String[] timeParts = parts[3].split(":");
        String hour = timeParts[0];
        String minute = timeParts[1];
        String second = timeParts[2];
        String year = parts[5];

        return format.replace("yyyy", year).replace("yy", year.substring(2)).replace("dd", dom).replace("mm", minute).replace("ss", second).replace("MM", month).replace("dd", dom).replace("HH", hour).replace("EEE", parts[0]);
    }
}
