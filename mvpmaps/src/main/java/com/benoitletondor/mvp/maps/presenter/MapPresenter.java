package com.benoitletondor.mvp.maps.presenter;

import android.location.Location;
import android.support.annotation.NonNull;

import com.benoitletondor.mvp.core.presenter.Presenter;
import com.benoitletondor.mvp.maps.view.MapView;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

/**
 * Interface for the map presenter that defines methods. You shouldn't directly implement but extend
 * {@link com.benoitletondor.mvp.maps.presenter.impl.BaseMapPresenterImpl}.
 *
 * @author Benoit LETONDOR
 */
public interface MapPresenter<V extends MapView> extends Presenter<V>, OnMapReadyCallback
{
    /**
     * Called when the map is set-up and ready to be filled with data
     *
     * @param map the ready to be used map
     */
    void onMapAvailable(@NonNull GoogleMap map);

    /**
     * Called when the map is not available due to an error
     */
    void onMapNotAvailable();

    /**
     * You should provide your location request here
     *
     * @return the location request used by the map
     */
    @NonNull
    LocationRequest getLocationRequest();

    /**
     * Called when the user location change. The location is automatically updated on the map but
     * you can use this callback to do something else with the user location.
     *
     * @param location the new user location
     */
    void onUserLocationChanged(@NonNull Location location);

// ---------------------------------------->

    /**
     * Method used by the {@link MapView} to send the permission result. There is no need to
     * override this method since {@link com.benoitletondor.mvp.maps.presenter.impl.BaseMapPresenterImpl} already does.
     */
    void onLocationPermissionGranted();

    /**
     * Method used by the {@link MapView} to send the permission result. There is no need to
     * override this method since {@link com.benoitletondor.mvp.maps.presenter.impl.BaseMapPresenterImpl} already does.
     */
    void onLocationPermissionDenied();
}
