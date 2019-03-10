package arcmonitize.ads;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;

import com.arcadio.arcmonitizemodule.BuildConfig;
import com.arcadio.arcmonitizemodule.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import arcmonitize.Settings;
import arcmonitize.analytic.AnalyticControllerPanel;

public class Admob {
    private static final boolean ENABLE_REWARD = true;
    private static final boolean ENABLE_INTERTITIAL = true;
    private RewardedVideoAd mRewardedVideoAd;
    private AnalyticControllerPanel mAnalyticControllerPanel;
    private Activity mActivity = null;
    private InterstitialAd interstitial;

    public Admob(Activity activity) {
        this.mActivity = activity;
        MobileAds.initialize(activity, activity.getString(R.string.admob_app_id));
        loadInterstitialAds();
        loadRewardVideoAds();
    }

    protected boolean isInstertialLoaded() {
        if (!ENABLE_INTERTITIAL) {
            return false;
        }
        if (mAnalyticControllerPanel != null)
            mAnalyticControllerPanel.pushToAnalytic("InterstitialAds", "showOnAdLoad", "loaded");
        if (interstitial == null || !interstitial.isLoaded()) {
            return false;
        }
        return interstitial.isLoaded();
    }

    protected boolean isLoadedReward() {
        if (!ENABLE_REWARD) {
            return false;
        }
        if (mAnalyticControllerPanel != null)
            mAnalyticControllerPanel.pushToAnalytic("InterstitialAds", "showOnAdLoad", "loaded");
        if (mRewardedVideoAd == null || !mRewardedVideoAd.isLoaded()) {
            return false;
        }
        return mRewardedVideoAd.isLoaded();
    }

    public void loadBannerAds(final LinearLayout adViewLayout) {
        try {

            MobileAds.initialize(mActivity, mActivity.getString(R.string.admob_app_id));
            AdView adView = new AdView(mActivity);
            adView.setAdSize(AdSize.SMART_BANNER);
            adView.setAdUnitId(mActivity.getString(R.string.admob_banner_id));
            adViewLayout.removeAllViews();

            adViewLayout.addView(adView);
            adView.loadAd(addTestDevicesId());
            adView.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    adViewLayout.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAdFailedToLoad(int i) {
                    super.onAdFailedToLoad(i);
//                    fbAds.loadBanner(adViewLayout);
                }
            });
        } catch (Exception e) {
//            fbAds.loadBanner(adViewLayout);
            e.printStackTrace();
        }

    }

    // Initial full screen ad...
    private void loadInterstitialAds() {
        mAnalyticControllerPanel = new AnalyticControllerPanel(mActivity.getApplicationContext());
        interstitial = new InterstitialAd(mActivity);
        interstitial.setAdUnitId(mActivity.getString(R.string.admob_fullscreen_id));
        interstitial.loadAd(addTestDevicesId());
    }

    private void loadRewardVideoAds() {

        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(mActivity);
        mRewardedVideoAd.loadAd(mActivity.getString(R.string.admob_reward_id),
                new AdRequest.Builder().build());
    }


    protected void showRewardVideoAds(final AdCloseListener adListener, final boolean isLoadAgain, final boolean shouldShow) {
        //boolean shouldShow = shouldShow();
        if (!shouldShow) {
            if (adListener != null)
                adListener.onAdClosed(false);
            return;
        }
        if (isLoadedReward()) {
            mRewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
                @Override
                public void onRewardedVideoAdLoaded() {
                }

                @Override
                public void onRewardedVideoAdOpened() {
                    mAnalyticControllerPanel.pushToAnalytic("InterstitialAds", "reward", "opened");
                }

                @Override
                public void onRewardedVideoStarted() {
                }

                @Override
                public void onRewardedVideoAdClosed() {
                    if (adListener != null) {
                        if (isLoadAgain)
                            loadRewardVideoAds();
                        adListener.onAdClosed(true);
                    }
                    if (mAnalyticControllerPanel != null)
                        mAnalyticControllerPanel.pushToAnalytic("InterstitialAds", "reward", "closed");
                }

                @Override
                public void onRewarded(RewardItem rewardItem) {
                }

                @Override
                public void onRewardedVideoAdLeftApplication() {
                }

                @Override
                public void onRewardedVideoAdFailedToLoad(int i) {
                }

                @Override
                public void onRewardedVideoCompleted() {
                    mAnalyticControllerPanel.pushToAnalytic("InterstitialAds", "reward", "completed");

                }
            });
            mRewardedVideoAd.show();
        }

    }

    private AdRequest addTestDevicesId() {
        String[] devicesId = mActivity.getResources().getStringArray(R.array.addTestDevices);

        AdRequest.Builder adRequestBuilder = new AdRequest.Builder();
        adRequestBuilder.addTestDevice(AdRequest.DEVICE_ID_EMULATOR);
        if (devicesId != null && devicesId.length > 0) {
            for (String id : devicesId)
                adRequestBuilder.addTestDevice(id);
        }
        if (BuildConfig.DEBUG) {
            adRequestBuilder.addTestDevice(Settings.getTestDeviceId(mActivity));
        } else {
            adRequestBuilder.addTestDevice(AdRequest.DEVICE_ID_EMULATOR);
        }
        return adRequestBuilder.build();

    }

    public synchronized void showOnAdLoad(final AdCloseListener adListener, final boolean isloadAgain) {
        //   showOnAdLoadIfReward(adListener, isloadAgain);
    }

    protected synchronized void showOnAdLoad(final AdCloseListener adListener, final boolean isloadAgain, boolean shouldShow) {
//        boolean shouldShow = shouldShow();
        if (!shouldShow) {
            if (adListener != null)
                adListener.onAdClosed(false);
            return;
        }
        if (isInstertialLoaded()) {
            interstitial.setAdListener(new AdListener() {
                public void onAdClosed() {

                    if (isloadAgain) {
                        loadInterstitialAds();
                    }
                    if (adListener != null) {
                        adListener.onAdClosed(true);
                        if (mAnalyticControllerPanel != null)
                            mAnalyticControllerPanel.pushToAnalytic("InterstitialAds", "showOnAdLoad", "closed");
                    }
                }
            });
            interstitial.show();
            if (mAnalyticControllerPanel != null)
                mAnalyticControllerPanel.pushToAnalytic("InterstitialAds", "showOnAdLoad", "showed");
        } else {
            if (adListener != null)
                adListener.onAdClosed(false);
        }
    }
}
