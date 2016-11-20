package fr.univ_tln.trailscommunity.root;

import junit.framework.Assert;

import org.junit.Test;

import fr.univ_tln.trailscommunity.utilities.validators.EmailValidator;

import static junit.framework.TestCase.assertTrue;

/**
 * Project TrailsCommunity.
 * Package fr.univ_tln.trailscommunity.root.
 * File EmailValidationTest.java.
 * Created by Ysee on 20/11/2016 - 01:00.
 * www.yseemonnier.com
 * https://github.com/YMonnier
 * <p>
 * Email validator tests
 * Test all validation possibilities email.
 */
public class EmailValidationTest {

    String[] validEmails = new String[]{
            "john@mail.com",
            "john-400@mail.com", "john.400@mail.com",
            "john441@john.com", "john-100@john.net",
            "john.600@john.com.au", "john@2.com",
            "john@gmail.com.com", "john+100@gmail.com",
            "john-100@mail-abcd.com", "john-100@mail-abcd.fr"

    };

    String[] invalidEmails = new String[]{
            "paul", "paul@.net.my",
            "paul123@gmail.a", "paul123@.net", "paul123@.net.net",
            ".paul@paul.net", "paul()*@gmail.net", "paul@%*.net",
            "paul..2002@gmail.net", "paul.@gmail.net",
            "paul@paul@gmail.net", "paul@gmail.net.1a",
            "23ferf.é§è!çà", "$^``ezg/.ddz", "java..@...com"

    };

    /**
     * Test all valid emails.
     */
    @Test
    public void validEmailTest() {
        validTest(validEmails, true);
    }

    /**
     * Test all invalid emails.
     */
    @Test
    public void invalidEmailTest() {
        validTest(invalidEmails, false);
    }


    /**
     * Test array of email
     *
     * @param emails array of emails
     * @param status assert status. True if we want
     *               to test the successful validation, otherwise, false.
     */
    public void validTest(String[] emails, boolean status) {

        for (String email : emails) {
            boolean valid = EmailValidator.validate(email);
            Assert.assertEquals(valid, status);
        }

    }
}
