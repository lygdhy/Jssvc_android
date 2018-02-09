package org.jssvc.lib.dagger.module;

import org.jssvc.lib.module.login.LoginActivity;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

/**
 * Created by jjj on 2018/2/2.
 *
 * @description:
 */

@Subcomponent(modules = {UserModule.class})
public interface UserComponent extends AndroidInjector<LoginActivity> {

    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<LoginActivity> {}

}
