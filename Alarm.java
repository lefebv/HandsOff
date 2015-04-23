package fr.lefebvre.handsoff;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Alarm extends ActionBarActivity  {
	static TextView tv;
	CountDownTimer timer;
	Button b;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (service.isServiceRunning(getApplicationContext()))
		{
			Intent intent = new Intent(this, Unlock.class);
	    	startActivity(intent);
	    	finish();
		}
		else
		{
			setContentView(R.layout.activity_alarm);
		   
			tv = (TextView) findViewById(R.id.Count);
		
			timer = new CountDownTimer(10000, 1000) {
			     public void onTick(long millisUntilFinished) {
			    	tv.setText("Alarm will start in: " + millisUntilFinished / 1000);		 		
			     }
			     public void onFinish() {
			    	Intent intent = new Intent();
			    	intent.setClassName("fr.lefebvre.handsoff", "fr.lefebvre.handsoff.service");
			    	startService(intent);
					Toast.makeText(getApplicationContext(),"Alarm launched", Toast.LENGTH_SHORT).show();
			    	Intent intent2 = new Intent();
			    	intent2.setClassName("fr.lefebvre.handsoff", "fr.lefebvre.handsoff.Unlock");
			    	startActivity(intent2);
			    	finish();
			     }
			  }.start();
			   
			 b = (Button) findViewById(R.id.button1);
		     b.setOnClickListener(new View.OnClickListener() {
		         public void onClick(View v) {
	      		timer.cancel();
	      		finish();
	      		}
		     });
		}
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.alarm, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Intent intent = new Intent(this, SettingsActivity.class);
		    startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();		
  		timer.cancel();
	}
}
