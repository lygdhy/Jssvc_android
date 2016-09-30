package org.jssvc.lib.data;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by lygdh on 2016/9/30.
 */

public class AppPref {
    private static final String KEY_IS_FIRST_RUNNING = "is_first_running";

    private static SharedPreferences getPreference(Context context) {
        return context.getApplicationContext().getSharedPreferences("app_preference", Context.MODE_PRIVATE);
    }

    public static void setAlreadyRun(Context context) {
        getPreference(context).edit().putBoolean(KEY_IS_FIRST_RUNNING, false).apply();
    }

    public static boolean isFirstRunning(Context context) {
        return getPreference(context).getBoolean(KEY_IS_FIRST_RUNNING, true);
    }
}
