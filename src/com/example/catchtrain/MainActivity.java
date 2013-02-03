package com.example.catchtrain;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.format.Time;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity {
	LocationManager locationManager;
	LocationListener locationListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

		locationListener = new LocationListener() {
			public void onLocationChanged(Location location) {
				TextView tGpsView = (TextView)findViewById(R.id.textView10);
				TextView tEstView = (TextView)findViewById(R.id.textView6);
				TextView tStatusView = (TextView)findViewById(R.id.textView9);
				TextView tAdviseView = (TextView)findViewById(R.id.textView8);
				float speed = location.getSpeed();


				Location dest = new Location("");
				dest.setLatitude(51.663426);
				dest.setLongitude(-0.396924);

				/* set GPS data */
				tGpsView.setText
				(
					"GPS:" + speed + "[m/h]," +
					"dst:" + location.distanceTo(dest) + "[m]" +
					"loc:" + location.getLatitude() + "," + location.getLongitude()
				);

				/* set estimated arrival */
				if( speed > 0 ) {
					float t = location.distanceTo(dest) / location.getSpeed();
					Calendar cal = Calendar.getInstance();
					int year = cal.get(Calendar.YEAR);
					int month = cal.get(Calendar.MONTH);
					int day = cal.get(Calendar.DAY_OF_MONTH);

					cal.add(Calendar.SECOND, (int) t);
					Date dateEst = cal.getTime();
					String dateEstString = (new SimpleDateFormat("HH:mm:ss")).format(dateEst);
					tEstView.setText(dateEstString );

					/* Set advise */
					Date dateTarget = new Date( year, month, day, 10, 50);

					long timeDiff = dateEst.getTime() - dateTarget.getTime();
					String adviseStr;
					long oneMin = 1 * 60 * 1000;
					if( timeDiff > oneMin )
					{
						adviseStr = "Slow down";
					} else if( timeDiff > - oneMin ){
						adviseStr = "Perfect timing";
					} else {
						adviseStr = "Run!";
					}
					CharSequence aheadLate = "ahead";
					if( timeDiff < 0 )
					{
						aheadLate = "late";
					}
					tAdviseView.setText(adviseStr + (new SimpleDateFormat("HH:mm:ss")).format(timeDiff) + " " + aheadLate);
				} else {
					tEstView.setText("inf");
					tAdviseView.setText("Go");
				}

				/* Set public status */
				if( speed > 5 ) {
					tStatusView.setText("Your status: running");
				} else if( speed > 0) {
					tStatusView.setText("Your status: walking");
				} else {
					tStatusView.setText("Your status: stopped");
				}
			}

			public void onStatusChanged(String provider, int status, Bundle extras) {}

			public void onProviderEnabled(String provider) {}

			public void onProviderDisabled(String provider) {}
		};

	   locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
//	   locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	protected void onDestroy() {
		super.onDestroy();
		locationManager.removeUpdates(locationListener);
	}
}
