package com.knoxpo.criminalintent.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.knoxpo.criminalintent.R;
import com.knoxpo.criminalintent.models.Crime;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * Created by Tejas Sherdiwala on 11/25/2016.
 * &copy; Knoxpo
 */

public class CrimeSerializer {

    private static final String FOLDER_NAME = "CrimeFolder";

    private Context mContext;
    private String mFileName;
    private File mFolder,mFile;

    public CrimeSerializer(Context context,String filename){
        mContext = context;
        mFileName = filename;
        /*mFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                ,FOLDER_NAME);
        */
        mFolder = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),FOLDER_NAME);
        mFolder.mkdirs();
        mFile = new File(mFolder,mFileName);
    }

    public void saveCrimes(ArrayList<Crime> crimes){

        if(ContextCompat.checkSelfPermission(mContext,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            if (isExternalStorageWritable()) {
                JSONArray crimesArray = new JSONArray();
                if (crimes != null) {
                    for (int i = 0; i < crimes.size(); i++) {
                        crimesArray.put(crimes.get(i).toJSON());
                    }
                }
                try {
                    if (!mFile.exists()) {
                        mFile.createNewFile();
                    }
                    FileOutputStream fileOutputStream = new FileOutputStream(mFile);
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(fileOutputStream)
                    );
                    writer.write(crimesArray.toString());
                    writer.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(mContext,R.string.not_mounted, Toast.LENGTH_LONG)
                        .show();
            }
        }
    }

    public ArrayList<Crime> loadCrimes(){
        ArrayList<Crime> crimes = new ArrayList<>();
        if(ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            try {
                FileInputStream fileInputStream = new FileInputStream(mFile);
                BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream));
                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }

                String jsonString = builder.toString();
                JSONArray array = new JSONArray(jsonString);

                for (int i = 0; i < array.length(); i++) {
                    JSONObject crimeObject = array.getJSONObject(i);
                    crimes.add(
                            new Crime(crimeObject)
                    );
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return crimes;
    }

    public boolean isExternalStorageWritable(){
        String state = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state)||Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)){
            return true;
        }
        return false;
    }
}
