package com.srinivas.ele.activity;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.CallLog;
import android.support.v7.app.AppCompatActivity;

import com.srinivas.ele.R;

import java.util.Date;

/**
 * Created by Mudavath Srinivas on 13-04-2016.
 */
public class SplashScreenActivity extends AppCompatActivity {

    private static final int SPLASH_WAIT_DELAY = 1000;
    private static final int URL_LOADER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spashscreen);

      /*  new CallLogTask(this).execute();
        getSMSDetails();*/

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent loginIntent = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(loginIntent);
                finish();
            }
        }, SPLASH_WAIT_DELAY);

    }


    private class CallLogTask extends AsyncTask<String, String, String> implements LoaderManager.LoaderCallbacks<Cursor> {

        private Context context;

        private CallLogTask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {
            getLoaderManager().initLoader(URL_LOADER, null, this);
            return null;
        }

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            switch (id) {
                case URL_LOADER:
                    return new CursorLoader(
                            context,   // Parent activity context
                            CallLog.Calls.CONTENT_URI,        // Table to query
                            null,     // Projection to return
                            null,            // No selection clause
                            null,            // No selection arguments
                            null             // Default sort order
                    );
                default:
                    return null;
            }
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor managedCursor) {

            int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
            int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
            int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
            int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);


            while (managedCursor.moveToNext()) {
                String phNumber = managedCursor.getString(number);
                String callType = managedCursor.getString(type);
                String callDate = managedCursor.getString(date);
                Date callDayTime = new Date(Long.valueOf(callDate));
                String callDuration = managedCursor.getString(duration);
                String dir = null;

                int callTypeCode = Integer.parseInt(callType);
                switch (callTypeCode) {
                    case CallLog.Calls.OUTGOING_TYPE:
                        dir = "Outgoing";
                        break;

                    case CallLog.Calls.INCOMING_TYPE:
                        dir = "Incoming";
                        break;

                    case CallLog.Calls.MISSED_TYPE:
                        dir = "Missed";
                        break;
                }
            }

            managedCursor.close();
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    }


    private void getSMSDetails() {

        Uri uri = Uri.parse("content://sms");
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);

        if (cursor.moveToFirst()) {
            for (int i = 0; i < cursor.getCount(); i++) {
                String body = cursor.getString(cursor.getColumnIndexOrThrow("body"))
                        .toString();
                String number = cursor.getString(cursor.getColumnIndexOrThrow("address"))
                        .toString();
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"))
                        .toString();
                Date smsDayTime = new Date(Long.valueOf(date));
                String type = cursor.getString(cursor.getColumnIndexOrThrow("type"))
                        .toString();
                String typeOfSMS = null;
                switch (Integer.parseInt(type)) {
                    case 1:
                        typeOfSMS = "INBOX";
                        break;

                    case 2:
                        typeOfSMS = "SENT";
                        break;

                    case 3:
                        typeOfSMS = "DRAFT";
                        break;
                }

                cursor.moveToNext();
            }
        }
        cursor.close();
    }

}
