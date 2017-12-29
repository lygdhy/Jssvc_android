package org.jssvc.lib.data;

import android.text.TextUtils;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.jssvc.lib.bean.KVBean;
import org.jssvc.lib.bean.ThirdAccountBean;
import org.jssvc.lib.bean.UserBean;

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

  // 保存用户Json
  public static void saveUserJson2Local(String json) {
    spUtils.put(Constants.LOCAL_USER, json);
  }

  // 获取用户Json
  public static String getLocalUserJson() {
    return spUtils.getString(Constants.LOCAL_USER, "");
  }

  // 判断是否有账户登录
  public static boolean hasUserLogin() {
    return !TextUtils.isEmpty(getLocalUserJson());
  }

  // 获取本地账户信息
  public static UserBean getLocalUserBean() {
    return TextUtils.isEmpty(getLocalUserJson()) ? null
        : new Gson().fromJson(getLocalUserJson(), UserBean.class);
  }

  // 保存第三方账户信息
  public static void saveThirdAccountJson2Local(String json) {
    spUtils.put(Constants.THIRD_ACCOUNT, json);
  }

  // 获取第三方账户信息
  public static String getLocalThirdAccountJson() {
    return spUtils.getString(Constants.THIRD_ACCOUNT, "");
  }

  // 获取第三方账户信息
  public static List<ThirdAccountBean> getThirdAccountBeans() {
    List<ThirdAccountBean> list = new ArrayList<>();
    try {
      JSONArray jsonArr = new JSONArray(getLocalThirdAccountJson());
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
  public static ThirdAccountBean getThirdAccountBeanByCode(String code) {
    List<ThirdAccountBean> list = DataSup.getThirdAccountBeans();
    for (int i = 0; i < list.size(); i++) {
      if (list.get(i).getPlatform().equals(code)) {
        return list.get(i);
      }
    }
    return null;
  }

  // 获取图书馆账号
  public static ThirdAccountBean getLibThirdAccount() {
    return getThirdAccountBeanByCode(Constants.THIRD_ACCOUNT_CODE_LIB);
  }

  // 获取教务账号
  public static ThirdAccountBean getJwThirdAccount() {
    return getThirdAccountBeanByCode(Constants.THIRD_ACCOUNT_CODE_JW);
  }

  // ===========================================================
  // 获取搜索记录
  public static List<KVBean> getSearchHisList() {
    List<KVBean> lvlist = new ArrayList<>();
    String hisStr = spUtils.getString(Constants.BOOK_SEARCH_HIS, "");
    if (!TextUtils.isEmpty(hisStr)) {
      // key^,^value^!^
      String[] arr = hisStr.split("\\^!\\^");
      for (int i = 0; i < arr.length; i++) {
        String[] kv = arr[i].split("\\^,\\^");
        if (kv.length == 2) lvlist.add(new KVBean(kv[0] + "", kv[1] + ""));
      }
    }
    return lvlist;
  }

  // 插入记录
  public static void insertSearchHis(String key, String value) {
    List<KVBean> lvlist = getSearchHisList();
    for (int i = 0; i < lvlist.size(); i++) {
      if (lvlist.get(i).getValue().equals(value)) {
        return;
      }
    }
    String hisStr = spUtils.getString(Constants.BOOK_SEARCH_HIS, "");
    hisStr = key + "^,^" + value + "^!^" + hisStr;
    spUtils.put(Constants.BOOK_SEARCH_HIS, hisStr);
  }

  // 删除记录
  public static void clearSearchHis() {
    spUtils.put(Constants.BOOK_SEARCH_HIS, "");
  }

  // 删除指定项
  public static void deleteSearchHis(String value) {
    List<KVBean> lvlist = getSearchHisList();
    String tagStr = "";
    for (int i = 0; i < lvlist.size(); i++) {
      if (lvlist.get(i).getValue().equals(value)) {
        tagStr = lvlist.get(i).getKey() + "^,^" + lvlist.get(i).getValue() + "^!^";
      }
    }
    String hisStr = spUtils.getString(Constants.BOOK_SEARCH_HIS, "");
    hisStr = hisStr.replace(tagStr, "");
    spUtils.put(Constants.BOOK_SEARCH_HIS, hisStr);
  }
  // ===========================================================
}
