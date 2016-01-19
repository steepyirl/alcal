package com.qualimony.alcal;

import android.accounts.AccountManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Space;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.fasterxml.jackson.core.JsonFactory;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import com.qualimony.ka.KaCalendar;
import com.qualimony.ka.KaDateFormat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    GoogleAccountCredential mCredential;
    private TextView mOutputText;
    ProgressDialog mProgress;
    private Button currentDate;
    private SelectedMonth selectedMonth;
    private Button previousMonth;
    private Button nextMonth;
    private MonthView monthView;

    private class MonthAdvanceListener implements View.OnClickListener {
        public void onClick(View view) {
            if(view == previousMonth)
                selectedMonth.advanceMonth(-1);
            else if(view == nextMonth)
                selectedMonth.advanceMonth(1);
            monthView.setDate(selectedMonth.getTime(), new MakeRequestTask(mCredential));
        }
    }

    private class ResetDateListener implements View.OnClickListener {
        public void onClick(View view) {
            selectedMonth.resetDate();
            monthView.setDate(selectedMonth.getTime(), new MakeRequestTask(mCredential));
        }
    }

    private class DaySelectListener implements View.OnClickListener {
        public void onClick(View view) {
            DateButton button = (DateButton)view;
            monthView.selectButton(button);
            String text = "";
            for(Event event : button.getEvents()) {
                text+= event.toString() + "\n";
            }
            mOutputText.setText(text);
        }
    }

    private ResetDateListener resetDateListener;
    private MonthAdvanceListener advanceMonthListener;
    private static final String[] SCOPES = { CalendarScopes.CALENDAR_READONLY };
    private static final String PREF_ACCOUNT_NAME = "accountName";
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;


    /**
     * An asynchronous task that handles the Google Calendar API call.
     * Placing the API calls in their own task ensures the UI stays responsive.
     */
    private class MakeRequestTask extends AsyncTask<Void, Void, List<Event>> implements EventGetter {
        private com.google.api.services.calendar.Calendar mService = null;
        private Exception mLastError = null;
        private long startTime;
        private long endTime;
        private EventTarget target;

        public MakeRequestTask(GoogleAccountCredential credential) {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            com.google.api.client.json.JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new com.google.api.services.calendar.Calendar.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName("Google Calendar API Android Quickstart")
                    .build();
        }

        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }

        public void setEndTime(long endTime) {
            this.endTime = endTime;
        }

        /**
         * Background task to call Google Calendar API.
         * @param params no parameters needed for this task.
         */
        @Override
        protected List<Event> doInBackground(Void... params) {
            try {
                return getDataFromApi();
            } catch (Exception e) {
                mLastError = e;
                cancel(true);
                return null;
            }
        }

        /**
         * Fetch a list of the next 10 events from the primary calendar.
         * @return List of returned events.
         * @throws java.io.IOException
         */
        private List<Event> getDataFromApi() throws IOException {
            // List the next 10 events from the primary calendar.
            DateTime now = new DateTime(System.currentTimeMillis());
            List<Event> eventList = new ArrayList<Event>();
            /*
            Events events = mService.events().list("primary")
                    .setMaxResults(10)
                    .setTimeMin(now)
                    .setOrderBy("startTime")
                    .setSingleEvents(true)
                    .execute();
            */

            Events events = mService.events().list("primary")
                    .setTimeMin(new DateTime(startTime))
                    .setTimeMax(new DateTime(endTime))
                    .setSingleEvents(true)
                    .execute();

            List<Event> items = events.getItems();

            for (Event event : items) {
                DateTime start = event.getStart().getDateTime();
                if (start == null) {
                    // All-day events don't have start times, so just use
                    // the start date.
                    start = event.getStart().getDate();
                }
                eventList.add(event);
            }
            return eventList;
        }


        @Override
        protected void onPreExecute() {
            mOutputText.setText("");
            mProgress.show();
        }

        @Override
        protected void onPostExecute(List<Event> output) {
            mProgress.hide();
            /*
            if (output == null || output.size() == 0) {
                mOutputText.setText("No results returned.");
            } else {
                output.add(0, "Data retrieved using the Google Calendar API:");
                mOutputText.setText(TextUtils.join("\n", output));
            }
            */
            if(target != null)
                target.setEvents(output);
        }

        @Override
        protected void onCancelled() {
            mProgress.hide();
            if (mLastError != null) {
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    showGooglePlayServicesAvailabilityErrorDialog(
                            ((GooglePlayServicesAvailabilityIOException) mLastError)
                                    .getConnectionStatusCode());
                } else if (mLastError instanceof UserRecoverableAuthIOException) {
                    startActivityForResult(
                            ((UserRecoverableAuthIOException) mLastError).getIntent(),
                            MainActivity.REQUEST_AUTHORIZATION);
                } else {
                    mOutputText.setText("The following error occurred:\n"
                            + mLastError.getMessage());
                }
            } else {
                mOutputText.setText("Request cancelled.");
            }
        }

        @Override
        public void startGetEvents(KaCalendar startTime, KaCalendar endTime, EventTarget target) {
            this.target = target;
            setStartTime(startTime.getTimeInMillis());
            setEndTime(endTime.getTimeInMillis());
            execute();
        }
    }


    public MainActivity() {
        super();
        resetDateListener = new ResetDateListener();
        advanceMonthListener = new MonthAdvanceListener();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/Quivira.ttf");

        Calendar cal = Calendar.getInstance();
        KaCalendar today = new KaCalendar();
        today.setTimeInMillis(cal.getTimeInMillis());

        KaDateFormat fullFormat = new KaDateFormat("G.y.MWE");

        // Initialize credentials and service object.
        SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
        mCredential = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff())
                .setSelectedAccountName(settings.getString(PREF_ACCOUNT_NAME, null));

        TableLayout layout = (TableLayout)findViewById(R.id.tableLayout);
        layout.setStretchAllColumns(true);
        layout.setShrinkAllColumns(true);

        selectedMonth = new SelectedMonth(this);
        selectedMonth.setTypeface(face);
        selectedMonth.setTime(today);
        selectedMonth.setTextSize(16);
        selectedMonth.setMaxLines(1);
        selectedMonth.setWidth(250);

        monthView = new MonthView(this, face, new DaySelectListener());


        //First row
        TableRow row = new TableRow(this);
        TableRow.LayoutParams rowParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1);

        currentDate = new Button(this);
        currentDate.setTypeface(face);
        currentDate.setText(fullFormat.format(today.getTimeInMillis()));
        currentDate.setMaxLines(1);
        currentDate.setWidth(300);
        currentDate.setTextSize(12);
        rowParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 0);
        rowParams.column = 0;
        rowParams.span = 8;
        rowParams.gravity = Gravity.CENTER;
        row.addView(currentDate, rowParams);
        currentDate.setOnClickListener(resetDateListener);

        monthView.renderLabels(row);

        layout.addView(row);

        //Second row
        row = new TableRow(this);

        rowParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 0);
        rowParams.column = 0;
        rowParams.span = 8;
        rowParams.gravity = Gravity.CENTER;
        row.addView(selectedMonth, rowParams);

        monthView.renderDateButtonRow(row, 0, 8);

        monthView.renderCaudalDateButtons(row, 0, 15);

        layout.addView(row);

        //Third row
        row = new TableRow(this);

        previousMonth = new Button(this);
        previousMonth.setTypeface(face);
        previousMonth.setText("\u2190");
        rowParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 0);
        rowParams.column = 0;
        rowParams.span = 4;
        rowParams.gravity = Gravity.CENTER;
        row.addView(previousMonth, rowParams);
        previousMonth.setOnClickListener(advanceMonthListener);

        nextMonth = new Button(this);
        nextMonth.setTypeface(face);
        nextMonth.setText("\u2192");
        rowParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 0);
        rowParams.column = 4;
        rowParams.span = 4;
        rowParams.gravity = Gravity.CENTER;
        row.addView(nextMonth, rowParams);
        nextMonth.setOnClickListener(advanceMonthListener);

        monthView.renderDateButtonRow(row, 1, 8);

        layout.addView(row);

        //fourth, fifth row
        for(int j = 0; j < 2; j++) {
            row = new TableRow(this);
            monthView.renderDateButtonRow(row, 2+j, 8);
            layout.addView(row);
        }

        for(int hour = 0; hour < 24; hour++) {
            row = new TableRow(this);
            rowParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 0);
            rowParams.column = 0;
            rowParams.span = 2;
            rowParams.gravity = Gravity.CENTER;

            TextView hourLabel = new TextView(this);
            hourLabel.setTypeface(face);
            hourLabel.setTextSize(20);
            hourLabel.setText(String.valueOf(hour));
            row.addView(hourLabel, rowParams);
            layout.addView(row);

            row = new TableRow(this);
            rowParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 0);
            rowParams.column = 0;
            rowParams.span = 2;
            rowParams.gravity = Gravity.CENTER;

            Space space = new Space(this);
            row.addView(space, rowParams);
            layout.addView(row);
        }

        //last row
        row = new TableRow(this);
        rowParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 0);
        rowParams.column = 0;
        rowParams.span = 17;
        rowParams.gravity = Gravity.CENTER;

        mProgress = new ProgressDialog(this);
        mProgress.setMessage("Calling Google Calendar API ...");

        mOutputText = new TextView(this);
        mOutputText.setLayoutParams(rowParams);
        mOutputText.setPadding(16, 16, 16, 16);
        mOutputText.setVerticalScrollBarEnabled(true);
        mOutputText.setMovementMethod(new ScrollingMovementMethod());
        row.addView(mOutputText, rowParams);
        layout.addView(row);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        monthView.setDate(today, new MakeRequestTask(mCredential));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Called whenever this activity is pushed to the foreground, such as after
     * a call to onCreate().
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (isGooglePlayServicesAvailable()) {
            refreshResults();
        } else {
            mOutputText.setText("Google Play Services required: " +
                    "after installing, close and relaunch this app.");
        }
    }

    /**
     * Called when an activity launched here (specifically, AccountPicker
     * and authorization) exits, giving you the requestCode you started it with,
     * the resultCode it returned, and any additional data from it.
     * @param requestCode code indicating which activity result is incoming.
     * @param resultCode code indicating the result of the incoming
     *     activity result.
     * @param data Intent (containing result data) returned by incoming
     *     activity result.
     */
    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != RESULT_OK) {
                    isGooglePlayServicesAvailable();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null &&
                        data.getExtras() != null) {
                    String accountName =
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        mCredential.setSelectedAccountName(accountName);
                        SharedPreferences settings =
                                getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(PREF_ACCOUNT_NAME, accountName);
                        editor.apply();
                    }
                } else if (resultCode == RESULT_CANCELED) {
                    mOutputText.setText("Account unspecified.");
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode != RESULT_OK) {
                    chooseAccount();
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Attempt to get a set of data from the Google Calendar API to display. If the
     * email address isn't known yet, then call chooseAccount() method so the
     * user can pick an account.
     */
    private void refreshResults() {
        if (mCredential.getSelectedAccountName() == null) {
            chooseAccount();
        } else {
            if (isDeviceOnline()) {
                new MakeRequestTask(mCredential).execute();
            } else {
                mOutputText.setText("No network connection available.");
            }
        }
    }

    /**
     * Starts an activity in Google Play Services so the user can pick an
     * account.
     */
    private void chooseAccount() {
        startActivityForResult(
                mCredential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
    }

    /**
     * Checks whether the device currently has a network connection.
     * @return true if the device has a network connection, false otherwise.
     */
    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    /**
     * Check that Google Play services APK is installed and up to date. Will
     * launch an error dialog for the user to update Google Play Services if
     * possible.
     * @return true if Google Play Services is available and up to
     *     date on this device; false otherwise.
     */
    private boolean isGooglePlayServicesAvailable() {
        final int connectionStatusCode =
                GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (GooglePlayServicesUtil.isUserRecoverableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
            return false;
        } else if (connectionStatusCode != ConnectionResult.SUCCESS ) {
            return false;
        }
        return true;
    }

    /**
     * Display an error dialog showing that Google Play Services is missing
     * or out of date.
     * @param connectionStatusCode code describing the presence (or lack of)
     *     Google Play Services on this device.
     */
    void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        Dialog dialog = GooglePlayServicesUtil.getErrorDialog(
                connectionStatusCode,
                MainActivity.this,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }





}
