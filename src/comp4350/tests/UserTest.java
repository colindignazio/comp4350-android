package comp4350.tests;

import com.robotium.solo.Solo;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;
import comp4350.boozr.R;
import comp4350.boozr.presentation.HomeActivity;
import comp4350.boozr.presentation.SearchResultsActivity;

public class UserTest extends ActivityInstrumentationTestCase2<HomeActivity> {
	private Solo solo;

	public UserTest() {
		super(HomeActivity.class);
	}

	public void setUp() throws Exception {
		solo = new Solo(getInstrumentation(), getActivity());
	}

	@Override
	public void tearDown() throws Exception {
		solo.finishOpenedActivities();
	}
		  
	public void testLoginAndLogout() throws Exception {
		solo.assertCurrentActivity("wrong activity", HomeActivity.class);
				
		solo.enterText((EditText) solo.getView(R.id.usernameText),"e2euser");
		solo.enterText((EditText) solo.getView(R.id.passwordText),"testpass");
		solo.clickOnView(solo.getView(R.id.button6));
		assertTrue(solo.waitForText("Logout"));
		solo.clickOnView(solo.getView(R.id.button4));
		assertTrue(solo.waitForText("Login"));
	}
}
