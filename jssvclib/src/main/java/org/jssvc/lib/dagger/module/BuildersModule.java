package org.jssvc.lib.dagger.module;

import android.app.Activity;

import org.jssvc.lib.module.login.LoginActivity;

import dagger.Binds;
import dagger.Module;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjector;
import dagger.multibindings.IntoMap;

/**
 * Created by jjj on 2018/2/2.
 *
 * @description:
 */
@Module(subcomponents = {UserComponent.class})
public abstract class BuildersModule {
    @Binds
    @IntoMap
    @ActivityKey(LoginActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindSecondActivityInjectorFactory(UserComponent.Builder builder);

}
