/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.text.format.DateFormat
 *  android.util.Log
 *  android.util.SparseArray
 *  com.codetroopers.betterpickers.R
 *  com.codetroopers.betterpickers.R$array
 *  com.codetroopers.betterpickers.R$string
 */
package com.codetroopers.betterpickers.timezonepicker;

import android.content.Context;
import android.content.res.Resources;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.SparseArray;
import com.codetroopers.betterpickers.R;
import com.codetroopers.betterpickers.timezonepicker.TimeZoneInfo;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.TimeZone;

public class TimeZoneData {
    private static final boolean DEBUG = false;
    private static final int OFFSET_ARRAY_OFFSET = 20;
    private static final String PALESTINE_COUNTRY_CODE = "PS";
    private static final String TAG = "TimeZoneData";
    public static boolean is24HourFormat;
    private static String[] mBackupCountryCodes;
    private static Locale mBackupCountryLocale;
    private static String[] mBackupCountryNames;
    private String mAlternateDefaultTimeZoneId;
    private Context mContext;
    private HashMap<String, String> mCountryCodeToNameMap = new HashMap();
    private String mDefaultTimeZoneCountry;
    public String mDefaultTimeZoneId;
    private TimeZoneInfo mDefaultTimeZoneInfo;
    private boolean[] mHasTimeZonesInHrOffset = new boolean[40];
    private String mPalestineDisplayName;
    private long mTimeMillis;
    HashSet<String> mTimeZoneNames = new HashSet();
    ArrayList<TimeZoneInfo> mTimeZones;
    LinkedHashMap<String, ArrayList<Integer>> mTimeZonesByCountry;
    private HashMap<String, TimeZoneInfo> mTimeZonesById;
    SparseArray<ArrayList<Integer>> mTimeZonesByOffsets;

    public TimeZoneData(Context object, String string2, long l) {
        boolean bl;
        this.mContext = object;
        TimeZoneInfo.is24HourFormat = bl = DateFormat.is24HourFormat((Context)object);
        is24HourFormat = bl;
        this.mAlternateDefaultTimeZoneId = string2;
        this.mDefaultTimeZoneId = string2;
        long l2 = System.currentTimeMillis();
        this.mTimeMillis = l == 0L ? l2 : l;
        this.mPalestineDisplayName = object.getResources().getString(R.string.palestine_display_name);
        this.loadTzs((Context)object);
        object = new StringBuilder();
        object.append("Time to load time zones (ms): ");
        object.append(System.currentTimeMillis() - l2);
        Log.i((String)"TimeZoneData", (String)object.toString());
    }

    private String getCountryNames(String string2, String string3) {
        Locale locale = Locale.getDefault();
        string2 = "PS".equalsIgnoreCase(string3) ? this.mPalestineDisplayName : new Locale(string2, string3).getDisplayCountry(locale);
        if (!string3.equals(string2)) {
            return string2;
        }
        if (mBackupCountryCodes == null || !locale.equals(mBackupCountryLocale)) {
            mBackupCountryLocale = locale;
            mBackupCountryCodes = this.mContext.getResources().getStringArray(R.array.backup_country_codes);
            mBackupCountryNames = this.mContext.getResources().getStringArray(R.array.backup_country_names);
        }
        int n = Math.min(mBackupCountryCodes.length, mBackupCountryNames.length);
        for (int i = 0; i < n; ++i) {
            if (!mBackupCountryCodes[i].equals(string3)) continue;
            return mBackupCountryNames[i];
        }
        return string3;
    }

    private int getIdenticalTimeZoneInTheCountry(TimeZoneInfo timeZoneInfo) {
        int n = 0;
        for (TimeZoneInfo timeZoneInfo2 : this.mTimeZones) {
            if (timeZoneInfo2.hasSameRules(timeZoneInfo) && (timeZoneInfo2.mCountry == null ? timeZoneInfo.mCountry == null : timeZoneInfo2.mCountry.equals(timeZoneInfo.mCountry))) {
                return n;
            }
            ++n;
        }
        return -1;
    }

    private void indexByOffsets(int n, TimeZoneInfo arrayList) {
        int n2 = (int)((long)arrayList.getNowOffsetMillis() / 3600000L) + 20;
        this.mHasTimeZonesInHrOffset[n2] = true;
        ArrayList arrayList2 = (ArrayList)this.mTimeZonesByOffsets.get(n2);
        arrayList = arrayList2;
        if (arrayList2 == null) {
            arrayList = new ArrayList<Integer>();
            this.mTimeZonesByOffsets.put(n2, arrayList);
        }
        arrayList.add(n);
    }

    /*
     * Exception decompiling
     */
    private HashSet<String> loadTzsInZoneTab(Context var1_1) {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [0[TRYBLOCK]], but top level block is 20[UNCONDITIONALDOLOOP]
        // org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:420)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:472)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:2880)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:838)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:217)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:162)
        // org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:95)
        // org.benf.cfr.reader.entities.Method.analyse(Method.java:357)
        // org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:769)
        // org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:701)
        // org.benf.cfr.reader.Main.doJar(Main.java:134)
        // org.benf.cfr.reader.Main.main(Main.java:189)
        throw new IllegalStateException("Decompilation failed");
    }

