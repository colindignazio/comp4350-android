package comp4350.boozr.presentation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import comp4350.boozr.R;
import comp4350.boozr.business.API;

public class AdvancedSearchActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_search);
    }

    public void searchAdvanced(View v) {
        EditText searchText = (EditText)findViewById(R.id.input_name);
        String searchString = searchText.getText().toString();
        Intent searchIntent = new Intent(AdvancedSearchActivity.this, SearchResultsActivity.class);

        try {
            String result = new API().execute("Beer/search", "searchToken", searchString).get();
            try {
                JSONObject jsonObject = new JSONObject(result);
                String status = jsonObject.getString("status");
                if(status.equals("200")) {
                    //Beer search results
                    searchIntent.putExtra("results", jsonObject.getString("searchResults"));
                    searchIntent.putExtra("resultType", "Beer");
                    AdvancedSearchActivity.this.startActivity(searchIntent);
                } else if(status.equals("400")) {
                    //No results found
                    new AlertDialog.Builder(AdvancedSearchActivity.this)
                            .setTitle("No Results Found")
                            .setMessage("Your search query didn't return any results")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // dismiss
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
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
