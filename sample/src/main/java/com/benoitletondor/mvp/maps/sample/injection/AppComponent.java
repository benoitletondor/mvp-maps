package com.benoitletondor.mvp.maps.sample.injection;

import android.content.Context;

import com.benoitletondor.mvp.maps.sample.App;

import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

/**
 * Base component for all app injection
 *
 * @author Benoit LETONDOR
 */
@Component(
    modules = {
        AndroidSupportInjectionModule.class,
        AppModule.class,
        SceneInjectorsModule.class
    }
)
interface AppComponent extends AndroidInjector<App>
{
    App getApplication();
    Context getApplicationContext();
}
