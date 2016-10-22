package com.example.circleseekbar;

import com.example.circleseekbar.DutyView.onDutyChangedListener;

import android.app.Activity;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends Activity {

	
	DutyView dutyView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		dutyView = (DutyView) findViewById(R.id.dutyView);
		
		dutyView.setOnDutyChangedListener(new onDutyChangedListener() {
			
			@Override
			public void onDutyChanged(int currentDuty) {
				// TODO Auto-generated method stub
				Toast.makeText(MainActivity.this, "current duty = "+currentDuty, Toast.LENGTH_SHORT)
						.show();
			}
		});
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
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
