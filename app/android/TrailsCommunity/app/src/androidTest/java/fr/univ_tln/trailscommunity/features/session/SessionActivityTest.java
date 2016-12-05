package fr.univ_tln.trailscommunity.features.session;

import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;

/**
 * Created by Stéphen on 05/12/2016.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SessionActivityTest {

    @Test
    public void test(){
        UiDevice device = UiDevice.getInstance(getInstrumentation());
        UiObject marker = device.findObject(new UiSelector().descriptionContains("marker title"));
        marker.click();
    }

}
