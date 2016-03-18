package comp4350.boozr.presentation;

import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import comp4350.boozr.R;
import comp4350.boozr.business.API;

public class CreateDrinkActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_create_drink);
	}
	    
	public void submitCreateDrink(View v){
	 	EditText type = (EditText)findViewById(R.id.drinkTypeText);
	    String typeString = type.getText().toString();
	    EditText name = (EditText)findViewById(R.id.drinkNameText);
	    String nameString = name.getText().toString();
	    EditText abv = (EditText)findViewById(R.id.drinkAbvText);
	    String abvString = abv.getText().toString();
	    EditText brewery = (EditText)findViewById(R.id.drinkBreweryText);
	    String breweryString = brewery.getText().toString();
	
	    try {
	    	String result = new API().execute("beer/newBeer", "Type", typeString, "Name", nameString,
                                                                "Alcohol_By_Volume", abvString, "Brewery", breweryString, "Rating", "0", "AvgPrice", "0").get();
	    	try {
	    		JSONObject jsonObject = new JSONObject(result);
	    		String status = jsonObject.getString("status");
	    		if(status.equals("200")) {
	    			Intent homeIntent = new Intent(CreateDrinkActivity.this, HomeActivity.class);
	    			CreateDrinkActivity.this.startActivity(homeIntent);
	    		} else {
	    			Messages.warning(this, "Error, drink could not be created.");
	    		}
	    	} catch(JSONException e) {
	    		e.printStackTrace();
	    	}
	    } catch(InterruptedException e) {
	    	e.printStackTrace();
	    } catch(ExecutionException e) {
	    	e.printStackTrace();
	    }
	}
}
