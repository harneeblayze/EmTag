package com.stickercamera.app.camera.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import java.io.InputStream;
import java.io.OutputStream;

public class Utils {

    public static Bitmap photo;
    public static String urlTagit = "http://www.emrals.com/api/add_alert/";
    public static String urlLogin = "http://www.emrals.com";
    public static int loginCheck = 0;
    public static String imgPath = "";


    public static void CopyStream(InputStream is, OutputStream os) {
        final int buffer_size = 1024;
        try {
            byte[] bytes = new byte[buffer_size];
            for (; ; ) {
                int count = is.read(bytes, 0, buffer_size);
                if (count == -1)
                    break;
                os.write(bytes, 0, count);
            }
        } catch (Exception ex) {
        }
    }

    /**
     * Method to remove SharedPreferences data
     *
     * @param preferenceName Name of the preference
     */
    public static void removeSharedPreference(Context context, String preferenceName) {
        SharedPreferences pref = context.getSharedPreferences(preferenceName, 0); // 0 - for private
        // mode
        pref.edit().remove(preferenceName).apply();
    }


}
