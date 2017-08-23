package com.benoitletondor.mvp.maps.sample;

import com.benoitletondor.mvp.maps.sample.injection.AppModule;
import com.benoitletondor.mvp.maps.sample.injection.DaggerAppComponent;

import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;

/**
 * Base application class
 *
 * @author Benoit LETONDOR
 */
public final class App extends DaggerApplication
{
    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector()
    {
        return DaggerAppComponent
            .builder()
            .appModule(new AppModule(this))
            .build();
    }
}
