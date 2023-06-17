package biz.binarysolutions.weatherusa;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

		setForecastButtonEnabled(false);
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
		if (button != null) {
			button.setOnClickListener(v -> refreshForecast());
		}

		button = (Button) findViewById(R.id.ButtonRefreshLocation);
		if (button != null) {
			button.setOnClickListener(v -> refreshLocation());
		}
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
	private void goToLocationSources() {
		startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
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

		AdHandler.initialize(this);

		refreshLocation();
		refreshForecast();
	}

	@Override
	public void onPause() {
		//TODO: this is saving location only, refactor it!
		Preferences.save(getPreferences(MODE_PRIVATE), locationHandler);
		super.onPause();
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
	public void onForecastAvailable() {
		updateForecastRequestView(new Date());
		setForecastButtonEnabled(true);
	}
	
	@Override
	public void onForecastUnavailable() {
		showDialog(DialogCode.FORECAST_UNAVAILABLE);
		setForecastButtonEnabled(true);
	}

	@Override
	public void onConnectionError() {
		showDialog(DialogCode.WIRELESS_CONTROLS);
	}
}