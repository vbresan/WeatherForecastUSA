package biz.binarysolutions.weatherusa;

import android.app.Activity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

class FlavorSpecific {

    private static final int MIN_AD_HEIGHT = 50;

    private static void showAd
        (
            Activity     activity,
            LinearLayout parent,
            int          widthDp,
            int          heightDp
        ) {

        AdSize adSize = AdSize.getInlineAdaptiveBannerAdSize(widthDp, heightDp);
        AdView bannerView = new AdView(activity);
        bannerView.setAdUnitId(activity.getString(R.string.admob_ad_id));
        bannerView.setAdSize(adSize);

        AdRequest adRequest = new AdRequest.Builder().build();
        bannerView.loadAd(adRequest);

        parent.addView(bannerView);
    }


    public static void initialize(Activity activity) {

        LinearLayout adContainer = activity.findViewById(R.id.adContainer);
        if (adContainer == null) {
            return;
        }

        adContainer.getViewTreeObserver().addOnGlobalLayoutListener(
            new ViewTreeObserver.OnGlobalLayoutListener() {

                private float getDensity() {
                    return activity.getResources().getDisplayMetrics().density;
                }

                @SuppressWarnings("UnnecessaryLocalVariable")
                private int getWidth(View view) {

                    int widthPx = view.getWidth();
                    int widthDp = (int) (widthPx / getDensity());

                    return widthDp;
                }

                @SuppressWarnings("UnnecessaryLocalVariable")
                private int getHeight(View view) {

                    int heightPx = view.getHeight();
                    int heightDp = (int) (heightPx / getDensity());

                    return heightDp;
                }

                @Override
                public void onGlobalLayout() {

                    adContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    adContainer.post(() -> {

                        int widthDp  = getWidth(adContainer);
                        int heightDp = getHeight(adContainer);

                        heightDp = Math.max(heightDp, MIN_AD_HEIGHT);
                        showAd(activity, adContainer, widthDp, heightDp);
                    });
                }
            }
        );
    }
}
