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

                ListView reviewsList = (ListView)findViewById(R.id.searchResults);
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, reviewArrayList );

                reviewsList.setAdapter(arrayAdapter);
            } catch(JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
