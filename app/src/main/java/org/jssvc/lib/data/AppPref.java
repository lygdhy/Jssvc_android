package org.jssvc.lib.data;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by lygdh on 2016/9/30.
 */

public class AppPref {
  private static final String KEY_IS_FIRST_RUNNING = "is_first_running";
  private static final String KEY_IS_READ_HELP_TIP = "is_first_help";
  private static final String KEY_BOOK_SEARCH = "search_key";

  private static SharedPreferences getPreference(Context context) {
    return context.getApplicationContext()
        .getSharedPreferences("app_preference", Context.MODE_PRIVATE);
  }

  public static void setAlreadyRun(Context context) {
    getPreference(context).edit().putBoolean(KEY_IS_FIRST_RUNNING, false).apply();
  }

  public static boolean isFirstRunning(Context context) {
    return getPreference(context).getBoolean(KEY_IS_FIRST_RUNNING, true);
  }

  public static void setHelpClose(Context context) {
    getPreference(context).edit().putBoolean(KEY_IS_READ_HELP_TIP, false).apply();
  }

  public static boolean isFirstHelp(Context context) {
    return getPreference(context).getBoolean(KEY_IS_READ_HELP_TIP, true);
  }

  // 搜索记录start
  public static String getSearchKey(Context context) {
    return getPreference(context).getString(KEY_BOOK_SEARCH, "");
  }

  public static boolean saveSearchKey(Context context, String values) {
    return getPreference(context).edit().putString(KEY_BOOK_SEARCH, values).commit();
  }

  public static boolean clearSearchKey(Context context) {
    return getPreference(context).edit().putString(KEY_BOOK_SEARCH, "").commit();
  }
  // 搜索记录end
}
