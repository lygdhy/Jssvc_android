package org.jssvc.lib.module.login;

/**
 * Created by jjj on 2018/1/31.
 *
 * @description:
 */

public class LoginContract {
  interface View {
    void onLoginSuccess(String msg);

    void onLoginComplete();
  }
}
