package com.benoitletondor.mvp.maps.sample.scene.main.impl;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.benoitletondor.mvp.core.presenter.PresenterFactory;
import com.benoitletondor.mvp.maps.sample.R;
import com.benoitletondor.mvp.maps.sample.scene.fragment.base.impl.FragmentActivity;
import com.benoitletondor.mvp.maps.sample.scene.main.MainPresenter;
import com.benoitletondor.mvp.maps.sample.scene.main.MainView;
import com.benoitletondor.mvp.maps.view.impl.BaseMVPMapActivity;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

/**
 * Implementation of the {@link MainView}
 *
 * @author Benoit LETONDOR
 */
public final class MainActivity extends BaseMVPMapActivity<MainPresenter, MainView> implements MainView
{
    @Inject
    PresenterFactory<MainPresenter> mPresenterFactory;

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        AndroidInjection.inject(this);

        super.onCreate(savedInstanceState, R.id.activity_map_container);
        setContentView(R.layout.activity_main);

        findViewById(R.id.start_fragment_activity_button).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if( mPresenter != null )
                {
                    mPresenter.onStartFragmentActivityButtonClicked();
                }
            }
        });
    }

    @Override
    @NonNull
    protected PresenterFactory<MainPresenter> getPresenterFactory()
    {
        return mPresenterFactory;
    }

    @Override
    public GoogleMapOptions getGoogleMapOptions()
    {
        return new GoogleMapOptions().zOrderOnTop(true);
    }

// ------------------------------------>

    @Override
    public void onMapReady()
    {
        if( mPresenter != null )
        {
            mPresenter.onMapReady();
        }
    }

    @Override
    public void onMapUnavailable()
    {
        if( mPresenter != null )
        {
            mPresenter.onMapUnavailable();
        }
    }

    @Override
    public void startFragmentActivity()
    {
        final Intent intent = new Intent(this, FragmentActivity.class);
        startActivity(intent);
    }

    @Override
    public void showMapNotAvailableAlert()
    {
        new AlertDialog.Builder(this)
            .setTitle(R.string.maps_not_available_alert_title)
            .setMessage(R.string.maps_not_available_alert_message)
            .setPositiveButton(android.R.string.ok, null)
            .show();
    }

    @Override
    public void updateMapCamera(CameraUpdate cameraUpdate)
    {
        final GoogleMap map = getMap();
        if( map != null )
        {
            map.animateCamera(cameraUpdate);
        }
    }
}
