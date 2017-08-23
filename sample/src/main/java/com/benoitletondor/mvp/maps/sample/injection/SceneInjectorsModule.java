package com.benoitletondor.mvp.maps.sample.injection;

import com.benoitletondor.mvp.maps.sample.scene.fragment.base.impl.FragmentActivity;
import com.benoitletondor.mvp.maps.sample.scene.fragment.base.injection.FragmentBaseSceneModule;
import com.benoitletondor.mvp.maps.sample.scene.fragment.samplefragment.impl.SampleFragment;
import com.benoitletondor.mvp.maps.sample.scene.fragment.samplefragment.injection.FragmentSampleSceneModule;
import com.benoitletondor.mvp.maps.sample.scene.main.impl.MainActivity;
import com.benoitletondor.mvp.maps.sample.scene.main.injection.MainSceneModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Module in charge of injecting all scenes of the app
 *
 * @author Benoit LETONDOR
 */
@Module
abstract class SceneInjectorsModule
{
    // Main scene
    @ActivityScope
    @ContributesAndroidInjector(modules = {
        MainSceneModule.class
    })
    abstract MainActivity MainActivityInjector();

    // Base fragment scene
    @ActivityScope
    @ContributesAndroidInjector(modules = {
        FragmentBaseSceneModule.class
    })
    abstract FragmentActivity FragmentActivityInjector();

    // Sample fragment scene
    @FragmentScope
    @ContributesAndroidInjector(modules = {
        FragmentSampleSceneModule.class
    })
    abstract SampleFragment SampleFragmentInjector();
}
