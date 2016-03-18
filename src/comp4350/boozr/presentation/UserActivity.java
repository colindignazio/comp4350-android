package comp4350.boozr.presentation;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.ListView;
import java.util.ArrayList;

import org.json.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

import comp4350.boozr.R;
import comp4350.boozr.business.API;

public class UserActivity extends Activity
{
	private String userId = "";
    ReviewAdapter adapter;
	JSONArray reviewArray;
	List<String> resultsList = new ArrayList<String>();
	

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user);

        String userId = "Error UserId Not Found";
        String username = "Error Username Not Found";
        String email = "Error Email Not Found";
        String location = "Error Location Not Found";
        String reviewsArray;
        
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
        	userId = extras.getString("userId");
            username = extras.getString("username");
            email = extras.getString("email");
            location = extras.getString("location");
            reviewsArray = extras.getString("reviews");

        	if(reviewsArray != null) {
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
        	
        	try {
        		String result = new API().execute("follow/getRecentReviews", "userId", userId).get();
			
				JSONObject jsonObject = new JSONObject(result);
				String status = jsonObject.getString("status");
				if(status.equals("200")) {
			        JSONArray followingReviewArray = jsonObject.getJSONArray("details");
			        List<String> followingResultsList = new ArrayList<String>();
					
                	for(int i = 0; i < followingReviewArray.length(); i++) {
						followingResultsList.add(followingReviewArray.getJSONObject(i).getString("review"));
                    }	
                	
                	if(!followingResultsList.isEmpty()) {        		
                		ListView reviewsList = (ListView)findViewById(R.id.followingReviewsList);
                        ReviewAdapter followingAdapter = new ReviewAdapter(this,R.layout.review_list_item, followingReviewArray, followingResultsList);
                        reviewsList.setAdapter(followingAdapter);
                	}
				}
        	} catch(Exception e) {
        		e.printStackTrace();
        	}
		}

        TextView usernameTextView = (TextView)findViewById(R.id.usernameText);
        TextView emailTextView = (TextView)findViewById(R.id.emailText);
        TextView locationTextView = (TextView)findViewById(R.id.locationText);
        usernameTextView.setText(username);
        emailTextView.setText(email);
        locationTextView.setText(location);
        this.userId = userId;
        
        ToggleButton followButton = (ToggleButton) findViewById(R.id.followButton);
        
    	SharedPreferences prefs = this.getSharedPreferences("com.boozr.app", Context.MODE_PRIVATE);
		String sessionId = prefs.getString("sessionId", null);
        
        try {
			String result = new API().execute("Follow/isUserFollowed", "sessionId", sessionId, "followeeId", userId).get();
			
			try {
                JSONObject jsonObject = new JSONObject(result);
                String status = jsonObject.getString("status");
                if(status.equals("200")) {
                	if(jsonObject.getString("details") == "true") {
                        followButton.setChecked(true);                		
                	} else {
                        followButton.setChecked(false);                		
                	}
                } else {
                    Messages.warning(this, "ERROR: Could not contact API.");
                }
            } catch(JSONException e) {
                e.printStackTrace();
            }
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void followButtonClick(View v) {
    	ToggleButton btn = ((ToggleButton) v);
    	
    	SharedPreferences prefs = this.getSharedPreferences("com.boozr.app", Context.MODE_PRIVATE);
    	
		String sessionId = prefs.getString("sessionId", null);
    	
    	if(btn.isChecked()) {
    		try {
				String result = new API().execute("Follow/followUser", "sessionId", sessionId, "followeeId", userId).get();
				
				try {
	                JSONObject jsonObject = new JSONObject(result);
	                String status = jsonObject.getString("status");
	                if(status.equals("200")) {
	                    btn.setChecked(true);
	                } else {
	                    Messages.warning(this, "ERROR: Could not unfollow user.");
	                }
	            } catch(JSONException e) {
	                e.printStackTrace();
	            }
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	} else {
    		try {
				String result = new API().execute("Follow/unfollowUser", "sessionId", sessionId, "followeeId", userId).get();
				
				try {
	                JSONObject jsonObject = new JSONObject(result);
	                String status = jsonObject.getString("status");
	                if(status.equals("200")) {
	                    btn.setChecked(false);
	                } else {
	                    Messages.warning(this, "ERROR: Could not follow user.");
	                }
	            } catch(JSONException e) {
	                e.printStackTrace();
	            }
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }
}
