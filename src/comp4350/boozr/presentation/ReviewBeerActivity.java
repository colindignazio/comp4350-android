package comp4350.boozr.presentation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


import org.json.*;

import java.util.concurrent.ExecutionException;

import comp4350.boozr.R;
import comp4350.boozr.business.API;

public class ReviewBeerActivity extends Activity
{
    private String beerId = "";
    private String userId ="1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_beer_review);

        String drinkName = "Error Drink Not Found";
        String beerId = "Error beerId not found";
        String userId = "1";
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            drinkName = extras.getString("drinkname");
            beerId = extras.getString("beerId");
            userId = extras.getString("userId");
        }

        TextView drinkNameTextView = (TextView) findViewById(R.id.drinknameText);

        drinkNameTextView.setText(drinkName);

        this.beerId = beerId;
        this.userId = userId;
    }

    public void postReview(View v) {
        //post the user's review

        EditText stars = (EditText)findViewById(R.id.starsText);
        String starsString = stars.getText().toString();
        EditText price = (EditText)findViewById(R.id.priceText);
        String priceString = price.getText().toString();
        EditText review = (EditText)findViewById(R.id.reviewText);
        String reviewString = review.getText().toString();

        try {
            String result = new API().execute("BeerReview/create/", "user_id", userId, "beer_id", beerId, "stars", starsString, "review", reviewString, "price", priceString).get();
            try {
                JSONObject jsonObject = new JSONObject(result);
                String status = jsonObject.getString("status");
                if(status.equals("200")) {
                    //post successful, go back to the home page
                    Intent homeIntent = new Intent(ReviewBeerActivity.this, HomeActivity.class);
                    ReviewBeerActivity.this.startActivity(homeIntent);
                } else {
                      //error message need to figure this out 
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
