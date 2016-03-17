package comp4350.boozr.presentation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

public class ReviewBeerActivity extends Activity
{
    private SharedPreferences prefs;
    private String userId ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_drink);

        String drinkName = "Error Drink Not Found";
        String type = "Error drink type Not Found";
        String alc = "Error alchohol content Not Found";
        String rating = "Error rating not found";
        String price = "Error price not found";
        String brewery = "Error brewery not found";
        String beerId = "Error beerId not found";
        String UserId = "";
        String reviews = "{}";
        JSONArray reviewsArray;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            drinkName = extras.getString("drinkname");
            type = extras.getString("type");
            alc = extras.getString("alc");
            rating = extras.getString("rating");
            price = extras.getString("price");
            brewery = extras.getString("brewery");
            beerId = extras.getString("beerId");
            userId = extras.getString("UserId");
        }

        TextView drinkNameTextView = (TextView) findViewById(R.id.drinknameText);
        TextView typeTextView = (TextView) findViewById(R.id.drinkTypeText);
        TextView alcTextView = (TextView) findViewById(R.id.alcText);
        TextView ratingTextView = (TextView) findViewById(R.id.ratingText);
        TextView priceTextView = (TextView) findViewById(R.id.priceText);
        TextView breweryTextView = (TextView) findViewById(R.id.breweryText);

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
        this.beerId = beerId;
        this.userId = userId;
    }

    public void postReview(View v) {
        //post the user's review

        EditText stars = (EditText)findViewById(R.id.stars);
        String starsString = stars.getText().toString();
        EditText price = (EditText)findViewById(R.id.price);
        String priceString = price.getText().toString();
        EditText name = (EditText)findViewById(R.id.nameText);
        String nameString = name.getText().toString();
        EditText address = (EditText)findViewById(R.id.addressText);
        String addressString = address.getText().toString();
        EditText review = (EditText)findViewById(R.id.reviewText);
        String reviewString = review.getText().toString();

        try {
            String result = new API().execute("BeerReview/create", "user_id", userId, "beer_id", beerId, "stars", starsString, "review", reviewString, "price", priceString).get();
            try {
                JSONObject jsonObject = new JSONObject(result);
                String status = jsonObject.getString("status");
                if(status.equals("200")) {
                    //Creation successful, set the sessionId in the prefs and push the logged in home page
                    Intent homeIntent = new Intent(CreateAccountActivity.this, HomeActivity.class);
                    CreateAccountActivity.this.startActivity(homeIntent);
                } else {
                    Messages.warning(this, "Review could not be posted");
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
