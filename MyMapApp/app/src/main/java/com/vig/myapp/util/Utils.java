package com.vig.myapp.util;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.valdesekamdem.library.mdtoast.MDToast;
import com.vig.myapp.models.UserData;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static String TAG = Utils.class.getSimpleName();
    public static List<UserData> list = new ArrayList<>();
    public static MDToast mdToast;
    public static String temp_filename = "user_data.txt";
    public static File tempFile;

    /**
     * Print the log with more value.
     **/
    public static void largeLog(String TAG, String str) {
        if (str.length() > 3000) {
            Log.i(TAG, str.substring(0, 3000));
            largeLog(TAG, str.substring(3000));
        } else {
            Log.i(TAG, str);
        }
    }

    /**
     * Show the success toast message
     **/
    public static void showSuccessToast(final Context context, final String s) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (mdToast != null) {
                    mdToast.cancel();
                }
                mdToast = MDToast.makeText(context, s, MDToast.LENGTH_LONG, MDToast.TYPE_SUCCESS);
                mdToast.show();
            }
        });
    }

    /**
     * Show the Error toast message
     **/
    public static void showErrorToast(final Context context, final String s) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (mdToast != null) {
                    mdToast.cancel();
                }
                mdToast = MDToast.makeText(context, s, MDToast.LENGTH_LONG, MDToast.TYPE_ERROR);
                mdToast.show();
            }
        });
    }

    public static void showInfoToast(final Context context, final String s) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (mdToast != null) {
                    mdToast.cancel();
                }
                mdToast = MDToast.makeText(context, s, MDToast.LENGTH_LONG, MDToast.TYPE_INFO);
                mdToast.show();
            }
        });
    }

    /**
     * Show the Warning toast message
     **/
    public static void showWarningToast(final Context context, final String s) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (mdToast != null) {
                    mdToast.cancel();
                }
                mdToast = MDToast.makeText(context, s, MDToast.LENGTH_LONG, MDToast.TYPE_WARNING);
                mdToast.show();
            }
        });
    }

    public static boolean validateCoOrdinates(double lat, double lon) {
        if (lat < -90 || lat > 90) {
            return false;
        } else if (lon < -180 || lon > 180) {
            return false;
        }
        return true;
    }

    public static Double roundTo6decimals(Double x) {
        return x == null ? null : Math.round(x * Math.pow(10, 6)) / Math.pow(10, 6);
    }

    public static File getCacheFile(Context context){
        File cacheDir = context.getCacheDir();
        tempFile = new File(cacheDir.getPath() + "/" + temp_filename);
        return tempFile;
    }

    public static void storeToCache(Context context, String data) {
        if (data != null && !data.equals("")) {
            FileWriter writer;
            try {
                writer = new FileWriter(getCacheFile(context));
                writer.write(data);
                writer.close();
                Log.d(TAG, "stored to cache");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isCacheExist(Context context) {
        boolean isExist;
        isExist = getCacheFile(context).exists();
        Log.d(TAG, "cache exists " + String.valueOf(isExist));
        return isExist;
    }

    public static void clearCache(Context context) {
        if (getCacheFile(context).exists()) {
            getCacheFile(context).delete();
            Log.d(TAG, "cache cleared");
        } else {
            showErrorToast(context, "File not found");
        }
    }

    public static String retrieveCache(Context context) {
        String strLine = "";
        StringBuilder text = new StringBuilder();
        /** Reading contents of the temporary file, if already exists */
        try {
            FileReader fReader = new FileReader(getCacheFile(context));
            BufferedReader bReader = new BufferedReader(fReader);
            /** Reading the contents of the file , line by line */
            while ((strLine = bReader.readLine()) != null) {
                text.append(strLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text.toString();
    }

}
