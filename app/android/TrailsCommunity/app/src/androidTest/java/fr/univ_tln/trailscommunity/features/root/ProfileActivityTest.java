package fr.univ_tln.trailscommunity.features.root;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import fr.univ_tln.trailscommunity.R;
import fr.univ_tln.trailscommunity.features.EspressoUtils;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by Stéphen on 13/12/2016.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ProfileActivityTest {

    private final String VALID_NICKNAME = "nickname";
    private final String VALID_NUMBER = "600000000";
    private final String EMPTY_FIELD = "";

    @Rule
    public ActivityTestRule<ProfileActivity_> mActivityRule = new ActivityTestRule<>(
            ProfileActivity_.class);

    @Test
    public void modifyDataTest(){
        onView(withId(R.id.nicknameField))
                .perform(typeText(VALID_NICKNAME));
        onView(withId(R.id.numberField))
                .perform(typeText(VALID_NUMBER));
        onView(withId(R.id.save))
                .perform(click());
    }

    @Test
    public void errorNickameTest(){
        onView(withId(R.id.nicknameField))
                .perform(typeText(EMPTY_FIELD));
        onView(withId(R.id.numberField))
                .perform(typeText(VALID_NUMBER));
        onView(withId(R.id.save))
                .perform(click());
        onView(withId(R.id.nicknameField)).check(matches(EspressoUtils.withError(
                mActivityRule.getActivity().getString(R.string.error_field_required))));
    }

    @Test
    public void errorNumberTest(){
        onView(withId(R.id.nicknameField))
                .perform(typeText(VALID_NICKNAME));
        onView(withId(R.id.numberField))
                .perform(typeText(EMPTY_FIELD));
        onView(withId(R.id.save))
                .perform(click());
        onView(withId(R.id.numberField)).check(matches(EspressoUtils.withError(
                mActivityRule.getActivity().getString(R.string.error_field_required))));
    }
}
