package fr.univ_tln.trailscommunity.features.session;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.Until;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import fr.univ_tln.trailscommunity.R;
import fr.univ_tln.trailscommunity.features.EspressoUtils;
import fr.univ_tln.trailscommunity.features.root.LoginActivity_;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Created by Stéphen on 05/12/2016.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SessionActivityTest {

    private static final String CHAT_MESSAGE = "loooooooooooooooooooooooooong message";

    private static final String EMPTY_FIELD = "";

    @Rule
    public ActivityTestRule<SessionActivity_> mActivityRule = new ActivityTestRule<>(
            SessionActivity_.class);

    @Test
    public void addWaypointTest(){
        onView(withId(R.id.map_navigation))
                .perform(longClick());
    }

    @Test
    public void openChatTest(){
        onView(withId(R.id.session_chat_menu))
                .perform(click());
    }

    @Test
    public void addChatMessage(){
        onView(withId(R.id.session_chat_menu))
                .perform(click());
        onView(withId(R.id.chatField))
                .perform(typeText(CHAT_MESSAGE));
        onView(withId(R.id.sendMessage))
                .perform(click());
    }

     @Test
     public void chatFieldNotNullErrorTest(){
         onView(withId(R.id.session_chat_menu))
                 .perform(click());
         onView(withId(R.id.chatField))
                 .perform(typeText(EMPTY_FIELD));
         onView(withId(R.id.sendMessage))
                 .perform(click());
         onView(withId(R.id.passwordField)).check(matches(EspressoUtils.withError(
                 mActivityRule.getActivity().getString(R.string.error_field_required))));
     }

    @Test
    public void closeChatTest(){
        onView(withId(R.id.session_chat_menu))
                .perform(click());
    }



}
