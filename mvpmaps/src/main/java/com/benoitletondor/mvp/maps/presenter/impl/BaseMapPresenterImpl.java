package com.benoitletondor.mvp.maps.presenter.impl;

import android.location.Location;
import android.support.annotation.NonNull;
import android.util.Log;

import com.benoitletondor.mvp.core.presenter.impl.BasePresenterImpl;
import com.benoitletondor.mvp.maps.presenter.MapPresenter;
import com.benoitletondor.mvp.maps.view.MapView;
import com.google.android.gms.location.FusedLocationProviderClient;

/**
 * Implementation of the {@link MapPresenter} that you should extends to provide a map view. If
 * you create it asking for user location, it will take care of the permission request.
 *
 * @author Benoit LETONDOR
 */
public abstract class BaseMapPresenterImpl<V extends MapView> extends BasePresenterImpl<V> implements MapPresenter<V>
{
    private final static String TAG = "BaseMapPresenterImpl";

    /**
     * Does this view needs geolocation of the user
     */
    private final boolean mNeedGeoloc;
    /**
     * Has the geolocation permission been denied by the user
     */
    private boolean mGeolocPermissionDenied = false;

    /**
     * Current presenter state
     */
    @NonNull
    private State mState = State.CREATED;

// ------------------------------------------->

    /**
     * Creates a new instance
     *
     * @param needGeolocation does the map needs user location
     */
    protected BaseMapPresenterImpl(boolean needGeolocation)
    {
        mNeedGeoloc = needGeolocation;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onStart(boolean viewCreated)
    {
        super.onStart(viewCreated);
        assert mView != null;

        switch ( mState )
        {
            case CREATED:
                askForLocationIfNeededOrDisplayMap();
                break;
            case WAITING_FOR_LOCATION_PERMISSION:
                mView.requestLocationPermission();
                break;
            case LOCATION_PERMISSION_GRANTED:
                loadMapAndLocationProvider();
                break;
            case LOCATION_PERMISSION_DENIED:
            case WAITING_FOR_MAP:
            case MAP_READY:
            case MAP_AVAILABLE:
                askForLocationIfNeededOrDisplayMap();
                break;
        }
    }

    /**
     * Called when GPS are ready or when there are not needed (since no location needed). This
     * method takes care of requesting the permission if needed or loading the map.
     */
    private void askForLocationIfNeededOrDisplayMap()
    {
        if ( mNeedGeoloc )
        {
            mState = State.WAITING_FOR_LOCATION_PERMISSION;
            if ( mView != null )
            {
                mView.requestLocationPermission();
            }
        }
        else
        {
            mState = State.WAITING_FOR_MAP;
            loadMapAndLocationProvider();
        }

        Log.d(TAG, "askForLocationIfNeededOrDisplayMap: " + mState);
    }

    /**
     * Load the map and optionally retrieve the {@link FusedLocationProviderClient} from the view if
     * {@link #mNeedGeoloc} is true
     */
    private void loadMapAndLocationProvider()
    {
        if ( mView != null )
        {
            if ( mNeedGeoloc && !mGeolocPermissionDenied )
            {
                mView.loadLocationProvider();
            }

            mView.loadMap();
        }
    }

    @Override
    public void onLocationPermissionGranted()
    {
        mState = State.LOCATION_PERMISSION_GRANTED;

        mGeolocPermissionDenied = false;
        loadMapAndLocationProvider();
    }

    @Override
    public void onLocationPermissionDenied()
    {
        mState = State.LOCATION_PERMISSION_DENIED;

        mGeolocPermissionDenied = true;
        loadMapAndLocationProvider();
    }

    @Override
    public void onLocationResult(@NonNull Location location)
    {
        if ( mView != null )
        {
            mView.updateUserLocation(location);
        }

        onUserLocationChanged(location);
    }

    @Override
    public void onMapReady()
    {
        if ( mState == State.MAP_READY )
        {
            return;
        }

        mState = State.MAP_READY;

        populateMap();
    }

    /**
     * Populate the map with the location source if needed
     */
    private void populateMap()
    {
        mState = State.MAP_AVAILABLE;

        if ( mNeedGeoloc && !mGeolocPermissionDenied )
        {
            if ( mView != null )
            {
                mView.enableUserLocation();
            }
        }

        if ( mView != null )
        {
            mView.onMapAvailable();
        }
    }

    @Override
    public void onLocationSourceActivated()
    {
        if ( mView != null )
        {
            mView.requestLocationUpdates(getLocationRequest());
        }
    }

    @Override
    public void onLocationSourceDeactivated()
    {
        if ( mView != null )
        {
            mView.removeLocationUpdates();
        }
    }

    /**
     * States of this presenter
     */
    private enum State
    {
        /**
         * Presenter has just been created and not yet started
         */
        CREATED,
        /**
         * Presenter is waiting for location permission response
         */
        WAITING_FOR_LOCATION_PERMISSION,
        /**
         * Location permission has been granted by the user
         */
        LOCATION_PERMISSION_GRANTED,
        /**
         * Location permission has been denied by the user
         */
        LOCATION_PERMISSION_DENIED,
        /**
         * Presenter is waiting for Google Maps to be ready
         */
        WAITING_FOR_MAP,
        /**
         * Google Maps is ready
         */
        MAP_READY,
        /**
         * Google maps is ready and available for child presenter
         */
        MAP_AVAILABLE
    }
}