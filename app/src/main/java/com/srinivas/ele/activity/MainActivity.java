package com.srinivas.ele.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsMessage;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableRow;

import com.srinivas.ele.R;
import com.srinivas.ele.activity.base.BaseActivity;
import com.srinivas.ele.fragment.AppUsageStatisticsFragment;
import com.srinivas.ele.fragment.CallStatisticsFragment;
import com.srinivas.ele.fragment.NotificationStatsFragment;
import com.srinivas.ele.fragment.SMSStatisticsFragment;
import com.srinivas.ele.utils.Common;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final long DRAWER_CLOSE_DELAY_MS = 200;
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private View mHeaderView;
    private LinearLayout mHeader;
    private CoordinatorLayout mCoordinatorLayout;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private Handler mDrawerActionHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SystemClock.uptimeMillis();

        mToolbar = setupToolbar(true);
        if(mToolbar != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        setUpDrawerView();

        mNavigationView.getMenu().findItem(R.id.nav_app_usage).setChecked(true);
        displayView(R.id.nav_app_usage);

    }

    private void setUpDrawerView() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setMotionEventSplittingEnabled(true);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open_drawer, R.string.close_drawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                Common.hideKeyboard(MainActivity.this);
            }
        };
        //Setting the actionbarToggle to drawer layout
        mDrawerLayout.setDrawerListener(actionBarDrawerToggle);
        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        mNavigationView.setNavigationItemSelectedListener(this);

    }




    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        item.setChecked(true);
        mDrawerLayout.closeDrawer(GravityCompat.START);
        displayView(item.getItemId());
        return true;
    }

    private void displayView(int id) {
        Fragment fragment = null;
        String title=null;
        mDrawerLayout.closeDrawer(GravityCompat.START);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        switch (id){
            case R.id.nav_app_usage:
                fragment = new AppUsageStatisticsFragment();
                title=getString(R.string.string_app_usage);
                break;
            case R.id.nav_notifications:
                fragment = new NotificationStatsFragment();
                title=getString(R.string.string_notifications);
                break;
            case R.id.nav_call_history:
                fragment = new CallStatisticsFragment();
                title=getString(R.string.string_call_history);
                break;
            case R.id.nav_sms_history:
                fragment = new SMSStatisticsFragment();
                title=getString(R.string.string_sms_history);
                break;
            default:
                break;
        }

        if (fragment != null) {
            // set the toolbar title
            setToolbarTitle(title);
            fragmentTransaction.replace(R.id.container_body, fragment);
            mDrawerActionHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    fragmentTransaction.commit();
                }
            }, DRAWER_CLOSE_DELAY_MS);

        }
    }
}
