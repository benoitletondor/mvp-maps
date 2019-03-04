package com.benoitletondor.mvp.maps.sample.scene.main.impl;

import android.location.Location;
import android.support.annotation.NonNull;

import com.benoitletondor.mvp.maps.presenter.impl.BaseMapPresenterImpl;
import com.benoitletondor.mvp.maps.sample.scene.main.MainPresenter;
import com.benoitletondor.mvp.maps.sample.scene.main.MainView;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;

/**
 * Implementation of the {@link MainPresenter}
 *
 * @author Benoit LETONDOR
 */
public class MainPresenterImpl extends BaseMapPresenterImpl<MainView> implements MainPresenter
{
    /**
     * Has the map been zoomed to user location yet ?
     */
    private boolean mMapZoomedOnUserPosition = false;

// --------------------------------------->

    public MainPresenterImpl()
    {
        super(true);
    }

    @Override
    public void onStartFragmentActivityButtonClicked()
    {
        if( mView != null )
        {
            mView.startFragmentActivity();
        }
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
        final MainView view = mView;

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
