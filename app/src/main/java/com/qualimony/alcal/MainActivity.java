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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/Quivira.ttf");
        /*((TextView)this.findViewById(R.id.selectedMonth)).setTypeface(face);
        ((TextView)this.findViewById(R.id.previous)).setTypeface(face);
        ((TextView)this.findViewById(R.id.next)).setTypeface(face);

        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 7; j++) {
                TextView tv = new TextView(this);
                tv.setTypeface(face);
                tv.setClickable(true);
            }
        }
        */

        Calendar cal = Calendar.getInstance();
        KaCalendar today = new KaCalendar();
        today.setTimeInMillis(cal.getTimeInMillis());

        KaDateFormat fullFormat = new KaDateFormat("G.y.MWE");

        TableLayout layout = (TableLayout)findViewById(R.id.tableLayout);
        layout.setStretchAllColumns(true);
        layout.setShrinkAllColumns(true);

        //First row
        TableRow row = new TableRow(this);
        TableRow.LayoutParams rowParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1);
        //row.setLayoutParams(rowParams);

        Button currentDate = new Button(this);
        currentDate.setTypeface(face);
        currentDate.setText(fullFormat.format(today.getTimeInMillis()));
        rowParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 0);
        rowParams.column = 0;
        rowParams.span = 8;
        rowParams.gravity = Gravity.CENTER;
        row.addView(currentDate, rowParams);


        TextView v = new TextView(this);
        v.setTypeface(face);
        v.setText("\u263d");
        rowParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 0);
        rowParams.column = 8;
        rowParams.span = 1;
        rowParams.gravity = Gravity.CENTER;
        row.addView(v, rowParams);

        v = new TextView(this);
        v.setTypeface(face);
        v.setText("\u2642");
        rowParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 0);
        rowParams.column = 9;
        rowParams.span = 1;
        rowParams.gravity = Gravity.CENTER;
        row.addView(v, rowParams);

        v = new TextView(this);
        v.setTypeface(face);
        v.setText("\u263f");
        rowParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 0);
        rowParams.column = 10;
        rowParams.span = 1;
        rowParams.gravity = Gravity.CENTER;
        row.addView(v, rowParams);

        v = new TextView(this);
        v.setTypeface(face);
        v.setText("\u2643");
        rowParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 0);
        rowParams.column = 11;
        rowParams.span = 1;
        rowParams.gravity = Gravity.CENTER;
        row.addView(v, rowParams);

        v = new TextView(this);
        v.setTypeface(face);
        v.setText("\u2640");
        rowParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 0);
        rowParams.column = 12;
        rowParams.span = 1;
        rowParams.gravity = Gravity.CENTER;
        row.addView(v, rowParams);

        v = new TextView(this);
        v.setTypeface(face);
        v.setText("\u2644");
        rowParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 0);
        rowParams.column = 13;
        rowParams.span = 1;
        rowParams.gravity = Gravity.CENTER;
        row.addView(v, rowParams);

        v = new TextView(this);
        v.setTypeface(face);
        v.setText("\u2609");
        rowParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 0);
        rowParams.column = 14;
        rowParams.span = 1;
        rowParams.gravity = Gravity.CENTER;
        row.addView(v, rowParams);

        layout.addView(row);

        //Second row
        row = new TableRow(this);
        rowParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 0);
        //row.setLayoutParams(rowParams);

        SelectedMonth selectedMonth = new SelectedMonth(this);
        selectedMonth.setTypeface(face);
        selectedMonth.setTime(today);
        selectedMonth.setTextSize(16);
        selectedMonth.setMaxLines(1);
        selectedMonth.setWidth(250);
        rowParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 0);
        rowParams.column = 0;
        rowParams.span = 8;
        rowParams.gravity = Gravity.CENTER;
        row.addView(selectedMonth, rowParams);

        addRowForWeek(row);

        layout.addView(row);

        //Third row
        row = new TableRow(this);
        rowParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 0);
        //row.setLayoutParams(rowParams);

        Button b = new AdvanceMonthButton(this, face, selectedMonth, -1);
        rowParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 0);
        rowParams.column = 0;
        rowParams.span = 4;
        rowParams.gravity = Gravity.CENTER;
        row.addView(b, rowParams);

        b = new AdvanceMonthButton(this, face, selectedMonth, 1);
        rowParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 0);
        rowParams.column = 4;
        rowParams.span = 4;
        rowParams.gravity = Gravity.CENTER;
        row.addView(b, rowParams);

        addRowForWeek(row);

        layout.addView(row);

        //fourth, fifth row
        for(int j = 0; j < 2; j++) {
            row = new TableRow(this);
            rowParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 0);
            //row.setLayoutParams(rowParams);
            addRowForWeek(row);
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

    private void addRowForWeek(TableRow row) {
        for(int i = 0; i < 7; i++) {
            Button v = new Button(this);
            v.setBackgroundColor(0xffff0000);
            v.setHighlightColor(0xff00ff00);
            TableRow.LayoutParams rowParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 0);
            rowParams.column = 8+i;
            //rowParams.span = 1;
            rowParams.gravity = Gravity.CENTER;
            rowParams.width = 50;
            rowParams.leftMargin = 1;
            rowParams.rightMargin = 1;
            rowParams.topMargin = 2;
            rowParams.bottomMargin = 2;
            row.addView(v, rowParams);
        }
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
