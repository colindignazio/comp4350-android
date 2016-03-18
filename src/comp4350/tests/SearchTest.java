package comp4350.tests;
import com.robotium.solo.Solo;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;
import comp4350.boozr.R;
import comp4350.boozr.presentation.HomeActivity;
import comp4350.boozr.presentation.SearchResultsActivity;
import comp4350.boozr.presentation.UserActivity;

public class SearchTest extends ActivityInstrumentationTestCase2<HomeActivity> {
	private Solo solo;

	public SearchTest() {
		super(HomeActivity.class);
	}

	public void setUp() throws Exception {
		solo = new Solo(getInstrumentation(), getActivity());
	}

	@Override
	public void tearDown() throws Exception {
		solo.finishOpenedActivities();
	}
	
	public void testUserSearch() throws Exception {
		solo.assertCurrentActivity("wrong activity", HomeActivity.class);
		solo.enterText((EditText) solo.getView(R.id.searchText),"e2euser");
		solo.clickOnView(solo.getView(R.id.userRadio));
		solo.clickOnView(solo.getView(R.id.searchButton));

		solo.assertCurrentActivity("wrong activity", SearchResultsActivity.class);
		assertTrue(solo.waitForText("e2euser@gmail.com"));
		
		solo.clickInList(0, 0); 
		solo.assertCurrentActivity("wrong activity", UserActivity.class);
		assertTrue(solo.waitForText("winnipeg"));
	}
	
	public void testBeerSearch() throws Exception {
		solo.assertCurrentActivity("wrong activity", HomeActivity.class);
		solo.enterText((EditText) solo.getView(R.id.searchText),"nosuchbeer");
		solo.clickOnView(solo.getView(R.id.beerRadio));
		solo.clickOnView(solo.getView(R.id.searchButton));

		solo.assertCurrentActivity("wrong activity", HomeActivity.class);
		assertTrue(solo.waitForText("Your search query didn't return any results"));
		
		//solo.clickInList(0, 0); 
		//assertTrue(solo.waitForText("e2euser@gmail.com"));
	}
}
