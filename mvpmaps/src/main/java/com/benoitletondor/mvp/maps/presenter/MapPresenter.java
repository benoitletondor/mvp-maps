package com.benoitletondor.mvp.maps.presenter;

import android.location.Location;
import android.support.annotation.NonNull;

import com.benoitletondor.mvp.core.presenter.Presenter;
import com.benoitletondor.mvp.maps.view.MapView;
import com.google.android.gms.location.LocationRequest;

/**
 * Interface for the map presenter that defines methods. You shouldn't directly implement but extend
 * {@link com.benoitletondor.mvp.maps.presenter.impl.BaseMapPresenterImpl}.
 *
 * @author Benoit LETONDOR
 */
public interface MapPresenter<V extends MapView> extends Presenter<V>
{
    /**
     * Called when the map is ready to be used
     */
    void onMapReady();

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
     * Called on a new location result from the location provider. There is no need to
     * override this method since {@link com.benoitletondor.mvp.maps.presenter.impl.BaseMapPresenterImpl} already does
     * to update the user location.
     *
     * @param location the new result location
     */
    void onLocationResult(@NonNull Location location);

    /**
     * Called when the user location change. The location is automatically updated on the map by
     * {@link #onLocationResult(Location)} but you can use this callback to do something else with the
     * user location.
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

    /**
     * Method used by the {@link MapView} to notify the location source was activated. There is no need to
     * override this method since {@link com.benoitletondor.mvp.maps.presenter.impl.BaseMapPresenterImpl} already does.
     */
    void onLocationSourceActivated();

    /**
     * Method used by the {@link MapView} to notify the location source was deactivated. There is no need to
     * override this method since {@link com.benoitletondor.mvp.maps.presenter.impl.BaseMapPresenterImpl} already does.
     */
    void onLocationSourceDeactivated();
}
