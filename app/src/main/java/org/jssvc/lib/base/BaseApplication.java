package org.jssvc.lib.base;

import android.support.multidex.MultiDexApplication;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.Utils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.cookie.store.PersistentCookieStore;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;
import com.mob.MobSDK;
import com.pgyersdk.crash.PgyCrashManager;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by lygdh on 2016/11/15.
 */

public class BaseApplication extends MultiDexApplication {

  public static SPUtils spUtils;
  private static final String FILE_NAME = "sp_jssvc_lib";

  BaseApplication appContext;

  @Override public void onCreate() {
    super.onCreate();
    appContext = this;

    Utils.init(appContext);
    //if (spUtils == null) {
    //  spUtils = new SPUtils(FILE_NAME);// 实例化SharePreferenceUtils
    //}

    // 初始化Fresco
    Fresco.initialize(this);

    // 蒲公英
    PgyCrashManager.register(this);

    // 友盟错误统计
    MobclickAgent.setCatchUncaughtExceptions(true);

    // 初始化OkGo
    initOkGo();

    // Mob SDK
    //SMSSDK.initSDK(this, "1e85bf08b2f08", "7d2ed9146830fca64601549ca9f87c93");
    MobSDK.init(this, null, null);
  }

  // 初始化OkGo
  private void initOkGo() {
    //---------这里给出的是示例代码,告诉你可以这么传,实际使用的时候,根据需要传,不需要就不传-------------//
    HttpHeaders headers = new HttpHeaders();
    //        headers.put("commonHeaderKey1", "commonHeaderValue1");    //header不支持中文
    //        headers.put("commonHeaderKey2", "commonHeaderValue2");
    HttpParams params = new HttpParams();
    //        params.put("commonParamsKey1", "commonParamsValue1");     //param支持中文,直接传,不要自己编码
    //        params.put("commonParamsKey2", "这里支持中文参数");
    //-----------------------------------------------------------------------------------//

    //必须调用初始化
    OkGo.init(this);

    //以下设置的所有参数是全局参数,同样的参数可以在请求的时候再设置一遍,那么对于该请求来讲,请求中的参数会覆盖全局参数
    //好处是全局参数统一,特定请求可以特别定制参数
    try {
      //以下都不是必须的，根据需要自行选择,一般来说只需要 debug,缓存相关,cookie相关的 就可以了
      OkGo.getInstance()

          //打开该调试开关,控制台会使用 红色error 级别打印log,并不是错误,是为了显眼,不需要就不要加入该行
          .debug("OkGo")

          //如果使用默认的 60秒,以下三行也不需要传
          .setConnectTimeout(OkGo.DEFAULT_MILLISECONDS)  //全局的连接超时时间
          .setReadTimeOut(OkGo.DEFAULT_MILLISECONDS)     //全局的读取超时时间
          .setWriteTimeOut(OkGo.DEFAULT_MILLISECONDS)    //全局的写入超时时间

          //可以全局统一设置缓存模式,默认是不使用缓存,可以不传,具体其他模式看 github 介绍 https://github.com/jeasonlzy/
          .setCacheMode(CacheMode.NO_CACHE)

          //可以全局统一设置缓存时间,默认永不过期,具体使用方法看 github 介绍
          .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)

          //如果不想让框架管理cookie,以下不需要
          // .setCookieStore(new MemoryCookieStore())           //cookie使用内存缓存（app退出后，cookie消失）
          .setCookieStore(new PersistentCookieStore())          //cookie持久化存储，如果cookie不过期，则一直有效

          //这两行同上,不需要就不要传
          .addCommonHeaders(headers)                                         //设置全局公共头
          .addCommonParams(params);                                          //设置全局公共参数
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
