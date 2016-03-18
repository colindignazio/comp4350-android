package comp4350.boozr.presentation;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import comp4350.boozr.R;

public class DrinkActivity extends Activity {

	private String drinkName = "";
	private String type = "";
	private String alc = "";
	private String rating = "";
	private String price = "";
	private String brewery = "";
	
	
	@Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_drink);

        String drinkName = "Error Drink Not Found";
        String type = "Error drink type Not Found";
        String alc = "Error alchohol content Not Found";
        String rating = "Error rating not found";
    	String price = "Error price not found";
    	String brewery = "Error brewery not found";
        String reviewsArray;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            drinkName = extras.getString("drinkname");
            type = extras.getString("type");
            alc = extras.getString("alc");
            rating = extras.getString("rating");
            price = extras.getString("price");
            brewery = extras.getString("brewery");
            
            
            reviewsArray = extras.getString("reviews");

        	if(reviewsArray != null) {
        		try{
            		JSONArray reviews = new JSONArray(reviewsArray);
            		JSONObject review;
            		//Log.d("Debug", "User Reviews from User Activity " + reviews.toString());
            		ArrayList<String> items = new ArrayList<String>();
            		for(int i = 0; i < reviews.length(); i++){
            			review = reviews.getJSONObject(i);
            			String text = review.getString("review");
            			items.add(text);
            		}
            		ArrayAdapter<String> reviewsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, items);
                	ListView reviewsListView = (ListView)findViewById(R.id.reviewsList);
                	reviewsListView.setAdapter(reviewsAdapter);
            	
            	} catch (JSONException e){
            		e.printStackTrace();
            	}
        	}
            
            
            
        }

        TextView drinkNameTextView = (TextView)findViewById(R.id.drinknameText);
        TextView typeTextView = (TextView)findViewById(R.id.drinkTypeText);
        TextView alcTextView = (TextView)findViewById(R.id.alcText);
        TextView ratingTextView = (TextView)findViewById(R.id.ratingText);
        TextView priceTextView = (TextView)findViewById(R.id.priceText);
        TextView breweryTextView = (TextView)findViewById(R.id.breweryText);
        
        drinkNameTextView.setText(drinkName);
        typeTextView.setText(type);
        alcTextView.setText(alc + "% Alcohol Content");
        ratingTextView.setText(rating + " stars");
        priceTextView.setText("$" + price);
        breweryTextView.setText(brewery);
        
        this.drinkName = drinkName;
        this.type = type;
        this.alc = alc;
        this.rating = rating;
        this.price = price;
        this.brewery = brewery;
    }
}
