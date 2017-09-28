package org.jssvc.lib.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;

import org.jssvc.lib.bean.LibraryUser;

/**
 * Created by lygdh on 2016/9/30.
 */

public class AccountPref {
  private static final String KEY_LOGIN_ACCOUNT_PWD = "login_account_pwd";
  private static final String KEY_LOGIN_ACCOUNT_NUMBER = "login_account_number";
  private static final String KEY_LOGIN_TYPE = "login_type";
  private static final String KEY_LOGON_USER = "logon_user";

  private static SharedPreferences getPreference(Context context) {
    return context.getApplicationContext()
        .getSharedPreferences("account_preference", Context.MODE_PRIVATE);
  }

  // 登录账号
  public static void saveLoginAccoundNumber(Context context, String loginToken) {
    getPreference(context).edit().putString(KEY_LOGIN_ACCOUNT_NUMBER, loginToken).apply();
  }

  public static String getLogonAccoundNumber(Context context) {
    return getPreference(context).getString(KEY_LOGIN_ACCOUNT_NUMBER, "");
  }

  // 登录方式
  public static void saveLoginType(Context context, String loginToken) {
    getPreference(context).edit().putString(KEY_LOGIN_TYPE, loginToken).apply();
  }

  public static String getLogonType(Context context) {
    return getPreference(context).getString(KEY_LOGIN_TYPE, "");
  }

  // 登录密码
  public static void saveLoginAccoundPwd(Context context, String loginToken) {
    getPreference(context).edit().putString(KEY_LOGIN_ACCOUNT_PWD, loginToken).apply();
  }

  public static String getLogonAccoundPwd(Context context) {
    return getPreference(context).getString(KEY_LOGIN_ACCOUNT_PWD, "");
  }

  public static void removeLogonAccoundPwd(Context context) {
    getPreference(context).edit().remove(KEY_LOGIN_ACCOUNT_PWD).apply();
  }

  // 用户信息
  public static void saveLogonUser(Context context, LibraryUser user) {
    String userJson = new Gson().toJson(user);
    getPreference(context).edit().putString(KEY_LOGON_USER, userJson).apply();
  }

  public static void removeLogonUser(Context context) {
    getPreference(context).edit().remove(KEY_LOGON_USER).apply();
  }

  public static LibraryUser getLogonUser(Context context) {
    LibraryUser user = null;
    String userJson = getPreference(context).getString(KEY_LOGON_USER, "");
    if (!TextUtils.isEmpty(userJson)) {
      user = new Gson().fromJson(userJson, LibraryUser.class);
    }
    return user;
  }

  // 判断登录的依据
  public static boolean isLogon(Context context) {
    return !TextUtils.isEmpty(getLogonAccoundNumber(context)) && !TextUtils.isEmpty(
        getLogonAccoundPwd(context));
  }
}
