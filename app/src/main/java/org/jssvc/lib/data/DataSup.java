package org.jssvc.lib.data;

import android.text.TextUtils;
import com.google.gson.Gson;
import org.jssvc.lib.bean.MemberBean;

import static org.jssvc.lib.base.BaseApplication.spUtils;
import static org.jssvc.lib.data.Constants.LOCAL_MEMBER;

/**
 * <pre>
 *     author : lygdh
 *     time   : 2017/09/28
 *     desc   : 数据支持
 *     version: 1.0
 * </pre>
 */
public class DataSup {

  // 设置本地会员信息
  public static void setMemberStr2Local(String json) {
    spUtils.put(LOCAL_MEMBER, json);
  }

  // 获取本地会员信息
  public static String getLocalMemberStr() {
    return spUtils.getString(LOCAL_MEMBER, "");
  }

  // 判断是否有账户登录
  public static boolean hasLogin() {
    return !TextUtils.isEmpty(getLocalMemberStr());
  }

  // 获取本地账户信息
  public static MemberBean getLocalMemberBean() {
    return TextUtils.isEmpty(getLocalMemberStr()) ? null
        : new Gson().fromJson(getLocalMemberStr(), MemberBean.class);
  }
}
