package com.knoxpo.criminalintent.activities;

import com.knoxpo.criminalintent.models.CrimeLab;

/**
 * Created by Tejas Sherdiwala on 11/25/2016.
 * &copy; Knoxpo
 */

public abstract class SaveCrimesActivity extends ToolbarActivity {

    @Override
    protected void onPause() {
       CrimeLab.getInstance(this).saveCrimes();
       super.onPause();
    }
}
