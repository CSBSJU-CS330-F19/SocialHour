package com.example.services;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.TimeZone;

public class TimeConverter {

    public static String Convert(String month, String day, String year, String hours, String minutes)
    {
//        2011-12-03T10:15:30
        TimeZone timeZone = TimeZone.getDefault();
        DateTimeFormatter inputFormatter =
                DateTimeFormatter.ofPattern("uuuu-MM-d HH:mm:ss");  // Specify locale to determine human language and cultural norms used in translating that input string.
        LocalDateTime ldt = LocalDateTime.parse(year + "-" + month + "-" +  day + " " +
                hours + ":" + minutes + ":00", inputFormatter );
        ZonedDateTime zonedDateTime = ldt.atZone(ZoneId.of(timeZone.getID()));

        //YYYY-MM-DDTHH:mm:ssZ).
        DateTimeFormatter outputFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        return zonedDateTime.format(outputFormatter) + zonedDateTime.getOffset();

//        return zonedDateTime.getYear() + "-" + zonedDateTime.getMonthValue() + "-" +
//                zonedDateTime.getDayOfMonth() + "T" + zonedDateTime.getHour() + ":" +
//                zonedDateTime.getMinute() + ":" + zonedDateTime.getSecond() +
//                zonedDateTime.getOffset();
    }
}
