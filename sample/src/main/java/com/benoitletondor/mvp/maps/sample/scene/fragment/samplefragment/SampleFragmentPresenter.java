package com.benoitletondor.mvp.maps.sample.scene.fragment.samplefragment;

import com.benoitletondor.mvp.maps.presenter.MapPresenter;

/**
 * Presenter of the {@link SampleFragmentView}
 *
 * @author Benoit LETONDOR
 */
public interface SampleFragmentPresenter extends MapPresenter<SampleFragmentView>
{
    /**
     * Called when the map is available to use and displayed to the user
     */
    void onMapReady();

    /**
     * Called when the map not available due to an error loading it
     */
    void onMapUnavailable();
}
