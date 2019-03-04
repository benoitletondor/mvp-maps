package com.benoitletondor.mvp.maps.sample.scene.main;

import com.benoitletondor.mvp.maps.presenter.MapPresenter;

/**
 * Presenter for the {@link MainView}
 *
 * @author Benoit LETONDOR
 */
public interface MainPresenter extends MapPresenter<MainView>
{
    /**
     * Called when the user clicks on the start fragment activity button
     */
    void onStartFragmentActivityButtonClicked();

    /**
     * Called when the map is available to use and displayed to the user
     */
    void onMapReady();

    /**
     * Called when the map not available due to an error loading it
     */
    void onMapUnavailable();
}
