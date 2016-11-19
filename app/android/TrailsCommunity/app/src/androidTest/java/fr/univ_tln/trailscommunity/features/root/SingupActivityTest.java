package fr.univ_tln.trailscommunity.features.root;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.widget.EditText;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.internal.matchers.TypeSafeMatcher;
import org.junit.runner.RunWith;

import fr.univ_tln.trailscommunity.R;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.core.deps.guava.net.HttpHeaders.TE;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;

/**
 * Created by Stéphen on 19/11/2016.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SingupActivityTest {

    private static final String VALID_EMAIL = "test@gmail.com";
    private static final String VALID_NICKNAME = "Nickname";
    private static final String VALID_PASSWORD = "password";
    private static final String NOT_VALID_PASSWORD = "motdepasse";
    private static final String CODE_NUMBER_COUNTRY = "+213";
    private static final String VALID_NUMBER = "0600000000";
    private static final String TEST_VALUE = "test";
    private static final String EMPTY_VALUE = "";

    @Rule
    public ActivityTestRule<SignupActivity_> mActivityRule = new ActivityTestRule<>(
            SignupActivity_.class);

    /**
     * Test all fields of the view
     */
    @Test
    public void testLoginButton(){
        onView(withId(R.id.emailField))
                .perform(typeText(VALID_EMAIL));
        onView(withId(R.id.nicknameField))
                .perform(typeText(VALID_NICKNAME));
        onView(withId(R.id.passwordField))
                .perform(typeText(VALID_PASSWORD));
        onView(withId(R.id.confirmPasswordField))
                .perform(typeText(VALID_PASSWORD));
        onView(withId(R.id.codeNumberCountrySpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(CODE_NUMBER_COUNTRY))).perform(click());
        onView(withId(R.id.codeNumberCountrySpinner)).check(matches(withSpinnerText(containsString(CODE_NUMBER_COUNTRY))));
        onView(withId(R.id.numberField))
                .perform(typeText(VALID_NUMBER));
        onView(withId(R.id.registerButton))
                .perform(click());
    }

    /**
     * Test if error "field requiered" display on emailField error
     */
    @Test
    public void testErrorEmptyEmailField(){
        onView(withId(R.id.emailField))
                .perform(typeText(EMPTY_VALUE));
        onView(withId(R.id.nicknameField))
                .perform(typeText(VALID_NICKNAME));
        onView(withId(R.id.passwordField))
                .perform(typeText(VALID_PASSWORD));
        onView(withId(R.id.confirmPasswordField))
                .perform(typeText(VALID_PASSWORD));
        onView(withId(R.id.codeNumberCountrySpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(CODE_NUMBER_COUNTRY))).perform(click());
        onView(withId(R.id.codeNumberCountrySpinner)).check(matches(withSpinnerText(containsString(CODE_NUMBER_COUNTRY))));

        onView(withId(R.id.numberField))
                .perform(typeText(VALID_NUMBER));
        onView(withId(R.id.registerButton))
                .perform(click());
        onView(withId(R.id.emailField)).check(matches(withError(
                mActivityRule.getActivity().getString(R.string.error_field_required))));
    }

    /**
     * Test if email form is valid
     */
    @Test
    public void testEmailFormNotValid(){
        onView(withId(R.id.emailField))
                .perform(typeText(TEST_VALUE));
        onView(withId(R.id.nicknameField))
                .perform(typeText(VALID_NICKNAME));
        onView(withId(R.id.passwordField))
                .perform(typeText(VALID_PASSWORD));
        onView(withId(R.id.confirmPasswordField))
                .perform(typeText(VALID_PASSWORD));
        onView(withId(R.id.codeNumberCountrySpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(CODE_NUMBER_COUNTRY))).perform(click());
        onView(withId(R.id.codeNumberCountrySpinner)).check(matches(withSpinnerText(containsString(CODE_NUMBER_COUNTRY))));
        onView(withId(R.id.numberField))
                .perform(typeText(VALID_NUMBER));
        onView(withId(R.id.registerButton))
                .perform(click());
        onView(withId(R.id.emailField)).check(matches(withError(
                mActivityRule.getActivity().getString(R.string.error_invalid_email))));
    }

    /**
      * Check if passwordValidator is valid
      */
    @Test
    public void testPasswordFormNotValid(){
        onView(withId(R.id.emailField))
                .perform(typeText(VALID_EMAIL));
        onView(withId(R.id.nicknameField))
                .perform(typeText(VALID_NICKNAME));
        onView(withId(R.id.passwordField))
                .perform(typeText(NOT_VALID_PASSWORD));
        onView(withId(R.id.confirmPasswordField))
                .perform(typeText(NOT_VALID_PASSWORD));
        onView(withId(R.id.codeNumberCountrySpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(CODE_NUMBER_COUNTRY))).perform(click());
        onView(withId(R.id.codeNumberCountrySpinner)).check(matches(withSpinnerText(containsString(CODE_NUMBER_COUNTRY))));
        onView(withId(R.id.numberField))
                .perform(typeText(VALID_NUMBER));
        onView(withId(R.id.registerButton))
                .perform(click());
        onView(withId(R.id.passwordField)).check(matches(withError(
                mActivityRule.getActivity().getString(R.string.error_invalid_password))));
    }

    /**
      * Check if error display if password is empty
      */
    @Test
    public void testErrorEmptyPasswordForm(){
        onView(withId(R.id.emailField))
                .perform(typeText(VALID_EMAIL));
        onView(withId(R.id.nicknameField))
                .perform(typeText(VALID_NICKNAME));
        onView(withId(R.id.passwordField))
                .perform(typeText(""));
        onView(withId(R.id.confirmPasswordField))
                .perform(typeText(VALID_PASSWORD));
        onView(withId(R.id.codeNumberCountrySpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(CODE_NUMBER_COUNTRY))).perform(click());
        onView(withId(R.id.codeNumberCountrySpinner)).check(matches(withSpinnerText(containsString(CODE_NUMBER_COUNTRY))));
        onView(withId(R.id.numberField))
                .perform(typeText(VALID_NUMBER));
        onView(withId(R.id.registerButton))
                .perform(click());
        onView(withId(R.id.passwordField)).check(matches(withError(
                mActivityRule.getActivity().getString(R.string.error_field_required))));
    }

    /**
     * Test when passwords matching display error
     */
    @Test
    public void testNotSamePassword(){
        onView(withId(R.id.emailField))
                .perform(typeText(VALID_EMAIL));
        onView(withId(R.id.nicknameField))
                .perform(typeText(VALID_NICKNAME));
        onView(withId(R.id.passwordField))
                .perform(typeText(NOT_VALID_PASSWORD));
        onView(withId(R.id.confirmPasswordField))
                .perform(typeText(VALID_PASSWORD));
        onView(withId(R.id.codeNumberCountrySpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(CODE_NUMBER_COUNTRY))).perform(click());
        onView(withId(R.id.codeNumberCountrySpinner)).check(matches(withSpinnerText(containsString(CODE_NUMBER_COUNTRY))));
        onView(withId(R.id.numberField))
                .perform(typeText(VALID_NUMBER));
        onView(withId(R.id.registerButton))
                .perform(click());
        onView(withId(R.id.confirmPasswordField)).check(matches(withError(
                mActivityRule.getActivity().getString(R.string.error_not_matching_password))));
    }

    /**
     * Test if display error when nicknameField is empty
     */
    @Test
    public void testEmptyErrorNicknameField(){
        onView(withId(R.id.emailField))
                .perform(typeText(VALID_EMAIL));
        onView(withId(R.id.nicknameField))
                .perform(typeText(EMPTY_VALUE));
        onView(withId(R.id.passwordField))
                .perform(typeText(VALID_PASSWORD));
        onView(withId(R.id.confirmPasswordField))
                .perform(typeText(VALID_PASSWORD));
        onView(withId(R.id.codeNumberCountrySpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(CODE_NUMBER_COUNTRY))).perform(click());
        onView(withId(R.id.codeNumberCountrySpinner)).check(matches(withSpinnerText(containsString(CODE_NUMBER_COUNTRY))));
        onView(withId(R.id.numberField))
                .perform(typeText(VALID_NUMBER));
        onView(withId(R.id.registerButton))
                .perform(click());
        onView(withId(R.id.nicknameField)).check(matches(withError(
                mActivityRule.getActivity().getString(R.string.error_field_required))));
    }

    /**
     * Test if display error when numberField is empty
     */
    @Test
    public void testEmptyErrorNumberField(){
        onView(withId(R.id.emailField))
                .perform(typeText(VALID_EMAIL));
        onView(withId(R.id.nicknameField))
                .perform(typeText(VALID_NICKNAME));
        onView(withId(R.id.passwordField))
                .perform(typeText(VALID_PASSWORD));
        onView(withId(R.id.confirmPasswordField))
                .perform(typeText(VALID_PASSWORD));
        onView(withId(R.id.codeNumberCountrySpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(CODE_NUMBER_COUNTRY))).perform(click());
        onView(withId(R.id.codeNumberCountrySpinner)).check(matches(withSpinnerText(containsString(CODE_NUMBER_COUNTRY))));
        onView(withId(R.id.numberField))
                .perform(typeText(EMPTY_VALUE));
        onView(withId(R.id.registerButton))
                .perform(click());
        onView(withId(R.id.numberField)).check(matches(withError(
                mActivityRule.getActivity().getString(R.string.error_field_required))));
    }
    /**
     * Match if error display in EditText
     * @param expected
     * @return
     */
    private static Matcher<View> withError(final String expected) {
        return new TypeSafeMatcher<View>() {

            @Override
            public boolean matchesSafely(View view) {
                if (!(view instanceof EditText)) {
                    return false;
                }
                EditText editText = (EditText) view;
                return editText.getError().toString().equals(expected);
            }

            @Override
            public void describeTo(Description description) {

            }
        };
    }



}
