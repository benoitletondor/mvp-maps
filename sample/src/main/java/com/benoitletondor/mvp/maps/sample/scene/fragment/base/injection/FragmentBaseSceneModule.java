package com.benoitletondor.mvp.maps.sample.scene.fragment.base.injection;

import androidx.annotation.NonNull;

import com.benoitletondor.mvp.core.presenter.PresenterFactory;
import com.benoitletondor.mvp.maps.sample.scene.fragment.base.FragmentPresenter;
import com.benoitletondor.mvp.maps.sample.scene.fragment.base.impl.FragmentPresenterImpl;

import dagger.Module;
import dagger.Provides;

/**
 * Injection module for the base fragment scene
 *
 * @author Benoit LETONDOR
 */
@Module
public final class FragmentBaseSceneModule
{
    @Provides
    PresenterFactory<FragmentPresenter> provideFragmentPresenterFactory()
    {
        return new FragmentPresenterImplFactory();
    }

    private static final class FragmentPresenterImplFactory implements PresenterFactory<FragmentPresenter>
    {
        @NonNull
        @Override
        public FragmentPresenter create()
        {
            return new FragmentPresenterImpl();
        }
    }
}
