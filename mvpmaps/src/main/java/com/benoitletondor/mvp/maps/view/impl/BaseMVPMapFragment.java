package com.benoitletondor.mvp.maps.view.impl;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.benoitletondor.mvp.core.view.impl.BaseMVPFragment;
import com.benoitletondor.mvp.maps.presenter.MapPresenter;
import com.benoitletondor.mvp.maps.view.MapView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

/**
 * A base fragment for all views including a map, wrapping all the runtime callbacks. Any fragment
 * displaying a map should extend this one.
 *
 * @author Benoit LETONDOR
 */
public abstract class BaseMVPMapFragment<P extends MapPresenter<V>, V extends MapView> extends BaseMVPFragment<P, V> implements MapView
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
    @NonNull
    public FusedLocationProviderClient getFusedLocationProviderClient()
    {
        return LocationServices.getFusedLocationProviderClient(getContext());
    }

    @Override
    public void loadMap()
    {
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
                public void onMapReady(GoogleMap googleMap)
                {
                    if( mPresenter != null )
                    {
                        mPresenter.onMapReady(googleMap);
                    }
                }
            });
        }
        else
        {
            Log.e(TAG, "Play Services not available");
            if( mPresenter != null )
            {
                mPresenter.onMapNotAvailable();
            }
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
}
