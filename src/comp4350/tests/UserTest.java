package comp4350.tests;
import com.robotium.solo.Solo;

import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;
import comp4350.boozr.R;
import comp4350.boozr.presentation.HomeActivity;
import comp4350.boozr.presentation.SearchResultsActivity;
import comp4350.boozr.presentation.UserActivity;

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
	
	public void testFollowAndUnfollow() throws Exception {		
		Context context = getInstrumentation().getTargetContext();
		context.getSharedPreferences("com.boozr.app", Context.MODE_PRIVATE).edit().clear().commit();
		
		solo.assertCurrentActivity("wrong activity", HomeActivity.class);
				
		solo.enterText((EditText) solo.getView(R.id.usernameText),"e2euser");
		solo.enterText((EditText) solo.getView(R.id.passwordText),"testpass");
		solo.clickOnView(solo.getView(R.id.button6));
		assertTrue(solo.waitForText("Logout"));

		solo.assertCurrentActivity("wrong activity", HomeActivity.class);
		solo.enterText((EditText) solo.getView(R.id.searchText),"chuffy");
		solo.clickOnView(solo.getView(R.id.userRadio));
		solo.clickOnView(solo.getView(R.id.searchButtonLoggedIn));

		solo.assertCurrentActivity("wrong activity", SearchResultsActivity.class);
		assertTrue(solo.waitForText("test@fakeemail.com"));
		
		solo.clickInList(0, 0); 
		solo.assertCurrentActivity("wrong activity", UserActivity.class);
		assertTrue(solo.waitForText("Follow"));
		solo.clickOnView(solo.getView(R.id.followButton));
		assertTrue(solo.waitForText("Unfollow"));
		solo.clickOnView(solo.getView(R.id.followButton));
		assertTrue(solo.waitForText("Follow"));
		solo.goBack();
		solo.assertCurrentActivity("wrong activity", SearchResultsActivity.class);
		solo.goBack();
		solo.assertCurrentActivity("wrong activity", HomeActivity.class);
		solo.clickOnView(solo.getView(R.id.button4));
		assertTrue(solo.waitForText("Login"));
	}
	public void testSaveNoChanges() throws Exception {
		solo.assertCurrentActivity("wrong activity", HomeActivity.class);
				
		solo.enterText((EditText) solo.getView(R.id.usernameText),"e2euser");
		solo.enterText((EditText) solo.getView(R.id.passwordText),"testpass");
		solo.clickOnView(solo.getView(R.id.button6));
		
		assertTrue(solo.waitForText("Edit Account"));
		solo.clickOnView(solo.getView(R.id.Button01));
		assertTrue(solo.waitForText("Profile Info"));
		assertTrue(solo.waitForText("Save"));
		solo.clickOnView(solo.getView(R.id.saveButton));
		assertTrue(solo.waitForText("You have not changed any fields"));
	}
}
