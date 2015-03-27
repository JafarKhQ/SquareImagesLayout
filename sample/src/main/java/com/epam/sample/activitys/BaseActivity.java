package com.epam.sample.activitys;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import butterknife.ButterKnife;

public abstract class BaseActivity extends ActionBarActivity {
    protected final String TAG = ((Object) this).getClass().getSimpleName();

    protected ActionBar mActionBar;
    protected Context mContext = BaseActivity.this;
    protected BaseActivity mActivity = BaseActivity.this;

    protected abstract int getLayoutResId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());

        ButterKnife.inject(mActivity);

        try {
            mActionBar = getSupportActionBar();
        } catch (Exception e) {
        }
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

        getWindow().setFormat(PixelFormat.RGBA_8888);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mActivity.onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void setupActionBar(boolean showUp) {
        setupActionBar(showUp, null);
    }

    protected void setupActionBar(boolean showUp, int resId) {
        setupActionBar(showUp, getString(resId));
    }

    protected void setupActionBar(boolean showUp, String customTitle) {
        mActionBar.setDisplayShowHomeEnabled(true);
        mActionBar.setHomeButtonEnabled(showUp);
        mActionBar.setDisplayHomeAsUpEnabled(showUp);

        updateActionBarTitle(customTitle);
    }

    protected void updateActionBarTitle(int resId) {
        updateActionBarTitle(getString(resId));
    }

    protected void updateActionBarTitle(String customTitle) {
        if (null == customTitle) {
            mActionBar.setDisplayShowTitleEnabled(false);
        } else {
            mActionBar.setDisplayShowTitleEnabled(true);
            mActionBar.setTitle(customTitle);
        }
    }

    protected void updateActionBarTitle(String customTitle, String subTitle) {
        if (null == customTitle) {
            mActionBar.setDisplayShowTitleEnabled(false);
        } else {
            mActionBar.setDisplayShowTitleEnabled(true);
            mActionBar.setTitle(customTitle);
            mActionBar.setSubtitle(subTitle);
        }
    }
}
