package com.qualimony.alcal;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.qualimony.ka.KaCalendar;
import com.qualimony.ka.KaDateFormat;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

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
            monthView.setDate(selectedMonth.getTime());
        }
    }

    private class ResetDateListener implements View.OnClickListener {
        public void onClick(View view) {
            selectedMonth.resetDate();
            monthView.setDate(selectedMonth.getTime());
        }
    }

    private ResetDateListener resetDateListener;
    private MonthAdvanceListener advanceMonthListener;

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

        TableLayout layout = (TableLayout)findViewById(R.id.tableLayout);
        layout.setStretchAllColumns(true);
        layout.setShrinkAllColumns(true);

        selectedMonth = new SelectedMonth(this);
        selectedMonth.setTypeface(face);
        selectedMonth.setTime(today);
        selectedMonth.setTextSize(16);
        selectedMonth.setMaxLines(1);
        selectedMonth.setWidth(250);

        monthView = new MonthView(this, face);


        //First row
        TableRow row = new TableRow(this);
        TableRow.LayoutParams rowParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1);

        currentDate = new Button(this);
        currentDate.setTypeface(face);
        currentDate.setText(fullFormat.format(today.getTimeInMillis()));
        currentDate.setMaxLines(1);
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

        monthView.setDate(today);

        //fourth, fifth row
        for(int j = 0; j < 2; j++) {
            row = new TableRow(this);
            monthView.renderDateButtonRow(row, 2+j, 8);
            layout.addView(row);
        }


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
}
