package comp4350.boozr.presentation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import java.util.ArrayList;

import org.json.*;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import comp4350.boozr.R;
import comp4350.boozr.business.API;

public class SearchResultsActivity extends Activity
{
    private String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search_results);

        String results = "{}";
        String resultType = "";
        JSONArray resultsArray;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            resultType = extras.getString("resultType");
            results = extras.getString("results");
            userId = extras.getString("userId");

            try {
                resultsArray = new JSONArray(results);
                List<String> reviewArrayList = new ArrayList<String>();
                for(int i = 0; i < resultsArray.length(); i++) {
                    if(resultType.equals("Beer")){
                        reviewArrayList.add(resultsArray.getJSONObject(i).getString("Name"));
                    } else {
                        reviewArrayList.add(resultsArray.getJSONObject(i).getString("User_name"));
                    }

                }

                if(resultType.equals("Beer")) {
                    Spinner spinner = (Spinner)findViewById(R.id.spinner_sortBy);
                    spinner.setVisibility(View.VISIBLE);
                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                            switch(position){
                                case 0:
                                    break;
                                case 1:
                                    break;
                                case 2:
                                    break;
                                case 3:
                                    break;
                                default:

                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parentView) {
                            // your code here
                        }

                    });
                    ListView reviewsList = (ListView)findViewById(R.id.searchResults);
                    BeerAdapter adapter = new BeerAdapter(this,R.layout.beer_list_item, resultsArray,reviewArrayList);
                    reviewsList.setAdapter(adapter);
                    reviewsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position,
                                                long id) {
                            TextView drinkid = (TextView) view.findViewById(R.id.drink_id);
                        	String dId = (String) drinkid.getText();
                        	Log.d("A", "Clicked");
                        	
                        	try {
                    			String result = new API().execute("beer/searchById", "beverage_id", dId).get();
                    			try {
                    				Log.d("Debug", "API call succeeded, beverage id was " + dId);
                    				JSONObject jsonObject = new JSONObject(result);
                    				String status = jsonObject.getString("status");
                    				Log.d("Debug", "Status " + status);
                    				if(status.equals("200")) {
                    					//User found
                    					JSONArray drinkA = jsonObject.getJSONArray("results");
                    					JSONObject drink = drinkA.getJSONObject(0);
                    					Intent drinkIntent = new Intent(SearchResultsActivity.this, DrinkActivity.class);
                    					drinkIntent.putExtra("drinkname", drink.getString("Name"));
                    					drinkIntent.putExtra("type", drink.getString("Type"));
                    					drinkIntent.putExtra("alc", drink.getString("Alcohol_By_Volume"));
                    					drinkIntent.putExtra("rating", drink.getString("Rating"));
                    					drinkIntent.putExtra("price", drink.getString("AvgPrice"));
                    					drinkIntent.putExtra("brewery", drink.getString("Brewery"));
                                        drinkIntent.putExtra("beerId", drink.getString("Beer_id"));
                                        drinkIntent.putExtra("userId", userId);
                                        SearchResultsActivity.this.startActivity(drinkIntent);
                    				}
                    			} catch(JSONException e) {
                    				e.printStackTrace();
                    			}
                    		} catch(InterruptedException e) {

                    		} catch(ExecutionException e) {
                    			e.printStackTrace();
                    		}
                        }
                    });
                } else {
                    Spinner spinner = (Spinner)findViewById(R.id.spinner_sortBy);
                    spinner.setVisibility(View.GONE);
                    ListView reviewsList = (ListView)findViewById(R.id.searchResults);
                    UserAdapter adapter = new UserAdapter(this,R.layout.user_list_item, resultsArray,reviewArrayList);
                    reviewsList.setAdapter(adapter);
                    
                    reviewsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        	TextView userId = (TextView) view.findViewById(R.id.user_id);
                        	String uid = (String) userId.getText();
                        	
                        	try {
                    			String result = new API().execute("user/getUser", "userId", uid).get();
                    			try {
                    				JSONObject jsonObject = new JSONObject(result);
                    				String status = jsonObject.getString("status");
                    				if(status.equals("200")) {
                    					//User found
                    					JSONObject user = jsonObject.getJSONObject("user");
                    					Intent userIntent = new Intent(SearchResultsActivity.this, UserActivity.class);
                    					userIntent.putExtra("username", user.getString("User_name"));
                    					userIntent.putExtra("email", user.getString("User_email"));
                    					userIntent.putExtra("location", user.getString("User_location"));
                    					//Log.d("Debug", "User Reviews from Search Results activity" + user.getString("reviews").toString());
                    					userIntent.putExtra("reviews", user.getString("reviews").toString());
                    					SearchResultsActivity.this.startActivity(userIntent);
                    				}
                    			} catch(JSONException e) {
                    				e.printStackTrace();
                    			}
                    		} catch(InterruptedException e) {

                    		} catch(ExecutionException e) {
                    			e.printStackTrace();
                    		}
                        }
                    }); 
                }
            } catch(JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void selectResult(View v) {
        Log.d("A", "A");
    }
}
