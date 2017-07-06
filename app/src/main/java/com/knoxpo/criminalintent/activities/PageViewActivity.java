package com.knoxpo.criminalintent.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.knoxpo.criminalintent.R;
import com.knoxpo.criminalintent.fragments.DetailFragment;
import com.knoxpo.criminalintent.models.Crime;
import com.knoxpo.criminalintent.models.CrimeLab;

import java.util.List;
import java.util.UUID;

/**
 * Created by Tejas Sherdiwala on 11/26/2016.
 * &copy; Knoxpo
 */

public class PageViewActivity extends AppCompatActivity {

    private static final String
            TAG = PageViewActivity.class.getSimpleName();
    public static final String
            EXTRA_CRIME_ID = TAG + ".EXTRA_CRIME_ID",
            EXTRA_POSITION = TAG + ".EXTRA_POSITION";
    private ViewPager mViewPager;
    private Toolbar mToolbar;
    private UUID crimeId;
    private int mPosition;
    private List<Crime> crimeList;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_main);
        getIntentData();
        init();
        if(mViewPager!=null){
            setUPViewPager();
        }
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                setTitle(crimeList.get(position).getTitle());
            }
        });
        setTitle(crimeList.get(0).getTitle());
        setSupportActionBar(mToolbar);
    }
    private void init(){
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        crimeList = CrimeLab.getInstance(this).getCrimes();
    }

    public void getIntentData() {
       crimeId = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);
        mPosition = getIntent().getIntExtra(EXTRA_POSITION,-1);
    }

    private void setUPViewPager(){
            Adapter adapter = new Adapter(getSupportFragmentManager());
            mViewPager.setAdapter(adapter);
            if(mPosition != -1)
            mViewPager.setCurrentItem(mPosition);
    }

    class Adapter extends FragmentStatePagerAdapter{


        public Adapter(FragmentManager fragmentManager){
            super(fragmentManager);
        }

        @Override
        public int getCount() {
            return crimeList.size();
        }

        @Override
        public Fragment getItem(int position) {
            return new DetailFragment().newInstance(crimeList.get(position).getId());
        }
    }
}
