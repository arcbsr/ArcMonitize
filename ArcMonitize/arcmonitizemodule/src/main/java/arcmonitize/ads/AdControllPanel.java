package arcmonitize.ads;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.widget.LinearLayout;

import arcmonitize.ArcPrefs;
import arcmonitize.Settings;
import arcmonitize.VLog;
import arcmonitize.analytic.AnalyticControllerPanel;


public class AdControllPanel {
    public static boolean FORCE_CLOSE_ADS = false;
    LinearLayout adViewLayout = null;
    private Activity mActivity = null;
    private AnalyticControllerPanel mAnalyticControllerPanel;

    public int getAdsInterval() {
        return adsInterval;
    }

    public void setAdsInterval(int adsInterval) {
        this.adsInterval = adsInterval;
    }

    private int adsInterval = Settings.ADS_INTERVAL;
    private Admob admob;
    private FBAds fbAds;

    public FBAds getFBAds() {
        return fbAds;
    }

    public AdControllPanel(Activity activity) {
        adControllPanel(activity, false);
    }
    public AdControllPanel(Activity activity, boolean isRemoved) {
        adControllPanel(activity, isRemoved);
    }

    private void adControllPanel(Activity activity, boolean isRemoved) {
        if (mActivity == null) {
            mActivity = activity;
        }
        if (isRemoved) {
            adsInterval = -1;
        }
        admob = new Admob(mActivity);
        fbAds = new FBAds(mActivity);
    }

    public void loadBanner(LinearLayout adLayout) {

        this.adViewLayout = adLayout;
        if (adsInterval == -1) {
            disAbleAllAds();
            return;
        }
        admob.loadBannerAds(adViewLayout);
    }

    public void loadToneshub(String id) {
        if (mAnalyticControllerPanel != null)
            mAnalyticControllerPanel.pushToAnalytic("full_ad_loaded", "full_ad", "toneshub");
        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://app.toneshub.com/?cid=" + id));
        mActivity.startActivity(myIntent);

    }

    public void analyticFit(String title, String appInstalled, String id) {
        if (mAnalyticControllerPanel != null)
            mAnalyticControllerPanel.pushToAnalytic(title, appInstalled, id);

    }

    public void disAbleAllAds() {
        AdControllPanel.FORCE_CLOSE_ADS = true;
        adsInterval = -1;
        if (adViewLayout != null) {
            adViewLayout.removeAllViews();
        }
    }

    public void enableAllAds() {
        adsInterval = Settings.ADS_INTERVAL;
        AdControllPanel.FORCE_CLOSE_ADS = false;
        if (adViewLayout != null) {
            loadBanner(adViewLayout);
        }
    }


    public void destroy() {
        if (mAnalyticControllerPanel != null) {
            mAnalyticControllerPanel = null;
        }
    }


    private boolean shouldShow() {
        if (!Settings.IS_ADS_ENABLE) {
            return false;
        }
        if (adsInterval == -1 || FORCE_CLOSE_ADS) {
            disAbleAllAds();
            return false;
        }
        if (adsInterval > 0) {
            int adsInterval2 = ArcPrefs.getIntSetting(mActivity, "adsintervalarc", 0);
            VLog.w("adsInterval", adsInterval + "<<>>" + adsInterval2);
            if (adsInterval2 < adsInterval) {
                ArcPrefs.setSetting(mActivity, "adsintervalarc", adsInterval2 + 1);
                return false;
            } else {
                ArcPrefs.setSetting(mActivity, "adsintervalarc", 0);
            }
        }
        //Log.w("adsInterval", "should show");
        return true;
    }


    public void showOnAdLoadIfReward(final AdCloseListener adListener, final boolean isloadAgain, boolean shouldShow) {
        if (admob != null && admob.isLoadedReward()) {
            admob.showRewardVideoAds(adListener, isloadAgain, shouldShow);
        } else if (admob != null && admob.isInstertialLoaded()) {
            admob.showOnAdLoad(adListener, isloadAgain, shouldShow);
        } else if (fbAds != null && fbAds.isLoaded()) {
            fbAds.showOnInterstitialAdLoad(adListener, isloadAgain, shouldShow);
        } else {
            if (adListener != null)
                adListener.onAdClosed(false);
        }
    }

    public void showOnAdLoadIfReward(final AdCloseListener adListener, final boolean isloadAgain) {
        boolean shouldShow = shouldShow();
        if (!shouldShow) {
            if (adListener != null)
                adListener.onAdClosed(false);
            return;
        }
        showOnAdLoadIfReward(adListener, isloadAgain, shouldShow);
    }
}
