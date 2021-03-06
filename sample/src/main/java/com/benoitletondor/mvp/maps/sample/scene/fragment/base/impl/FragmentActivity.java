package com.benoitletondor.mvp.maps.sample.scene.fragment.base.impl;

import android.os.Bundle;
import android.view.MenuItem;

import com.benoitletondor.mvp.core.presenter.PresenterFactory;
import com.benoitletondor.mvp.maps.sample.R;
import com.benoitletondor.mvp.maps.sample.scene.fragment.base.FragmentPresenter;
import com.benoitletondor.mvp.maps.sample.scene.fragment.base.FragmentView;
import com.benoitletondor.mvp.maps.sample.scene.fragment.samplefragment.impl.SampleFragment;
import com.benoitletondor.mvp.core.view.impl.BaseMVPActivity;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

/**
 * Implementation of the {@link FragmentView}
 *
 * @author Benoit LETONDOR
 */
public final class FragmentActivity extends BaseMVPActivity<FragmentPresenter, FragmentView> implements FragmentView
{
    @Inject
    PresenterFactory<FragmentPresenter> mPresenterFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        AndroidInjection.inject(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // This is done directly into the activity cause it's not related to any app logic and thus shouldn't be put in presenter
        if( getSupportFragmentManager().findFragmentById(R.id.activity_fragment_container) == null )
        {
            getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.activity_fragment_container, new SampleFragment())
                .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected PresenterFactory<FragmentPresenter> getPresenterFactory()
    {
        return mPresenterFactory;
    }
}
