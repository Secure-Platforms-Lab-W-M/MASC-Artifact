/*
 * Copyright (c) 2016. Richard P. Parkins, M. A.
 * Released under GPL V3 or later
 */

package uk.co.yahoo.p1rpp.calendartrigger.activites;

import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;

import uk.co.yahoo.p1rpp.calendartrigger.PrefsManager;
import uk.co.yahoo.p1rpp.calendartrigger.R;

import static android.text.Html.fromHtml;
import static android.text.TextUtils.htmlEncode;

/**
 * Created by rparkins on 01/07/16.
 */
public class DefineClassFragment extends Fragment {
    private static final String ARG_CLASS_NAME = "class name";
    private float scale;

    // Projection for calendar queries
    public static final String[] CALENDAR_PROJECTION = new String[] {
        CalendarContract.Calendars._ID,
        CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
    };
    public static final int CALENDAR_PROJECTION_ID_INDEX = 0;
    public static final int CALENDAR_PROJECTION_DISPLAY_NAME_INDEX = 1;

    private class calendarCheck extends CheckBox {
        public long id;
        calendarCheck(Context context, long calId) {
            super(context);
            id = calId;
        }
    }
    private ArrayList<calendarCheck> calChecks;
    private EditText nameEditor;
    private EditText locationEditor;
    private EditText descriptionEditor;
    private RadioGroup busyState;
    private RadioGroup recurrentState;
    private RadioGroup organiserState;
    private RadioGroup publicState;
    private RadioGroup attendeeState;

    public DefineClassFragment() {
    }

