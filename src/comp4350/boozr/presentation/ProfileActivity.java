package comp4350.boozr.presentation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import org.json.*;

import java.util.concurrent.ExecutionException;

import comp4350.boozr.R;
import comp4350.boozr.objects.API;

public class ProfileActivity extends Activity {
    private String username = "";
    private String location = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);

        String username = "Error Username Not Found";
        String email = "Error Email Not Found";
        String location = "Error Location Not Found";
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            username = extras.getString("username");
            email = extras.getString("email");
            location = extras.getString("location");
        }

        EditText usernameTextView = (EditText)findViewById(R.id.usernameText);
        EditText emailTextView = (EditText)findViewById(R.id.emailText);
        EditText locationTextView = (EditText)findViewById(R.id.locationText);
        usernameTextView.setText(username);
        emailTextView.setText(email);
        locationTextView.setText(location);
        this.username = username;
        this.location = location;
    }

    public void saveProfile(View v) {
        EditText usernameText = (EditText)findViewById(R.id.usernameText);
        String username = usernameText.getText().toString();
        EditText locationText = (EditText)findViewById(R.id.locationText);
        String location = locationText.getText().toString();

        if(this.username.equals(username) && this.location.equals(location)) {
            //No fields changed
            new AlertDialog.Builder(ProfileActivity.this)
                    .setTitle("No Fields Changed")
                    .setMessage("You have not changed any fields")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {}
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        } else {
            SharedPreferences prefs = this.getSharedPreferences(
                    "com.boozr.app", Context.MODE_PRIVATE);
            String sessionId = prefs.getString("sessionId", null);

            if(!this.username.equals(username)) {
                //change username
                try {
                    String result = new API().execute("user/setUsername", "sessionId", sessionId, "userName", username).get();
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String status = jsonObject.getString("status");
                        if(status.equals("200")) {
                            //update successful
                            this.username = username;
                            new AlertDialog.Builder(ProfileActivity.this)
                            	.setTitle("Username Updated")
                                .setMessage("Your Username has been updated")
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {}
                                    })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                        } else if(status.equals("400")) {
                            //error updating
                            new AlertDialog.Builder(ProfileActivity.this)
                                .setTitle("Error Updating Username")
                                .setMessage("Your Username has not been updated, error: " + jsonObject.getString("details"))
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {}
                                    })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
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

            if(!this.location.equals(location)) {
                //change location
                try {
                    String result = new API().execute("user/setLocation", "sessionId", sessionId, "location", location).get();
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String status = jsonObject.getString("status");
                        if(status.equals("200")) {
                            //update successful
                            this.location = location;
                            new AlertDialog.Builder(ProfileActivity.this)
                                .setTitle("Location Updated")
                                .setMessage("Your location has been updated")
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {}
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                        } else if(status.equals("400")) {
                            //error updating
                            new AlertDialog.Builder(ProfileActivity.this)
                                .setTitle("Error Updating Location")
                                .setMessage("Your location has not been updated, error: " + jsonObject.getString("details"))
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {}
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
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
        }
    }
}
