package com.knoxpo.criminalintent.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.knoxpo.criminalintent.R;
import com.knoxpo.criminalintent.fragments.DetailFragment;
import com.knoxpo.criminalintent.fragments.MainFragment;
import com.knoxpo.criminalintent.models.Crime;

import java.util.UUID;

/**
 * Created by knoxpo on 24/11/16.
 */

public class MainActivity extends MasterDetailActivity implements MainFragment.Callback, DetailFragment.Callback {
    private Crime mCrime;

    @Override
    public Fragment getContentFragment() {
        return new MainFragment();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCrimeItemClick(Crime crime) {
        mCrime = crime;
        View view = findViewById(R.id.detail_container);

        if(view == null){
            Intent detailIntent = new Intent(this, DetailActivity.class);
            detailIntent.putExtra(DetailActivity.EXTRA_CRIME_ID, mCrime.getId());
           // detailIntent.putExtra(PageViewActivity.EXTRA_POSITION,getAdapterPosition());
            startActivity(detailIntent);
        }else{
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.detail_container, new DetailFragment().newInstance(crime.getId()))
                        .commit();
        }
    }

    @Override
    public void onNewCrimeRequest() {
        View view = findViewById(R.id.detail_container);

        if(view == null){
            Intent detailIntent = new Intent(this,DetailActivity.class);
            startActivity(detailIntent);
        }else{
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_container,new DetailFragment())
                    .commit();
        }
    }

    @Override
    public void onCrimeAdd() {
        MainFragment fragment = (MainFragment) getSupportFragmentManager()
                                    .findFragmentById(R.id.fragment_container);
        fragment.updateAdapter();
    }

    @Override
    public void onCrimeUpdate(UUID id) {
        MainFragment fragment = (MainFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);
        fragment.updateCrimeItem(id);
    }

    @Override
    public void onCrimeStatusChange(UUID id) {
        DetailFragment fragment = (DetailFragment) getSupportFragmentManager()
                .findFragmentById(R.id.detail_container);
        fragment.updateUI();
    }
}
