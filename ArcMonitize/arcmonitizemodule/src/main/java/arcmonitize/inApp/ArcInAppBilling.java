package arcmonitize.inApp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.annotation.Nullable;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.arcadio.arcmonitizemodule.R;

import arcmonitize.VLog;


public class ArcInAppBilling {
    private BillingProcessor bp;
    public static final int INAPP_ERROR_UNKNOWN = -1021;
    public static final int INAPP_ERROR_SERVICE = -1022;
    public static final int INAPP_ERROR_LOADING = -1023;
    public static final int INAPP_ERROR_PID = -1024;
    public static final int INAPP_ERROR_PURCHASE = -1025;
    public static final int INAPP_PROCESSING = 1020;
    public static final int INAPP_ALREADY_PURCHASED = 1021;
    public static final int INAPP_SUCCESS_PURCHASED = 1022;
    private ProgressDialog dialog;

    public BillingProcessor getBillingProcessor() {
        return bp;
    }

    public boolean isAvailableService() {
        boolean isAvailable = BillingProcessor.isIabServiceAvailable(context);
        return isAvailable;
    }

    private boolean isOneTimePurchaseSupported() {
        boolean isOneTimePurchaseSupported = bp.isOneTimePurchaseSupported();
        return isOneTimePurchaseSupported;
    }

    private boolean isSubsUpdateSupported() {
        boolean isSubsUpdateSupported = bp.isSubscriptionUpdateSupported();
        return isSubsUpdateSupported;
    }

    private void consumePurchase(String pID) {
        bp.consumePurchase(pID);
    }

    private boolean loadOwnedPurchasesFromGoogle() {
        return bp.loadOwnedPurchasesFromGoogle();
    }

    public static String getbpCodeStatus(final int bpCode) {
        VLog.w("<<>>" + bpCode);
        switch (bpCode) {
            case INAPP_ALREADY_PURCHASED:
                return "INAPP_ALREADY_PURCHASED";
            case INAPP_ERROR_LOADING:
                return "INAPP_ERROR_LOADING";
            case INAPP_ERROR_SERVICE:
                return "INAPP_ERROR_SERVICE";
            case INAPP_ERROR_UNKNOWN:
                return "INAPP_ERROR_UNKNOWN";
            case INAPP_PROCESSING:
                return "INAPP_PROCESSING";
            case INAPP_ERROR_PURCHASE:
                return "INAPP_ERROR_PURCHASE";
            case INAPP_SUCCESS_PURCHASED:
                return "INAPP_SUCCESS_PURCHASED";
            case INAPP_ERROR_PID:
                return "INAPP_ERROR_PID";
            default:
                return "Unhandled";
        }
    }

    public int purchase(String pID) {
        int bpCode = bpErrorCode();
        if (bpCode != INAPP_PROCESSING) {

            return bpCode;
        }
        if (isPurchased(pID)) {

            return INAPP_ALREADY_PURCHASED;
        }
        bp.purchase(context, pID);
        return INAPP_PROCESSING;
    }

    public int bpErrorCode() {
        if (bp == null) {
            return INAPP_ERROR_UNKNOWN;
        }
        if (!isAvailableService()) {
            return INAPP_ERROR_SERVICE;
        }
        if (!bp.isInitialized()) {
            return INAPP_ERROR_LOADING;
        }
        return INAPP_PROCESSING;
    }

    public boolean isPurchased(String pID) {
        if (InAppBillingPref.getBooleanSetting(context, pID, false)) {
            return true;
        }
        int bpCode = bpErrorCode();
        if (bpCode != INAPP_PROCESSING) {
            return false;
        }
        if (bp.isPurchased(pID)) {
            InAppBillingPref.setSetting(context, pID, true);
            return true;
        }
        return false;
    }

    Activity context = null;

    public ArcInAppBilling(@Nullable Activity context) {
        this.context = context;
    }
    public void init(BillingProcessor.IBillingHandler appBillingListener){
        dialog = new ProgressDialog(context);
        dialog.setMessage("please wait...");
        dialog.show();
        VLog.w("GP license: " + context.getString(R.string.gp_inapp_li));
        VLog.w("GP Marchent: " + context.getString(R.string.gp_inapp_marchent));
        bp = new BillingProcessor(context, context.getString(R.string.gp_inapp_li),
                context.getString(R.string.gp_inapp_marchent), appBillingListener);
        bp.initialize();
    }
    public void onBillingInitialized() {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }


    public void onDestroy() {
        if (bp != null) {
            bp.release();
        }
    }
}
