package arcmonitize.ads;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;

import com.arcadio.arcmonitizemodule.R;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.facebook.ads.RewardedVideoAd;
import com.facebook.ads.RewardedVideoAdListener;

public class FBAds {
    private final String TAG = "FB-ADs";
    private InterstitialAd interstitialAd;
    private Activity mActivity;
    private boolean isInterstitialError = false;
    private boolean isRewardError = false;
    private RewardedVideoAd rewardedVideoAd;

    public FBAds(Activity activity) {
        this.mActivity = activity;
        loadFbInterstitialAds();

    }

    public boolean isLoaded() {
        if (interstitialAd == null) {
            return false;
        }
        return interstitialAd.isAdLoaded();
    }

    protected void onDestroy() {
        if (interstitialAd != null) {
            interstitialAd.destroy();
        }
        if (rewardedVideoAd != null) {
            rewardedVideoAd.destroy();
        }
    }

    public void loadBanner(final LinearLayout linearLayout) {
        linearLayout.removeAllViews();
        AdView adView = new AdView(mActivity, mActivity.getString(R.string.fb_ads_banner_id), AdSize.BANNER_HEIGHT_50);
        // Add the ad view to your activity layout
        linearLayout.addView(adView);
        // Request an ad
        adView.loadAd();
        adView.setAdListener(new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {

            }

            @Override
            public void onInterstitialDismissed(Ad ad) {

            }

            @Override
            public void onError(Ad ad, AdError adError) {

            }

            @Override
            public void onAdLoaded(Ad ad) {
                linearLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        });
    }

    public void loadFbInterstitialAds() {
        isInterstitialError = false;
        AdSettings.setIntegrationErrorMode(AdSettings.IntegrationErrorMode.INTEGRATION_ERROR_CRASH_DEBUG_MODE);
        AdSettings.addTestDevice("d9ab9a9a-d6e0-4829-96fc-c2603cd4c092");
        interstitialAd = new InterstitialAd(mActivity, mActivity.getString(R.string.fb_ads_interstitial_id));
        interstitialAd.setAdListener(new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                isInterstitialError = true;
            }

            @Override
            public void onAdLoaded(Ad ad) {
            }

            @Override
            public void onAdClicked(Ad ad) {
            }

            @Override
            public void onLoggingImpression(Ad ad) {
            }
        });

        // For auto play video ads, it's recommended to load the ad
        // at least 30 seconds before it is shown
        interstitialAd.loadAd();
    }

//    private boolean shouldShow() {
//        if (!AdsSettings.IS_ADS_ENABLE) {
//            return false;
//        }
//        if (AdsSettings.ADS_INTERVAL > 0) {
//            int adsInterval = AdsPrefUtil.getIntSetting(mActivity, "adsintervalarc", 0);
//            if (adsInterval < AdsSettings.ADS_INTERVAL) {
//                AdsPrefUtil.setSetting(mActivity, "adsintervalarc", adsInterval + 1);
//                return false;
//            } else {
//                AdsPrefUtil.setSetting(mActivity, "adsintervalarc", 0);
//            }
//        }
//        return true;
//    }

    private boolean isInterstitialLoaded(boolean shouldShow) {

        if (interstitialAd == null || !interstitialAd.isAdLoaded() || isInterstitialError) {
            return false;
        }
        if (!shouldShow) {
            return false;
        }
        if (interstitialAd.isAdLoaded()) {
            return true;
        }
        return false;
    }

    private boolean isRewardLoaded(boolean shouldShow) {

        if (rewardedVideoAd == null || !rewardedVideoAd.isAdLoaded() || isRewardError) {
            return false;
        }
        if (!shouldShow) {
            return false;
        }
        if (rewardedVideoAd.isAdLoaded()) {
            return true;
        }
        return false;
    }

    public void showOnInterstitialAdLoad(final AdCloseListener adCloseListener, final boolean isLoadAgain, boolean shouldShow) {
        if (!isInterstitialLoaded(shouldShow)) {
            if (adCloseListener != null)
                adCloseListener.onAdClosed(false);
            return;
        }
        interstitialAd.setAdListener(new InterstitialAdListener() {
            boolean isDisplayed = false;

            @Override
            public void onInterstitialDisplayed(Ad ad) {
                // Interstitial ad displayed callback
                isDisplayed = true;
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                // Interstitial dismissed callback
                if (adCloseListener != null)
                    adCloseListener.onAdClosed(isDisplayed);
                if (isLoadAgain) {
                    loadFbInterstitialAds();
                }
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Ad error callback
                if (adCloseListener != null)
                    adCloseListener.onAdClosed(false);
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Interstitial ad is loaded and ready to be displayed
                // Show the ad

            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
            }
        });
        interstitialAd.show();
    }

    public void loadFBRewardVideo() {
        isRewardError = false;
        rewardedVideoAd = new RewardedVideoAd(mActivity, mActivity.getString(R.string.fb_ads_interstitial_id));
        rewardedVideoAd.setAdListener(new RewardedVideoAdListener() {
            @Override
            public void onError(Ad ad, AdError error) {
                isRewardError = true;
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Rewarded video ad is loaded and ready to be displayed
            }

            @Override
            public void onAdClicked(Ad ad) {
            }

            @Override
            public void onLoggingImpression(Ad ad) {
            }

            @Override
            public void onRewardedVideoCompleted() {
            }

            @Override
            public void onRewardedVideoClosed() {
            }
        });
        rewardedVideoAd.loadAd();
    }

    public void showOnRewardAdLoad(final AdCloseListener adCloseListener, final boolean isLoadAgain, boolean shouldShow) {

        if (!isRewardLoaded(shouldShow)) {
            if (adCloseListener != null)
                adCloseListener.onAdClosed(false);
            return;
        }
        rewardedVideoAd.setAdListener(new RewardedVideoAdListener() {
            boolean isDisplayed = false;

            @Override
            public void onError(Ad ad, AdError error) {
            }

            @Override
            public void onAdLoaded(Ad ad) {
            }

            @Override
            public void onAdClicked(Ad ad) {
            }

            @Override
            public void onLoggingImpression(Ad ad) {
            }

            @Override
            public void onRewardedVideoCompleted() {
            }

            @Override
            public void onRewardedVideoClosed() {
                if (adCloseListener != null)
                    adCloseListener.onAdClosed(true);
            }
        });
        rewardedVideoAd.show();
    }

    public void showOnAdLoadIfReward(final AdCloseListener adListener, final boolean isloadAgain, boolean shouldShow) {

        if (isInterstitialLoaded(shouldShow)) {
            showOnInterstitialAdLoad(adListener, isloadAgain, shouldShow);
        } else if (isRewardLoaded(shouldShow)) {
            showOnRewardAdLoad(adListener, isloadAgain, shouldShow);
        } else {
            if (adListener != null)
                adListener.onAdClosed(false);
        }
    }
}

