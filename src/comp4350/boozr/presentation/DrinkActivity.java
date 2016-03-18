package comp4350.boozr.presentation;

import java.util.ArrayList;
import java.util.List;

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
	ReviewAdapter adapter;
	JSONArray reviewArray;
	List<String> resultsList = new ArrayList<String>();
	
	
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
            
            try{
            	reviewArray =  new JSONArray(reviewsArray);
            	for(int i = 0; i < reviewArray.length(); i++) {
                    resultsList.add(reviewArray.getJSONObject(i).getString("review"));
                }
            	
            } catch(JSONException e) {
                e.printStackTrace();
            }
            

        	if(reviewsArray != null) {        		
        		ListView reviewsList = (ListView)findViewById(R.id.reviewsList);
                adapter = new ReviewAdapter(this,R.layout.review_list_item, reviewArray, resultsList);
                reviewsList.setAdapter(adapter);
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
