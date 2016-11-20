package fr.univ_tln.trailscommunity.features.sessions;

import android.support.test.espresso.contrib.PickerActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.internal.matchers.TypeSafeMatcher;
import org.junit.runner.RunWith;

import fr.univ_tln.trailscommunity.R;
import fr.univ_tln.trailscommunity.features.sessions.CreateSessionActivity_;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;

/**
 * Created by Stéphen on 20/11/2016.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SessionActivityTest {

    private static final String VALID_DEPARTURE_ADRESS = "Nice";
    private static final String VALID_ARRIVAL_ADRESS = "Paris";
    private static final String VALID_TYPE_ACTIVITY = "VTT";
    private static final String VALID_PASSWORD = "password";
    private static final String INVALID_PASSWORD = "ab";
    private static final String EMPTY_FIELD = "";

    @Rule
    public ActivityTestRule<CreateSessionActivity_> mActivityRule = new ActivityTestRule<>(
            CreateSessionActivity_.class);

    /**
     * Test all fields of the view
     */
    @Test
    public void testCreateSessionButton(){
        onView(withId(R.id.departurePlaceField))
                .perform(typeText(VALID_DEPARTURE_ADRESS));
        onView(withId(R.id.arrivalPlaceField))
                .perform(typeText(VALID_ARRIVAL_ADRESS));
        onView(withId(R.id.typeActivitySpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(VALID_TYPE_ACTIVITY))).perform(click());
        onView(withId(R.id.typeActivitySpinner)).check(matches(withSpinnerText(containsString(VALID_TYPE_ACTIVITY))));

        //Double tap to display datePicker dialog
        onView(withId(R.id.startDateField))
                .perform(click());
        onView(withId(R.id.startDateField)).perform(click());
        //Select date on datePicker
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2016, 11, 25));
        // Click on the "OK" button to confirm and close the dialog
        onView(withText("OK")).perform(click());

        onView(withId(R.id.passwordField))
                .perform(typeText(VALID_PASSWORD));
        onView(withId(R.id.createSessionButton))
                .perform(click());
    }

    @Test
    public void testEmptyErrorDeparturePlaceField(){
        onView(withId(R.id.departurePlaceField))
                .perform(typeText(EMPTY_FIELD));
        onView(withId(R.id.arrivalPlaceField))
                .perform(typeText(VALID_ARRIVAL_ADRESS));
        onView(withId(R.id.typeActivitySpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(VALID_TYPE_ACTIVITY))).perform(click());
        onView(withId(R.id.typeActivitySpinner)).check(matches(withSpinnerText(containsString(VALID_TYPE_ACTIVITY))));

        //Double tap to display datePicker dialog
        onView(withId(R.id.startDateField))
                .perform(click());
        onView(withId(R.id.startDateField)).perform(click());
        //Select date on datePicker
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2016, 11, 25));
        // Click on the "OK" button to confirm and close the dialog
        onView(withText("OK")).perform(click());

        onView(withId(R.id.passwordField))
                .perform(typeText(VALID_PASSWORD));
        onView(withId(R.id.createSessionButton))
                .perform(click());

        onView(withId(R.id.departurePlaceField)).check(matches(withError(
                mActivityRule.getActivity().getString(R.string.error_field_required))));
    }

    @Test
    public void testEmptyErrorArrivalPlaceField(){
        onView(withId(R.id.departurePlaceField))
                .perform(typeText(VALID_DEPARTURE_ADRESS));
        onView(withId(R.id.arrivalPlaceField))
                .perform(typeText(EMPTY_FIELD));
        onView(withId(R.id.typeActivitySpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(VALID_TYPE_ACTIVITY))).perform(click());
        onView(withId(R.id.typeActivitySpinner)).check(matches(withSpinnerText(containsString(VALID_TYPE_ACTIVITY))));

        //Double tap to display datePicker dialog
        onView(withId(R.id.startDateField))
                .perform(click());
        onView(withId(R.id.startDateField)).perform(click());
        //Select date on datePicker
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2016, 11, 25));
        // Click on the "OK" button to confirm and close the dialog
        onView(withText("OK")).perform(click());

        onView(withId(R.id.passwordField))
                .perform(typeText(VALID_PASSWORD));
        onView(withId(R.id.createSessionButton))
                .perform(click());

        onView(withId(R.id.arrivalPlaceField)).check(matches(withError(
                mActivityRule.getActivity().getString(R.string.error_field_required))));
    }

    @Test
    public void testEmptyErrorStartDateField(){
        onView(withId(R.id.departurePlaceField))
                .perform(typeText(VALID_DEPARTURE_ADRESS));
        onView(withId(R.id.arrivalPlaceField))
                .perform(typeText(VALID_ARRIVAL_ADRESS));
        onView(withId(R.id.typeActivitySpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(VALID_TYPE_ACTIVITY))).perform(click());
        onView(withId(R.id.typeActivitySpinner)).check(matches(withSpinnerText(containsString(VALID_TYPE_ACTIVITY))));
        onView(withId(R.id.passwordField))
                .perform(typeText(VALID_PASSWORD));
        onView(withId(R.id.createSessionButton))
                .perform(click());

        onView(withId(R.id.startDateField)).check(matches(withError(
                mActivityRule.getActivity().getString(R.string.error_field_required))));
    }

    @Test
    public void testEmptyErrorPasswordField(){
        onView(withId(R.id.departurePlaceField))
                .perform(typeText(VALID_DEPARTURE_ADRESS));
        onView(withId(R.id.arrivalPlaceField))
                .perform(typeText(VALID_ARRIVAL_ADRESS));
        onView(withId(R.id.typeActivitySpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(VALID_TYPE_ACTIVITY))).perform(click());
        onView(withId(R.id.typeActivitySpinner)).check(matches(withSpinnerText(containsString(VALID_TYPE_ACTIVITY))));

        //Double tap to display datePicker dialog
        onView(withId(R.id.startDateField))
                .perform(click());
        onView(withId(R.id.startDateField)).perform(click());
        //Select date on datePicker
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2016, 11, 25));
        // Click on the "OK" button to confirm and close the dialog
        onView(withText("OK")).perform(click());

        onView(withId(R.id.passwordField))
                .perform(typeText(EMPTY_FIELD));
        onView(withId(R.id.createSessionButton))
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
