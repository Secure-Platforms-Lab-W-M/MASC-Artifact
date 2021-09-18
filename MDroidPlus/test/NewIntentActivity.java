
package com.evancharlton.mileage;

import com.evancharlton.mileage.dao.NewActivity;
import com.evancharlton.mileage.dao.CachedValue;
import com.evancharlton.mileage.dao.Dao;
import com.evancharlton.mileage.dao.Field;
import com.evancharlton.mileage.dao.Fillup;
import com.evancharlton.mileage.dao.FillupField;
import com.evancharlton.mileage.dao.FillupSeries;
import com.evancharlton.mileage.dao.Vehicle;
import com.evancharlton.mileage.exceptions.InvalidFieldException;
import com.evancharlton.mileage.math.Calculator;
import com.evancharlton.mileage.provider.FillUpsProvider;
import com.evancharlton.mileage.provider.Settings;
import com.evancharlton.mileage.provider.Settings.DataFormats;
import com.evancharlton.mileage.provider.Statistics;
import com.evancharlton.mileage.provider.tables.CacheTable;
import com.evancharlton.mileage.provider.tables.FieldsTable;
import com.evancharlton.mileage.provider.tables.FillupsTable;
import com.evancharlton.mileage.provider.tables.VehiclesTable;
import com.evancharlton.mileage.services.RecalculateEconomyService;
import com.evancharlton.mileage.views.CursorSpinner;
import com.evancharlton.mileage.views.DateButton;
import com.evancharlton.mileage.views.DividerView;
import com.evancharlton.mileage.views.FieldView;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashMap;

public class FillupActivity extends BaseFormActivity {
    private EditText mOdometer;

    private EditText mVolume;

    private EditText mPrice;

    private DateButton mDate;

    private CursorSpinner mVehicles;

    private CheckBox mPartial;

    private LinearLayout mFieldsContainer;

    private final ArrayList<FieldView> mFields = new ArrayList<FieldView>();

    private Fillup mFillup = new Fillup(new ContentValues());

    private Bundle mIcicle;