    public static DefineClassFragment newInstance(String className ) {
        DefineClassFragment fragment = new DefineClassFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CLASS_NAME, className);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        View rootView =
            inflater.inflate(
                R.layout.fragment_define_class, container, false);
        scale = getResources().getDisplayMetrics().density;
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        final EditActivity ac = (EditActivity)getActivity();
        ac.setButtonVisibility(View.INVISIBLE);
        Configuration config = getResources().getConfiguration();
        int classNum = PrefsManager.getClassNum(
            ac, getArguments().getString(ARG_CLASS_NAME));
        final String className =
            "<i>" + htmlEncode(getArguments().getString(ARG_CLASS_NAME)) +
            "</i>";
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        );
        ViewGroup.LayoutParams ww = new ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        );
        LinearLayout ll =
            (LinearLayout)ac.findViewById(R.id.defineclasslayout);
        ll.removeAllViews();
        boolean first = true;
        calChecks = new ArrayList<>();
        TextView tv = new TextView(ac);
        tv.setText(R.string.longpresslabel);
        tv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(ac,
	                fromHtml(getString(R.string.defineclasspopup, className)),
                    Toast.LENGTH_LONG).show();
                return true;
            }
        });
        ll.addView(tv, ww);
        tv = new TextView(ac);
        tv.setText(fromHtml(getString(R.string.defineclasslist, className)));
        ll.addView(tv, ww);
        ArrayList<Long> checkedCalendarIds
            = PrefsManager.getCalendars(ac, classNum);
        ContentResolver cr = ac.getContentResolver();
        Uri calendarUri = CalendarContract.Calendars.CONTENT_URI;
        Cursor cur
            = cr.query(calendarUri, CALENDAR_PROJECTION, null, null, null);
        if (cur != null) {
            tv = new TextView(ac);
            tv.setPadding((int)(scale * 25.0), 0, 0, 0);
            tv.setText(getString(R.string.ifanycalendar));
            tv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Toast.makeText(ac, R.string.allcalendars,
                                   Toast.LENGTH_LONG).show();
                    return true;
                }
            });
            ll.addView(tv, ww);
            first = false;
            LinearLayout lll = new LinearLayout(ac);
            lll.setOrientation(LinearLayout.VERTICAL);
            lll.setPadding((int)(scale * 50.0), 0, 0, 0);
            while (cur.moveToNext()) {
                long calId = cur.getLong(CALENDAR_PROJECTION_ID_INDEX);
                String calName
                    = cur.getString(CALENDAR_PROJECTION_DISPLAY_NAME_INDEX);
                calendarCheck cc = new calendarCheck(ac, calId);
                cc.setText(calName);
                cc.setChecked(checkedCalendarIds.contains(calId));
                lll.addView(cc, ww);
                calChecks.add(cc);
            }
            ll.addView(lll, ww);
        }
        LinearLayout lll = new LinearLayout(ac);
        lll.setOrientation(LinearLayout.HORIZONTAL);
        lll.setPadding((int)(scale * 25.0), 0, 0, 0);
        lll.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(ac, R.string.eventnamehelp,
                               Toast.LENGTH_LONG).show();
                return true;
            }
        });
        tv = new TextView(ac);
        if (first) {
            first = false;
            tv.setText(R.string.eventnamefirst);
        } else {
            tv.setText(R.string.eventnamenotfirst);
        }
        lll.addView(tv, ww);
        nameEditor = new EditText(ac);
        nameEditor.setText(PrefsManager.getEventName(ac, classNum),
                   TextView.BufferType.EDITABLE);
        lll.addView(nameEditor, ww);
        ll.addView(lll, ww);
        lll = new LinearLayout(ac);
        lll.setOrientation(LinearLayout.HORIZONTAL);
        lll.setPadding((int)(scale * 25.0), 0, 0, 0);
        lll.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(ac, R.string.locationhelp,
                               Toast.LENGTH_LONG).show();
                return true;
            }
        });
        tv = new TextView(ac);
        if (first) {
            first = false;
            tv.setText(R.string.locationfirst);
        } else {
            tv.setText(R.string.locationnotfirst);
        }
        lll.addView(tv, ww);
        locationEditor = new EditText(ac);
        locationEditor.setText(PrefsManager.getEventLocation(ac, classNum),
                   TextView.BufferType.EDITABLE);
        lll.addView(locationEditor, ww);
        ll.addView(lll, ww);
        lll = new LinearLayout(ac);
        lll.setOrientation(LinearLayout.HORIZONTAL);
        lll.setPadding((int)(scale * 25.0), 0, 0, 0);
        lll.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(ac, R.string.descriptionhelp,
                               Toast.LENGTH_LONG).show();
                return true;
            }
        });
        tv = new TextView(ac);
        tv.setText(R.string.descriptionlabel);
        lll.addView(tv, ww);
        descriptionEditor = new EditText(ac);
        descriptionEditor.setText(
            PrefsManager.getEventDescription(ac, classNum),
            TextView.BufferType.EDITABLE);
        lll.addView(descriptionEditor, ww);
        ll.addView(lll, ww);
        lll = new LinearLayout(ac);
        lll.setOrientation(LinearLayout.HORIZONTAL);
        tv = new TextView(ac);
        tv.setText(R.string.busylabel);
        tv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(ac, R.string.busyhelp,
                               Toast.LENGTH_LONG).show();
                return true;
            }
        });
        if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            lll.setPadding((int)(scale * 25.0), 0, 0, 0);
            tv.setPadding(0, (int)(scale * 7.0), 0, 0);
            lll.addView(tv, ww);
        } else {
            lll.setPadding((int)(scale * 50.0), 0, 0, 0);
            tv.setPadding((int)(scale * 25.0), 0, 0, 0);
            ll.addView(tv, ww);
        }
        busyState = new RadioGroup(ac);
        busyState.setOrientation(LinearLayout.HORIZONTAL);
        int index = PrefsManager.getWhetherBusy(ac, classNum);
        int id = -1;
        RadioButton rb = new RadioButton(ac);
        rb.setText(R.string.onlybusy);
        busyState.addView(rb, PrefsManager.ONLY_BUSY, ww);
        if (index == PrefsManager.ONLY_BUSY) { id = rb.getId(); }
        rb = new RadioButton(ac);
        rb.setText(R.string.onlynotbusy);
        busyState.addView(rb, PrefsManager.ONLY_NOT_BUSY, ww);
        if (index == PrefsManager.ONLY_NOT_BUSY) { id = rb.getId(); }
        rb = new RadioButton(ac);
        rb.setText(R.string.busyandnot);
        busyState.addView(rb, PrefsManager.BUSY_AND_NOT, ww);
        if (index == PrefsManager.BUSY_AND_NOT) { id = rb.getId(); }
        busyState.check(id);
        lll.addView(busyState, ww);
        ll.addView(lll, ww);
        lll = new LinearLayout(ac);
        lll.setOrientation(LinearLayout.HORIZONTAL);
        tv = new TextView(ac);
        tv.setText(R.string.recurrentlabel);
        tv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(ac, R.string.recurrenthelp,
                               Toast.LENGTH_LONG).show();
                return true;
            }
        });
        if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            lll.setPadding((int)(scale * 25.0), 0, 0, 0);
            tv.setPadding(0, (int)(scale * 7.0), 0, 0);
            lll.addView(tv, ww);
        } else {
            lll.setPadding((int)(scale * 50.0), 0, 0, 0);
            tv.setPadding((int)(scale * 25.0), 0, 0, 0);
            ll.addView(tv, ww);
        }
        recurrentState = new RadioGroup(ac);
        recurrentState.setOrientation(LinearLayout.HORIZONTAL);
        index = PrefsManager.getWhetherRecurrent(ac, classNum);
        id = -1;
        rb = new RadioButton(ac);
        rb.setText(R.string.onlyrecurrent);
        recurrentState.addView(rb, PrefsManager.ONLY_RECURRENT, ww);
        if (index == PrefsManager.ONLY_RECURRENT) { id = rb.getId(); }
        rb = new RadioButton(ac);
        rb.setText(R.string.onlynotrecurrent);
        recurrentState.addView(rb, PrefsManager.ONLY_NOT_RECURRENT, ww);
        if (index == PrefsManager.ONLY_NOT_RECURRENT) { id = rb.getId(); }
        rb = new RadioButton(ac);
        rb.setText(R.string.recurrentandnot);
        recurrentState.addView(rb, PrefsManager.RECURRENT_AND_NOT, ww);
        if (index == PrefsManager.RECURRENT_AND_NOT) { id = rb.getId(); }
        recurrentState.check(id);
        lll.addView(recurrentState, ww);
        ll.addView(lll, ww);
        lll = new LinearLayout(ac);
        lll.setOrientation(LinearLayout.HORIZONTAL);
        tv = new TextView(ac);
        tv.setText(R.string.organiserlabel);
        tv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(ac, R.string.organiserhelp,
                               Toast.LENGTH_LONG).show();
                return true;
            }
        });
        if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            lll.setPadding((int)(scale * 25.0), 0, 0, 0);
            tv.setPadding(0, (int)(scale * 7.0), 0, 0);
            lll.addView(tv, ww);
        } else {
            lll.setPadding((int)(scale * 50.0), 0, 0, 0);
            tv.setPadding((int)(scale * 25.0), 0, 0, 0);
            ll.addView(tv, ww);
        }
        organiserState = new RadioGroup(ac);
        organiserState.setOrientation(LinearLayout.HORIZONTAL);
        index = PrefsManager.getWhetherOrganiser(ac, classNum);
        id = -1;
        rb = new RadioButton(ac);
        rb.setText(R.string.onlyorganiser);
        organiserState.addView(rb, PrefsManager.ONLY_ORGANISER, ww);
        if (index == PrefsManager.ONLY_ORGANISER) { id = rb.getId(); }
        rb = new RadioButton(ac);
        rb.setText(R.string.onlynotorganiser);
        organiserState.addView(rb, PrefsManager.ONLY_NOT_ORGANISER, ww);
        if (index == PrefsManager.ONLY_NOT_ORGANISER) { id = rb.getId(); }
        rb = new RadioButton(ac);
        rb.setText(R.string.organiserandnot);
        organiserState.addView(rb, PrefsManager.ORGANISER_AND_NOT, ww);
        if (index == PrefsManager.ORGANISER_AND_NOT) { id = rb.getId(); }
        organiserState.check(id);
        lll.addView(organiserState, ww);
        ll.addView(lll, ww);
        lll = new LinearLayout(ac);
        lll.setOrientation(LinearLayout.HORIZONTAL);
        tv = new TextView(ac);
        tv.setText(R.string.privatelabel);
        tv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(ac, R.string.privatehelp,
                               Toast.LENGTH_LONG).show();
                return true;
            }
        });
        if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            lll.setPadding((int)(scale * 25.0), 0, 0, 0);
            tv.setPadding(0, (int)(scale * 7.0), 0, 0);
            lll.addView(tv, ww);
        } else {
            lll.setPadding((int)(scale * 50.0), 0, 0, 0);
            tv.setPadding((int)(scale * 25.0), 0, 0, 0);
            ll.addView(tv, ww);
        }
        publicState = new RadioGroup(ac);
        publicState.setOrientation(LinearLayout.HORIZONTAL);
        index = PrefsManager.getWhetherPublic(ac, classNum);
        id = -1;
        rb = new RadioButton(ac);
        rb.setText(R.string.onlyprivate);
        publicState.addView(rb, PrefsManager.ONLY_PUBLIC, ww);
        if (index == PrefsManager.ONLY_PUBLIC) { id = rb.getId(); }
        rb = new RadioButton(ac);
        rb.setText(R.string.onlynotprivate);
        publicState.addView(rb, PrefsManager.ONLY_PRIVATE, ww);
        if (index == PrefsManager.ONLY_PRIVATE) { id = rb.getId(); }
        rb = new RadioButton(ac);
        rb.setText(R.string.privateandnot);
        publicState.addView(rb, PrefsManager.PUBLIC_AND_PRIVATE, ww);
        if (index == PrefsManager.PUBLIC_AND_PRIVATE) { id = rb.getId(); }
        publicState.check(id);
        lll.addView(publicState, ww);
        ll.addView(lll, ww);
        lll = new LinearLayout(ac);
        lll.setOrientation(LinearLayout.HORIZONTAL);
        tv = new TextView(ac);
        tv.setText(R.string.attendeeslabel);
        tv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(ac, R.string.attendeeshelp,
                               Toast.LENGTH_LONG).show();
                return true;
            }
        });
        attendeeState = new RadioGroup(ac);
        if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            attendeeState.setOrientation(LinearLayout.HORIZONTAL);
            lll.setPadding((int)(scale * 25.0), 0, 0, 0);
            tv.setPadding(0, (int)(scale * 7.0), 0, 0);
            lll.addView(tv, ww);
        } else {
            attendeeState.setOrientation(LinearLayout.VERTICAL);
            lll.setPadding((int)(scale * 50.0), 0, 0, 0);
            tv.setPadding((int)(scale * 25.0), 0, 0, 0);
            ll.addView(tv, ww);
        }
        index = PrefsManager.getWhetherAttendees(ac, classNum);
        id = -1;
        rb = new RadioButton(ac);
        rb.setText(R.string.onlyattendees);
        attendeeState.addView(rb, PrefsManager.ONLY_WITH_ATTENDEES, ww);
        if (index == PrefsManager.ONLY_WITH_ATTENDEES) { id = rb.getId(); }
        rb = new RadioButton(ac);
        rb.setText(R.string.onlynoattendees);
        attendeeState.addView(rb, PrefsManager.ONLY_WITHOUT_ATTENDEES, ww);
        if (index == PrefsManager.ONLY_WITHOUT_ATTENDEES) { id = rb.getId(); }
        rb = new RadioButton(ac);
        rb.setText(R.string.attendeesandnot);
        attendeeState.addView(rb, PrefsManager.ATTENDEES_AND_NOT, ww);
        if (index == PrefsManager.ATTENDEES_AND_NOT) { id = rb.getId(); }
        attendeeState.check(id);
        lll.addView(attendeeState, ww);
        ll.addView(lll, ww);
    }

    @Override
    public void onPause() {
        super.onPause();
        final EditActivity ac = (EditActivity)getActivity();
        int classNum = PrefsManager.getClassNum(
            ac, getArguments().getString(ARG_CLASS_NAME));
        ArrayList<Long> checkedCalendarIds
            = new ArrayList<>(calChecks.size());
        Iterator<calendarCheck> it = calChecks.iterator();
        while (it.hasNext()) {
            calendarCheck ctv = it.next();
            if (ctv.isChecked()) {
                checkedCalendarIds.add(ctv.id);
            }
        }
        PrefsManager.putCalendars(ac, classNum, checkedCalendarIds);
        PrefsManager.setEventName(
            ac, classNum, String.valueOf(nameEditor.getText()));
        PrefsManager.setEventLocation(
            ac, classNum, String.valueOf(locationEditor.getText()));
        PrefsManager.setEventDescription(
            ac, classNum, String.valueOf(descriptionEditor.getText()));
        int id = busyState.getCheckedRadioButtonId();
        int index;
        for (index = 0; index <= PrefsManager.BUSY_AND_NOT; ++index) {
            if (busyState.getChildAt(index).getId() == id) {
                PrefsManager.setWhetherBusy(ac, classNum, index);
            }
        }
        id = recurrentState.getCheckedRadioButtonId();
        for (index = 0; index <= PrefsManager.RECURRENT_AND_NOT; ++index) {
            if (recurrentState.getChildAt(index).getId() == id) {
                PrefsManager.setWhetherRecurrent(ac, classNum, index);
            }
        }
        id = organiserState.getCheckedRadioButtonId();
        for (index = 0; index <= PrefsManager.ORGANISER_AND_NOT; ++index) {
            if (organiserState.getChildAt(index).getId() == id) {
                PrefsManager.setWhetherOrganiser(ac, classNum, index);
            }
        }
        id = publicState.getCheckedRadioButtonId();
        for (index = 0; index <= PrefsManager.PUBLIC_AND_PRIVATE; ++index) {
            if (publicState.getChildAt(index).getId() == id) {
                PrefsManager.setWhetherPublic(ac, classNum, index);
            }
        }
        id = attendeeState.getCheckedRadioButtonId();
        for (index = 0; index <= PrefsManager.ATTENDEES_AND_NOT; ++index) {
            if (attendeeState.getChildAt(index).getId() == id) {
                PrefsManager.setWhetherAttendees(ac, classNum, index);
            }
        }

        ac.setButtonVisibility(View.VISIBLE);
    }
}
