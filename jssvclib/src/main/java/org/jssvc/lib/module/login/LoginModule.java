package org.jssvc.lib.module.login;

import dagger.Module;
import dagger.Provides;

/**
 * Created by jjj on 2018/1/31.
 *
 * @description:
 */
@Module
public class LoginModule {
    private final LoginContract.View view;

    public LoginModule(LoginContract.View view) {
        this.view = view;
    }

    @Provides
    LoginContract.View provideView(){
        return view;
    }

}
