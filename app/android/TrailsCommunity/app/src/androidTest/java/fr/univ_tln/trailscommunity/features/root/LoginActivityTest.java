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
import fr.univ_tln.trailscommunity.features.root.LoginActivity_;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class LoginActivityTest {

    private static final String VALID_EMAIL = "test@gmail.com";
    private static final String NOT_VALID_EMAIL = "test@test";
    private static final String VALID_PASSWORD = "password";
    private static final String NOT_VALIDE_PASSWORD = "abcd";
    private static final String EMPTY_VALUE = "";

    @Rule
    public ActivityTestRule<LoginActivity_> mActivityRule = new ActivityTestRule<>(
            LoginActivity_.class);

    /**
     * Test all fields of the view
     */
    @Test
    public void testLoginButton(){
        onView(withId(R.id.emailField))
                .perform(typeText(VALID_EMAIL));
        onView(withId(R.id.passwordField))
                .perform(typeText(VALID_PASSWORD));
        onView(withId(R.id.email_sign_in_button))
                .perform(click());
    }

    /**
     * Check if emailValidator is valid
     */
    @Test
    public void testEmailFormNotValid(){
        onView(withId(R.id.emailField))
                .perform(typeText(NOT_VALID_EMAIL));
        onView(withId(R.id.email_sign_in_button))
                .perform(click());
        onView(withId(R.id.emailField)).check(matches(withError(
                mActivityRule.getActivity().getString(R.string.error_invalid_email))));
    }

    /**
     * Check if error display if email is empty
     */
    @Test
    public void testErrorEmptyEmailForm(){
        onView(withId(R.id.emailField))
                .perform(typeText(EMPTY_VALUE));
        onView(withId(R.id.email_sign_in_button))
                .perform(click());
        onView(withId(R.id.emailField)).check(matches(withError(
                mActivityRule.getActivity().getString(R.string.error_field_required))));
    }

    /**
     * Check if passwordValidator is valid
     */
    @Test
    public void testPasswordFormNotValid(){
        onView(withId(R.id.emailField))
                .perform(typeText(VALID_EMAIL));
        onView(withId(R.id.passwordField))
                .perform(typeText(NOT_VALIDE_PASSWORD));
        onView(withId(R.id.email_sign_in_button))
                .perform(click());
        onView(withId(R.id.passwordField)).check(matches(withError(
                mActivityRule.getActivity().getString(R.string.error_invalid_password))));
    }

    /**
     * Check if error display if password is empty
     */
    @Test
    public void testErrorEmptyPasswordForm(){
        onView(withId(R.id.passwordField))
                .perform(typeText(EMPTY_VALUE));
        onView(withId(R.id.email_sign_in_button))
                .perform(click());
        onView(withId(R.id.passwordField)).check(matches(withError(
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
