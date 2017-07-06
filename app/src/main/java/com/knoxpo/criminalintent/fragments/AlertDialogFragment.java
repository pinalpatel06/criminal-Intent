package com.knoxpo.criminalintent.fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.knoxpo.criminalintent.R;

/**
 * Created by Tejas Sherdiwala on 11/25/2016.
 * &copy; Knoxpo
 */

public class AlertDialogFragment extends DialogFragment {

    private static final String TAG = AlertDialogFragment.class.getSimpleName();
    public static final String ARGUMENTS_TITLE= TAG + ".TITLE";
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 0;
    public static AlertDialogFragment newInstance(String title) {

        Bundle args = new Bundle();
        args.putString(ARGUMENTS_TITLE,title);
        AlertDialogFragment fragment = new AlertDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString(ARGUMENTS_TITLE);
        return new AlertDialog.Builder(getActivity())
                .setIcon(R.drawable.ic_alert)
                .setTitle(title)
                .setMessage(R.string.alter_dialog_message)
                .setPositiveButton(R.string.alert_dialog_ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        REQUEST_WRITE_EXTERNAL_STORAGE);

                            }
                        }
                )
                .setNegativeButton(R.string.alert_dialog_cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Toast.makeText(getActivity(), R.string.resqest_denied, Toast.LENGTH_LONG)
                                        .show();
                            }
                        }
                )
                .create();
    }

}
