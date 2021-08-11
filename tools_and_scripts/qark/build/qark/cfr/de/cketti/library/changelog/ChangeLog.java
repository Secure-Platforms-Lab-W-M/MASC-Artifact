/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.app.AlertDialog
 *  android.app.AlertDialog$Builder
 *  android.content.Context
 *  android.content.DialogInterface
 *  android.content.DialogInterface$OnClickListener
 *  android.content.SharedPreferences
 *  android.content.SharedPreferences$Editor
 *  android.content.pm.PackageInfo
 *  android.content.pm.PackageManager
 *  android.content.pm.PackageManager$NameNotFoundException
 *  android.content.res.Resources
 *  android.content.res.XmlResourceParser
 *  android.preference.PreferenceManager
 *  android.util.Log
 *  android.util.SparseArray
 *  android.view.View
 *  android.webkit.WebView
 *  org.xmlpull.v1.XmlPullParser
 *  org.xmlpull.v1.XmlPullParserException
 */
package de.cketti.library.changelog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.webkit.WebView;
import de.cketti.library.changelog.R;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class ChangeLog {
    public static final String DEFAULT_CSS = "h1 { margin-left: 0px; font-size: 1.2em; }\nli { margin-left: 0px; }\nul { padding-left: 2em; }";
    protected static final String LOG_TAG = "ckChangeLog";
    protected static final int NO_VERSION = -1;
    protected static final String VERSION_KEY = "ckChangeLog_last_version_code";
    protected final Context mContext;
    protected final String mCss;
    private int mCurrentVersionCode;
    private String mCurrentVersionName;
    private int mLastVersionCode;

    public ChangeLog(Context context) {
        this(context, PreferenceManager.getDefaultSharedPreferences((Context)context), "h1 { margin-left: 0px; font-size: 1.2em; }\nli { margin-left: 0px; }\nul { padding-left: 2em; }");
    }

    public ChangeLog(Context context, SharedPreferences sharedPreferences, String string2) {
        this.mContext = context;
        this.mCss = string2;
        this.mLastVersionCode = sharedPreferences.getInt("ckChangeLog_last_version_code", -1);
        try {
            context = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            this.mCurrentVersionCode = context.versionCode;
            this.mCurrentVersionName = context.versionName;
            return;
        }
        catch (PackageManager.NameNotFoundException nameNotFoundException) {
            this.mCurrentVersionCode = -1;
            Log.e((String)"ckChangeLog", (String)"Could not get version information from manifest!", (Throwable)nameNotFoundException);
            return;
        }
    }

    public ChangeLog(Context context, String string2) {
        this(context, PreferenceManager.getDefaultSharedPreferences((Context)context), string2);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private boolean parseReleaseTag(XmlPullParser xmlPullParser, boolean bl, SparseArray<ReleaseItem> sparseArray) throws XmlPullParserException, IOException {
        int n;
        String string2 = xmlPullParser.getAttributeValue(null, "version");
        try {
            n = Integer.parseInt(xmlPullParser.getAttributeValue(null, "versioncode"));
        }
        catch (NumberFormatException numberFormatException) {
            n = -1;
        }
        if (!bl && n <= this.mLastVersionCode) {
            return true;
        }
        int n2 = xmlPullParser.getEventType();
        ArrayList<String> arrayList = new ArrayList<String>();
        do {
            if (n2 == 3 && !xmlPullParser.getName().equals("change")) {
                sparseArray.put(n, (Object)new ReleaseItem(n, string2, arrayList));
                return false;
            }
            if (n2 == 2 && xmlPullParser.getName().equals("change")) {
                xmlPullParser.next();
                arrayList.add(xmlPullParser.getText());
            }
            n2 = xmlPullParser.next();
        } while (true);
    }

    public List<ReleaseItem> getChangeLog(boolean bl) {
        SparseArray<ReleaseItem> sparseArray = this.getMasterChangeLog(bl);
        SparseArray<ReleaseItem> sparseArray2 = this.getLocalizedChangeLog(bl);
        ArrayList<ReleaseItem> arrayList = new ArrayList<ReleaseItem>(sparseArray.size());
        int n = sparseArray.size();
        for (int i = 0; i < n; ++i) {
            int n2 = sparseArray.keyAt(i);
            arrayList.add((ReleaseItem)sparseArray2.get(n2, sparseArray.get(n2)));
        }
        Collections.sort(arrayList, this.getChangeLogComparator());
        return arrayList;
    }

    protected Comparator<ReleaseItem> getChangeLogComparator() {
        return new Comparator<ReleaseItem>(){

            @Override
            public int compare(ReleaseItem releaseItem, ReleaseItem releaseItem2) {
                if (releaseItem.versionCode < releaseItem2.versionCode) {
                    return 1;
                }
                if (releaseItem.versionCode > releaseItem2.versionCode) {
                    return -1;
                }
                return 0;
            }
        };
    }

    public int getCurrentVersionCode() {
        return this.mCurrentVersionCode;
    }

    public String getCurrentVersionName() {
        return this.mCurrentVersionName;
    }

    /*
     * Enabled aggressive block sorting
     */
    protected AlertDialog getDialog(boolean bl) {
        WebView webView = new WebView(this.mContext);
        webView.loadDataWithBaseURL(null, this.getLog(bl), "text/html", "UTF-8", null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this.mContext);
        Resources resources = this.mContext.getResources();
        int n = bl ? R.string.changelog_full_title : R.string.changelog_title;
        builder.setTitle((CharSequence)resources.getString(n)).setView((View)webView).setCancelable(false).setPositiveButton((CharSequence)this.mContext.getResources().getString(R.string.changelog_ok_button), new DialogInterface.OnClickListener(){

            public void onClick(DialogInterface dialogInterface, int n) {
                ChangeLog.this.updateVersionInPreferences();
            }
        });
        if (!bl) {
            builder.setNegativeButton(R.string.changelog_show_full, new DialogInterface.OnClickListener(){

                public void onClick(DialogInterface dialogInterface, int n) {
                    ChangeLog.this.getFullLogDialog().show();
                }
            });
        }
        return builder.create();
    }

    public String getFullLog() {
        return this.getLog(true);
    }

    public AlertDialog getFullLogDialog() {
        return this.getDialog(true);
    }

    public int getLastVersionCode() {
        return this.mLastVersionCode;
    }

    protected SparseArray<ReleaseItem> getLocalizedChangeLog(boolean bl) {
        return this.readChangeLogFromResource(R.xml.changelog, bl);
    }

    public String getLog() {
        return this.getLog(false);
    }

    protected String getLog(boolean bl) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<html><head><style type=\"text/css\">");
        stringBuilder.append(this.mCss);
        stringBuilder.append("</style></head><body>");
        String string2 = this.mContext.getResources().getString(R.string.changelog_version_format);
        Iterator<ReleaseItem> iterator = this.getChangeLog(bl).iterator();
        while (iterator.hasNext()) {
            ReleaseItem releaseItem = iterator.next();
            stringBuilder.append("<h1>");
            stringBuilder.append(String.format(string2, releaseItem.versionName));
            stringBuilder.append("</h1><ul>");
            for (String string3 : releaseItem.changes) {
                stringBuilder.append("<li>");
                stringBuilder.append(string3);
                stringBuilder.append("</li>");
            }
            stringBuilder.append("</ul>");
        }
        stringBuilder.append("</body></html>");
        return stringBuilder.toString();
    }

    public AlertDialog getLogDialog() {
        return this.getDialog(this.isFirstRunEver());
    }

    protected SparseArray<ReleaseItem> getMasterChangeLog(boolean bl) {
        return this.readChangeLogFromResource(R.xml.changelog_master, bl);
    }

    public boolean isFirstRun() {
        if (this.mLastVersionCode < this.mCurrentVersionCode) {
            return true;
        }
        return false;
    }

    public boolean isFirstRunEver() {
        if (this.mLastVersionCode == -1) {
            return true;
        }
        return false;
    }

    protected SparseArray<ReleaseItem> readChangeLog(XmlPullParser xmlPullParser, boolean bl) {
        SparseArray sparseArray = new SparseArray();
        int n = xmlPullParser.getEventType();
        while (n != 1) {
            block7 : {
                if (n == 2) {
                    if (!xmlPullParser.getName().equals("release") || !this.parseReleaseTag(xmlPullParser, bl, sparseArray)) break block7;
                    return sparseArray;
                }
            }
            try {
                n = xmlPullParser.next();
            }
            catch (XmlPullParserException xmlPullParserException) {
                Log.e((String)"ckChangeLog", (String)xmlPullParserException.getMessage(), (Throwable)xmlPullParserException);
                return sparseArray;
            }
            catch (IOException iOException) {
                Log.e((String)"ckChangeLog", (String)iOException.getMessage(), (Throwable)iOException);
                break;
            }
        }
        return sparseArray;
    }

    protected final SparseArray<ReleaseItem> readChangeLogFromResource(int n, boolean bl) {
        XmlResourceParser xmlResourceParser = this.mContext.getResources().getXml(n);
        try {
            SparseArray<ReleaseItem> sparseArray = this.readChangeLog((XmlPullParser)xmlResourceParser, bl);
            return sparseArray;
        }
        finally {
            xmlResourceParser.close();
        }
    }

    public void skipLogDialog() {
        this.updateVersionInPreferences();
    }

    protected void updateVersionInPreferences() {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences((Context)this.mContext).edit();
        editor.putInt("ckChangeLog_last_version_code", this.mCurrentVersionCode);
        editor.commit();
    }

    protected static interface ChangeLogTag {
        public static final String NAME = "changelog";
    }

    protected static interface ChangeTag {
        public static final String NAME = "change";
    }

    public static class ReleaseItem {
        public final List<String> changes;
        public final int versionCode;
        public final String versionName;

        ReleaseItem(int n, String string2, List<String> list) {
            this.versionCode = n;
            this.versionName = string2;
            this.changes = list;
        }
    }

    protected static interface ReleaseTag {
        public static final String ATTRIBUTE_VERSION = "version";
        public static final String ATTRIBUTE_VERSION_CODE = "versioncode";
        public static final String NAME = "release";
    }

}

