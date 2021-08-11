// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package de.cketti.library.changelog;

import android.content.SharedPreferences$Editor;
import android.content.res.XmlResourceParser;
import java.util.Iterator;
import android.content.res.Resources;
import android.content.DialogInterface;
import android.content.DialogInterface$OnClickListener;
import android.view.View;
import android.app.AlertDialog$Builder;
import android.webkit.WebView;
import android.app.AlertDialog;
import java.util.Comparator;
import java.util.Collections;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParserException;
import java.util.List;
import java.util.ArrayList;
import android.util.SparseArray;
import org.xmlpull.v1.XmlPullParser;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager$NameNotFoundException;
import android.util.Log;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.content.Context;

public class ChangeLog
{
    public static final String DEFAULT_CSS = "h1 { margin-left: 0px; font-size: 1.2em; }\nli { margin-left: 0px; }\nul { padding-left: 2em; }";
    protected static final String LOG_TAG = "ckChangeLog";
    protected static final int NO_VERSION = -1;
    protected static final String VERSION_KEY = "ckChangeLog_last_version_code";
    protected final Context mContext;
    protected final String mCss;
    private int mCurrentVersionCode;
    private String mCurrentVersionName;
    private int mLastVersionCode;
    
    public ChangeLog(final Context context) {
        this(context, PreferenceManager.getDefaultSharedPreferences(context), "h1 { margin-left: 0px; font-size: 1.2em; }\nli { margin-left: 0px; }\nul { padding-left: 2em; }");
    }
    
    public ChangeLog(final Context mContext, final SharedPreferences sharedPreferences, final String mCss) {
        this.mContext = mContext;
        this.mCss = mCss;
        this.mLastVersionCode = sharedPreferences.getInt("ckChangeLog_last_version_code", -1);
        try {
            final PackageInfo packageInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            this.mCurrentVersionCode = packageInfo.versionCode;
            this.mCurrentVersionName = packageInfo.versionName;
        }
        catch (PackageManager$NameNotFoundException ex) {
            this.mCurrentVersionCode = -1;
            Log.e("ckChangeLog", "Could not get version information from manifest!", (Throwable)ex);
        }
    }
    
    public ChangeLog(final Context context, final String s) {
        this(context, PreferenceManager.getDefaultSharedPreferences(context), s);
    }
    
    private boolean parseReleaseTag(final XmlPullParser xmlPullParser, final boolean b, final SparseArray<ReleaseItem> sparseArray) throws XmlPullParserException, IOException {
        final String attributeValue = xmlPullParser.getAttributeValue((String)null, "version");
        int int1;
        while (true) {
            try {
                int1 = Integer.parseInt(xmlPullParser.getAttributeValue((String)null, "versioncode"));
                if (!b && int1 <= this.mLastVersionCode) {
                    return true;
                }
            }
            catch (NumberFormatException ex) {
                int1 = -1;
                continue;
            }
            break;
        }
        int n = xmlPullParser.getEventType();
        final ArrayList<String> list = new ArrayList<String>();
        while (n != 3 || xmlPullParser.getName().equals("change")) {
            if (n == 2 && xmlPullParser.getName().equals("change")) {
                xmlPullParser.next();
                list.add(xmlPullParser.getText());
            }
            n = xmlPullParser.next();
        }
        sparseArray.put(int1, (Object)new ReleaseItem(int1, attributeValue, list));
        return false;
    }
    
    public List<ReleaseItem> getChangeLog(final boolean b) {
        final SparseArray<ReleaseItem> masterChangeLog = this.getMasterChangeLog(b);
        final SparseArray<ReleaseItem> localizedChangeLog = this.getLocalizedChangeLog(b);
        final ArrayList list = new ArrayList<Object>(masterChangeLog.size());
        for (int i = 0; i < masterChangeLog.size(); ++i) {
            final int key = masterChangeLog.keyAt(i);
            list.add((ReleaseItem)localizedChangeLog.get(key, masterChangeLog.get(key)));
        }
        Collections.sort((List<Object>)list, (Comparator<? super Object>)this.getChangeLogComparator());
        return (List<ReleaseItem>)list;
    }
    
