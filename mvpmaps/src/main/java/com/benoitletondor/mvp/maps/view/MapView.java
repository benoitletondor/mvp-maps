package com.benoitletondor.mvp.maps.view;

import android.location.Location;

import com.benoitletondor.mvp.core.view.View;
import com.google.android.gms.location.LocationRequest;

/**
 * Base interface for a view displaying a map.
 *
 * @author Benoit LETONDOR
 */
public interface MapView extends View
{
    /**
     * Display the map fragment and load the map. The OnMapReadyCallback
     * needs to be forwarded to the {@link com.benoitletondor.mvp.maps.presenter.MapPresenter}.
     */
    void loadMap();

    /**
     * Request the location permission and forward the result to the {@link com.benoitletondor.mvp.maps.presenter.MapPresenter}.
     */
    void requestLocationPermission();

    /**
     * Enables user location on the map and links it to its location source
     */
    void enableUserLocation();

    /**
     * Called when the map is set-up and ready to be filled with data
     */
    void onMapAvailable();

    void loadLocationProvider();

    /**
     * Request location updates from the location provider
     *
     * @param locationRequest
     */
    void requestLocationUpdates(LocationRequest locationRequest);

    /**
     * Removes location request updates from the location provider
     */
    void removeLocationUpdates();

    /**
     * Updates the location source with {@param location}
     *
     * @param location
     */
    void updateUserLocation(Location location);
}
