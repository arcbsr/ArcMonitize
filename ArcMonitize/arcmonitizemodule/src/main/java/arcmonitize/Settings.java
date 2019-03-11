package arcmonitize;

import android.app.Activity;

public class Settings {

    public static final boolean IS_ADS_ENABLE = true;
    private static String TEST_DEVICE_ID = "";
    public static final int ADS_INTERVAL = 3;

    public static String getTestDeviceId(Activity activity) {
        if (!TEST_DEVICE_ID.isEmpty()) {
            return TEST_DEVICE_ID;
        }
        String androidId = android.provider.Settings.Secure.getString(activity.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        TEST_DEVICE_ID = MD5(androidId).toUpperCase();
        return TEST_DEVICE_ID;
    }

    public static String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (Exception e) {
        }
        return "";
    }
}
