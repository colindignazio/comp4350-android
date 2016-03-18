package comp4350.boozr.presentation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.String;
import java.util.concurrent.ExecutionException;

import comp4350.boozr.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.content.SharedPreferences;
import android.app.*;
import android.content.*;

import comp4350.boozr.application.Main;
import comp4350.boozr.business.API;
import org.json.*;

public class HomeActivity extends Activity 
{
	private SharedPreferences prefs;
	private String searchType = "Beer";
	private String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);

        copyDatabaseToDevice();
        
        Main.startUp();

		this.prefs = this.getSharedPreferences(
				"com.boozr.app", Context.MODE_PRIVATE);
		String sessionId = prefs.getString("sessionId", null);

		if(sessionId == null) {
			//No previous session exists
			setContentView(R.layout.activity_home);
		} else {
			//Previous session exists so get the users details
			setContentView(R.layout.activity_home_loggedin);
		}
    }

    @Override
    protected void onDestroy() 
    {
        super.onDestroy();

        Main.shutDown();
    }
    
    private void copyDatabaseToDevice() {
    	final String DB_PATH = "db";

    	String[] assetNames;
    	Context context = getApplicationContext();
    	File dataDirectory = context.getDir(DB_PATH, Context.MODE_PRIVATE);
    	AssetManager assetManager = getAssets();
    	
    	try {

    		assetNames = assetManager.list(DB_PATH);
    		for (int i = 0; i < assetNames.length; i++) {
    			assetNames[i] = DB_PATH + "/" + assetNames[i];
    		}

    		copyAssetsToDirectory(assetNames, dataDirectory);
    	

    	} catch (IOException ioe) {
    		Messages.warning(this, "Unable to access application data: " + ioe.getMessage());
    	}
    }
    
    public void copyAssetsToDirectory(String[] assets, File directory) throws IOException {
    	AssetManager assetManager = getAssets();

    	for (String asset : assets) {
    		String[] components = asset.split("/");
    		String copyPath = directory.toString() + "/" + components[components.length - 1];
    		char[] buffer = new char[1024];
    		int count;
    		
    		File outFile = new File(copyPath);
    		
    		if (!outFile.exists()) {
    			InputStreamReader in = new InputStreamReader(assetManager.open(asset));
	    		FileWriter out = new FileWriter(outFile);
	    		
	    		count = in.read(buffer);
	    		while (count != -1) {
	    			out.write(buffer, 0, count);
	        		count = in.read(buffer);
	    		}
	    		
	    		out.close();
	    		in.close();
    		}
    	}
	}

	public void advancedSearch(View v){
		Intent advancedSearchIntent = new Intent(HomeActivity.this, AdvancedSearchActivity.class);
		HomeActivity.this.startActivity(advancedSearchIntent);
	}

	public void search(View v) {
		EditText searchText = (EditText)findViewById(R.id.searchText);
		String searchString = searchText.getText().toString();
		Intent searchIntent = new Intent(HomeActivity.this, SearchResultsActivity.class);

		if(this.searchType.equals("Beer")) {
			try {
				String result = new API().execute("Beer/search", "searchToken", searchString).get();
				try {
					JSONObject jsonObject = new JSONObject(result);
					String status = jsonObject.getString("status");
					if(status.equals("200")) {
						//Beer search results
						searchIntent.putExtra("results", jsonObject.getString("searchResults"));
						searchIntent.putExtra("resultType", "Beer");
						searchIntent.putExtra("userId", userId);
						HomeActivity.this.startActivity(searchIntent);
					} else if(status.equals("400")) {
						//No results found
						new AlertDialog.Builder(HomeActivity.this)
								.setTitle("No Results Found")
								.setMessage("Your search query didn't return any results")
								.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int which) {
										// dismiss
									}
								})
								.setIcon(android.R.drawable.ic_dialog_alert)
								.show();
					}
				} catch(JSONException e) {
					e.printStackTrace();
				}
			} catch(InterruptedException e) {

			} catch(ExecutionException e) {
				e.printStackTrace();
			}
		} else if(this.searchType.equals("User")) {
			try {
				String result = new API().execute("user/search/", "searchToken", searchString).get();
				try {
					JSONObject jsonObject = new JSONObject(result);
					String status = jsonObject.getString("status");
					if(status.equals("200")) {
						//User search results
						searchIntent.putExtra("results", jsonObject.getString("searchResults"));
						searchIntent.putExtra("resultType", "User");
						searchIntent.putExtra("userId", userId);
						HomeActivity.this.startActivity(searchIntent);
					} else if(status.equals("400")) {
						//No results found
						new AlertDialog.Builder(HomeActivity.this)
								.setTitle("No Results Found")
								.setMessage("Your search query didn't return any results")
								.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int which) {
										// dismiss
									}
								})
								.setIcon(android.R.drawable.ic_dialog_alert)
								.show();
					}
				} catch(JSONException e) {
					e.printStackTrace();
				}
			} catch(InterruptedException e) {

			} catch(ExecutionException e) {
				e.printStackTrace();
			}
		} else {
			//Error, neither user nor beer specified
		}
	}

	public void searchTypeSelect(View v) {
		boolean checked = ((RadioButton) v).isChecked();
		switch(v.getId()) {
			case R.id.beerRadio:
				if (checked)
					this.searchType = "Beer";
					break;
			case R.id.userRadio:
				if (checked)
					this.searchType = "User";
					break;
		}
	}

	public void createAccount(View v) {
		Intent createAccountIntent = new Intent(HomeActivity.this, CreateAccountActivity.class);
		HomeActivity.this.startActivity(createAccountIntent);
	}

	public void logout(View v) {
		try {
			String sessionId = this.prefs.getString("sessionId", null);
			String result = new API().execute("user/logout", "sessionId", sessionId).get();
			try {
				JSONObject jsonObject = new JSONObject(result);
				String status = jsonObject.getString("status");
				if(status.equals("200")) {
					//Logout success
					this.prefs.edit().putString("sessionId", null).apply();
					Intent homeIntent = new Intent(HomeActivity.this, HomeActivity.class);
					userId="0";
					HomeActivity.this.startActivity(homeIntent);
				} else {
					//Login failed
				}
			} catch(JSONException e) {
				e.printStackTrace();
			}
		} catch(InterruptedException e) {

		} catch(ExecutionException e) {
			e.printStackTrace();
		}
	}

	public void editUserProfile(View v) {
		try {
			String sessionId = this.prefs.getString("sessionId", null);
			String result = new API().execute("user/getUserDetails", "sessionId", sessionId).get();
			try {
				JSONObject jsonObject = new JSONObject(result);
				String status = jsonObject.getString("status");
				if(status.equals("200")) {
					//Login success
					JSONObject user = jsonObject.getJSONObject("user");
					Intent profileIntent = new Intent(HomeActivity.this, ProfileActivity.class);
					profileIntent.putExtra("username", user.getString("User_name"));
					profileIntent.putExtra("email", user.getString("User_email"));
					profileIntent.putExtra("location", user.getString("User_location"));
					HomeActivity.this.startActivity(profileIntent);
				} else {
					//Login failed
				}
			} catch(JSONException e) {
				e.printStackTrace();
			}
		} catch(InterruptedException e) {

		} catch(ExecutionException e) {
			e.printStackTrace();
		}
	}
	
	public void loadUserProfile(View v) {
		try {
			String sessionId = this.prefs.getString("sessionId", null);
			String result = new API().execute("user/getUserDetails", "sessionId", sessionId).get();
			try {
				JSONObject jsonObject = new JSONObject(result);
				String status = jsonObject.getString("status");
				if(status.equals("200")) {
					//User found
					JSONObject user = jsonObject.getJSONObject("user");
					Intent userIntent = new Intent(HomeActivity.this, UserActivity.class);
					userIntent.putExtra("username", user.getString("User_name"));
					userIntent.putExtra("email", user.getString("User_email"));
					userIntent.putExtra("location", user.getString("User_location"));
					HomeActivity.this.startActivity(userIntent);
				} else {
					Messages.fatalError(this, "User was not found.");
				}
			} catch(JSONException e) {
				e.printStackTrace();
			}
		} catch(InterruptedException e) {

		} catch(ExecutionException e) {
			e.printStackTrace();
		}
	}

	public void login(View v) {
		EditText username = (EditText)findViewById(R.id.usernameText);
		String usernameString = username.getText().toString();
		EditText password = (EditText)findViewById(R.id.passwordText);
		String passwordString = password.getText().toString();

		try {
			String result = new API().execute("user/login", "userName", usernameString, "password", passwordString).get();
			try {
				JSONObject jsonObject = new JSONObject(result);
				String status = jsonObject.getString("status");
				if(status.equals("200")) {
					//Login success
					this.prefs.edit().putString("sessionId", jsonObject.getString("sessionId")).apply();
					Intent profileIntent = new Intent(HomeActivity.this, HomeActivity.class);
					HomeActivity.this.startActivity(profileIntent);
					userId = jsonObject.getJSONObject("user").getString("User_id");
				} else {
					Messages.warning(this, "Invalid username or password.");
				}
			} catch(JSONException e) {
				e.printStackTrace();
			}
		} catch(InterruptedException e) {

		} catch(ExecutionException e) {
			e.printStackTrace();
		}
	}
}