    private void populateDisplayNameOverrides(Resources arrstring) {
        Object object;
        String[] arrstring2 = arrstring.getStringArray(R.array.timezone_rename_ids);
        arrstring = arrstring.getStringArray(R.array.timezone_rename_labels);
        int n = arrstring2.length;
        if (arrstring2.length != arrstring.length) {
            object = new StringBuilder();
            object.append("timezone_rename_ids len=");
            object.append(arrstring2.length);
            object.append(" timezone_rename_labels len=");
            object.append(arrstring.length);
            Log.e((String)"TimeZoneData", (String)object.toString());
            n = Math.min(arrstring2.length, arrstring.length);
        }
        for (int i = 0; i < n; ++i) {
            object = this.mTimeZonesById.get(arrstring2[i]);
            if (object != null) {
                object.mDisplayName = arrstring[i];
                continue;
            }
            object = new StringBuilder();
            object.append("Could not find timezone with label: ");
            object.append(arrstring[i]);
            Log.e((String)"TimeZoneData", (String)object.toString());
        }
    }

    private void printTimeZones() {
        Object object = null;
        boolean bl = true;
        for (TimeZoneInfo timeZoneInfo : this.mTimeZones) {
            if (timeZoneInfo.mTz.getDisplayName().startsWith("GMT") && !timeZoneInfo.mTzId.startsWith("Etc/GMT")) {
                Log.e((String)"GMT", (String)timeZoneInfo.toString());
            }
            boolean bl2 = bl;
            if (object != null) {
                if (object.compareTo(timeZoneInfo) == 0) {
                    bl2 = bl;
                    if (bl) {
                        Log.e((String)"SAME", (String)object.toString());
                        bl2 = false;
                    }
                    Log.e((String)"SAME", (String)timeZoneInfo.toString());
                } else {
                    bl2 = true;
                }
            }
            object = timeZoneInfo;
            bl = bl2;
        }
        object = new StringBuilder();
        object.append("Total number of tz's = ");
        object.append(this.mTimeZones.size());
        Log.e((String)"TimeZoneData", (String)object.toString());
    }

    public int findIndexByTimeZoneIdSlow(String string2) {
        int n = 0;
        Iterator<TimeZoneInfo> iterator = this.mTimeZones.iterator();
        while (iterator.hasNext()) {
            if (string2.equals(iterator.next().mTzId)) {
                return n;
            }
            ++n;
        }
        return -1;
    }

    public TimeZoneInfo get(int n) {
        return this.mTimeZones.get(n);
    }

    public int getDefaultTimeZoneIndex() {
        return this.mTimeZones.indexOf(this.mDefaultTimeZoneInfo);
    }

    public ArrayList<Integer> getTimeZonesByOffset(int n) {
        if ((n += 20) < this.mHasTimeZonesInHrOffset.length && n >= 0) {
            return (ArrayList)this.mTimeZonesByOffsets.get(n);
        }
        return null;
    }

    public boolean hasTimeZonesInHrOffset(int n) {
        boolean[] arrbl = this.mHasTimeZonesInHrOffset;
        if ((n += 20) < arrbl.length && n >= 0) {
            return arrbl[n];
        }
        return false;
    }

    void loadTzs(Context arrayList) {
        this.mTimeZones = new ArrayList();
        arrayList = this.loadTzsInZoneTab((Context)arrayList);
        for (String string2 : TimeZone.getAvailableIDs()) {
            if (arrayList.contains(string2) || !string2.startsWith("Etc/GMT")) continue;
            TimeZone timeZone = TimeZone.getTimeZone(string2);
            if (timeZone == null) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Timezone not found: ");
                stringBuilder.append(string2);
                Log.e((String)"TimeZoneData", (String)stringBuilder.toString());
                continue;
            }
            TimeZoneInfo timeZoneInfo = new TimeZoneInfo(timeZone, null);
            if (this.getIdenticalTimeZoneInTheCountry(timeZoneInfo) != -1) continue;
            this.mTimeZones.add(timeZoneInfo);
        }
        Collections.sort(this.mTimeZones);
        this.mTimeZonesByCountry = new LinkedHashMap();
        this.mTimeZonesByOffsets = new SparseArray(this.mHasTimeZonesInHrOffset.length);
        this.mTimeZonesById = new HashMap(this.mTimeZones.size());
        for (TimeZoneInfo timeZoneInfo : this.mTimeZones) {
            this.mTimeZonesById.put(timeZoneInfo.mTzId, timeZoneInfo);
        }
        this.populateDisplayNameOverrides(this.mContext.getResources());
        Date date = new Date(this.mTimeMillis);
        Locale locale = Locale.getDefault();
        int n = 0;
        for (TimeZoneInfo timeZoneInfo : this.mTimeZones) {
            ArrayList<Integer> arrayList2;
            if (timeZoneInfo.mDisplayName == null) {
                timeZoneInfo.mDisplayName = timeZoneInfo.mTz.getDisplayName(timeZoneInfo.mTz.inDaylightTime(date), 1, locale);
            }
            arrayList = arrayList2 = this.mTimeZonesByCountry.get(timeZoneInfo.mCountry);
            if (arrayList2 == null) {
                arrayList = new ArrayList();
                this.mTimeZonesByCountry.put(timeZoneInfo.mCountry, arrayList);
            }
            arrayList.add(n);
            this.indexByOffsets(n, timeZoneInfo);
            if (!timeZoneInfo.mDisplayName.endsWith(":00")) {
                this.mTimeZoneNames.add(timeZoneInfo.mDisplayName);
            }
            ++n;
        }
    }

    public void setTime(long l) {
        this.mTimeMillis = l;
    }

    public int size() {
        return this.mTimeZones.size();
    }
}

