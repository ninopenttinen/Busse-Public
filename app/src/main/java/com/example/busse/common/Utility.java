package com.example.busse.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utility {
    public static int timeDifferenceInMinutes(Date earlierDate, Date laterDate) {
        if( earlierDate == null || laterDate == null )
            return 0;

        return (int)((laterDate.getTime()/60000) - (earlierDate.getTime()/60000));
    }

    public static Date formatDateTime(String dateTime, String inputPattern) {
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        Date outputDateTime = new Date();

        try {
            outputDateTime = inputFormat.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return outputDateTime;
    }

    public static String parseDateTimeForDisplay(String dateTime, String inputPattern) {
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat("HH.mm");
        String outputString = "";

        try {
            outputString = outputFormat.format(inputFormat.parse(dateTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return outputString;
    }
}
