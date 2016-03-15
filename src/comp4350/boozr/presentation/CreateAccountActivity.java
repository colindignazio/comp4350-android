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

public class CreateAccountActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_account);
    }

    public void submitCreate(View v) {
        //Create the users account
    }
}
