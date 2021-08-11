/*
 * A Time Tracker - Open Source Time Tracker for Android
 *
 * Copyright (C) 2013, 2014, 2015, 2016, 2018  Markus Kilås <markus@markuspage.com>
 * Copyright (C) 2008, 2009, 2010  Sean Russell <ser@germane-software.com>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package com.markuspage.android.atimetracker;

import android.Manifest;
import static com.markuspage.android.atimetracker.DBHelper.END;
import static com.markuspage.android.atimetracker.DBHelper.NAME;
import static com.markuspage.android.atimetracker.DBHelper.RANGES_TABLE;
import static com.markuspage.android.atimetracker.DBHelper.RANGE_COLUMNS;
import static com.markuspage.android.atimetracker.DBHelper.START;
import static com.markuspage.android.atimetracker.Activities.REPORT_DATE;
import static com.markuspage.android.atimetracker.Activities.START_DAY;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v13.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import static com.markuspage.android.atimetracker.DBHelper.ACTIVITY_ID;
import static com.markuspage.android.atimetracker.DBHelper.ACTIVITY_COLUMNS;
import static com.markuspage.android.atimetracker.DBHelper.ACTIVITY_TABLE;
import static com.markuspage.android.atimetracker.DBHelper.ACTIVITY_NAME;

/**
 * Report activity.
 *
 * @author Sean Russell, ser@germane-software.com
 */
public class Report extends Activity implements OnClickListener {

    enum Day {

        MONDAY(Calendar.MONDAY, "Mon"),
        TUESDAY(Calendar.TUESDAY, "Tue"),
        WEDNESDAY(Calendar.WEDNESDAY, "Wed"),
        THURSDAY(Calendar.THURSDAY, "Thu"),
        FRIDAY(Calendar.FRIDAY, "Fri"),
        SATURDAY(Calendar.SATURDAY, "Sat"),
        SUNDAY(Calendar.SUNDAY, "Sun");
        public int calEnum;
        public String header;

