package fr.univ_tln.trailscommunity.features.session;

import android.support.test.espresso.contrib.PickerActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.widget.DatePicker;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import fr.univ_tln.trailscommunity.R;
import fr.univ_tln.trailscommunity.models.Session;

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
 * Created by Stéphen on 13/12/2016.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ModifySessionActivityTest {

    private static final String VALID_DEPARTURE_ADRESS = "Nice";
    private static final String VALID_ARRIVAL_ADRESS = "Paris";
    private static final String VALID_TYPE_ACTIVITY = "Off road 4x4";
    private static final String VALID_PASSWORD = "password";
    private static final String INVALID_PASSWORD = "ab";
    private static final String EMPTY_FIELD = "";

    @Rule
    public ActivityTestRule<ModifySessionActivity_> mActivityRule = new ActivityTestRule<>(
            ModifySessionActivity_.class);

    @Test
    public void modifySessionTest(){
        onView(withId(R.id.departurePlaceField))
                .perform(typeText(VALID_DEPARTURE_ADRESS));
        onView(withId(R.id.arrivalPlaceField))
                .perform(typeText(VALID_ARRIVAL_ADRESS));

        onView(withId(R.id.typeActivitySpinner)).perform(click());
        onData(allOf(is(instanceOf(Session.TypeActivity.class)), withMyValue(VALID_TYPE_ACTIVITY))).perform(click());


        //Double tap to display datePicker dialog
        onView(withId(R.id.startDateField)).perform(click());
        //Select date on datePicker
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2016, 12, 23));
        // Click on the "OK" button to confirm and close the dialog
        onView(withText("OK")).perform(click());

        onView(withId(R.id.passwordField))
                .perform(typeText(VALID_PASSWORD));
        onView(withId(R.id.saveSessionButton))
                .perform(click());
    }

    public static <T> Matcher<T> withMyValue(final String name) {
        return new BaseMatcher<T>() {
            @Override
            public boolean matches(Object item) {
                return item.toString().equals(name);
            }

            @Override
            public void describeTo(Description description) {

            }
        };
    }

}
