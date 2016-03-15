package comp4350.boozr.presentation;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import java.util.ArrayList;

import org.json.*;

import java.util.List;

import comp4350.boozr.R;

public class ProfileActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);

        String username = "Error Username Not Found";
        String email = "Error Email Not Found";
        String location = "Error Location Not Found";
        String reviews = "{}";
        JSONArray reviewsArray;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            username = extras.getString("username");
            email = extras.getString("email");
            location = extras.getString("location");
            //reviews = extras.getString("reviews");

            /*try {
                reviewsArray = new JSONArray(reviews);
                List<String> reviewArrayList = new ArrayList<String>();
                for(int i = 0; i < reviewsArray.length(); i++) {
                    reviewArrayList.add(reviewsArray.getJSONObject(i).getString("review"));
                }

                ListView reviewsList = (ListView)findViewById(R.id.reviewsList);
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, reviewArrayList );

                reviewsList.setAdapter(arrayAdapter);
            } catch(JSONException e) {
                e.printStackTrace();
            }*/
        }

        TextView usernameTextView = (TextView)findViewById(R.id.usernameText);
        TextView emailTextView = (TextView)findViewById(R.id.emailText);
        TextView locationTextView = (TextView)findViewById(R.id.locationText);
        usernameTextView.setText(username);
        emailTextView.setText(email);
        locationTextView.setText(location);
    }
}
