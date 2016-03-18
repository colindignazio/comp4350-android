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
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import comp4350.boozr.R;
import comp4350.boozr.business.API;

public class SearchResultsActivity extends Activity
{
	Spinner spinner;
	TextView spinnerLabel;
	JSONArray resultsArray = new JSONArray();
    List<String> resultsList = new ArrayList<String>();
    BeerAdapter adapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search_results);
        spinner = (Spinner)findViewById(R.id.spinner_sortBy);
        spinnerLabel = (TextView)findViewById(R.id.label_spinner);

        String results = "{}";
        String resultType = "";
        
        List<JSONObject> resultsJsonList = new ArrayList<JSONObject>();
        
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            resultType = extras.getString("resultType");
            results = extras.getString("results");

            try {
                resultsArray = new JSONArray(results);
                
                for(int i = 0; i < resultsArray.length(); i++) {
                	resultsJsonList.add(resultsArray.getJSONObject(i));
                	if(resultType.equals("Beer")){
                    	resultsList.add(resultsArray.getJSONObject(i).getString("Name"));
                    	
                    } else {
                    	resultsList.add(resultsArray.getJSONObject(i).getString("User_name"));
                    }

                }
            } catch(JSONException e) {
                e.printStackTrace();
            }
        }
        if(resultType!=null && resultType.equals("Beer")) {
            spinner.setVisibility(View.VISIBLE);
            spinnerLabel.setVisibility(View.VISIBLE);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                
                    switch(parentView.getItemAtPosition(position).toString()){
                        case "Name":
                        	sortBy("Name");
                        	updateData();
                            break;
                        case "Brewery":
                        	sortBy("Brewery");
                        	updateData();
                            break;
                        case "Type":
                        	sortBy("Type");
                        	updateData();
                            break;
                        case "Alcohol":
                        	sortBy("Alcohol_By_Volume");
                        	updateData();
                            break;
                        case "Rating":
                        	sortBy("Rating");
                        	updateData();
                            break;
                        case "Price":
                        	sortBy("AvgPrice");
                        	updateData();
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
            adapter = new BeerAdapter(this,R.layout.beer_list_item, resultsArray,resultsList);
            reviewsList.setAdapter(adapter);
            reviewsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {
                    TextView drinkid = (TextView) view.findViewById(R.id.drink_id);
                	String dId = (String) drinkid.getText();
                	Log.d("A", "Clicked");
                	
                	try {
                		String beverage = new API().execute("beer/searchById", "beverage_id", dId).get();
            			String reviews = new API().execute("BeerReview/getSpecificBeerReviews", "beer_id", dId).get();
            			try {
            				Log.d("Debug", "API call succeeded, beverage id was " + dId);
            				JSONObject jsonObject = new JSONObject(beverage);
            				String status = jsonObject.getString("status");
            				
            				JSONObject reviewsObject = new JSONObject(reviews);
            				String status2 = jsonObject.getString("status");
            				
            				Log.d("Debug", "Status " + status);
            				if(status.equals("200")&&status2.equals("200")) {
            					JSONArray drinkA = jsonObject.getJSONArray("results");
            					JSONObject drink = drinkA.getJSONObject(0);
            					
            					JSONArray reviewsArray = reviewsObject.getJSONArray("results");
            					
            					
            					Intent drinkIntent = new Intent(SearchResultsActivity.this, DrinkActivity.class);
            					drinkIntent.putExtra("drinkname", drink.getString("Name"));
            					drinkIntent.putExtra("type", drink.getString("Type"));
            					drinkIntent.putExtra("alc", drink.getString("Alcohol_By_Volume"));
            					drinkIntent.putExtra("rating", drink.getString("Rating"));
            					drinkIntent.putExtra("price", drink.getString("AvgPrice"));
            					drinkIntent.putExtra("brewery", drink.getString("Brewery"));
            					drinkIntent.putExtra("reviews", reviewsArray.toString());
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
            spinnerLabel.setVisibility(View.GONE);
            ListView reviewsList = (ListView)findViewById(R.id.searchResults);
            UserAdapter adapter = new UserAdapter(this,R.layout.user_list_item, resultsArray,resultsList);
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
    }
    private void sortBy(final String filter) {
    	JSONArray sortedJsonArray = new JSONArray();
    	List<JSONObject> jsonList = new ArrayList<JSONObject>();
    	
    	for (int i = 0; i < resultsArray.length(); i++) {
    	    try {
				jsonList.add(resultsArray.getJSONObject(i));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	Collections.sort( jsonList, new Comparator<JSONObject>() {

    	    public int compare(JSONObject a, JSONObject b) {
    	        String valA = new String();
    	        String valB = new String();

    	        try {
    	            valA = (String) a.get(filter);
    	            valB = (String) b.get(filter);
    	        } 
    	        catch (JSONException e) {
    	            //do something
    	        }
    	        
    	        if(filter=="Alcohol_By_Volume" || filter=="Rating" ){
    	        	//Return High to Low
    	        	return valB.compareTo(valA);
    	        } else {
    	        	return valA.compareTo(valB);
    	        }
    	        	
    	        
    	    }
    	});
    	
    	for (int i = 0; i < resultsArray.length(); i++) {
    	    sortedJsonArray.put(jsonList.get(i));
    	}
    	resultsArray = sortedJsonArray;
	}
    
    
    private void updateData() {
    	if(adapter!=null){
    		adapter.data = resultsArray;
    	    adapter.notifyDataSetChanged();
    	}
       
    }
    
    public void selectResult(View v) {
        Log.d("A", "A");
    }
}
