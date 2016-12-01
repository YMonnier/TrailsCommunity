package fr.univ_tln.trailscommunity.utilities.validators;

import java.util.Calendar;

/**
 * Project TrailsCommunity.
 * Package fr.univ_tln.trailscommunity.utilities.validators.
 * File DateValidator.java.
 * Created by Ysee on 26/11/2016 - 15:54.
 * www.yseemonnier.com
 * https://github.com/YMonnier
 */

public class DateValidator {
    /**
     * Test if the calendar date is not a past date.
     * @param calendar calendar for validation.
     * @return true if time calendar in millisecond is
     * greater than current system time.
     */
    public static boolean validate(Calendar calendar) {
        Calendar currentCalendar = Calendar.getInstance();
        if (currentCalendar == null)
            return false;
        currentCalendar.setTimeInMillis(System.currentTimeMillis());
        return calendar.get(Calendar.YEAR) >= currentCalendar.get(Calendar.YEAR)
                && calendar.get(Calendar.MONTH) >= currentCalendar.get(Calendar.MONTH)
                && calendar.get(Calendar.DATE) >= currentCalendar.get(Calendar.DATE);
    }
}
