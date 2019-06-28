package com.benoitletondor.mvp.maps.sample.scene.fragment.samplefragment.impl;

import android.location.Location;

import androidx.annotation.NonNull;

import com.benoitletondor.mvp.maps.presenter.impl.BaseMapPresenterImpl;
import com.benoitletondor.mvp.maps.sample.scene.fragment.samplefragment.SampleFragmentPresenter;
import com.benoitletondor.mvp.maps.sample.scene.fragment.samplefragment.SampleFragmentView;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;

/**
 * Implementation of the {@link SampleFragmentPresenter}
 *
 * @author Benoit LETONDOR
 */
public final class SampleFragmentPresenterImpl extends BaseMapPresenterImpl<SampleFragmentView> implements SampleFragmentPresenter
{
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
    public void onMapReady()
    {
        // Whatever you want to do once the map is available
    }

    @Override
    public void onMapUnavailable()
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
        final SampleFragmentView view = mView;

        // This code simply zoom the map camera to the user position the first time we have it
        if( !mMapZoomedOnUserPosition && view != null )
        {
            mMapZoomedOnUserPosition = true;

            final CameraUpdate update = CameraUpdateFactory.newLatLngZoom(
                new LatLng(location.getLatitude(), location.getLongitude()), 11f);

            view.updateMapCamera(update);
        }
    }
}
