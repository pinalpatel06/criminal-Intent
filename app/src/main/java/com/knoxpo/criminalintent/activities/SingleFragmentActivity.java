package com.knoxpo.criminalintent.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.knoxpo.criminalintent.R;

/**
 * Created by knoxpo on 24/11/16.
 */

public abstract class SingleFragmentActivity extends AppCompatActivity {

    public abstract Fragment getContentFragment();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(getContainerId());
        if(fragment==null){
            fm
                    .beginTransaction()
                    .replace(getContainerId(),getContentFragment())
                    .commit();
        }
    }


    protected int getContentViewId(){
        return R.layout.activity_single_fragment;
    }

    protected int getContainerId(){
        return R.id.fragment_container;
    }
}
