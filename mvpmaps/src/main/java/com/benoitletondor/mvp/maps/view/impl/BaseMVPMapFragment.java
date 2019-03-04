package com.benoitletondor.mvp.maps.view.impl;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import com.benoitletondor.mvp.core.view.impl.BaseMVPFragment;
import com.benoitletondor.mvp.maps.presenter.MapPresenter;
import com.benoitletondor.mvp.maps.view.MapView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

/**
 * A base fragment for all views including a map, wrapping all the runtime callbacks. Any fragment
 * displaying a map should extend this one.
 *
 * @author Benoit LETONDOR
 */
@SuppressLint("MissingPermission")
public abstract class BaseMVPMapFragment<P extends MapPresenter<V>, V extends MapView> extends BaseMVPFragment<P, V> implements MapView, LocationSource
{
    private final static String TAG = "BaseMVPMapFragment";

    private static final int REQUEST_CODE_LOCATION_PERMISSION = 10414;
    private static final int DEFAULT_TEMP_LOCATION_RESULT = -10;

    /**
     * Temp storage of the location permission result to avoid doing with the result outside of the lifecycle
     */
    private int mTempLocationResult = DEFAULT_TEMP_LOCATION_RESULT;
    /**
     * The res id of the container for the SupportMapFragment
     */
    @IdRes
    private final int mMapContainerId;

    /**
     * Play services client
     */
    @Nullable
    private FusedLocationProviderClient mLocationProviderClient;

    /**
     * Location listener gave by the map to send user location update.
     * Will be null if location is not requested.
     */
    @Nullable
    private LocationSource.OnLocationChangedListener mLocationChangeListener;

    /**
     * Map displayed by the activity. Loaded by {@link #loadMap()}. Children can use
     * {@link #getMap()} to access it.
     */
    private GoogleMap map;

    /**
     * Listener for location update. Will be null if location is not requested.
     */
    @Nullable
    private LocationCallback mLocationCallback;

// ------------------------------------------>

    /**
     * Creates a new fragment
     *
     * @param mapContainerId the res id of the container for the SupportMapFragment.
     */
    protected BaseMVPMapFragment(@IdRes int mapContainerId)
    {
        mMapContainerId = mapContainerId;
    }

    @Override
    public void onResume()
    {
        super.onResume();

        if( mTempLocationResult != DEFAULT_TEMP_LOCATION_RESULT && mPresenter != null )
        {
            if( mTempLocationResult == PackageManager.PERMISSION_GRANTED )
            {
                mPresenter.onLocationPermissionGranted();
            }
            else
            {
                mPresenter.onLocationPermissionDenied();
            }

            mTempLocationResult = DEFAULT_TEMP_LOCATION_RESULT;
        }
    }

    @Override
    public void onStop()
    {
        // Stop asking for user location when view moves in background
        try
        {
            if( mLocationProviderClient != null && mLocationCallback != null )
            {
                Log.d(TAG, "onStop removeLocationUpdates");
                mLocationProviderClient.removeLocationUpdates(mLocationCallback);
            }
        }
        catch ( Exception e )
        {
            Log.e(TAG, "Error while removing location updates onStop", e);
        }

        mLocationProviderClient = null;
        mLocationCallback = null;
        map = null;

        super.onStop();
    }

// ------------------------------------------>

    /**
     * Can be used by children to access the displayed map.
     *
     * @return Displayed GoogleMap instance, or null if the map was not initialized or is not available
     */
    @Nullable
    protected GoogleMap getMap()
    {
        return map;
    }

    @Override
    public void loadMap()
    {
        final View view = getActivity().findViewById(mMapContainerId);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(mMapContainerId);
        if( mapFragment == null )
        {
            mapFragment = SupportMapFragment.newInstance();
            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
            fragmentTransaction.replace(mMapContainerId, mapFragment);
            fragmentTransaction.commitAllowingStateLoss();
        }

        if( GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getActivity()) == ConnectionResult.SUCCESS )
        {
            mapFragment.getMapAsync(new OnMapReadyCallback()
            {
                @Override
                public void onMapReady(final GoogleMap googleMap)
                {
                    map = googleMap;
                    // If layout hasn't happen yet, just wait for it and then trigger onMapLoaded
                    // FIXME this is very leak prone, find a better way?
                    if( view.getWidth() == 0 && view.getHeight() == 0 )
                    {
                        view.addOnLayoutChangeListener(new View.OnLayoutChangeListener()
                        {
                            @Override
                            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7)
                            {
                                view.removeOnLayoutChangeListener(this);

                                if( mPresenter != null )
                                {
                                    mPresenter.onMapLoaded();
                                }
                            }
                        });
                    }
                    // If layout has been made, call onMapLoaded directly
                    else
                    {
                        if( mPresenter != null )
                        {
                            mPresenter.onMapLoaded();
                        }
                    }
                }
            });
        }
        else
        {
            Log.e(TAG, "Play Services not available");
            if( mPresenter != null )
            {
                mPresenter.onErrorLoadingMap();
            }
        }
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener)
    {
        if( mPresenter != null )
        {
            mPresenter.onLocationSourceActivated();
        }

        mLocationChangeListener = onLocationChangedListener;
    }

    @Override
    public void deactivate()
    {
        if( mPresenter != null )
        {
            mPresenter.onLocationSourceDeactivated();
        }

        mLocationChangeListener = null;
    }

    @Override
    public void requestLocationUpdates(LocationRequest locationRequest)
    {
        if( mLocationProviderClient != null && mLocationCallback != null )
        {
            mLocationProviderClient.requestLocationUpdates(locationRequest, mLocationCallback, null);
        }
    }

    @Override
    public void removeLocationUpdates()
    {
        if( mLocationProviderClient != null && mLocationCallback != null )
        {
            mLocationProviderClient.removeLocationUpdates(mLocationCallback);
        }
    }

    @Override
    public void enableUserLocation()
    {
        if( map != null )
        {
            mLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
            mLocationCallback = new LocationCallback()
            {
                @Override
                public void onLocationResult(LocationResult locationResult)
                {
                    super.onLocationResult(locationResult);

                    final Location location = locationResult.getLastLocation();

                    if( mPresenter != null )
                    {
                        mPresenter.onLocationResult(location);
                    }
                }

                @Override
                public void onLocationAvailability(LocationAvailability locationAvailability)
                {
                    super.onLocationAvailability(locationAvailability);
                }
            };

            map.setLocationSource(this);
            map.setMyLocationEnabled(true);
        }
    }

    @Override
    public void updateUserLocation(Location location)
    {
        if( mLocationChangeListener != null )
        {
            mLocationChangeListener.onLocationChanged(location);
        }
    }

    @Override
    public void requestLocationPermission()
    {
        if( ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED )
        {
            if( mPresenter != null )
            {
                mPresenter.onLocationPermissionGranted();
            }
        }
        else
        {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if( requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0 )
        {
            if( mPresenter != null )
            {
                if( grantResults[0] == PackageManager.PERMISSION_GRANTED )
                {
                    mPresenter.onLocationPermissionGranted();
                }
                else
                {
                    mPresenter.onLocationPermissionDenied();
                }
            }
            else
            {
                mTempLocationResult = grantResults[0];
            }
        }
    }
}
