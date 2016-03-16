package comp4350.boozr.presentation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
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

import comp4350.boozr.R;

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
                } else {
                    Spinner spinner = (Spinner)findViewById(R.id.spinner_sortBy);
                    spinner.setVisibility(View.GONE);
                    ListView reviewsList = (ListView)findViewById(R.id.searchResults);
                    UserAdapter adapter = new UserAdapter(this,R.layout.user_list_item, resultsArray,reviewArrayList);
                    reviewsList.setAdapter(adapter);
                }



            } catch(JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
