package biz.binarysolutions.weatherusa.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.provider.Settings;

import biz.binarysolutions.weatherusa.MainActivity;
import biz.binarysolutions.weatherusa.R;

/**
 * 
 *
 */
public class DialogBuilder {

	/**
	 *
	 * @param context
	 */
	private static Dialog getWirelessControlsDialog(final Context context) {

		DialogInterface.OnClickListener goToSettingsListener =
			new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					context.startActivity(
							new Intent(Settings.ACTION_WIRELESS_SETTINGS)
					);
				}
			};

		return new AlertDialog.Builder(context)
			.setTitle(R.string.Error)
			.setMessage(R.string.ErrorConnecting)
			.setPositiveButton(android.R.string.yes, goToSettingsListener)
			.setNegativeButton(android.R.string.no, null)
			.create();
	}

	/**
	 *
	 * @param context
	 * @return
	 */
	private static Dialog getOnForecastUnavailableDialog(final Context context) {

		OnClickListener listener = new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (context instanceof MainActivity) {
					((MainActivity) context).zipCodeEntry();
				}
			}
		};
		return new AlertDialog.Builder(context)
			.setIcon(android.R.drawable.ic_dialog_alert)
			.setTitle(R.string.Error)
			.setMessage(R.string.ForecastUnavailable)
			.setPositiveButton(android.R.string.ok, listener)
			.create();
	}

	/*

	 */
	public static Dialog get(int dialogId, Context context) {
		
		Dialog dialog;
		switch (dialogId) {
		
		case DialogCode.WIRELESS_CONTROLS:
			dialog = getWirelessControlsDialog(context);
			break;
			
		case DialogCode.FORECAST_UNAVAILABLE:
			dialog = getOnForecastUnavailableDialog(context);
			break;
	
		default:
			dialog = null;
			break;		
		}
		
		return dialog;
	}
}
