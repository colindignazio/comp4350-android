package comp4350.boozr.presentation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import java.util.ArrayList;

import org.json.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

import comp4350.boozr.R;
import comp4350.boozr.business.API;

public class UserActivity extends Activity
{
    private String username = "";
    private String email = "";
    private String location = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user);

        String username = "Error Username Not Found";
        String email = "Error Email Not Found";
        String location = "Error Location Not Found";
        String reviewsArray;
        
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            username = extras.getString("username");
            email = extras.getString("email");
            location = extras.getString("location");
            reviewsArray = extras.getString("reviews");
            
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

        TextView usernameTextView = (TextView)findViewById(R.id.usernameText);
        TextView emailTextView = (TextView)findViewById(R.id.emailText);
        TextView locationTextView = (TextView)findViewById(R.id.locationText);
        usernameTextView.setText(username);
        emailTextView.setText(email);
        locationTextView.setText(location);
        this.username = username;
        this.email = email;
        this.location = location;
    }
}