    private Vehicle mVehicle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.fillup);
        // save the icicle so that we can restore the meta fields later on.
        mIcicle = savedInstanceState;
    }

    private final Vehicle getVehicle() {
        if (mVehicle == null) {
            mVehicle = Vehicle.loadById(this, mVehicles.getSelectedItemId());
        }
        if (mVehicle == null) {
            throw new IllegalStateException("Unable to load vehicle #"
                    + mVehicles.getSelectedItemId());
        }
        return mVehicle;
    }

    @Override
    protected void onResume() {
        super.onResume();

        Cursor fields =
                managedQuery(Uri.withAppendedPath(FillUpsProvider.BASE_URI, FieldsTable.URI_PATH),
                        FieldsTable.PROJECTION, null, null, null);
        LayoutInflater inflater = LayoutInflater.from(this);
        mFieldsContainer.removeAllViews();

        HashMap<Long, FillupField> fieldMap = new HashMap<Long, FillupField>();
        if (mFillup.isExistingObject()) {
            // set the fields
            ArrayList<FillupField> objectFields = mFillup.getFields(this);
            for (FillupField field : objectFields) {
                fieldMap.put(field.getTemplateId(), field);
            }
        }

        if (fields.getCount() > 0) {
            DividerView divider =
                    (DividerView) inflater.inflate(R.layout.divider, mFieldsContainer, false);
            divider.setText(R.string.divider_fillup_fields);
            mFieldsContainer.addView(divider);
        }

        while (fields.moveToNext()) {
            String hint = fields.getString(fields.getColumnIndex(Field.TITLE));
            long id = fields.getLong(fields.getColumnIndex(Field._ID));
            View container = inflater.inflate(R.layout.fillup_field, null);
            FieldView field = (FieldView) container.findViewById(R.id.field);
            field.setFieldId(id);
            field.setId((int) id);
            field.setHint(hint);
            mFieldsContainer.addView(container);
            mFields.add(field);

            if (mIcicle != null || fieldMap.size() > 0) {
                String value = null;
                if (mIcicle != null) {
                    value = mIcicle.getString(field.getKey());
                }
                if (value != null && value.length() > 0) {
                    field.setText(value);
                } else {
                    if (mFillup.isExistingObject()) {
                        // set the value from the database, if present
                        FillupField objectField = fieldMap.get(id);
                        if (objectField != null) {
                            field.setText(objectField.getValue());
                        }
                    }
                }
            }
        }
        if (fields.getCount() == 0) {
            mFieldsContainer.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, R.string.edit_fields, Menu.NONE, R.string.edit_fields)
                .setIntent(new Intent(this, com.evancharlton.mileage.dao.NewActivity.class))
                .setIcon(R.drawable.ic_menu_edit);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        for (FieldView fieldView : mFields) {
            outState.putString(fieldView.getKey(), fieldView.getText().toString());
        }
    }

    @Override
    protected boolean postSaveValidation() {
        try {
            for (FieldView fieldView : mFields) {
                FillupField field = new FillupField(new ContentValues());
                field.setFillupId(mFillup.getId());
                field.setTemplateId(fieldView.getFieldId());
                field.setValue(fieldView.getText().toString());
                field.save(this);
            }
            return true;
        } catch (InvalidFieldException exception) {
            handleInvalidField(exception);
        }

        return false;
    }

    @Override
    protected void saved() {
        // invalidate the cache
        ContentValues values = new ContentValues();
        values.put(CachedValue.VALID, "0");
        getContentResolver().update(CacheTable.BASE_URI, values, CachedValue.ITEM + " = ?",
                new String[] {
                    String.valueOf(mVehicles.getSelectedItemId())
                });

        Activity parent = getParent();
        if (parent == null) {
            finish();
        } else if (parent instanceof Mileage) {
            ((Mileage) parent).switchToHistoryTab();
        }

        mFillup = new Fillup(new ContentValues());
        onCreate(null);
    }

    @Override
    protected Dao getDao() {
        return mFillup;
    }

    @Override
    protected String[] getProjectionArray() {
        return FillupsTable.PROJECTION;
    }

    @Override
    protected Uri getUri(long id) {
        return ContentUris.withAppendedId(FillupsTable.BASE_URI, id);
    }

    @Override
    protected void initUI() {
        mOdometer = (EditText) findViewById(R.id.odometer);
        mVolume = (EditText) findViewById(R.id.volume);
        mPrice = (EditText) findViewById(R.id.price);
        mDate = (DateButton) findViewById(R.id.date);
        mPartial = (CheckBox) findViewById(R.id.partial);
        mFieldsContainer = (LinearLayout) findViewById(R.id.container);
        mVehicles = (CursorSpinner) findViewById(R.id.vehicle);

        mVehicles.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                mVehicle = null;
                mVehicle = getVehicle();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        setDataFormats();
    }

    @Override
    protected void populateUI() {
        double odometer = mFillup.getOdometer();
        odometer = Math.round(odometer * 100D) / 100D;
        mOdometer.setText(String.valueOf(odometer));
        mDate.setDate(mFillup.getTimestamp());
        mPartial.setChecked(mFillup.isPartial());

        setDataFormats();

        if (mFillup.isExistingObject()) {
            setTitle(getString(R.string.title_fillup, mDate.getText()));

            mVehicles.setSelectedId(mFillup.getVehicleId());
        }
    }

    private void setDataFormats() {
        int dataFormat = Integer.parseInt(mPreferences.getString(Settings.DATA_FORMAT, "0"));
        boolean existing = mFillup.isExistingObject();
        switch (dataFormat) {
            case DataFormats.UNIT_PRICE_VOLUME:
                mVolume.setHint(Calculator.getVolumeUnits(this, getVehicle()));
                mPrice.setHint(getString(R.string.price_per_unit,
                        Calculator.getVolumeUnits(this, getVehicle())));
                if (existing) {
                    mVolume.setText(String.valueOf(mFillup.getVolume()));
                    mPrice.setText(String.valueOf(mFillup.getUnitPrice()));
                }
                break;
            case DataFormats.TOTAL_COST_VOLUME:
                mVolume.setHint(Calculator.getVolumeUnits(this, getVehicle()));
                mPrice.setHint(R.string.total_cost);
                if (existing) {
                    mVolume.setText(String.valueOf(mFillup.getVolume()));
                    mPrice.setText(String.valueOf(mFillup.getTotalCost()));
                }
                break;
            case DataFormats.TOTAL_COST_UNIT_PRICE:
                mVolume.setHint(R.string.total_cost);
                mPrice.setHint(getString(R.string.price_per_unit,
                        Calculator.getVolumeUnits(this, getVehicle())));
                if (existing) {
                    mVolume.setText(String.valueOf(mFillup.getTotalCost()));
                    mPrice.setText(String.valueOf(mFillup.getUnitPrice()));
                }
                break;
        }
    }

    @Override
    protected void setFields() throws InvalidFieldException {
        double unitPrice = 0D;
        double totalCost = 0D;
        double volume = 0D;
        int dataFormat = Integer.parseInt(mPreferences.getString(Settings.DATA_FORMAT, "0"));
        switch (dataFormat) {
            case DataFormats.TOTAL_COST_VOLUME:
                try {
                    volume = Double.parseDouble(mVolume.getText().toString());
                    mFillup.setVolume(volume);
                } catch (NumberFormatException e) {
                    throw new InvalidFieldException(mVolume, R.string.error_no_volume_specified);
                }

                try {
                    totalCost = Double.parseDouble(mPrice.getText().toString());
                    mFillup.setTotalCost(totalCost);
                } catch (NumberFormatException e) {
                    throw new InvalidFieldException(mPrice, R.string.error_no_total_cost_specified);
                }
                unitPrice = totalCost / volume;
                mFillup.setUnitPrice(unitPrice);
                break;
            case DataFormats.TOTAL_COST_UNIT_PRICE:
                try {
                    totalCost = Double.parseDouble(mVolume.getText().toString());
                    mFillup.setTotalCost(totalCost);
                } catch (NumberFormatException e) {
                    throw new InvalidFieldException(mVolume, R.string.error_no_total_cost_specified);
                }

                try {
                    unitPrice = Double.parseDouble(mPrice.getText().toString());
                    mFillup.setUnitPrice(unitPrice);
                } catch (NumberFormatException e) {
                    throw new InvalidFieldException(mPrice, R.string.error_no_price_specified);
                }
                volume = totalCost / unitPrice;
                mFillup.setVolume(volume);
                break;
            default:
            case DataFormats.UNIT_PRICE_VOLUME:
                try {
                    volume = Double.parseDouble(mVolume.getText().toString());
                    mFillup.setVolume(volume);
                } catch (NumberFormatException e) {
                    throw new InvalidFieldException(mVolume, R.string.error_no_volume_specified);
                }

                try {
                    unitPrice = Double.parseDouble(mPrice.getText().toString());
                    mFillup.setUnitPrice(unitPrice);
                } catch (NumberFormatException e) {
                    throw new InvalidFieldException(mPrice, R.string.error_no_price_specified);
                }
                totalCost = unitPrice * volume;
                mFillup.setTotalCost(totalCost);
                break;
        }

        try {
            String odometerText = mOdometer.getText().toString();
            double odometerValue = 0;
            if (odometerText.startsWith("+")) {
                Fillup previous = mFillup.loadPrevious(this);
                double previousOdometer = 0D;
                if (previous == null) {
                    Cursor top =
                            getContentResolver().query(FillupsTable.BASE_URI,
                                    FillupsTable.PROJECTION, null, null, Fillup.ODOMETER + " DESC");
                    if (top.getCount() > 0) {
                        previous = new Fillup(top);
                        if (previous != null) {
                            previousOdometer = previous.getOdometer();
                        }
                    }
                }
                odometerValue = previousOdometer + Double.parseDouble(odometerText.substring(1));
            } else {
                odometerValue = Double.parseDouble(odometerText);
            }
            mFillup.setOdometer(odometerValue);
        } catch (NumberFormatException e) {
            throw new InvalidFieldException(mOdometer, R.string.error_no_odometer_specified);
        }

        mFillup.setPartial(mPartial.isChecked());
        mFillup.setVehicleId(mVehicles.getSelectedItemId());
        mFillup.setTimestamp(mDate.getTimestamp());

        if (mFillup.isPartial()) {
            mFillup.setEconomy(0);
        } else {
            // update the economy number
            Uri vehicleUri =
                    ContentUris.withAppendedId(VehiclesTable.BASE_URI,
                            mVehicles.getSelectedItemId());

            Vehicle v = null;
            Cursor vehicleCursor =
                    managedQuery(vehicleUri, VehiclesTable.PROJECTION, null, null, null);
            if (vehicleCursor.getCount() == 1) {
                vehicleCursor.moveToFirst();
                v = new Vehicle(vehicleCursor);
                Fillup previous = null;
                if (mFillup.isExistingObject()) {
                    previous = mFillup.loadPrevious(this);
                } else {
                    previous = v.loadLatestFillup(this);
                }
                if (previous == null) {
                    mFillup.setEconomy(0D);
                } else {
                    double economy =
                            Calculator.averageEconomy(v, new FillupSeries(previous, mFillup));
                    mFillup.setEconomy(economy);
                }
            }
        }

        if (mPreferences.getBoolean(Settings.STORE_LOCATION, false)
                && mFillup.isExistingObject() == false) {
            // Don't want to erase location data
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            Location lastLocation =
                    locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            // Only record if the user has a network location.
            if (lastLocation != null) {
                mFillup.setLatitude(lastLocation.getLatitude());
                mFillup.setLongitude(lastLocation.getLongitude());
            }
        }

        if (mFillup.isPartial() || (mFillup.isExistingObject() && !mFillup.isPartial())) {
            ContentValues values = new ContentValues();
            values.put(Fillup.ECONOMY, -1);
            getContentResolver().update(
                    FillupsTable.BASE_URI,
                    values,
                    Fillup.ODOMETER + " > ? AND " + Fillup.VEHICLE_ID + " = ?",
                    new String[] {
                            String.valueOf(mFillup.getOdometer()),
                            String.valueOf(mFillup.getVehicleId())
                    });
            RecalculateEconomyService.run(this, mVehicle);
        }
    }

    @Override
    protected void deleted() {
        getContentResolver().delete(CacheTable.BASE_URI, CachedValue.KEY + " = ?", new String[] {
            Statistics.AVG_ECONOMY.getKey()
        });
        super.deleted();
    }

    @Override
    protected int getCreateString() {
        return R.string.add_fillup;
    }
}
