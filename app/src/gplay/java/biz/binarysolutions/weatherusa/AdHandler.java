package biz.binarysolutions.weatherusa;

import android.app.Activity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

class FlavorSpecific {

    public static void initialize(Activity activity) {

        MobileAds.initialize(activity);
        AdView mAdView = activity.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }
}
