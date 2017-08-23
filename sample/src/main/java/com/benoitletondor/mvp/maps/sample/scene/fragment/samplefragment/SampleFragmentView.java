package com.benoitletondor.mvp.maps.sample.scene.fragment.samplefragment;

import com.benoitletondor.mvp.maps.view.MapView;

/**
 * View that displays a simple map
 *
 * @author Benoit LETONDOR
 */
public interface SampleFragmentView extends MapView
{
    /**
     * Show an alert explaining that map isn't available
     */
    void showMapNotAvailableAlert();
}
