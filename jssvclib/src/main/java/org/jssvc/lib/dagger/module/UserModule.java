package org.jssvc.lib.dagger.module;

import org.jssvc.lib.dagger.Login;

import dagger.Module;
import dagger.Provides;

/**
 * Created by jjj on 2018/2/2.
 *
 * @description:
 */

@Module
public class UserModule {

    @Provides
    Login provideXiaoMingUser() {
        Login xiaomin = new Login();
        xiaomin.setPassword("******");
        xiaomin.setName("小明");
        return xiaomin;
    }

}
