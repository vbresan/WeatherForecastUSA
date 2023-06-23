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
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import biz.binarysolutions.weatherusa.components.forecast.ForecastHandler;
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
	implements LocationHandlerListener {
	
	private static final int ZIP_LENGTH = 5;
	
	private LocationHandler locationHandler;
	private ForecastHandler forecastHandler;

	/**
	 * @param location 
	 * 
	 */
	private void updateLocationView(Location location) {
	
		if (location == null) {
			return;
		}

		TextView view = (TextView) findViewById(R.id.TextViewLocation);
		view.setText(LocationFormatter.format(location));

		setForecastButtonEnabled(true);
	}
	
	/**
	 * 
	 * @param date
	 */
	private void updateForecastRequestView(Date date) {

		if (date == null) {
			return;
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		TextView view = (TextView) findViewById(R.id.TextViewForecastRequest);
		view.setText(sdf.format(date));
	}

	/**
	 * 
	 */
	private void displayLastKnownLocation() {
		updateLocationView(locationHandler.getLastKnownLocation());
	}

	/**
	 *
	 */
	private void displayLastKnownForecast() {
		
		Date date = forecastHandler.getLastKnownForecastDate();
		updateForecastRequestView(date);
		
		forecastHandler.getLastKnownForecast();
	}
	
	/**
	 * 
	 */
	private void updateForecast() {

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
		
		final EditText editText = dialog.findViewById(R.id.EditTextZIP);
		final Button button = dialog.findViewById(R.id.ButtonZIPContinue);
		
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
			.setView(R.layout.dialog_zipcode)
			.setPositiveButton(R.string.UseGPS, goToSettingsListener)
			.setNegativeButton(R.string.EnterZIPCode, enterZIPCodeListener)
			.show();			
	}

	/**
	 *
	 */
	private void showDialogForecastUnavailable() {

		new AlertDialog.Builder(this)
			.setIcon(android.R.drawable.ic_dialog_alert)
			.setTitle(R.string.ErrorGettingForecast)
			.setMessage(R.string.ForecastUnavailable)
			.setPositiveButton(android.R.string.ok, null)
			.show();
	}
	
	/**
	 * 
	 */
	private void updateLocation() {

		Intent intent = new Intent(this, LocationActivity.class);
		startActivity(intent);

		//TODO: fix this
		//TODO: on location change refresh forecast
		//determineLocationDialog();

		/*
		if (locationHandler.hasProvider()) {
			locationHandler.requestLocationUpdate();
		} else {
			determineLocationDialog();
		}
		*/
	}
	
	/**
	 * 
	 * @param enabled
	 */
	private void setLocationButtonEnabled(boolean enabled) {
		
		Button button = findViewById(R.id.buttonUpdateLocation);
		if (button != null) {
			button.setEnabled(enabled);
		}
	}
	
	/**
	 * 
	 * @param enabled
	 */
	private void setForecastButtonEnabled(boolean enabled) {
		
		Button button = findViewById(R.id.buttonUpdateForecast);
		if (button != null) {
			button.setEnabled(enabled);
		}
	}

	/**
	 *
	 * @param visible
	 */
	private void setAlertVisible(boolean visible) {

		ImageView imageView = findViewById(R.id.imageViewAlert);
		if (imageView != null) {
			imageView.setVisibility(visible? View.VISIBLE : View.INVISIBLE);
		}
	}

	/**
	 * 
	 */
	private void setButtonListeners() {
		
		Button button = findViewById(R.id.buttonUpdateForecast);
		if (button != null) {
			button.setOnClickListener(v -> updateForecast());
		}

		button = findViewById(R.id.buttonUpdateLocation);
		if (button != null) {
			button.setOnClickListener(v -> updateLocation());
		}

		ImageView imageView = findViewById(R.id.imageViewAlert);
		if (imageView != null) {
			imageView.setOnClickListener(v ->
				showDialogForecastUnavailable()
			);
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
		forecastHandler = new ForecastHandler(this);
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
		setContentView(R.layout.activity_main);
	    
	    setLocationHandler();
	    setForecastHandler();

		Preferences.load(getPreferences(MODE_PRIVATE), locationHandler);

		displayLastKnownLocation();
		displayLastKnownForecast();
		setButtonListeners();

		AdHandler.initialize(this);

		//TODO: uncomment this
		//updateLocation();
		updateForecast();
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

	/**
	 *
	 */
	public void onForecastAvailable() {
		setAlertVisible(false);
		updateForecastRequestView(new Date());
		setForecastButtonEnabled(true);
	}

	/**
	 *
	 */
	public void onForecastUnavailable() {
		setAlertVisible(true);
		setForecastButtonEnabled(true);
	}

	@Override
	public void onConnectionError() {
		showDialog(DialogCode.WIRELESS_CONTROLS);
	}
}