package comp4350.boozr.presentation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import org.json.*;

import java.util.concurrent.ExecutionException;

import comp4350.boozr.R;
import comp4350.boozr.business.API;

public class CreateAccountActivity extends Activity
{
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        this.prefs = this.getSharedPreferences("com.boozr.app", Context.MODE_PRIVATE);
    }

    public void submitCreate(View v) {
        //Create the users account
        EditText username = (EditText)findViewById(R.id.usernameText);
        String usernameString = username.getText().toString();
        EditText password = (EditText)findViewById(R.id.passwordText);
        String passwordString = password.getText().toString();
        EditText email = (EditText)findViewById(R.id.emailText);
        String emailString = email.getText().toString();
        EditText location = (EditText)findViewById(R.id.locationText);
        String locationString = location.getText().toString();

        try {
            String result = new API().execute("user/createAccount", "userName", usernameString, "password", passwordString,
                                                                    "email", emailString, "location", locationString).get();
            try {
                JSONObject jsonObject = new JSONObject(result);
                String status = jsonObject.getString("status");
                if(status.equals("200")) {
                    //Creation successful, set the sessionId in the prefs and push the logged in home page
                    this.prefs.edit().putString("sessionId", jsonObject.getString("sessionId")).apply();
                    Intent homeIntent = new Intent(CreateAccountActivity.this, HomeActivity.class);
                    CreateAccountActivity.this.startActivity(homeIntent);
                } else {
                    Messages.warning(this, "Account could not be created.");
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
