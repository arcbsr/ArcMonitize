//package arcmonitize.inApp;
//
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.anjlab.android.iab.v3.BillingProcessor;
//import com.anjlab.android.iab.v3.TransactionDetails;
//import com.arcadio.arcmonitizemodule.R;
//
//public class InAppBillingActivity extends AppCompatActivity implements BillingProcessor.IBillingHandler {
//    BillingProcessor bp;
//    public static final int INAPP_ERROR_UNKNOWN = -1;
//    public static final int INAPP_ERROR_SERVICE = -2;
//    public static final int INAPP_ERROR_LOADING = -3;
//    public static final int INAPP_PROCESSING = 0;
//    public static final int INAPP_ALREADY_PURCHASED = 1;
//    private ProgressDialog dialog;
//
//    public boolean isAvailableService() {
//        boolean isAvailable = BillingProcessor.isIabServiceAvailable(this);
//        return isAvailable;
//    }
//
//    public boolean isOneTimePurchaseSupported() {
//        boolean isOneTimePurchaseSupported = bp.isOneTimePurchaseSupported();
//        return isOneTimePurchaseSupported;
//    }
//
//    public boolean isSubsUpdateSupported() {
//        boolean isSubsUpdateSupported = bp.isSubscriptionUpdateSupported();
//        return isSubsUpdateSupported;
//    }
//
//    public void consumePurchase(String pID) {
//        bp.consumePurchase(pID);
//    }
//
//    public boolean loadOwnedPurchasesFromGoogle() {
//        return bp.loadOwnedPurchasesFromGoogle();
//    }
//
//    InAppBillingListener mAppBillingListener;
//
//    public int purchase(String pID, InAppBillingListener inAppBillingListener) {
//        mAppBillingListener = inAppBillingListener;
//        int bpCode = bpErrorCode();
//        if (bpCode != INAPP_PROCESSING) {
//            return bpCode;
//        }
//        if (isPurchased(pID)) {
//            return INAPP_ALREADY_PURCHASED;
//        }
//        bp.purchase(this, pID);
//        return INAPP_PROCESSING;
//    }
//
//    public int bpErrorCode() {
//        if (bp == null) {
//            return INAPP_ERROR_UNKNOWN;
//        }
//        if (!isAvailableService()) {
//            return INAPP_ERROR_SERVICE;
//        }
//        if (!bp.isInitialized()) {
//            return INAPP_ERROR_LOADING;
//        }
//        return INAPP_PROCESSING;
//    }
//
//    public boolean isPurchased(String pID) {
//        if (InAppBillingPref.getBooleanSetting(this, pID, false)) {
//            return true;
//        }
//        int bpCode = bpErrorCode();
//        if (bpCode != INAPP_PROCESSING) {
//            return false;
//        }
//        if (bp.isPurchased(pID)) {
//            InAppBillingPref.setSetting(this, pID, true);
//            return true;
//        }
//        return false;
//    }
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        dialog = new ProgressDialog(this);
//        dialog.setMessage("please wait.");
//        dialog.show();
//        bp = new BillingProcessor(this, getString(R.string.gp_inapp_li),
//                getString(R.string.gp_inapp_marchent), this);
//        bp.initialize();
//    }
//
//    @Override
//    public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {
//        InAppBillingPref.setSetting(this, productId, true);
//        Log.w("BillingProcessor", "onProductPurchased>>" + productId);
//        if (mAppBillingListener != null) {
//            mAppBillingListener.purchasedSucessFully(productId, details);
//        }
//    }
//
//    @Override
//    public void onPurchaseHistoryRestored() {
//        Log.w("BillingProcessor", "onPurchaseHistoryRestored>>");
//        if (mAppBillingListener != null) {
//            mAppBillingListener.onPurchaseHistoryRestored(bp);
//        }
//    }
//
//    @Override
//    public void onBillingError(int errorCode, @Nullable Throwable error) {
//        Log.w("BillingProcessor", "onBillingError>>");
//        if (dialog.isShowing()) {
//            dialog.dismiss();
//        }
//        if (mAppBillingListener != null) {
//            mAppBillingListener.purchasedFailed(errorCode, error);
//        }
//    }
//
//    @Override
//    public void onBillingInitialized() {
//        if (dialog.isShowing()) {
//            dialog.dismiss();
//        }
//        if (bp != null && !isAvailableService()) {
//            Toast.makeText(this, "Service not available", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        Log.w("BillingProcessor", "onBillingInitialized>>");
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (!bp.handleActivityResult(requestCode, resultCode, data)) {
//            super.onActivityResult(requestCode, resultCode, data);
//        }
//    }
//
//    @Override
//    public void onDestroy() {
//        if (bp != null) {
//            bp.release();
//        }
//        super.onDestroy();
//    }
//}
