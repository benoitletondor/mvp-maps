package com.benoitletondor.mvp.maps.sample.scene.main;

import com.benoitletondor.mvp.maps.view.MapView;
import com.google.android.gms.maps.CameraUpdate;

/**
 * Main view, starting point of the application, displaying a map and a button to the fragment view
 *
 * @author Benoit LETONDOR
 */
public interface MainView extends MapView
{
    /**
     * Start the fragment activity
     */
    void startFragmentActivity();

    /**
     * Show an alert explaining that map isn't available
     */
    void showMapNotAvailableAlert();

    void animateMapCamera(CameraUpdate cameraUpdate);
}
