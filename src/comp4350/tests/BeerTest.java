package comp4350.tests;
import com.robotium.solo.Solo;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;
import comp4350.boozr.R;
import comp4350.boozr.presentation.HomeActivity;

public class BeerTest extends ActivityInstrumentationTestCase2<HomeActivity> {
    private Solo solo;

    public BeerTest() {
        super(HomeActivity.class);
    }

    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

    public void testViewBeer() throws Exception {
        solo.assertCurrentActivity("wrong activity", HomeActivity.class);
        solo.enterText((EditText) solo.getView(R.id.searchText), "grass");
        solo.clickOnView(solo.getView(R.id.beerRadio));
        solo.clickOnView(solo.getView(R.id.searchButton));

        solo.assertCurrentActivity("wrong activity", HomeActivity.class);
        assertTrue(solo.waitForText("Grasshopper"));
        solo.clickInList(0, 0);
        assertTrue(solo.waitForText("Grasshopper"));
        assertTrue(solo.waitForText("Ale"));
        assertTrue(solo.waitForText("Big Rock"));
    }

    public void testReviewBeer() throws Exception {
		solo.enterText((EditText) solo.getView(R.id.usernameText),"e2euser");
		solo.enterText((EditText) solo.getView(R.id.passwordText),"testpass");
		solo.clickOnView(solo.getView(R.id.button6));
		assertTrue(solo.waitForText("Logout"));
        solo.assertCurrentActivity("wrong activity", HomeActivity.class);
        solo.enterText((EditText) solo.getView(R.id.searchText), "grass");
        solo.clickOnView(solo.getView(R.id.beerRadio));
        solo.clickOnView(solo.getView(R.id.searchButton));

        solo.assertCurrentActivity("wrong activity", HomeActivity.class);
        assertTrue(solo.waitForText("Grasshopper"));
        solo.clickInList(0, 0);
        assertTrue(solo.waitForText("Grasshopper"));
        assertTrue(solo.waitForText("Ale"));
        assertTrue(solo.waitForText("Big Rock"));
        solo.clickOnView(solo.getView(R.id.reviewButton));

        solo.enterText((EditText) solo.getView(R.id.starsText), "5");
        solo.enterText((EditText) solo.getView(R.id.priceText), "5");
        solo.enterText((EditText) solo.getView(R.id.nameText), "Billy Bob");
        solo.enterText((EditText) solo.getView(R.id.addressText), "24 Sussex Drive");
        solo.enterText((EditText) solo.getView(R.id.reviewText), "The beer was good");

        solo.clickOnView(solo.getView(R.id.PostReview));

		solo.clickOnView(solo.getView(R.id.button4));
		assertTrue(solo.waitForText("Login"));
    }

    public void testSortBeer() throws Exception {
        solo.assertCurrentActivity("wrong activity", HomeActivity.class);
        solo.enterText((EditText) solo.getView(R.id.searchText), "ale");
        solo.clickOnView(solo.getView(R.id.beerRadio));
        solo.clickOnView(solo.getView(R.id.searchButton));

        solo.assertCurrentActivity("wrong activity", HomeActivity.class);
        assertTrue(solo.waitForText("Grasshopper"));
        solo.pressSpinnerItem(0, 1);
        solo.clickInList(0, 0);
        assertTrue(solo.waitForText("Hoegaarden"));
        assertTrue(solo.waitForText("Ale"));
        assertTrue(solo.waitForText("Ab Inbev"));
    }
}
