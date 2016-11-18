package fr.univ_tln.trailscommunity.utilities.validators;

import java.util.regex.Pattern;

/**
 * Project TrailsCommunity.
 * Package fr.univ_tln.trailscommunity.utilities.validators.
 * File EmailValidator.java.
 * Created by Ysee on 18/11/2016 - 21:30.
 * www.yseemonnier.com
 * https://github.com/YMonnier
 */

public  class EmailValidator {
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    /**
     * Validate email with regular expression
     *
     * @param email email for validation
     * @return true valid email, otherwise, false
     */
    public static boolean validate(final String email) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
         return pattern.matcher(email).matches();
    }
}
