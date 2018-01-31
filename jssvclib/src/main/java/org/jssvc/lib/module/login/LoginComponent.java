package org.jssvc.lib.module.login;

import dagger.Component;

/**
 * Created by jjj on 2018/1/31.
 *
 * @description:
 */
@Component(modules = LoginModule.class)
public interface LoginComponent {
    void inject(LoginActivity activity);
}
