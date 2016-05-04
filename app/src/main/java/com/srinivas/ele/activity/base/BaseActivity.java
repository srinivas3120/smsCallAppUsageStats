package com.srinivas.ele.activity.base;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.srinivas.ele.R;


/**
 * Created by Mudavath Srinivas on 18-04-2016.
 */
public abstract class BaseActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    public Toolbar setupToolbar(boolean supportStatusBarTint) {
        try {
            mToolbar = (Toolbar) findViewById(R.id.toolbar);
            if (supportStatusBarTint) {
                if (Build.VERSION.SDK_INT >= 21) {
                    int statusBarHeight = getStatusBarHeight();
                    if (statusBarHeight > 0) {
                        ViewGroup.LayoutParams layoutParams = (ViewGroup.LayoutParams) mToolbar.getLayoutParams();
                        layoutParams.height += statusBarHeight;
                        mToolbar.setLayoutParams(layoutParams);
                        mToolbar.setPadding(0, statusBarHeight, 0, 0);
                    }
                }
            }
            setSupportActionBar(mToolbar);
            return mToolbar;
        } catch (NullPointerException exception) {}
        return null;
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0)
            result = getResources().getDimensionPixelSize(resourceId);
        return result;
    }

    public void setToolbarTitle(String title){
        getSupportActionBar().setTitle(title);
    }

}
