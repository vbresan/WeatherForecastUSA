package biz.binarysolutions.weatherusa;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.text.SimpleDateFormat;
import java.util.Date;

import biz.binarysolutions.weatherusa.components.forecast.ForecastHandler;
import biz.binarysolutions.weatherusa.components.forecast.ForecastHandlerListener;
import biz.binarysolutions.weatherusa.components.location.LocationHandler;
import biz.binarysolutions.weatherusa.components.location.LocationHandlerListener;
import biz.binarysolutions.weatherusa.components.preferences.Preferences;
import biz.binarysolutions.weatherusa.dialog.DialogBuilder;
import biz.binarysolutions.weatherusa.dialog.DialogCode;
import biz.binarysolutions.weatherusa.util.location.LocationFormatter;

/**
 * TODO: app is rejected from Samsung Store. Check their report
 * 	received on support email address. Fix it.
 *
 */
public class MainActivity 
	extends Activity
	implements 
		LocationHandlerListener, 
		ForecastHandlerListener {
	
	private static final int ZIP_LENGTH = 5;
	
	private LocationHandler locationHandler;
	private ForecastHandler forecastHandler;

	/**
	 * @param location 
	 * 
	 */
	private void updateLocationView(Location location) {
	
		if (location != null) {
			TextView view = (TextView) findViewById(R.id.TextViewLocation);
			view.setText(LocationFormatter.format(location));
			
			setForecastButtonEnabled(true);
		}
	}
	
	/**
	 * 
	 * @param date
	 */
	private void updateForecastRequestView(Date date) {
		
		if (date != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			TextView view = (TextView) findViewById(R.id.TextViewForecastRequest);
			view.setText(sdf.format(date));
		}
	}

	/**
	 * 
	 */
	private void displayLastKnownLocation() {
		updateLocationView(locationHandler.getLastKnownLocation());
	}
	

	
	/**
	 * TODO: save it in preferences?
	 */
	private void displayLastKnownForecast() {
		
		Date date = forecastHandler.getLastKnownForecastDate();
		updateForecastRequestView(date);
		
		forecastHandler.getLastKnownForecast();
	}
	
	/**
	 * 
	 */
	private void refreshForecast() {
		Location location = locationHandler.getLastKnownLocation();
		forecastHandler.updateForecast(location);
	}
	
	/**
	 * 
	 */
	public void zipCodeEntry() {
		
		final Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.dialog_zipcode);
		dialog.setTitle(R.string.EnterZIPCode);
		
		final EditText editText = (EditText) dialog.findViewById(R.id.EditTextZIP);
		final Button button = (Button) dialog.findViewById(R.id.ButtonZIPContinue);
		
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				String zip = editText.getText().toString();
				if (zip.length() == ZIP_LENGTH) {
					
					dialog.dismiss();
					setLocationButtonEnabled(false);
					locationHandler.setLocationUsingZIP(zip);
				}
			}
		});

		dialog.show();
	}

	/**
	 * 
	 */
	private void determineLocationDialog() {
		
		DialogInterface.OnClickListener goToSettingsListener = 
			new DialogInterface.OnClickListener() {
		
				@Override
				public void onClick(DialogInterface dialog, int which) {
					goToLocationSources();
				}
		};

		DialogInterface.OnClickListener enterZIPCodeListener = 
			new DialogInterface.OnClickListener() {
		
				@Override
				public void onClick(DialogInterface dialog, int which) {
					zipCodeEntry();
				}
		};		
		
		
		new AlertDialog.Builder(this)
			.setMessage(R.string.LocationChoice)
			.setPositiveButton(R.string.EnableGPS, goToSettingsListener)
			.setNegativeButton(R.string.EnterZIPCode, enterZIPCodeListener)
			.show();			
	}
	
	/**
	 * 
	 */
	private void refreshLocation() {
		
		if (locationHandler.hasProvider()) {
			locationHandler.requestLocationUpdate();
		} else {
			determineLocationDialog();
		}
	}
	
	/**
	 * 
	 * @param enabled
	 */
	private void setLocationButtonEnabled(boolean enabled) {
		
		Button button = (Button) findViewById(R.id.ButtonRefreshLocation);
		button.setEnabled(enabled);		
	}
	
	/**
	 * 
	 * @param enabled
	 */
	private void setForecastButtonEnabled(boolean enabled) {
		
		Button button = (Button) findViewById(R.id.ButtonRefreshForecast);
		button.setEnabled(enabled);		
	}	

	/**
	 * 
	 */
	private void setButtonListeners() {
		
		Button button = (Button) findViewById(R.id.ButtonRefreshForecast);
		button.setOnClickListener(new OnClickListener() {
	
			@Override
			public void onClick(View view) {
				refreshForecast();
			}
		});
		
		
		button = (Button) findViewById(R.id.ButtonRefreshLocation);
		button.setOnClickListener(new OnClickListener() {
	
			@Override
			public void onClick(View view) {
				refreshLocation();
			}
		});
	}
	
	/**
	 * 
	 */
	private void setLocationHandler() {
		
		locationHandler = new LocationHandler(
				(LocationManager) getSystemService(Context.LOCATION_SERVICE), 
				this
			);
	}
	
	/**
	 * 
	 */
	private void setForecastHandler() {
		forecastHandler = new ForecastHandler(this, this);
	}
	
	/**
	 * 
	 */
	private void goToSettings() {
		
		Intent settings = new Intent(this, PreferencesActivity.class);
    	startActivity(settings);
	}	
	
	/**
	 * 
	 */
	private void goToLocationSources() {
		startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
	}
	
	/**
	 * 
	 * @param preferences
	 * @return
	 */
	private boolean shouldRefreshLocation(SharedPreferences preferences) {
	
		String key          = getString(R.string.preferences_locationUpdates_key);
		String defaultValue = getString(R.string.preferences_locationUpdates_default_value);
		
		return preferences.getBoolean(key, Boolean.getBoolean(defaultValue));
	}

	/**
	 * 
	 * @param preferences
	 * @return
	 */
	private boolean shouldRefreshForecast(SharedPreferences preferences) {
	
		String key          = getString(R.string.preferences_forecastUpdates_key);
		String defaultValue = getString(R.string.preferences_forecastUpdates_default_value);
		
		return preferences.getBoolean(key, Boolean.getBoolean(defaultValue));
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	    
	    setLocationHandler();
	    setForecastHandler();
	    
	    setForecastButtonEnabled(false);
	    Preferences.load(getPreferences(MODE_PRIVATE), locationHandler);
	
	    displayLastKnownLocation();
	    displayLastKnownForecast();
	    setButtonListeners();

		MobileAds.initialize(this);
		AdView mAdView = findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().build();
		mAdView.loadAd(adRequest);
	}
	
	@Override
	protected void onResume() {
		
		SharedPreferences preferences = 
			PreferenceManager.getDefaultSharedPreferences(this);
		
		if (shouldRefreshLocation(preferences)) {
			refreshLocation();
		}
		
		if (shouldRefreshForecast(preferences)) {
			refreshForecast();
		}
		
		super.onResume();
	}

	@Override
	public void onPause() {
		forecastHandler.saveForecast();
		Preferences.save(getPreferences(MODE_PRIVATE), locationHandler);
		super.onPause();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu, menu);
	    return true;		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
	 	switch (item.getItemId()) {
	 	
	 	case R.id.menuItemSettings:
	 		goToSettings();
	 		return true;
	 	
	 	case R.id.menuItemRefreshForecast:
	 		refreshForecast();
	 		return true;
	 		
	 	case R.id.menuItemRefreshLocation:
	 		refreshLocation();
	 		return true;

        case R.id.menuItemLocationSources:
        	goToLocationSources();
        	return true;        	
        }
		
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		
		Dialog dialog = DialogBuilder.get(id, this);
		if (dialog == null) {
			dialog = super.onCreateDialog(id);
		}
			
		return dialog;
	}

	@Override
	public void onLocationChanged(Location location) {
		updateLocationView(location);
		setLocationButtonEnabled(true);
	}

	@Override
	public void onForecastAvailable(String forecast) {
		updateForecastRequestView(new Date());
	}
	
	@Override
	public void onForecastUnavailable() {
		showDialog(DialogCode.FORECAST_UNAVAILABLE);
	}

	@Override
	public void onConnectionError() {
		showDialog(DialogCode.WIRELESS_CONTROLS);
	}
}