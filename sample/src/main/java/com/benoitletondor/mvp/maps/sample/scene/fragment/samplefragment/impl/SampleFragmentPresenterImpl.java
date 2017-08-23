package com.benoitletondor.mvp.maps.sample.scene.fragment.samplefragment.impl;

import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.benoitletondor.mvp.maps.presenter.impl.BaseMapPresenterImpl;
import com.benoitletondor.mvp.maps.sample.scene.fragment.samplefragment.SampleFragmentPresenter;
import com.benoitletondor.mvp.maps.sample.scene.fragment.samplefragment.SampleFragmentView;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

/**
 * Implementation of the {@link SampleFragmentPresenter}
 *
 * @author Benoit LETONDOR
 */
public final class SampleFragmentPresenterImpl extends BaseMapPresenterImpl<SampleFragmentView> implements SampleFragmentPresenter
{
    /**
     * Reference of the current map shown (don't forget to null it when onStop is called !)
     */
    @Nullable
    private GoogleMap mMap;
    /**
     * Has the map been zoomed to user location yet ?
     */
    private boolean mMapZoomedOnUserPosition = false;

// --------------------------------------->

    public SampleFragmentPresenterImpl()
    {
        super(true);
    }

    @Override
    public void onStop()
    {
        // IMPORTANT: Don't forget to clear any instance of GoogleMap you have here to avoid leaks.
        // It will be recreated at next view start
        mMap = null;

        super.onStop();
    }

    @Override
    public void onMapAvailable(@NonNull GoogleMap map)
    {
        // You can store the map to perform actions on it later, like adding pins
        // IMPORTANT: Don't forget to clear it when onStop is called !
        mMap = map;
    }

    @Override
    public void onMapNotAvailable()
    {
        if( mView != null )
        {
            mView.showMapNotAvailableAlert();
        }
    }

    @NonNull
    @Override
    public LocationRequest getLocationRequest()
    {
        return LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(5000)
                .setFastestInterval(1000);
    }

    @Override
    public void onUserLocationChanged(@NonNull Location location)
    {
        // This code simply zoom the map camera to the user position the first time we have it
        if( !mMapZoomedOnUserPosition && mMap != null )
        {
            mMapZoomedOnUserPosition = true;

            final CameraUpdate update = CameraUpdateFactory.newLatLngZoom(
                    new LatLng(location.getLatitude(), location.getLongitude()), 11f);

            mMap.animateCamera(update);
        }
    }
}
