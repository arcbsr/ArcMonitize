package arcmonitize.inApp;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;

public interface InAppBillingListener {
    void notReady();

    void readyToPurchase();

    void alreadyPurchased( String productId);

    void onPurchaseHistoryRestored();

    void purchasedSucessFully( String productId);

    void purchasedFailed(int errorCode, Throwable error);

    void iabServiceNotAvailable();
    void iabServiceError(int arcErrorCode);
}