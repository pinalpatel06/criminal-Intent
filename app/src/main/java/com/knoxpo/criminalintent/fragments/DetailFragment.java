package com.knoxpo.criminalintent.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.knoxpo.criminalintent.R;
import com.knoxpo.criminalintent.models.Crime;
import com.knoxpo.criminalintent.models.CrimeLab;
import com.knoxpo.criminalintent.utils.PictureUtils;

import java.io.File;
import java.util.Date;
import java.util.UUID;

/**
 * Created by knoxpo on 24/11/16.
 */
public class DetailFragment extends Fragment
        implements TextWatcher, View.OnClickListener,
        CompoundButton.OnCheckedChangeListener {

    private static final String
            TAG = DetailFragment.class.getSimpleName(),
            ARGS_CRIME_ID = TAG + ".ARGS_CRIME_ID",
            DATE_DIALOG_TAG = TAG + ".DATE_DIALOG_TAG";

    private static final int
            REQUEST_SELECT_DATE = 0,
            REQUEST_PICK_CONTACT = 1,
            REQUEST_PHOTO=2;

    public static DetailFragment newInstance(UUID crimeId) {
        Bundle args = new Bundle();
        args.putSerializable(ARGS_CRIME_ID, crimeId);
        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private Crime mCrime;
    private EditText mTitleET;
    private CheckBox mSolvedCB;
    private Button mDateBtn,mContactBtn,mSaveBtn;
    private ImageView mPersonImg;
    private File mPhotoFile;

    public interface Callback{
        public void onCrimeAdd();
        public void onCrimeUpdate(UUID id);
    }

    private Callback mCallback;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mCallback = (Callback) getActivity();
        }catch (Exception e){
            throw  new RuntimeException(getActivity().getClass() + "Does not implemented Callback Interface");
        }
    }

    @Override
    public void onDetach() {
        mCallback = null;
        super.onDetach();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_detail, container, false);
        init(v);

        mDateBtn.setOnClickListener(this);
        mContactBtn.setOnClickListener(this);
        mTitleET.addTextChangedListener(this);
        mSolvedCB.setOnCheckedChangeListener(this);
        mPersonImg.setOnClickListener(this);
        mSaveBtn.setOnClickListener(this);

        ViewTreeObserver vto = mPersonImg.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
                    mPersonImg.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }else{
                    mPersonImg.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
                //updatePhotoView(mPersonImg.getWidth(),mPersonImg.getHeight());
                Log.d(TAG, "Image Size VTO: "+ mPersonImg.getWidth() + ","+mPersonImg.getHeight());
            }
        });

        Log.d(TAG, "Image Size: "+ mPersonImg.getWidth() + ","+mPersonImg.getHeight());

        updateUI();

        return v;
    }

    private void init(View v) {
        Bundle args = getArguments();
        if (args != null && args.getSerializable(ARGS_CRIME_ID) != null) {
            mCrime =
                    CrimeLab.getInstance(getActivity())
                            .getCrime((UUID) args.getSerializable(ARGS_CRIME_ID));
        } else {
            mCrime = new Crime();
        }
        mTitleET = (EditText) v.findViewById(R.id.et_title);
        mDateBtn = (Button) v.findViewById(R.id.btn_choose_date);
        mSolvedCB = (CheckBox) v.findViewById(R.id.cb_solved);
        mContactBtn = (Button) v.findViewById(R.id.btn_choose_criminal);
        mPersonImg = (ImageView) v.findViewById(R.id.img_person);
        mPhotoFile = CrimeLab.getInstance(getActivity()).getPhotoFile(mCrime);
        mSaveBtn = (Button) v.findViewById(R.id.btn_new_crime);
    }

    public void updateUI(){
        mSolvedCB.setChecked(mCrime.isSolved());
        mTitleET.setText(mCrime.getTitle());

        Date date = mCrime.getDate();
        if(date!=null){
            mDateBtn.setText(date.toString());
        }
        if(mCrime.getPersonInvolved()==null){
            mContactBtn.setText("Choose Criminal");
        }else {
            mContactBtn.setText(mCrime.getPersonInvolved());
        }
        updatePhotoView();
    }

    public Crime getCrime() {
        return mCrime;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        mCrime.setTitle(charSequence.toString());
        mCallback.onCrimeUpdate(mCrime.getId());

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onClick(View view) {
        int itemId = view.getId();
        switch (itemId){
            case R.id.btn_choose_date:
                DatePickerFragment fragment = DatePickerFragment.newInstance(mCrime.getDate());
                fragment.setTargetFragment(this, REQUEST_SELECT_DATE);
                fragment.show(getFragmentManager(), DATE_DIALOG_TAG);
                break;
            case R.id.btn_choose_criminal:
                Intent pickContectIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                pickContectIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(pickContectIntent,REQUEST_PICK_CONTACT);
                break;
            case R.id.img_person:
                requestPhotoFromCamara();
                break;
            case R.id.btn_new_crime:
                 saveCrime();
                 mCallback.onCrimeAdd();
                 break;
        }
    }

    private void saveCrime(){
        CrimeLab.getInstance(getActivity()).addCrime(mCrime);
    }
    private void requestPhotoFromCamara(){
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        PackageManager packageManager = getActivity().getPackageManager();
        boolean canTakePhoto = mPhotoFile!= null && captureImage.resolveActivity(packageManager) != null;

        if(canTakePhoto){
            Uri uri = Uri.fromFile(mPhotoFile);
            captureImage.putExtra(MediaStore.EXTRA_OUTPUT,uri);
            startActivityForResult(captureImage,REQUEST_PHOTO);
        }else{
            Toast.makeText(getActivity(), R.string.resqest_denied, Toast.LENGTH_LONG)
                    .show();
        }
    }

    private void updatePhotoView(){
        if(mPhotoFile == null || !mPhotoFile.exists()){
            mPersonImg.setImageDrawable(null);
        }else{
            Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(),getActivity());
            mPersonImg.setImageBitmap(bitmap);
        }
    }
    private void updatePhotoView(int width, int height){
        if(mPhotoFile == null || !mPhotoFile.exists()){
            mPersonImg.setImageDrawable(null);
        }else{
            Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(),width,height);
            mPersonImg.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

        mCrime.setSolved(b);
        mCallback.onCrimeUpdate(mCrime.getId());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SELECT_DATE && resultCode == Activity.RESULT_OK && data != null) {
            Date date = (Date) data.getSerializableExtra(Intent.EXTRA_RETURN_RESULT);
            mCrime.setDate(date);
            updateUI();
            mCallback.onCrimeUpdate(mCrime.getId());
        }else if(requestCode == REQUEST_PICK_CONTACT && resultCode == Activity.RESULT_OK && data != null ) {
            Uri contactData = data.getData();
            String projection[] = {ContactsContract.CommonDataKinds.Phone._ID, ContactsContract.Contacts.DISPLAY_NAME};
            Cursor cursor = getActivity().getContentResolver().query(contactData, projection, null, null, null);

            if (cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME);
                mCrime.setPersonInvolved(cursor.getString(index));
                updateUI();
                mCallback.onCrimeUpdate(mCrime.getId());
            }
        }else if(requestCode == REQUEST_PHOTO && resultCode == Activity.RESULT_OK){
            //updatePhotoView(mPersonImg.getWidth(),mPersonImg.getHeight());
            updatePhotoView();
        }
    }
}
