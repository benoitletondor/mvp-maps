package com.benoitletondor.mvp.maps.presenter.impl;

import android.annotation.SuppressLint;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.benoitletondor.mvp.core.presenter.impl.BasePresenterImpl;
import com.benoitletondor.mvp.maps.presenter.MapPresenter;
import com.benoitletondor.mvp.maps.view.MapView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.LocationSource;

/**
 * Implementation of the {@link MapPresenter} that you should extends to provide a map view. If
 * you create it asking for user location, it will take care of the permission request.
 *
 * @author Benoit LETONDOR
 */
@SuppressLint("MissingPermission")
public abstract class BaseMapPresenterImpl<V extends MapView> extends BasePresenterImpl<V> implements MapPresenter<V>, LocationSource
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
     * Listener for location update
     */
    @NonNull
    private final LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult)
        {
            super.onLocationResult(locationResult);

            final Location location = locationResult.getLastLocation();

            if( mLocationChangeListener != null )
            {
                mLocationChangeListener.onLocationChanged(location);
            }

            onUserLocationChanged(location);
        }

        @Override
        public void onLocationAvailability(LocationAvailability locationAvailability)
        {
            super.onLocationAvailability(locationAvailability);
        }
    };

    /**
     * Play services client
     */
    @Nullable
    private FusedLocationProviderClient mLocationProviderClient;
    /**
     * Current presenter state
     */
    @NonNull
    private State mState = State.CREATED;
    /**
     * Location listener gave by the map to send user location update
     */
    @Nullable
    private OnLocationChangedListener mLocationChangeListener;

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

        switch (mState)
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

    @Override
    public void onStop()
    {
        // Stop asking for user location when view moves in background
        try
        {
            if( mLocationProviderClient != null )
            {
                Log.d(TAG, "onStop removeLocationUpdates");
                mLocationProviderClient.removeLocationUpdates(mLocationCallback);
            }
        }
        catch (Exception e)
        {
            Log.e(TAG, "Error while removing location updates onStop", e);
        }

        mLocationProviderClient = null;

        super.onStop();
    }

    /**
     * Called when GPS are ready or when there are not needed (since no location needed). This
     * method takes care of requesting the permission if needed or loading the map.
     */
    private void askForLocationIfNeededOrDisplayMap()
    {
        if (mNeedGeoloc)
        {
            mState = State.WAITING_FOR_LOCATION_PERMISSION;
            if( mView != null )
            {
                mView.requestLocationPermission();
            }
        }
        else
        {
            mState = State.WAITING_FOR_MAP;
            loadMapAndLocationProvider();
        }

        Log.d(TAG, "askForLocationIfNeededOrDisplayMap: "+ mState);
    }

    /**
     * Load the map and optionally retrieve the {@link FusedLocationProviderClient} from the view if
     * {@link #mNeedGeoloc} is true
     */
    private void loadMapAndLocationProvider() {
        if( mView != null )
        {
            if( mNeedGeoloc && !mGeolocPermissionDenied ) {
                mLocationProviderClient = mView.getFusedLocationProviderClient();
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
    public void onMapReady()
    {
        if (mState == State.MAP_READY)
        {
            return;
        }

        mState = State.MAP_READY;

        populateMap();
    }

    /**
     * Populate the map with the location source if needed and calls {@link #onMapAvailable()}
     */
    private void populateMap()
    {
        mState = State.MAP_AVAILABLE;

        if (mNeedGeoloc && !mGeolocPermissionDenied && mLocationProviderClient != null)
        {
            if (mView != null) {
                mView.setLocation(this);
            }
        }

        onMapAvailable();
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener)
    {
        mLocationChangeListener = onLocationChangedListener;

        try
        {
            if( mLocationProviderClient != null ) {
                mLocationProviderClient.requestLocationUpdates(getLocationRequest(), mLocationCallback, null);
            }
        }
        catch (Exception e)
        {
            Log.e(TAG, "Exception while activating location updates", e);
        }
    }

    @Override
    public void deactivate()
    {
        try
        {
            if( mLocationProviderClient != null ) {
                mLocationProviderClient.removeLocationUpdates(mLocationCallback);
            }
        }
        catch (Exception e)
        {
            Log.e(TAG, "Error while removing location updates on map deactivate", e);
        }

        mLocationChangeListener = null;
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
