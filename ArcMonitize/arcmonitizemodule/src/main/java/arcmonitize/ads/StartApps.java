package arcmonitize.ads;

import android.app.Activity;

import com.arcadio.arcmonitizemodule.R;
import com.startapp.android.publish.adsCommon.Ad;
import com.startapp.android.publish.adsCommon.StartAppAd;
import com.startapp.android.publish.adsCommon.StartAppSDK;
import com.startapp.android.publish.adsCommon.adListeners.AdDisplayListener;

public class StartApps {
    private StartAppAd startAppAd;
    private Activity mActivity;

    public StartApps(Activity activity) {
        mActivity = activity;
        loadStartAds();
    }

    public void loadStartAds() {
        StartAppSDK.init(mActivity, mActivity.getString(R.string.startappid), true);
        startAppAd = new StartAppAd(mActivity);
    }

    public void showStartAds(final AdCloseListener adListenerCustom, boolean shouldshow) {
        if (startAppAd.isReady()) {
            showStartAds(adListenerCustom, true, shouldshow);
            return;
        }
    }

    private void showStartAds(final AdCloseListener adListenerCustom, boolean isLoadAgain, boolean shouldshow) {
        if (!shouldshow) {
            if (adListenerCustom != null)
                adListenerCustom.onAdClosed(false);
            return;
        }
        if (startAppAd == null) {
            if (adListenerCustom != null)
                adListenerCustom.onAdClosed(false);
            return;
        }

        startAppAd.showAd(new AdDisplayListener() {
            @Override
            public void adHidden(Ad ad) {
                if (adListenerCustom != null)
                    adListenerCustom.onAdClosed(true);
            }

            @Override
            public void adDisplayed(Ad ad) {

            }

            @Override
            public void adClicked(Ad ad) {
            }

            @Override
            public void adNotDisplayed(Ad ad) {
                if (adListenerCustom != null)
                    adListenerCustom.onAdClosed(false);
            }
        });
    }

    public void onDestroy() {

    }
}
