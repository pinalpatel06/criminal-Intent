package com.knoxpo.criminalintent.models;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.knoxpo.criminalintent.utils.CrimeSerializer;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by knoxpo on 24/11/16.
 */

public class CrimeLab {

    private static final String TAG = CrimeLab.class.getSimpleName();

    private static final String FILENAME = "crimesTest.json";

    private static CrimeLab sCrimeLab;
    private Context mContext;

    public static CrimeLab getInstance(Context context) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context.getApplicationContext());
        }
        return sCrimeLab;
    }

    private ArrayList<Crime> mCrimes;
    private Context mAppContext;
    private CrimeSerializer mSerializer;

    private CrimeLab(Context context) {
        mAppContext = context.getApplicationContext();
        mSerializer = new CrimeSerializer(mAppContext, FILENAME);
        mCrimes = mSerializer.loadCrimes();
        mContext = context;
        Log.d(TAG, "Crimes loaded from file");
    }

    public void addCrime(Crime crime) {
        mCrimes.add(crime);
    }

    public void deleteCrime(Crime crime) {
        mCrimes.remove(crime);
    }

    public Crime getCrime(UUID crimeId) {
        for (int i = 0; i < mCrimes.size(); i++) {
            Crime crime = mCrimes.get(i);
            if (crime.getId().equals(crimeId)) {
                return crime;
            }
        }
        return null;
    }

    public int countCrime(){
        return mCrimes.size();
    }
    public ArrayList<Crime> getCrimes() {
        return mCrimes;
    }

    public void saveCrimes() {
        Log.d(TAG, "Crime saved to file");
        //Log.d(TAG,String.valueOf(mSerializer.isExternalStorageWritable()));
        mSerializer.saveCrimes(mCrimes);
    }
    public File getPhotoFile(Crime crime){
        File externalFileDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        if(externalFileDir == null){
            return  null;
        }
        return new File(externalFileDir,crime.getPhotoFIleName());
    }
}
