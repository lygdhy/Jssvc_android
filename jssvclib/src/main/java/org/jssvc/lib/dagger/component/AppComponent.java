package org.jssvc.lib.dagger.component;

import org.jssvc.lib.dagger.module.AppModule;
import org.jssvc.lib.dagger.module.BuildersModule;
import org.jssvc.lib.dagger.module.StorageModule;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjectionModule;

/**
 * Created by jjj on 2018/2/2.
 *
 * @description:
 */

@Singleton
@Component(modules = {
        AppModule.class,
        StorageModule.class,
        BuildersModule.class,
        AndroidInjectionModule.class
})
interface AppComponent{

}
