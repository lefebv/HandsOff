package fr.lefebvre.handsoff;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Unlock extends Activity {
	Button   mButton;
	EditText mEdit;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_unlock);
		
	    mButton = (Button)findViewById(R.id.button1);
	    mEdit   = (EditText)findViewById(R.id.editText1);
	    final Intent intent = new Intent(this, service.class);
	    mButton.setOnClickListener(
	        new View.OnClickListener()
	        {
	            public void onClick(View view)
	            {
	            	String pass = mEdit.getText().toString();
	            	SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
	            	String savedpass = preferences.getString("password", "Error");
	            	if (pass.equals(savedpass))
	            	{
		            	Toast.makeText(getApplicationContext(), "Alarm Stopped", Toast.LENGTH_SHORT).show();
		            	stopService(intent);
		            	finish();
	            	}
	            }
	        });
	}
	@Override
	public void	onBackPressed() {
		//disable back button.
	}
}