    protected Comparator<ReleaseItem> getChangeLogComparator() {
        return new Comparator<ReleaseItem>() {
            @Override
            public int compare(final ReleaseItem releaseItem, final ReleaseItem releaseItem2) {
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
    
    protected AlertDialog getDialog(final boolean b) {
        final WebView view = new WebView(this.mContext);
        view.loadDataWithBaseURL((String)null, this.getLog(b), "text/html", "UTF-8", (String)null);
        final AlertDialog$Builder alertDialog$Builder = new AlertDialog$Builder(this.mContext);
        final Resources resources = this.mContext.getResources();
        int n;
        if (b) {
            n = R.string.changelog_full_title;
        }
        else {
            n = R.string.changelog_title;
        }
        alertDialog$Builder.setTitle((CharSequence)resources.getString(n)).setView((View)view).setCancelable(false).setPositiveButton((CharSequence)this.mContext.getResources().getString(R.string.changelog_ok_button), (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
            public void onClick(final DialogInterface dialogInterface, final int n) {
                ChangeLog.this.updateVersionInPreferences();
            }
        });
        if (!b) {
            alertDialog$Builder.setNegativeButton(R.string.changelog_show_full, (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                public void onClick(final DialogInterface dialogInterface, final int n) {
                    ChangeLog.this.getFullLogDialog().show();
                }
            });
        }
        return alertDialog$Builder.create();
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
    
    protected SparseArray<ReleaseItem> getLocalizedChangeLog(final boolean b) {
        return this.readChangeLogFromResource(R.xml.changelog, b);
    }
    
    public String getLog() {
        return this.getLog(false);
    }
    
    protected String getLog(final boolean b) {
        final StringBuilder sb = new StringBuilder();
        sb.append("<html><head><style type=\"text/css\">");
        sb.append(this.mCss);
        sb.append("</style></head><body>");
        final String string = this.mContext.getResources().getString(R.string.changelog_version_format);
        for (final ReleaseItem releaseItem : this.getChangeLog(b)) {
            sb.append("<h1>");
            sb.append(String.format(string, releaseItem.versionName));
            sb.append("</h1><ul>");
            for (final String s : releaseItem.changes) {
                sb.append("<li>");
                sb.append(s);
                sb.append("</li>");
            }
            sb.append("</ul>");
        }
        sb.append("</body></html>");
        return sb.toString();
    }
    
    public AlertDialog getLogDialog() {
        return this.getDialog(this.isFirstRunEver());
    }
    
    protected SparseArray<ReleaseItem> getMasterChangeLog(final boolean b) {
        return this.readChangeLogFromResource(R.xml.changelog_master, b);
    }
    
    public boolean isFirstRun() {
        return this.mLastVersionCode < this.mCurrentVersionCode;
    }
    
    public boolean isFirstRunEver() {
        return this.mLastVersionCode == -1;
    }
    
    protected SparseArray<ReleaseItem> readChangeLog(final XmlPullParser xmlPullParser, final boolean b) {
        final SparseArray sparseArray = new SparseArray();
        try {
            for (int i = xmlPullParser.getEventType(); i != 1; i = xmlPullParser.next()) {
                if (i == 2 && xmlPullParser.getName().equals("release") && this.parseReleaseTag(xmlPullParser, b, (SparseArray<ReleaseItem>)sparseArray)) {
                    return (SparseArray<ReleaseItem>)sparseArray;
                }
            }
            return (SparseArray<ReleaseItem>)sparseArray;
        }
        catch (XmlPullParserException ex) {
            Log.e("ckChangeLog", ex.getMessage(), (Throwable)ex);
            return (SparseArray<ReleaseItem>)sparseArray;
        }
        catch (IOException ex2) {
            Log.e("ckChangeLog", ex2.getMessage(), (Throwable)ex2);
        }
        return (SparseArray<ReleaseItem>)sparseArray;
    }
    
    protected final SparseArray<ReleaseItem> readChangeLogFromResource(final int n, final boolean b) {
        final XmlResourceParser xml = this.mContext.getResources().getXml(n);
        try {
            return this.readChangeLog((XmlPullParser)xml, b);
        }
        finally {
            xml.close();
        }
    }
    
    public void skipLogDialog() {
        this.updateVersionInPreferences();
    }
    
    protected void updateVersionInPreferences() {
        final SharedPreferences$Editor edit = PreferenceManager.getDefaultSharedPreferences(this.mContext).edit();
        edit.putInt("ckChangeLog_last_version_code", this.mCurrentVersionCode);
        edit.commit();
    }
    
    protected interface ChangeLogTag
    {
        public static final String NAME = "changelog";
    }
    
    protected interface ChangeTag
    {
        public static final String NAME = "change";
    }
    
    public static class ReleaseItem
    {
        public final List<String> changes;
        public final int versionCode;
        public final String versionName;
        
        ReleaseItem(final int versionCode, final String versionName, final List<String> changes) {
            this.versionCode = versionCode;
            this.versionName = versionName;
            this.changes = changes;
        }
    }
    
    protected interface ReleaseTag
    {
        public static final String ATTRIBUTE_VERSION = "version";
        public static final String ATTRIBUTE_VERSION_CODE = "versioncode";
        public static final String NAME = "release";
    }
}
