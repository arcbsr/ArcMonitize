package arcmonitize.analytic;

import android.content.Context;


/**
 * Created by arcadio on 18/08/18.
 */

public class AnalyticControllerPanel {
//    private FirebaseAnalytics mFirebaseAnalytics;
    private Context mContext;

    public AnalyticControllerPanel(Context context) {
        this.mContext = context;
//        mFirebaseAnalytics = FirebaseAnalytics.getInstance(mContext);
    }

    public void pushToAnalytic(String name, String type, String id) {

//        if (mFirebaseAnalytics == null || !PMSharedPrefUtil.getBooleanSetting(mContext, "analytics", true)) {
//            //LogUtil.printLog("analytics status: ", "Cancel and reject");
//            return;
//        }
        //LogUtil.printLog("analytics status: ", "Triggerd");
//        Bundle bundle = new Bundle();
//        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id);
//        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);
//        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, type);
//        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    public void pushToAnalytic(String type) {
        pushToAnalytic("not_given", type, "not_given_id");
    }

    public void pushToAnalytic(String type, String id) {
        pushToAnalytic("not_given", type, id);
    }

}
