package com.benoitletondor.mvp.maps.view;

import android.location.Location;

import androidx.annotation.Nullable;

import com.benoitletondor.mvp.core.view.View;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.GoogleMapOptions;

/**
 * Base interface for a view displaying a map.
 *
 * @author Benoit LETONDOR
 */
public interface MapView extends View
{
    /**
     * Called when the map is set-up and ready to be filled with data
     */
    void onMapReady();

    /**
     * Called when the map is not available due to an error
     */
    void onMapUnavailable();

    /**
     * Called when creating the map to get the options to pass to the map
     *
     * @return specific options if the view needs them, null otherwise
     */
    @Nullable
    GoogleMapOptions getGoogleMapOptions();

// ------------------------------------------------>

    /**
     * Display the map fragment and load the map. The OnMapReadyCallback
     * needs to be forwarded to the {@link com.benoitletondor.mvp.maps.presenter.MapPresenter}.
     * You don't need to override this method as {@link com.benoitletondor.mvp.maps.view.impl.BaseMVPMapActivity}
     * and {@link com.benoitletondor.mvp.maps.view.impl.BaseMVPMapFragment} already do it.
     */
    void loadMap();

    /**
     * Request the location permission and forward the result to the {@link com.benoitletondor.mvp.maps.presenter.MapPresenter}.
     * You don't need to override this method as {@link com.benoitletondor.mvp.maps.view.impl.BaseMVPMapActivity}
     * and {@link com.benoitletondor.mvp.maps.view.impl.BaseMVPMapFragment} already do it.
     */
    void requestLocationPermission();

    /**
     * Enables user location on the map and links it to its location source.
     * You don't need to override this method as {@link com.benoitletondor.mvp.maps.view.impl.BaseMVPMapActivity}
     * and {@link com.benoitletondor.mvp.maps.view.impl.BaseMVPMapFragment} already do it.
     */
    void enableUserLocation();

    /**
     * Request location updates from the location provider.
     * You don't need to override this method as {@link com.benoitletondor.mvp.maps.view.impl.BaseMVPMapActivity}
     * and {@link com.benoitletondor.mvp.maps.view.impl.BaseMVPMapFragment} already do it.
     *
     * @param locationRequest the location request given to the location provider.
     */
    void requestLocationUpdates(LocationRequest locationRequest);

    /**
     * Removes location request updates from the location provider.
     * You don't need to override this method as {@link com.benoitletondor.mvp.maps.view.impl.BaseMVPMapActivity}
     * and {@link com.benoitletondor.mvp.maps.view.impl.BaseMVPMapFragment} already do it.
     */
    void removeLocationUpdates();

    /**
     * Updates the user location on the map.
     * You don't need to override this method as {@link com.benoitletondor.mvp.maps.view.impl.BaseMVPMapActivity}
     * and {@link com.benoitletondor.mvp.maps.view.impl.BaseMVPMapFragment} already do it.
     *
     * @param location the location with which to update the user location
     */
    void updateUserLocation(Location location);
}
