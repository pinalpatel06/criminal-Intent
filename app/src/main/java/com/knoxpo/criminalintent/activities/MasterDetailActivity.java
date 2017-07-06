package com.knoxpo.criminalintent.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.knoxpo.criminalintent.R;

/**
 * Created by Tejas Sherdiwala on 11/29/2016.
 * &copy; Knoxpo
 */

public abstract class MasterDetailActivity extends ToolbarActivity {
    @Override
    protected int getContentViewId() {
        return R.layout.master_detail_layout;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
