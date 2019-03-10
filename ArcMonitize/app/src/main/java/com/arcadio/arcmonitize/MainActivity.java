package com.arcadio.arcmonitize;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;

import arcmonitize.Settings;
import arcmonitize.VLog;
import arcmonitize.ads.AdCloseListener;
import arcmonitize.ads.AdControllPanel;
import arcmonitize.inApp.ArcInAppBilling;
import arcmonitize.inApp.InAppBillingBaseActivity;

public class MainActivity extends InAppBillingBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        arcInAppBilling = new ArcInAppBilling(this, new BillingProcessor.IBillingHandler() {
            @Override
            public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {
                VLog.w("onProductPurchased");
            }

            @Override
            public void onPurchaseHistoryRestored() {
                VLog.w("onPurchaseHistoryRestored");
            }

            @Override
            public void onBillingError(int errorCode, @Nullable Throwable error) {
                VLog.w("onBillingError" + errorCode);
            }

            @Override
            public void onBillingInitialized() {
                arcInAppBilling.onBillingInitialized();
                VLog.w("onBillingInitialized>>" +
                        arcInAppBilling.getbpCodeStatus(arcInAppBilling.purchase(Settings.KEY_INAPP_TEST)));
            }
        });
        final AdControllPanel adControllPanel = new AdControllPanel(this, false);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adControllPanel.showOnAdLoadIfReward(new AdCloseListener() {
                    @Override
                    public void onAdClosed(boolean isShowedAds) {

                    }
                }, true);
            }
        });
    }

    //adb shell pm clear com.android.vending
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (!arcInAppBilling.getBillingProcessor().handleActivityResult(requestCode, resultCode, data)) {
//            super.onActivityResult(requestCode, resultCode, data);
//        }
//    }
}
