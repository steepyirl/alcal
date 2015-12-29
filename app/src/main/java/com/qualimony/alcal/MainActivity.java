package com.qualimony.alcal;

import android.app.ActionBar;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridLayout;
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
        KaDateFormat monthFormat = new KaDateFormat("G.y.M");
        KaDateFormat fullFormat = new KaDateFormat("G.y.MWE");

        TableLayout layout = (TableLayout)findViewById(R.id.tableLayout);
        layout.setStretchAllColumns(true);

        //First row
        TableRow row = new TableRow(this);
        TableRow.LayoutParams rowParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1);
        row.setLayoutParams(rowParams);
        //row.setPadding(0, 5, 0, 5);

        Button currentDate = new Button(this);
        currentDate.setTypeface(face);
        currentDate.setText(fullFormat.format(today.getTimeInMillis()));
        rowParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1);
        rowParams.column = 0;
        rowParams.span = 8;
        rowParams.gravity = Gravity.CENTER;
        row.addView(currentDate, rowParams);


        TextView v = new TextView(this);
        v.setTypeface(face);
        v.setText("\u263d");
        rowParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1);
        rowParams.column = 8;
        rowParams.span = 1;
        rowParams.gravity = Gravity.CENTER;
        row.addView(v, rowParams);

        v = new TextView(this);
        v.setTypeface(face);
        v.setText("\u2642");
        rowParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1);
        rowParams.column = 9;
        rowParams.span = 1;
        rowParams.gravity = Gravity.CENTER;
        row.addView(v, rowParams);

        v = new TextView(this);
        v.setTypeface(face);
        v.setText("\u263f");
        rowParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1);
        rowParams.column = 10;
        rowParams.span = 1;
        rowParams.gravity = Gravity.CENTER;
        row.addView(v, rowParams);

        v = new TextView(this);
        v.setTypeface(face);
        v.setText("\u2643");
        rowParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1);
        rowParams.column = 11;
        rowParams.span = 1;
        rowParams.gravity = Gravity.CENTER;
        row.addView(v, rowParams);

        v = new TextView(this);
        v.setTypeface(face);
        v.setText("\u2640");
        rowParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1);
        rowParams.column = 12;
        rowParams.span = 1;
        rowParams.gravity = Gravity.CENTER;
        row.addView(v, rowParams);

        v = new TextView(this);
        v.setTypeface(face);
        v.setText("\u2644");
        rowParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1);
        rowParams.column = 13;
        rowParams.span = 1;
        rowParams.gravity = Gravity.CENTER;
        row.addView(v, rowParams);

        v = new TextView(this);
        v.setTypeface(face);
        v.setText("\u2609");
        rowParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1);
        rowParams.column = 14;
        rowParams.span = 1;
        rowParams.gravity = Gravity.CENTER;
        row.addView(v, rowParams);

        layout.addView(row);

        //Second row
        row = new TableRow(this);
        rowParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1);
        row.setLayoutParams(rowParams);
        //row.setPadding(0, 5, 0, 5);

        TextView selectedMonth = new TextView(this);
        selectedMonth.setTypeface(face);
        selectedMonth.setText(monthFormat.format(today.getTimeInMillis()));
        rowParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1);
        rowParams.column = 0;
        rowParams.span = 8;
        rowParams.gravity = Gravity.CENTER;
        row.addView(selectedMonth, rowParams);

        addRowForWeek(row);

        layout.addView(row);

        //Third row
        row = new TableRow(this);
        rowParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1);
        row.setLayoutParams(rowParams);
        //row.setPadding(0, 5, 0, 5);

        v = new Button(this);
        v.setTypeface(face);
        v.setText("<");
        rowParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1);
        rowParams.column = 0;
        rowParams.span = 4;
        rowParams.gravity = Gravity.CENTER;
        row.addView(v, rowParams);

        v = new Button(this);
        v.setTypeface(face);
        v.setText(">");
        rowParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1);
        rowParams.column = 4;
        rowParams.span = 4;
        rowParams.gravity = Gravity.CENTER;
        row.addView(v, rowParams);

        addRowForWeek(row);

        layout.addView(row);

        //fourth, fifth row
        for(int j = 0; j < 2; j++) {
            row = new TableRow(this);
            rowParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1);
            row.setLayoutParams(rowParams);
            //row.setPadding(0, 5, 0, 5);
            addRowForWeek(row);
            layout.addView(row);
        }



        /*
        GridLayout layout = (GridLayout)findViewById(R.id.gridLayout);

        TextView selectedMonth = new TextView(this);
        selectedMonth.setTypeface(face);
        selectedMonth.setText("364.28.\u2650");
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.rowSpec = GridLayout.spec(0, 1, GridLayout.CENTER);
        params.columnSpec = GridLayout.spec(0, 8, GridLayout.CENTER);
        layout.addView(selectedMonth, params);

        TextView previous = new TextView(this);
        previous.setTypeface(face);
        previous.setText("<");
        params = new GridLayout.LayoutParams();
        params.rowSpec = GridLayout.spec(1, 1, GridLayout.CENTER);
        params.columnSpec = GridLayout.spec(0, 4, GridLayout.CENTER);
        layout.addView(previous, params);

        TextView next = new TextView(this);
        next.setTypeface(face);
        next.setText(">");
        params = new GridLayout.LayoutParams();
        params.rowSpec = GridLayout.spec(1, 1, GridLayout.CENTER);
        params.columnSpec = GridLayout.spec(4, 4, GridLayout.CENTER);
        layout.addView(next, params);

        TextView mo = new TextView(this);
        mo.setTypeface(face);
        mo.setText("\u263d");
        params = new GridLayout.LayoutParams();
        params.rowSpec = GridLayout.spec(0, 1, GridLayout.CENTER);
        params.columnSpec = GridLayout.spec(9, 1, GridLayout.CENTER);
        layout.addView(mo, params);

        TextView tu = new TextView(this);
        tu.setTypeface(face);
        tu.setText("\u2642");
        params = new GridLayout.LayoutParams();
        params.rowSpec = GridLayout.spec(0, 1, GridLayout.CENTER);
        params.columnSpec = GridLayout.spec(10, 1, GridLayout.CENTER);
        layout.addView(tu, params);

        TextView we = new TextView(this);
        we.setTypeface(face);
        we.setText("\u263f");
        params = new GridLayout.LayoutParams();
        params.rowSpec = GridLayout.spec(0, 1, GridLayout.CENTER);
        params.columnSpec = GridLayout.spec(11, 1, GridLayout.CENTER);
        layout.addView(we, params);

        TextView th = new TextView(this);
        th.setTypeface(face);
        th.setText("\u2643");
        params = new GridLayout.LayoutParams();
        params.rowSpec = GridLayout.spec(0, 1, GridLayout.CENTER);
        params.columnSpec = GridLayout.spec(12, 1, GridLayout.CENTER);
        layout.addView(th, params);

        TextView fr = new TextView(this);
        fr.setTypeface(face);
        fr.setText("\u2640");
        params = new GridLayout.LayoutParams();
        params.rowSpec = GridLayout.spec(0, 1, GridLayout.CENTER);
        params.columnSpec = GridLayout.spec(13, 1, GridLayout.CENTER);
        layout.addView(fr, params);

        TextView sa = new TextView(this);
        sa.setTypeface(face);
        sa.setText("\u2644");
        params = new GridLayout.LayoutParams();
        params.rowSpec = GridLayout.spec(0, 1, GridLayout.CENTER);
        params.columnSpec = GridLayout.spec(14, 1, GridLayout.CENTER);
        layout.addView(sa, params);

        TextView su = new TextView(this);
        su.setTypeface(face);
        su.setText("\u2609");
        params = new GridLayout.LayoutParams();
        params.rowSpec = GridLayout.spec(0, 1, GridLayout.CENTER);
        params.columnSpec = GridLayout.spec(15, 1, GridLayout.CENTER);
        layout.addView(su, params);
        */

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
            TableRow.LayoutParams rowParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1);
            rowParams.column = 8+i;
            rowParams.span = 1;
            rowParams.gravity = Gravity.CENTER;
            rowParams.width = 50;
            rowParams.leftMargin = 2;
            rowParams.rightMargin = 2;
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
