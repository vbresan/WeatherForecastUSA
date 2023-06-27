package biz.binarysolutions.weatherusa;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import biz.binarysolutions.weatherusa.forecast.ForecastHandler;
import biz.binarysolutions.weatherusa.location.LocationHandler;
import biz.binarysolutions.weatherusa.preferences.Preferences;
import biz.binarysolutions.weatherusa.util.WeatherLocation;

/**
 * TODO: app is rejected from Samsung Store. Check their report
 * 	received on support email address. Fix it.
 *
 */
public class MainActivity extends Activity {
	
	public static final int ZIP_LENGTH = 5;
	
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

		TextView view = findViewById(R.id.TextViewLocation);
		if (view != null) {
			view.setText(new WeatherLocation(location).toString());
		}
	}

	/**
	 * @param zip
	 *
	 */
	private void updateLocationView(String zip) {

		TextView view = findViewById(R.id.TextViewLocation);
		if (view != null) {
			view.setText(zip);
		}
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
		TextView view = findViewById(R.id.TextViewForecastRequest);
		view.setText(sdf.format(date));
	}

	/**
	 * 
	 */
	private void displayLastKnownLocation() {

		boolean isGPS = Preferences.isGPS(this);
		String  zip   = Preferences.getZIP(this);

		if (isGPS) {
			updateLocationView(locationHandler.getLastKnownLocation());
		} else if (zip.length() == ZIP_LENGTH) {
			updateLocationView(zip);
		}

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

		boolean isGPS = Preferences.isGPS(this);
		String  zip   = Preferences.getZIP(this);

		if (isGPS) {
			Location location = locationHandler.getLastKnownLocation();
			forecastHandler.updateForecast(location);
		} else if (zip.length() == ZIP_LENGTH) {
			forecastHandler.updateForecast(zip);
		}
	}
	
	/**
	 * 
	 */
	private void determineLocationDialog() {

		new AlertDialog.Builder(this)
			.setMessage(R.string.LocationChoice)
			.setPositiveButton(android.R.string.ok, null)
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
	private void refreshLocation() {

		boolean isGPS = Preferences.isGPS(this);
		String  zip   = Preferences.getZIP(this);

		if (isGPS) {
			if (locationHandler.hasProvider()) {
				locationHandler.requestLocationUpdate();
			} else {
				determineLocationDialog();
			}
		} else if (zip.length() != ZIP_LENGTH) {
			updateLocation();
		}
	}
	
	/**
	 * 
	 */
	private void updateLocation() {

		Intent intent = new Intent(this, LocationActivity.class);
		startActivity(intent);
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		locationHandler = new LocationHandler(this);
		forecastHandler = new ForecastHandler(this);

		setButtonListeners();

		AdHandler.initialize(this);
	}

	@Override
	protected void onResume() {
		super.onResume();

		displayLastKnownLocation();
		displayLastKnownForecast();

		refreshLocation();
		updateForecast();
	}

	/**
	 *
	 * @param location
	 */
	public void onLocationChanged(Location location) {

		updateLocationView(location);
		setLocationButtonEnabled(true);
		updateForecast();
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
}