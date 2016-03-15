package comp4350.boozr.presentation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
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

import comp4350.boozr.application.Main;
import comp4350.boozr.business.API;

public class HomeActivity extends Activity 
{

    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);

        copyDatabaseToDevice();
        
        Main.startUp();

        setContentView(R.layout.activity_home);
    }

    @Override
    protected void onDestroy() 
    {
        super.onDestroy();

        Main.shutDown();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
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
    
    public void buttonTestOnClick(View v)
    {
    	Intent testIntent = new Intent(HomeActivity.this, TestActivity.class);
    	HomeActivity.this.startActivity(testIntent);
    }

	public void login(View v) {
		EditText username = (EditText)findViewById(R.id.usernameText);
		String usernameString = username.getText().toString();
		EditText password = (EditText)findViewById(R.id.passwordText);
		String passwordString = password.getText().toString();

		try {
			String result = new API().execute("user/login", "userName", usernameString, "password", passwordString).get();
			Log.d("A", result);
		} catch(InterruptedException e) {

		} catch(ExecutionException e) {

		}

	}
}