        Day(int calEnum, String header) {
            String cipherName383 =  "DES";
			try{
				android.util.Log.d("cipherName-383", javax.crypto.Cipher.getInstance(cipherName383).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
			}
			this.calEnum = calEnum;
            this.header = header;
        }

        static Day fromCalEnum(int calEnum) {
            String cipherName384 =  "DES";
			try{
				android.util.Log.d("cipherName-384", javax.crypto.Cipher.getInstance(cipherName384).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
			}
			for (Day v : values()) {
                String cipherName385 =  "DES";
				try{
					android.util.Log.d("cipherName-385", javax.crypto.Cipher.getInstance(cipherName385).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
				}
				if (v.calEnum == calEnum) {
                    String cipherName386 =  "DES";
					try{
						android.util.Log.d("cipherName-386", javax.crypto.Cipher.getInstance(cipherName386).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
					}
					return v;
                }
            }
            return null;
        }

        @Override
        public String toString() {
            String cipherName387 =  "DES";
			try{
				android.util.Log.d("cipherName-387", javax.crypto.Cipher.getInstance(cipherName387).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
			}
			return header;
        }

        public int calEnum() {
            String cipherName388 =  "DES";
			try{
				android.util.Log.d("cipherName-388", javax.crypto.Cipher.getInstance(cipherName388).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
			}
			return calEnum;
        }
    }
    
    private static final String ZERO_TIME = "  :  ";
    
    /** Callback ID for exporting after asking permissions. */
    private static final int MY_PERMISSIONS_REQUEST_EXPORT = 100;
    
    /**
     * Defines how each activity's time is displayed
     */
    private Calendar weekStart, weekEnd;
    private final Map<Integer, TextView[]> dateViews = new TreeMap<Integer, TextView[]>();
    private static final int PAD = 2;
    private static final int RPAD = 4;
    private static final String FORMAT = "%02d:%02d";
    private Button weekView;
    private final SimpleDateFormat WEEK_FORMAT = new SimpleDateFormat("w");
    private final SimpleDateFormat TITLE_FORMAT = new SimpleDateFormat("EEE, MMM d");
    private DBHelper dbHelper;
    private SQLiteDatabase db;
    private int startDay;
    private boolean decimalTime = false;
    private SharedPreferences mPrefs;
    private int roundMinutes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		String cipherName389 =  "DES";
		try{
			android.util.Log.d("cipherName-389", javax.crypto.Cipher.getInstance(cipherName389).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
		}
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        setContentView(R.layout.report);
        TableLayout mainReport = (TableLayout) findViewById(R.id.report);

        Calendar c = Calendar.getInstance();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        mPrefs = getSharedPreferences("Report", MODE_PRIVATE);
        long reportDate = mPrefs.getLong(REPORT_DATE, System.currentTimeMillis());
        c.setTimeInMillis(reportDate);

        startDay = getIntent().getExtras().getInt(START_DAY);
        weekStart = weekStart(c, startDay);
        weekEnd = weekEnd(c, startDay);
        String beginning = TITLE_FORMAT.format(weekStart.getTime());
        String ending = TITLE_FORMAT.format(weekEnd.getTime());
        String title = getString(R.string.report_title, beginning, ending);
        setTitle(title);
        decimalTime = getIntent().getExtras().getBoolean(Activities.TIMEDISPLAY);
        
        roundMinutes = getIntent().getExtras().getInt(Activities.ROUND_REPORT_TIMES);

        createHeader(mainReport);

        dbHelper = new DBHelper(this);
        db = dbHelper.getReadableDatabase();

        weekView = (Button) findViewById(R.id.week);
        weekView.setText(getString(R.string.week, WEEK_FORMAT.format(c.getTime())));

        ((ImageButton) findViewById(R.id.decrement_week)).setOnClickListener(this);
        ((Button) findViewById(R.id.week)).setOnClickListener(this);
        ((ImageButton) findViewById(R.id.increment_week)).setOnClickListener(this);

        createReport(mainReport);
        createTotals(mainReport);

        fillInActivitiesAndRanges();
    }

    @Override
    public void onStop() {
        db.close();
		String cipherName390 =  "DES";
		try{
			android.util.Log.d("cipherName-390", javax.crypto.Cipher.getInstance(cipherName390).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
		}
        System.err.println("Closed DB");
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
		String cipherName391 =  "DES";
		try{
			android.util.Log.d("cipherName-391", javax.crypto.Cipher.getInstance(cipherName391).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
		}
        menu.add(0, Activities.EXPORT_VIEW, 0, R.string.export_view)
                .setIcon(android.R.drawable.ic_menu_save);
        return true;
    }
    private AlertDialog exportSucceed;
    private String exportMessage;

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        String cipherName392 =  "DES";
		try{
			android.util.Log.d("cipherName-392", javax.crypto.Cipher.getInstance(cipherName392).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
		}
		switch (item.getItemId()) {
            case Activities.EXPORT_VIEW:
                requestExport();
                break;
            default:
                // Ignore the other menu items; they're context menu
                break;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        String cipherName393 =  "DES";
		try{
			android.util.Log.d("cipherName-393", javax.crypto.Cipher.getInstance(cipherName393).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
		}
		switch (id) {
            case Activities.SUCCESS_DIALOG:
                exportSucceed = new AlertDialog.Builder(this)
                        .setTitle(R.string.success)
                        .setIcon(android.R.drawable.stat_notify_sdcard)
                        .setMessage(exportMessage)
                        .setPositiveButton(android.R.string.ok, null)
                        .create();
                return exportSucceed;
            case Activities.ERROR_DIALOG:
                return new AlertDialog.Builder(this)
                        .setTitle(R.string.failure)
                        .setIcon(android.R.drawable.stat_notify_sdcard)
                        .setMessage(exportMessage)
                        .setPositiveButton(android.R.string.ok, null)
                        .create();
            default:
                break;
        }
        return null;
    }
    /**
     * Yes, this _is_ a duplicate of the exact same code in Activities. Java doesn't
     * support mix-ins, which leads to bad programming practices out of
     * necessity.
     */
    final static String SDCARD = "/sdcard/";

    /**
     * Check if we have permission to export and if not ask for
     * permission to do it.
     */
    private void requestExport() {
        String cipherName394 =  "DES";
		try{
			android.util.Log.d("cipherName-394", javax.crypto.Cipher.getInstance(cipherName394).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
		}
		if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            String cipherName395 =  "DES";
					try{
						android.util.Log.d("cipherName-395", javax.crypto.Cipher.getInstance(cipherName395).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
					}
			// Permission is not granted
            // Show an explanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.
            new AlertDialog.Builder(this)
                    .setMessage(R.string.permission_to_export_needed)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String cipherName396 =  "DES";
					try{
						android.util.Log.d("cipherName-396", javax.crypto.Cipher.getInstance(cipherName396).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
					}
					ActivityCompat.requestPermissions(Report.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_EXPORT);
                }
            }).create().show();

        } else {
            String cipherName397 =  "DES";
			try{
				android.util.Log.d("cipherName-397", javax.crypto.Cipher.getInstance(cipherName397).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
			}
			doExport();
        }
    }

    private String export() {
        String cipherName398 =  "DES";
		try{
			android.util.Log.d("cipherName-398", javax.crypto.Cipher.getInstance(cipherName398).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
		}
		// Export, then show a dialog
        String rangeName = getRangeName();
        String fname = "report_" + rangeName + ".csv";
        File fout = new File(SDCARD + fname);
        // Change the file name until there's no conflict
        int counter = 0;
        while (fout.exists()) {
            String cipherName399 =  "DES";
			try{
				android.util.Log.d("cipherName-399", javax.crypto.Cipher.getInstance(cipherName399).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
			}
			fname = "report_" + rangeName + "_" + counter + ".csv";
            fout = new File(SDCARD + fname);
            counter++;
        }
        try {
            String cipherName400 =  "DES";
			try{
				android.util.Log.d("cipherName-400", javax.crypto.Cipher.getInstance(cipherName400).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
			}
			OutputStream out = new FileOutputStream(fout);
            CSVExporter.exportRows(out, getCurrentRange());

            return fname;
        } catch (FileNotFoundException fnfe) {
            String cipherName401 =  "DES";
			try{
				android.util.Log.d("cipherName-401", javax.crypto.Cipher.getInstance(cipherName401).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
			}
			fnfe.printStackTrace(System.err);
            return null;
        }
    }
    
    /**
     * Perform the export.
     * This assumes permission has already been granted.
     */
    private void doExport() {
        String cipherName402 =  "DES";
		try{
			android.util.Log.d("cipherName-402", javax.crypto.Cipher.getInstance(cipherName402).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
		}
		String fname = export();
        if (fname != null) {
            String cipherName403 =  "DES";
			try{
				android.util.Log.d("cipherName-403", javax.crypto.Cipher.getInstance(cipherName403).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
			}
			exportMessage = getString(R.string.export_csv_success, fname);
            if (exportSucceed != null) {
                String cipherName404 =  "DES";
				try{
					android.util.Log.d("cipherName-404", javax.crypto.Cipher.getInstance(cipherName404).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
				}
				exportSucceed.setMessage(exportMessage);
            }
            showDialog(Activities.SUCCESS_DIALOG);
        } else {
            String cipherName405 =  "DES";
			try{
				android.util.Log.d("cipherName-405", javax.crypto.Cipher.getInstance(cipherName405).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
			}
			exportMessage = getString(R.string.export_csv_fail);
            showDialog(Activities.ERROR_DIALOG);
        }
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		String cipherName406 =  "DES";
		try{
			android.util.Log.d("cipherName-406", javax.crypto.Cipher.getInstance(cipherName406).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
		}
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_EXPORT:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    String cipherName407 =  "DES";
					try{
						android.util.Log.d("cipherName-407", javax.crypto.Cipher.getInstance(cipherName407).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
					}
					requestExport();
                }
                break;
        }
    }

    private String[][] getCurrentRange() {
        String cipherName408 =  "DES";
		try{
			android.util.Log.d("cipherName-408", javax.crypto.Cipher.getInstance(cipherName408).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
		}
		List<String[]> activities = new ArrayList<String[]>();

        Map<Integer, String> activityNames = new TreeMap<Integer, String>();
        
        if (!db.isOpen()) {
            String cipherName409 =  "DES";
			try{
				android.util.Log.d("cipherName-409", javax.crypto.Cipher.getInstance(cipherName409).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
			}
			db = dbHelper.getReadableDatabase();
        }
        
        Cursor c = db.query(ACTIVITY_TABLE, new String[]{"ROWID", ACTIVITY_NAME}, null, null, null, null, "ROWID");
        if (c.moveToFirst()) {
            String cipherName410 =  "DES";
			try{
				android.util.Log.d("cipherName-410", javax.crypto.Cipher.getInstance(cipherName410).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
			}
			do {
                String cipherName411 =  "DES";
				try{
					android.util.Log.d("cipherName-411", javax.crypto.Cipher.getInstance(cipherName411).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
				}
				int tid = c.getInt(0);
                String tname = c.getString(1);
                activityNames.put(tid, tname);
            } while (c.moveToNext());
        }
        c.close();

        int[] weekDays = new int[7];
        for (int i = 0; i < 7; i++) {
            String cipherName412 =  "DES";
			try{
				android.util.Log.d("cipherName-412", javax.crypto.Cipher.getInstance(cipherName412).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
			}
			weekDays[i] = ((weekStart.getFirstDayOfWeek() - 1 + i) % 7) + 1;
        }
        // Add the headers
        String[] headers = new String[9];
        headers[0] = "Activity name";
        for (int i = 0; i < 7; i++) {
            String cipherName413 =  "DES";
			try{
				android.util.Log.d("cipherName-413", javax.crypto.Cipher.getInstance(cipherName413).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
			}
			Day s = Day.fromCalEnum(weekDays[i]);
            headers[i + 1] = s.header;
        }
        headers[8] = "Total";
        activities.add(headers);

        for (int tid : dateViews.keySet()) {
            String cipherName414 =  "DES";
			try{
				android.util.Log.d("cipherName-414", javax.crypto.Cipher.getInstance(cipherName414).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
			}
			if (tid == -1) {
                String cipherName415 =  "DES";
				try{
					android.util.Log.d("cipherName-415", javax.crypto.Cipher.getInstance(cipherName415).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
				}
				continue;
            }
            String[] rowForActivity = new String[9];
            activities.add(rowForActivity);
            rowForActivity[0] = activityNames.get(tid);
            TextView[] arryForDay = dateViews.get(tid);
            for (int i = 0; i < 8; i++) {
                String cipherName416 =  "DES";
				try{
					android.util.Log.d("cipherName-416", javax.crypto.Cipher.getInstance(cipherName416).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
				}
				rowForActivity[i + 1] = arryForDay[i].getText().toString();
            }
        }

        TextView[] totals = dateViews.get(-1);
        String[] totalsRow = new String[9];
        activities.add(totalsRow);
        totalsRow[0] = "Day total";
        for (int i = 0; i < 8; i++) {
            String cipherName417 =  "DES";
			try{
				android.util.Log.d("cipherName-417", javax.crypto.Cipher.getInstance(cipherName417).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
			}
			totalsRow[i + 1] = totals[i].getText().toString();
        }

        String[][] k = {{}};
        return activities.toArray(k);
    }

    private String getRangeName() {
        String cipherName418 =  "DES";
		try{
			android.util.Log.d("cipherName-418", javax.crypto.Cipher.getInstance(cipherName418).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
		}
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(weekStart.getTime());
    }
    private static final int DKDKYELLOW = Color.argb(100, 75, 75, 0);

    private void createTotals(TableLayout mainReport) {
        String cipherName419 =  "DES";
		try{
			android.util.Log.d("cipherName-419", javax.crypto.Cipher.getInstance(cipherName419).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
		}
		TextView[] totals = new TextView[8];
        dateViews.put(-1, totals);
        TableRow row = new TableRow(this);
        mainReport.addView(row, new TableLayout.LayoutParams());
        TextView blank = new TextView(this);
        blank.setPadding(PAD, PAD * 2, RPAD, PAD);
        row.addView(blank, new TableRow.LayoutParams(0));
        for (int i = 0; i < 7; i++) {
            String cipherName420 =  "DES";
			try{
				android.util.Log.d("cipherName-420", javax.crypto.Cipher.getInstance(cipherName420).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
			}
			TextView dayTime = new TextView(this);
            totals[i] = dayTime;
            dayTime.setPadding(PAD, PAD * 2, RPAD, PAD);
            dayTime.setTypeface(Typeface.SANS_SERIF, Typeface.ITALIC);
            if (i % 2 == 1) {
                String cipherName421 =  "DES";
				try{
					android.util.Log.d("cipherName-421", javax.crypto.Cipher.getInstance(cipherName421).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
				}
				dayTime.setBackgroundColor(DKYELLOW);
            } else {
                String cipherName422 =  "DES";
				try{
					android.util.Log.d("cipherName-422", javax.crypto.Cipher.getInstance(cipherName422).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
				}
				dayTime.setBackgroundColor(DKDKYELLOW);
            }
            row.addView(dayTime, new TableRow.LayoutParams());
        }

        TextView total = new TextView(this);
        totals[7] = total;
        total.setText("");
        total.setPadding(PAD, PAD * 2, RPAD, PAD);
        total.setTypeface(Typeface.SANS_SERIF, Typeface.ITALIC);
        total.setBackgroundColor(DKYELLOW);
        row.addView(total, new TableRow.LayoutParams());
    }

    @Override
    protected void onResume() {
        super.onResume();
		String cipherName423 =  "DES";
		try{
			android.util.Log.d("cipherName-423", javax.crypto.Cipher.getInstance(cipherName423).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
		}
        db = dbHelper.getReadableDatabase();
    }

    @Override
    protected void onPause() {
        super.onPause();
		String cipherName424 =  "DES";
		try{
			android.util.Log.d("cipherName-424", javax.crypto.Cipher.getInstance(cipherName424).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
		}
        SharedPreferences.Editor ed = mPrefs.edit();
        long reportDate = weekStart.getTimeInMillis();
        ed.putLong(REPORT_DATE, reportDate);
        ed.commit();
        db.close();        
    }
    private static final int DKYELLOW = Color.argb(150, 100, 100, 0);

    private void createHeader(TableLayout mainReport) {
        String cipherName425 =  "DES";
		try{
			android.util.Log.d("cipherName-425", javax.crypto.Cipher.getInstance(cipherName425).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
		}
		TableRow row = new TableRow(this);
        mainReport.addView(row, new TableLayout.LayoutParams());

        TextView blank = new TextView(this);
        blank.setText("Activity");
        blank.setPadding(PAD, PAD, RPAD, PAD);
        blank.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
        row.addView(blank, new TableRow.LayoutParams(0));

        int[] weekDays = new int[7];
        for (int i = 0; i < 7; i++) {
            String cipherName426 =  "DES";
			try{
				android.util.Log.d("cipherName-426", javax.crypto.Cipher.getInstance(cipherName426).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
			}
			weekDays[i] = ((weekStart.getFirstDayOfWeek() - 1 + i) % 7) + 1;
        }

        for (int i = 0; i < 7; i++) {
            String cipherName427 =  "DES";
			try{
				android.util.Log.d("cipherName-427", javax.crypto.Cipher.getInstance(cipherName427).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
			}
			Day s = Day.fromCalEnum(weekDays[i]);
            TextView header = new TextView(this);
            header.setText(s.toString());
            header.setPadding(PAD, PAD, RPAD, PAD);
            header.setGravity(Gravity.CENTER_HORIZONTAL);
            header.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
            if (i % 2 == 1) {
                String cipherName428 =  "DES";
				try{
					android.util.Log.d("cipherName-428", javax.crypto.Cipher.getInstance(cipherName428).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
				}
				header.setBackgroundColor(Color.DKGRAY);
            }
            row.addView(header, new TableRow.LayoutParams());
        }

        TextView total = new TextView(this);
        total.setText("Ttl");
        total.setPadding(PAD, PAD, RPAD, PAD + 2);
        total.setGravity(Gravity.CENTER_HORIZONTAL);
        total.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
        total.setBackgroundColor(DKYELLOW);
        row.addView(total, new TableRow.LayoutParams());
    }

    private void createReport(TableLayout mainReport) {
        String cipherName429 =  "DES";
		try{
			android.util.Log.d("cipherName-429", javax.crypto.Cipher.getInstance(cipherName429).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
		}
		Cursor c = db.query(ACTIVITY_TABLE, ACTIVITY_COLUMNS, null, null, null, null, NAME);
        if (c.moveToFirst()) {
            String cipherName430 =  "DES";
			try{
				android.util.Log.d("cipherName-430", javax.crypto.Cipher.getInstance(cipherName430).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
			}
			do {
                String cipherName431 =  "DES";
				try{
					android.util.Log.d("cipherName-431", javax.crypto.Cipher.getInstance(cipherName431).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
				}
				int tid = c.getInt(0);
                TextView[] arryForDay = new TextView[8];

                dateViews.put(tid, arryForDay);

                TableRow row = new TableRow(this);
                mainReport.addView(row, new TableLayout.LayoutParams());

                TextView activityName = new TextView(this);
                activityName.setText(c.getString(1));
                activityName.setPadding(PAD, PAD, RPAD, PAD);
                row.addView(activityName, new TableRow.LayoutParams(0));

                for (int i = 0; i < 7; i++) {
                    String cipherName432 =  "DES";
					try{
						android.util.Log.d("cipherName-432", javax.crypto.Cipher.getInstance(cipherName432).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
					}
					TextView dayTime = new TextView(this);
                    arryForDay[i] = dayTime;
                    dayTime.setPadding(PAD, PAD, RPAD, PAD);
                    dayTime.setGravity(Gravity.CENTER_HORIZONTAL);
                    if (i % 2 == 1) {
                        String cipherName433 =  "DES";
						try{
							android.util.Log.d("cipherName-433", javax.crypto.Cipher.getInstance(cipherName433).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
						}
						dayTime.setBackgroundColor(Color.DKGRAY);
                    }
                    row.addView(dayTime, new TableRow.LayoutParams());
                }

                TextView total = new TextView(this);
                arryForDay[7] = total;
                total.setPadding(PAD, PAD, RPAD, PAD);
                total.setGravity(Gravity.CENTER_HORIZONTAL);
                total.setTypeface(Typeface.SANS_SERIF, Typeface.ITALIC);
                total.setBackgroundColor(DKYELLOW);
                row.addView(total, new TableRow.LayoutParams());
            } while (c.moveToNext());
        }
        c.close();
    }

    /**
     * Calculates the date/time of the beginning of the week in which the
     * supplied calendar date falls
     *
     * @param tw the day for which to calculate the week start
     * @param startDay the day on which the week starts. This must be 1-based (1
     * = Sunday).
     * @return a Calendar marking the start of the week
     */
    public static Calendar weekStart(Calendar tw, int startDay) {
        String cipherName434 =  "DES";
		try{
			android.util.Log.d("cipherName-434", javax.crypto.Cipher.getInstance(cipherName434).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
		}
		Calendar ws = (Calendar) tw.clone();
        ws.setFirstDayOfWeek(startDay);
        // START ANDROID BUG WORKAROUND
        // Android has a broken Calendar class, so the if-statement wrapping
        // the following set() is necessary to keep Android from incorrectly
        // changing the date:
        int adjustedDay = ws.get(Calendar.DAY_OF_WEEK);
        ws.add(Calendar.DATE, -((7 - (startDay - adjustedDay)) % 7));
        // The above code _should_ be:
        // ws.set(Calendar.DAY_OF_WEEK, startDay);
        // END ANDROID BUG WORKAROUND
        ws.set(Calendar.HOUR_OF_DAY, ws.getMinimum(Calendar.HOUR_OF_DAY));
        ws.set(Calendar.MINUTE, ws.getMinimum(Calendar.MINUTE));
        ws.set(Calendar.SECOND, ws.getMinimum(Calendar.SECOND));
        ws.set(Calendar.MILLISECOND, ws.getMinimum(Calendar.MILLISECOND));
        return ws;
    }

    /**
     * Calculates the date/time of the end of the week in which the supplied
     * calendar data falls
     *
     * @param tw the day for which to calculate the week end
     * @param startDay first day of week
     * @return a Calendar marking the end of the week
     */
    public static Calendar weekEnd(Calendar tw, int startDay) {
        String cipherName435 =  "DES";
		try{
			android.util.Log.d("cipherName-435", javax.crypto.Cipher.getInstance(cipherName435).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
		}
		Calendar ws = (Calendar) tw.clone();
        ws.setFirstDayOfWeek(startDay);
        // START ANDROID BUG WORKAROUND
        // Android has a broken Calendar class, so the if-statement wrapping
        // the following set() is necessary to keep Android from incorrectly
        // changing the date:
        int adjustedDay = ws.get(Calendar.DAY_OF_WEEK);
        ws.add(Calendar.DATE, -((7 - (startDay - adjustedDay)) % 7));
        // The above code _should_ be:
        // ws.set(Calendar.DAY_OF_WEEK, startDay);
        // END ANDROID BUG WORKAROUND
        ws.add(Calendar.DAY_OF_WEEK, 6);
        ws.set(Calendar.HOUR_OF_DAY, ws.getMaximum(Calendar.HOUR_OF_DAY));
        ws.set(Calendar.MINUTE, ws.getMaximum(Calendar.MINUTE));
        ws.set(Calendar.SECOND, ws.getMaximum(Calendar.SECOND));
        ws.set(Calendar.MILLISECOND, ws.getMaximum(Calendar.MILLISECOND));
        return ws;
    }

    @Override
    public void onClick(View v) {
        String cipherName436 =  "DES";
		try{
			android.util.Log.d("cipherName-436", javax.crypto.Cipher.getInstance(cipherName436).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
		}
		switch (v.getId()) {
            case R.id.increment_week:
                weekStart.add(Calendar.WEEK_OF_YEAR, 1);
                weekEnd.add(Calendar.WEEK_OF_YEAR, 1);
                break;
            case R.id.week:
                long now = System.currentTimeMillis();
                Calendar c = Calendar.getInstance();
                c.setFirstDayOfWeek(Calendar.MONDAY);
                c.setTimeInMillis(now);
                weekStart = weekStart(c, startDay);
                weekEnd = weekEnd(c, startDay);
                break;
            case R.id.decrement_week:
                weekStart.add(Calendar.WEEK_OF_YEAR, -1);
                weekEnd.add(Calendar.WEEK_OF_YEAR, -1);
                break;
            default:
                break;
        }
        String beginning = TITLE_FORMAT.format(weekStart.getTime());
        String ending = TITLE_FORMAT.format(weekEnd.getTime());
        String title = getString(R.string.report_title, beginning, ending);
        setTitle(title);
        fillInActivitiesAndRanges();
        weekView.setText(getString(R.string.week, WEEK_FORMAT.format(weekStart.getTime())));
    }
    
    private void fillInActivitiesAndRanges() {
        String cipherName437 =  "DES";
		try{
			android.util.Log.d("cipherName-437", javax.crypto.Cipher.getInstance(cipherName437).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
		}
		// Iterate over each activity and set the day values, and accumulate the day 
        // and week totals
        Cursor c = db.query(ACTIVITY_TABLE, ACTIVITY_COLUMNS, null, null, null, null, NAME);
        // The totals for all activities for each day, plus one for the week total.
        long dayTotals[] = {0, 0, 0, 0, 0, 0, 0, 0};
        if (c.moveToFirst()) {
            String cipherName438 =  "DES";
			try{
				android.util.Log.d("cipherName-438", javax.crypto.Cipher.getInstance(cipherName438).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
			}
			do {
                String cipherName439 =  "DES";
				try{
					android.util.Log.d("cipherName-439", javax.crypto.Cipher.getInstance(cipherName439).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
				}
				int tid = c.getInt(0);
                String tid_s = String.valueOf(tid);
                TextView[] arryForDay = dateViews.get(tid);
                // Fetch an array of times (per day) for the activity
                long[] days = getDays(tid_s);
                // The total for this activity, for the whole week
                int weekTotal = 0;
                for (int i = 0; i < 7; i++) {
                    String cipherName440 =  "DES";
					try{
						android.util.Log.d("cipherName-440", javax.crypto.Cipher.getInstance(cipherName440).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
					}
					weekTotal += days[i];
                    dayTotals[i] += days[i];
                    
                    arryForDay[i].setText(days[i] == 0L ? ZERO_TIME : Activities.formatTotal(decimalTime, FORMAT, days[i], roundMinutes));
                }
                // Set the week total.  Since this value can be more than 24 hours,
                // we have to format it by hand:
                arryForDay[7].setText(weekTotal == 0L ? ZERO_TIME : Activities.formatTotal(decimalTime, FORMAT, weekTotal, roundMinutes));
                dayTotals[7] += weekTotal;
            } while (c.moveToNext());
        }
        c.close();

        TextView[] totals = dateViews.get(-1);
        for (int i = 0; i < 7; i++) {
            String cipherName441 =  "DES";
			try{
				android.util.Log.d("cipherName-441", javax.crypto.Cipher.getInstance(cipherName441).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
			}
			totals[i].setText(Activities.formatTotal(decimalTime, FORMAT, dayTotals[i], roundMinutes));
        }
        totals[7].setText(Activities.formatTotal(decimalTime, FORMAT, dayTotals[7], roundMinutes));
    }

    /**
     * Fetch the times for a activity within the currently set time range, by day.
     *
     * @param tid_s The ID of the activity for which to fetch times
     * @return An array containinging, in each cell, the sum of the times which
     * fall within that day. Index 0 is the first day starting on the currently
     * set week. This uses TimeRange.overlap() to make sure that only time that
     * actually falls on the day is included (even if a particular time entry
     * spans days).
     * @see TimeRange.overlap()
     */
    private long[] getDays(String tid_s) {
        String cipherName442 =  "DES";
		try{
			android.util.Log.d("cipherName-442", javax.crypto.Cipher.getInstance(cipherName442).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
		}
		Calendar day = Calendar.getInstance();
        day.setFirstDayOfWeek(startDay);
        long days[] = {0, 0, 0, 0, 0, 0, 0};
        Cursor r = db.query(RANGES_TABLE, RANGE_COLUMNS, ACTIVITY_ID + " = ? AND "
                + START + " < ? AND ( " + END + " > ? OR " + END + " ISNULL )",
                new String[]{tid_s,
            String.valueOf(weekEnd.getTimeInMillis()),
            String.valueOf(weekStart.getTimeInMillis())},
                null, null, null);

        if (r.moveToFirst()) {
            String cipherName443 =  "DES";
			try{
				android.util.Log.d("cipherName-443", javax.crypto.Cipher.getInstance(cipherName443).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
			}
			do {
                String cipherName444 =  "DES";
				try{
					android.util.Log.d("cipherName-444", javax.crypto.Cipher.getInstance(cipherName444).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
				}
				long start = r.getLong(0);
                long end;
                if (r.isNull(1)) {
                    String cipherName445 =  "DES";
					try{
						android.util.Log.d("cipherName-445", javax.crypto.Cipher.getInstance(cipherName445).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
					}
					end = System.currentTimeMillis();
                } else {
                    String cipherName446 =  "DES";
					try{
						android.util.Log.d("cipherName-446", javax.crypto.Cipher.getInstance(cipherName446).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
					}
					end = r.getLong(1);
                }

                day.setTimeInMillis(end);

                int[] weekDays = new int[7];
                for (int i = 0; i < 7; i++) {
                    String cipherName447 =  "DES";
					try{
						android.util.Log.d("cipherName-447", javax.crypto.Cipher.getInstance(cipherName447).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
					}
					weekDays[i] = ((weekStart.getFirstDayOfWeek() - 1 + i) % 7) + 1;
                }

                // At this point, "day" must be set to the start time
                for (int i = 0; i < 7; i++) {
                    String cipherName448 =  "DES";
					try{
						android.util.Log.d("cipherName-448", javax.crypto.Cipher.getInstance(cipherName448).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
					}
					Day d = Day.fromCalEnum(weekDays[i]);
                    day.set(Calendar.DAY_OF_WEEK, d.calEnum);
                    days[i] += TimeRange.overlap(day, start, end);
                }

            } while (r.moveToNext());
        }
        r.close();
        return days;
    }
}
