package org.jssvc.lib.data;

import android.text.TextUtils;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.jssvc.lib.bean.MemberBean;
import org.jssvc.lib.bean.ThirdAccountBean;

import static org.jssvc.lib.base.BaseApplication.spUtils;

/**
 * <pre>
 *     author : lygdh
 *     time   : 2017/09/28
 *     desc   : 数据支持
 *     version: 1.0
 * </pre>
 */
public class DataSup {

  // 保存本地会员信息
  public static void setMemberStr2Local(String json) {
    spUtils.put(Constants.LOCAL_MEMBER, json);
  }

  // 获取本地会员信息
  public static String getLocalMemberStr() {
    return spUtils.getString(Constants.LOCAL_MEMBER, "");
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

  // 保存第三方账户信息
  public static void setThirdAccountStr2Local(String json) {
    spUtils.put(Constants.THIRD_ACCOUNT, json);
  }

  // 获取第三方账户信息
  public static String getLocalThirdAccountStr() {
    return spUtils.getString(Constants.THIRD_ACCOUNT, "");
  }

  // 获取第三方账户信息
  public static List<ThirdAccountBean> getThirdAccountArr() {
    List<ThirdAccountBean> list = new ArrayList<>();
    try {
      JSONArray jsonArr = new JSONArray(getLocalThirdAccountStr());
      for (int i = 0; i < jsonArr.length(); i++) {
        ThirdAccountBean item =
            new Gson().fromJson(jsonArr.get(i).toString(), ThirdAccountBean.class);
        list.add(item);
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return list;
  }

  // 获取单项第三方账户数据
  public static ThirdAccountBean getThirdAccountBean(String code) {
    List<ThirdAccountBean> list = DataSup.getThirdAccountArr();
    for (int i = 0; i < list.size(); i++) {
      if (list.get(i).getPlatform().equals(code)) {
        return list.get(i);
      }
    }
    return null;
  }
}
