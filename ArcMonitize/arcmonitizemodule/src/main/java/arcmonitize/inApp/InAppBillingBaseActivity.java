package arcmonitize.inApp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

public class InAppBillingBaseActivity extends AppCompatActivity {
    public ArcInAppBilling arcInAppBilling;

    //adb shell pm clear com.android.vending
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (arcInAppBilling != null) {
            if (!arcInAppBilling.getBillingProcessor().handleActivityResult(requestCode, resultCode, data)) {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }
}
