package com.benoitletondor.mvp.maps.sample.scene.fragment.samplefragment;

import com.benoitletondor.mvp.maps.presenter.MapPresenter;

/**
 * Presenter of the {@link SampleFragmentView}
 *
 * @author Benoit LETONDOR
 */
public interface SampleFragmentPresenter extends MapPresenter<SampleFragmentView>
{
    void onMapAvailable();
}
