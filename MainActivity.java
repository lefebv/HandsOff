package fr.lefebvre.handsoff;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		if (service.isServiceRunning(getApplicationContext()))
		{
			Intent intent = new Intent(this, Unlock.class);
	    	startActivity(intent);
		}
		else
		{
			setContentView(R.layout.activity_main);
			SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());		
			if (shared.getBoolean("firstuse", true))
			{
				SharedPreferences.Editor editor = shared.edit();			
				editor.putBoolean("firstuse", false);
				editor.commit();
				AlertDialog.Builder manual = new AlertDialog.Builder(this).setTitle("How To");
				Resources res = getResources();
				CharSequence[] msgs = {res.getString(R.string.display_HowTo1), res.getString(R.string.display_HowTo2),
						res.getString(R.string.display_HowTo3), res.getString(R.string.display_Note)};
				manual.setItems(msgs, null);
				manual.setNeutralButton("OK", null);
				manual.show();
			}
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}
	}
	
//	@Override
//	public void 	onResume() {
//		super.onResume();
//		if (service.isServiceRunning(getApplicationContext()))
//		{
//			Intent intent = new Intent(this, Unlock.class);
//	    	startActivity(intent);
//		}
//	}
	
	public void onLaunch(View view)
	{
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		String savedpass = preferences.getString("password", "Error");
		if (savedpass != "Error")
		{
			Intent intent = new Intent(this, Alarm.class);
			startActivity(intent);
		}
		else
		{
			Toast.makeText(getApplicationContext(), "You must set a password before launching the alarm !", Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			// Ajouter un intent pour lancer une activité (avec xml correspondant) pour la page de setting.
			Intent intent = new Intent(this, SettingsActivity.class);
		    startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}
}
