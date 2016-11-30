package fr.univ_tln.trailscommunity.features.session;

import android.support.test.rule.ActivityTestRule;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import fr.univ_tln.trailscommunity.features.root.LoginActivity_;
import fr.univ_tln.trailscommunity.models.Coordinate;
import fr.univ_tln.trailscommunity.utilities.geocoder.GMGeocoder;

/**
 * Created by Stéphen on 28/11/2016.
 */

public class SessionFormActivityTest {

    public static final String city = "Toulon";
    public static final double city_latitude = 1545.025;
    public static final double delta = 0.1;

    @Rule
    public ActivityTestRule<SessionFormActivity_> mActivityRule = new ActivityTestRule<>(
            SessionFormActivity_.class);

    @Test
    public void encodeAdressTest(){
        Coordinate coordinate = GMGeocoder.addressToCoordinates(mActivityRule.getActivity(), city);
        Assert.assertEquals(coordinate.getLatitude(), city_latitude, delta);
    }

}
