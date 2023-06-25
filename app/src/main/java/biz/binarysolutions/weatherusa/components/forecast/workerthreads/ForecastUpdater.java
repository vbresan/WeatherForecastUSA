package biz.binarysolutions.weatherusa.components.forecast.workerthreads;

import android.location.Location;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import biz.binarysolutions.weatherusa.util.InternetUtil;

/**
 *
 * https://graphical.weather.gov/xml/
 * https://graphical.weather.gov/xml/SOAP_server/ndfdXML.htm
 *
 */
public abstract class ForecastUpdater extends Thread {
	
	private static final String URL =
		"https://graphical.weather.gov/xml/SOAP_server/ndfdXMLclient.php";

	private static final String APP_URL =
		"https://github.com/vbresan/WeatherForecastUSA";

	private static final String APP_EMAIL =
		"support+weatherusa@binarysolutions.biz";

	public static final String USER_AGENT =
		"WeatherForecastUSA/v4.x (" + APP_URL + "; " + APP_EMAIL + ")";

	private static final SimpleDateFormat sdf = 
		new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

	private Location location;
	private String   zip;

	/**
	 *
	 * @return
	 */
	private StringBuffer getParameters() {

		StringBuffer sb = new StringBuffer();

		sb.append("&product=time-series&begin=");

		try {
			sb.append(URLEncoder.encode(sdf.format(new Date()), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// do nothing
		}

		sb.append("&maxt=maxt");	// Maximum Temperature
		sb.append("&mint=mint");	// Minimum Temperature

//		sb.append("&temp=temp");	// 3 Hourly Temperature

		sb.append("&dew=dew");		// Dewpoint Temperature
		sb.append("&appt=appt");	// Apparent Temperature

//		sb.append("&pop12=pop12");	// 12 Hour Probability of Precipitation
//		sb.append("&qpf=qpf");		// Liquid Precipitation Amount
//		sb.append("&snow=snow");	// Snowfall Amount
//		sb.append("&sky=sky");		// Cloud Cover Amount
//		sb.append("&rh=rh");		// Relative Humidity

//		sb.append("&wspd=wspd");	// Wind Speed
//		sb.append("&wdir=wdir");	// Wind Direction

		sb.append("&wx=wx");		// Weather
		sb.append("&icons=icons");	// Weather Icons

//		sb.append("&waveh=waveh");	// Wave Height

/*
		request.addProperty("incw34", "false");	// Probabilistic Tropical Cyclone Wind Speed >34 Knots (Incremental)
		request.addProperty("incw50", "false");	// Probabilistic Tropical Cyclone Wind Speed >50 Knots (Incremental)
		request.addProperty("incw64", "false");	// Probabilistic Tropical Cyclone Wind Speed >64 Knots (Incremental)
		request.addProperty("cumw34", "false");	// Probabilistic Tropical Cyclone Wind Speed >34 Knots (Cumulative)
		request.addProperty("cumw50", "false");	// Probabilistic Tropical Cyclone Wind Speed >50 Knots (Cumulative)
		request.addProperty("cumw64", "false");	// Probabilistic Tropical Cyclone Wind Speed >64 Knots (Cumulative)

		request.addProperty("wgust", "false");	// Wind Gust

		request.addProperty("conhazo", "false");		// Convective Hazard Outlook

		request.addProperty("ptornado",     "false");	// Probability of Tornadoes
		request.addProperty("phail",        "false");	// Probability of Hail
		request.addProperty("ptstmwinds",   "false");	// Probability of Damaging Thunderstorm Winds
		request.addProperty("pxtornado",    "false");	// Probability of Extreme Tornadoes
		request.addProperty("pxhail",       "false");	// Probability of Extreme Hail
		request.addProperty("pxtstmwinds",  "false");	// Probability of Extreme Thunderstorm Winds
		request.addProperty("ptotsvrtstm",  "false");	// Probability of Severe Thunderstorms
		request.addProperty("pxtotsvrtstm", "false");	// Probability of Extreme Severe Thunderstorms

		request.addProperty("tmpabv14d", "false");	// Probability of 8- To 14-Day Average Temperature Above Normal
		request.addProperty("tmpblw14d", "false");	// Probability of 8- To 14-Day Average Temperature Below Normal
		request.addProperty("tmpabv30d", "false");	// Probability of One-Month Average Temperature Above Normal
		request.addProperty("tmpblw30d", "false");	// Probability of One-Month Average Temperature Below Normal
		request.addProperty("tmpabv90d", "false");	// Probability of Three-Month Average Temperature Above Normal
		request.addProperty("tmpblw90d", "false");	// Probability of Three-Month Average Temperature Below Normal

		request.addProperty("prcpabv14d", "false");	// Probability of 8- To 14-Day Total Precipitation Above Median
		request.addProperty("prcpblw14d", "false");	// Probability of 8- To 14-Day Total Precipitation Below Median
		request.addProperty("prcpabv30d", "false");	// Probability of One-Month Total Precipitation Above Median
		request.addProperty("prcpblw30d", "false");	// Probability of One-Month Total Precipitation Below Median
		request.addProperty("prcpabv90d", "false");	// Probability of Three-Month Total Precipitation Above Median
		request.addProperty("prcpblw90d", "false");	// Probability of Three-Month Total Precipitation Below Median

		request.addProperty("precipa_r", "false");	// Real-time Mesoscale Analysis Precipitation
		request.addProperty("sky_r",     "false");	// Real-time Mesoscale Analysis GOES Effective Cloud Amount
		request.addProperty("td_r",      "false");	// Real-time Mesoscale Analysis Dewpoint Temperature
		request.addProperty("temp_r",    "false");	// Real-time Mesoscale Analysis Temperature
		request.addProperty("wdir_r",    "false");	// Real-time Mesoscale Analysis Wind Direction
*/
		sb.append("&wwa=wwa");		// Watches, Warnings, and Advisories

//		request.addProperty("wspd_r", "false");		// Real-time Mesoscale Analysis Wind Speed

		sb.append("&Submit=Submit");

		return sb;
	}

	/**
	 * 
	 * @param location
	 * @return
	 */
	private String getParameters(Location location) {
		
		StringBuffer sb = new StringBuffer();

		sb.append("?whichClient=NDFDgen");
		sb.append("&lat=");
		sb.append(location.getLatitude());
		sb.append("&lon=");
		sb.append(location.getLongitude());
		sb.append(getParameters());

		return sb.toString();
	}

	/**
	 *
	 * @param zip
	 * @return
	 */
	private String getParameters(String zip) {

		StringBuffer sb = new StringBuffer();

		sb.append("?whichClient=NDFDgen");
		sb.append("&zipCodeList=");
		sb.append(zip);
		sb.append(getParameters());

		return sb.toString();
	}

	/**
	 *
	 * @param response
	 */
	protected abstract void onResponseReceived(String response);

	/**
	 * 
	 * @param location
	 */
	public ForecastUpdater(Location location) {
		this.location = location;
	}

	/**
	 *
	 * @param zip
	 */
	public ForecastUpdater(String zip) {
		this.zip = zip;
	}

	@Override
	public void run() {
		
		String url = URL;

		if (location != null) {
			url += getParameters(location);
		} else if (zip != null) {
			url += getParameters(zip);
		}

		String forecast = InternetUtil.getGetResponse(url, USER_AGENT);
		onResponseReceived(forecast);
	}
}
