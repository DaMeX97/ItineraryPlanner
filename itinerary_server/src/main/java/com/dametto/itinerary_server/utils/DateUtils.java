package com.dametto.itinerary_server.utils;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateUtils {
    /**
     * Get a diff between two dates
     * @param start the start date
     * @param end the end date
     * @param timeUnit the unit in which you want the diff
     * @return the diff value, in the provided unit
     */
    public static Long getDateDiff(Date start, Date end, TimeUnit timeUnit) {
        long diffInMillies = end.getTime() - start.getTime();
        return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
    }
}
