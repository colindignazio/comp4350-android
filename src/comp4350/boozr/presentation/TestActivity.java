package comp4350.boozr.presentation;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import comp4350.boozr.R;

public class TestActivity extends Activity 
{	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_test);
	}

	public void buttonSubmitOnClick(View v) 
	{
		EditText field1 = (EditText)findViewById(R.id.textField);
		String value = field1.getText().toString();
		
		Messages.warning(this, value);
	}
}
