package biz.binarysolutions.weatherusa.util;

import java.lang.reflect.Field;

import android.view.Display;

/**
 * 
 *
 */
public class DeviceUtil {
	
	private static final int SMALL_SCREEN_WIDTH  = 240;
	private static final int SMALL_SCREEN_HEIGHT = 320;

	/**
	 * 
	 * @return
	 */
	public static int getAPILevel() {
		
		try {
			Class version = android.os.Build.VERSION.class; 
			Field sdkInt  = version.getDeclaredField("SDK_INT");
			
			return sdkInt.getInt(version);
			
		} catch (Exception e) {
			// do nothing
		}
		
		return Integer.parseInt(android.os.Build.VERSION.SDK);
	}

	/**
	 * 
	 * @param display 
	 * @return
	 */
	public static boolean isSmallScreen(Display display) {
		
		int width  = display.getWidth();
		int height = display.getHeight();
		
		return 
			(width  <= SMALL_SCREEN_WIDTH && height <= SMALL_SCREEN_HEIGHT) ||
			(height <= SMALL_SCREEN_WIDTH && width  <= SMALL_SCREEN_HEIGHT);
	}

}
