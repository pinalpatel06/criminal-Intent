package com.knoxpo.criminalintent.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.widget.Toast;

import com.knoxpo.criminalintent.R;
import com.knoxpo.criminalintent.fragments.DetailFragment;
import com.knoxpo.criminalintent.models.Crime;
import com.knoxpo.criminalintent.models.CrimeLab;

import java.util.UUID;

/**
 * Created by knoxpo on 24/11/16.
 */

public class DetailActivity extends ToolbarActivity {
    private static final String
            TAG = DetailActivity.class.getSimpleName();

    public static final String
            EXTRA_CRIME_ID = TAG + ".EXTRA_CRIME_ID",
            EXTRA_POSITION = TAG + ".EXTRA_POSITION";


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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public Fragment getContentFragment() {
        UUID crimeId = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);
        return DetailFragment.newInstance(crimeId);
    }


    @Override
    public void onBackPressed() {
        DetailFragment fragment
                = (DetailFragment) getSupportFragmentManager().findFragmentById(getContainerId());

        Crime crime = fragment.getCrime();

        boolean isCrimeEdit = CrimeLab.getInstance(this).getCrime(crime.getId()) !=null;
        boolean isTitleEmpty = crime.getTitle() == null || crime.getTitle().trim().isEmpty();

        if(isCrimeEdit && isTitleEmpty){
            /*
            existing crime an needs to be
             */
            Toast.makeText(this, R.string.no_title_error,Toast.LENGTH_LONG).show();
            return;
        }else if(isTitleEmpty){
            /*
            Title is empty and crime is not editing. Hence just dismiss the new crime being created.
             */
            setResult(RESULT_CANCELED);
        }else{

            if(!isCrimeEdit){
                CrimeLab.getInstance(this).addCrime(crime);
            }
            Intent intent = new Intent();
            intent.putExtras(getIntent());
            intent.putExtra(EXTRA_CRIME_ID,crime.getId());
            setResult(RESULT_OK,intent);
        }

        super.onBackPressed();
    }

}