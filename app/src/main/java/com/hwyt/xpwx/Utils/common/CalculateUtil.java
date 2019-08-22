package com.hwyt.xpwx.Utils.common;

/**
 * Created by Ray on 2017/3/29 0029.
 */

public class CalculateUtil {

    private static final String[] units = new String[]{"米", "千米", "万米"};

    public static String getSmartDistance(String meters) {
        long km = 1000;
        long tenKm = km * 10;
        long mMeter = 0;
        try {
            mMeter = Long.parseLong(meters);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mMeter >= tenKm) {
            return String.format("%.2f" + units[2], (float) mMeter / tenKm);
        } else if (mMeter >= km) {
            return String.format("%.1f"+ units[1], (float) mMeter / tenKm);
        } else
            return String.format("%d" + units[0], mMeter);
    }

}
