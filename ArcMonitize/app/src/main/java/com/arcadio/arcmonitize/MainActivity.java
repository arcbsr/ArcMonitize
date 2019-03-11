package com.arcadio.arcmonitize;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import arcmonitize.Settings;
import arcmonitize.inApp.InAppBillingBaseActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InAppBillingBaseActivity.startActivityForPurchase(MainActivity.this,
                        1000, Settings.KEY_INAPP_TEST);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            Log.w("onActivityResult", data.getStringExtra(InAppBillingBaseActivity.KEY_PRODUCT_ID));
            Log.w("onActivityResult",
                    data.getStringExtra(InAppBillingBaseActivity.KEY_RESULTMSG));
        }
    }
}
