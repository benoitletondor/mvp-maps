package com.benoitletondor.mvp.maps.sample;

import com.benoitletondor.mvp.maps.sample.injection.AppModule;
import com.benoitletondor.mvp.maps.sample.injection.DaggerAppComponent;
import com.squareup.leakcanary.LeakCanary;

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
    public void onCreate()
    {
        super.onCreate();

        if( LeakCanary.isInAnalyzerProcess(this) )
        {
            return;
        }

        LeakCanary.install(this);
    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector()
    {
        return DaggerAppComponent
            .builder()
            .appModule(new AppModule(this))
            .build();
    }
}
