package biz.binarysolutions.weatherusa.util;

import android.location.Location;
import android.location.LocationManager;

import androidx.annotation.NonNull;

import java.text.DecimalFormat;

/**
 *
 */
public class WeatherLocation extends Location {

    private static final DecimalFormat df = new DecimalFormat("###.00");

    /**
     *
     * @param latitude
     * @return
     */
    private static String getFormattedLatitude(double latitude) {

        StringBuffer  sb = new StringBuffer();

        if (latitude < 0) {
            sb.append(df.format(Math.abs(latitude)));
            sb.append("°S");
        } else {
            sb.append(df.format(latitude));
            sb.append("°N");
        }

        return sb.toString();
    }

    /**
     *
     * @param longitude
     * @return
     */
    private static String getFormattedLongitude(double longitude) {

        StringBuffer sb = new StringBuffer();

        if (longitude < 0) {
            sb.append(df.format(Math.abs(longitude)));
            sb.append("°W");
        } else {
            sb.append(df.format(longitude));
            sb.append("°E");
        }

        return sb.toString();
    }

    /**
     *
     * @param provider
     */
    public WeatherLocation(String provider) {
        super(provider);
    }

    /**
     *
     * @param l
     */
    public WeatherLocation(Location l) {
        super(l);
    }

    /**
     *
     * @param latitude
     * @param longitude
     */
    public WeatherLocation(double latitude, double longitude) {
        super(LocationManager.GPS_PROVIDER);

        setLatitude(latitude);
        setLongitude(longitude);
        setTime(System.currentTimeMillis());
    }

    @NonNull
    @Override
    public String toString() {

        return new StringBuffer()
            .append(getFormattedLatitude(getLatitude()))
            .append(" ")
            .append(getFormattedLongitude(getLongitude()))
            .toString();
    }

    /**
     *
     * @return
     */
    public boolean isValid() {
        return getLongitude() != 0.0 && getLatitude() != 0.0;
    }
}
