package arcmonitize.inApp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;

import arcmonitize.Settings;

public class InAppBillingBaseActivity extends AppCompatActivity implements BillingProcessor.IBillingHandler {
    public ArcInAppBilling arcInAppBilling;
    String mProductId = Settings.KEY_INAPP_TEST;
    public static final String KEY_PRODUCT_ID = "mProductId";
    public static final String KEY_RESULTCODE = "req_data";
    public static final String KEY_RESULTMSG = "req_msg";
    public static final int KEY_REQUESTCODE = 1155;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProductId = getIntent().getStringExtra(KEY_PRODUCT_ID);
        arcInAppBilling = new ArcInAppBilling(this);
        if (mProductId.length() == 0) {
            onFinishedActivity(RESULT_CANCELED, ArcInAppBilling.INAPP_ERROR_PID);
            return;
        }
        if (arcInAppBilling.isPurchased(mProductId)) {
            onFinishedActivity(RESULT_CANCELED, ArcInAppBilling.INAPP_ALREADY_PURCHASED);
            return;
        }
        arcInAppBilling.init(this);
    }

    //adb shell pm clear com.android.vending
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (arcInAppBilling != null) {
            if (!arcInAppBilling.getBillingProcessor().handleActivityResult(requestCode, resultCode, data)) {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    @Override
    public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {
        onFinishedActivity(RESULT_OK, ArcInAppBilling.INAPP_SUCCESS_PURCHASED);
    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int errorCode, @Nullable Throwable error) {
        onFinishedActivity(RESULT_CANCELED, errorCode);
    }

    @Override
    public void onBillingInitialized() {
        arcInAppBilling.onBillingInitialized();
        if (arcInAppBilling == null) {
            onFinishedActivity(RESULT_CANCELED);
            return;
        }
        int bpCode = arcInAppBilling.purchase(mProductId);
        if (bpCode != ArcInAppBilling.INAPP_PROCESSING) {
            onFinishedActivity(RESULT_CANCELED, bpCode);
        }
    }

    public void onFinishedActivity(int resultCode) {
        onFinishedActivity(resultCode, ArcInAppBilling.INAPP_ERROR_UNKNOWN);
    }

    public void onFinishedActivity(int activityResult, int resultCode) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(KEY_RESULTCODE, resultCode);
        resultIntent.putExtra(KEY_RESULTMSG, ArcInAppBilling.getbpCodeStatus(resultCode));
        resultIntent.putExtra(KEY_PRODUCT_ID, mProductId);
        setResult(activityResult, resultIntent);
        finish();
    }

    @Override
    protected void onDestroy() {
        if (arcInAppBilling != null)
            arcInAppBilling.onDestroy();
        super.onDestroy();
    }

    public static void startActivityForPurchase(Activity context, int requestcode, String pid) {
        Intent intent = new Intent(context, InAppBillingBaseActivity.class);
        intent.putExtra(KEY_PRODUCT_ID, pid);
        context.startActivityForResult(intent, requestcode);
    }

    public static boolean isPurchased(Activity context, String pid) {
        if (InAppBillingPref.getBooleanSetting(context, pid, false)) {
            return true;
        }
        return false;
    }
}
