package com.benoitletondor.mvp.maps.view;

import android.support.annotation.NonNull;

import com.benoitletondor.mvp.core.view.View;
import com.google.android.gms.location.FusedLocationProviderClient;

/**
 * Base interface for a view displaying a map.
 *
 * @author Benoit LETONDOR
 */
public interface MapView extends View
{
    /**
     * Creates a location provider client associated with this view
     *
     * @return a location provider, ready to be used by the presenter
     */
    @NonNull
    FusedLocationProviderClient getFusedLocationProviderClient();

    /**
     * Display the map fragment and load the map. The OnMapReadyCallback
     * needs to be forwarded to the {@link com.benoitletondor.mvp.maps.presenter.MapPresenter}.
     */
    void loadMap();

    /**
     * Request the location permission and forward the result to the {@link com.benoitletondor.mvp.maps.presenter.MapPresenter}.
     */
    void requestLocationPermission();
}
