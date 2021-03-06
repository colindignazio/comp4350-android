package comp4350.boozr.presentation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ListView;
import java.util.ArrayList;

import org.json.*;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import comp4350.boozr.R;
import comp4350.boozr.objects.API;

public class SearchResultsActivity extends Activity {
    private String userId;
	Spinner spinner;
	TextView spinnerLabel;
	JSONArray resultsArray = new JSONArray();
    List<String> resultsList = new ArrayList<String>();
    BeerAdapter adapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
            userId = extras.getString("userId");
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
				public void onNothingSelected(AdapterView<?> parent) {}
            });
            
            ListView reviewsList = (ListView)findViewById(R.id.searchResults);
            adapter = new BeerAdapter(this,R.layout.beer_list_item, resultsArray,resultsList);
            reviewsList.setAdapter(adapter);
            reviewsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView drinkid = (TextView) view.findViewById(R.id.drink_id);
                	String dId = (String) drinkid.getText();
                	
                	try {
                		String beverage = new API().execute("beer/searchById", "beverage_id", dId).get();
            			String reviews = new API().execute("BeerReview/getSpecificBeerReviews", "beer_id", dId).get();
            			try {
            				JSONObject jsonObject = new JSONObject(beverage);
            				String status = jsonObject.getString("status");
            				
            				JSONObject reviewsObject = new JSONObject(reviews);
            				String status2 = jsonObject.getString("status");
            				
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
                                drinkIntent.putExtra("userId", userId);
                                drinkIntent.putExtra("beerId", drink.getString("Beer_id"));
                                SearchResultsActivity.this.startActivity(drinkIntent);
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
            					userIntent.putExtra("userId", uid);
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
            			e.printStackTrace();
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
                e.printStackTrace();
            }
        }
    	
        Collections.sort(jsonList, new Comparator<JSONObject>() {
            public int compare(JSONObject a, JSONObject b) {
            	int comp = 0;
            	 if (filter == "Alcohol_By_Volume" || filter == "Rating" || filter =="AvgPrice") {
            		 Double valA = 0.0;
            		 Double valB = 0.0;
            		 
            		 try {
                         valA = Double.parseDouble((String) a.get(filter)) ;
                         valB = Double.parseDouble((String) b.get(filter)) ;
                     } catch (JSONException e) {
                         //do something
                     }
            		 if (filter == "Alcohol_By_Volume" || filter == "Rating") {
            			 comp =  valB.compareTo(valA);
            		 } else {
            			 comp =  valA.compareTo(valB);
            		 }
            	 } else{
            		 String valA = new String();
                     String valB = new String();

                     try {
                         valA = (String) a.get(filter);
                         valB = (String) b.get(filter);
                     } catch (JSONException e) {
                         e.printStackTrace();
                     }

                     comp =  valA.compareTo(valB);
            	 }
               
    	        return comp;
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
}
