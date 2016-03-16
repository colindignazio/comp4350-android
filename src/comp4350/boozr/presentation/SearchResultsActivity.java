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
                            //Selection Code
                            //setContentView(R.layout.activity_user);
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
                    					Intent profileIntent = new Intent(SearchResultsActivity.this, ProfileActivity.class);
                    					profileIntent.putExtra("username", user.getString("User_name"));
                    					profileIntent.putExtra("email", user.getString("User_email"));
                    					profileIntent.putExtra("location", user.getString("User_location"));
                    					SearchResultsActivity.this.startActivity(profileIntent);
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
