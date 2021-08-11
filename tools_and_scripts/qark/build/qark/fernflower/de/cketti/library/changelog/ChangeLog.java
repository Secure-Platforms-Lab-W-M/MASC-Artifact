package de.cketti.library.changelog;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.SparseArray;
import android.webkit.WebView;
import de.cketti.library.changelog.R.string;
import de.cketti.library.changelog.R.xml;
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

   public ChangeLog(Context var1) {
      this(var1, PreferenceManager.getDefaultSharedPreferences(var1), "h1 { margin-left: 0px; font-size: 1.2em; }\nli { margin-left: 0px; }\nul { padding-left: 2em; }");
   }

   public ChangeLog(Context var1, SharedPreferences var2, String var3) {
      this.mContext = var1;
      this.mCss = var3;
      this.mLastVersionCode = var2.getInt("ckChangeLog_last_version_code", -1);

      try {
         PackageInfo var5 = var1.getPackageManager().getPackageInfo(var1.getPackageName(), 0);
         this.mCurrentVersionCode = var5.versionCode;
         this.mCurrentVersionName = var5.versionName;
      } catch (NameNotFoundException var4) {
         this.mCurrentVersionCode = -1;
         Log.e("ckChangeLog", "Could not get version information from manifest!", var4);
      }
   }

   public ChangeLog(Context var1, String var2) {
      this(var1, PreferenceManager.getDefaultSharedPreferences(var1), var2);
   }

   private boolean parseReleaseTag(XmlPullParser var1, boolean var2, SparseArray var3) throws XmlPullParserException, IOException {
      String var6 = var1.getAttributeValue((String)null, "version");

      int var4;
      try {
         var4 = Integer.parseInt(var1.getAttributeValue((String)null, "versioncode"));
      } catch (NumberFormatException var8) {
         var4 = -1;
      }

      if (!var2 && var4 <= this.mLastVersionCode) {
         return true;
      } else {
         int var5 = var1.getEventType();

         ArrayList var7;
         for(var7 = new ArrayList(); var5 != 3 || var1.getName().equals("change"); var5 = var1.next()) {
            if (var5 == 2 && var1.getName().equals("change")) {
               var1.next();
               var7.add(var1.getText());
            }
         }

         var3.put(var4, new ChangeLog.ReleaseItem(var4, var6, var7));
         return false;
      }
   }

   public List getChangeLog(boolean var1) {
      SparseArray var5 = this.getMasterChangeLog(var1);
      SparseArray var6 = this.getLocalizedChangeLog(var1);
      ArrayList var7 = new ArrayList(var5.size());
      int var2 = 0;

      for(int var3 = var5.size(); var2 < var3; ++var2) {
         int var4 = var5.keyAt(var2);
         var7.add((ChangeLog.ReleaseItem)var6.get(var4, var5.get(var4)));
      }

      Collections.sort(var7, this.getChangeLogComparator());
      return var7;
   }

   protected Comparator getChangeLogComparator() {
      return new Comparator() {
         public int compare(ChangeLog.ReleaseItem var1, ChangeLog.ReleaseItem var2) {
            if (var1.versionCode < var2.versionCode) {
               return 1;
            } else {
               return var1.versionCode > var2.versionCode ? -1 : 0;
            }
         }
      };
   }

   public int getCurrentVersionCode() {
      return this.mCurrentVersionCode;
   }

   public String getCurrentVersionName() {
      return this.mCurrentVersionName;
   }

   protected AlertDialog getDialog(boolean var1) {
      WebView var3 = new WebView(this.mContext);
      var3.loadDataWithBaseURL((String)null, this.getLog(var1), "text/html", "UTF-8", (String)null);
      Builder var4 = new Builder(this.mContext);
      Resources var5 = this.mContext.getResources();
      int var2;
      if (var1) {
         var2 = string.changelog_full_title;
      } else {
         var2 = string.changelog_title;
      }

      var4.setTitle(var5.getString(var2)).setView(var3).setCancelable(false).setPositiveButton(this.mContext.getResources().getString(string.changelog_ok_button), new OnClickListener() {
         public void onClick(DialogInterface var1, int var2) {
            ChangeLog.this.updateVersionInPreferences();
         }
      });
      if (!var1) {
         var4.setNegativeButton(string.changelog_show_full, new OnClickListener() {
            public void onClick(DialogInterface var1, int var2) {
               ChangeLog.this.getFullLogDialog().show();
            }
         });
      }

      return var4.create();
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

   protected SparseArray getLocalizedChangeLog(boolean var1) {
      return this.readChangeLogFromResource(xml.changelog, var1);
   }

   public String getLog() {
      return this.getLog(false);
   }

   protected String getLog(boolean var1) {
      StringBuilder var2 = new StringBuilder();
      var2.append("<html><head><style type=\"text/css\">");
      var2.append(this.mCss);
      var2.append("</style></head><body>");
      String var3 = this.mContext.getResources().getString(string.changelog_version_format);
      Iterator var4 = this.getChangeLog(var1).iterator();

      while(var4.hasNext()) {
         ChangeLog.ReleaseItem var5 = (ChangeLog.ReleaseItem)var4.next();
         var2.append("<h1>");
         var2.append(String.format(var3, var5.versionName));
         var2.append("</h1><ul>");
         Iterator var7 = var5.changes.iterator();

         while(var7.hasNext()) {
            String var6 = (String)var7.next();
            var2.append("<li>");
            var2.append(var6);
            var2.append("</li>");
         }

         var2.append("</ul>");
      }

      var2.append("</body></html>");
      return var2.toString();
   }

   public AlertDialog getLogDialog() {
      return this.getDialog(this.isFirstRunEver());
   }

   protected SparseArray getMasterChangeLog(boolean var1) {
      return this.readChangeLogFromResource(xml.changelog_master, var1);
   }

   public boolean isFirstRun() {
      return this.mLastVersionCode < this.mCurrentVersionCode;
   }

   public boolean isFirstRunEver() {
      return this.mLastVersionCode == -1;
   }

   protected SparseArray readChangeLog(XmlPullParser var1, boolean var2) {
      SparseArray var4 = new SparseArray();

      XmlPullParserException var13;
      label44: {
         IOException var10000;
         label43: {
            int var3;
            boolean var10001;
            try {
               var3 = var1.getEventType();
            } catch (XmlPullParserException var9) {
               var13 = var9;
               var10001 = false;
               break label44;
            } catch (IOException var10) {
               var10000 = var10;
               var10001 = false;
               break label43;
            }

            while(true) {
               if (var3 == 1) {
                  return var4;
               }

               if (var3 == 2) {
                  try {
                     if (var1.getName().equals("release") && this.parseReleaseTag(var1, var2, var4)) {
                        return var4;
                     }
                  } catch (XmlPullParserException var7) {
                     var13 = var7;
                     var10001 = false;
                     break label44;
                  } catch (IOException var8) {
                     var10000 = var8;
                     var10001 = false;
                     break;
                  }
               }

               try {
                  var3 = var1.next();
               } catch (XmlPullParserException var5) {
                  var13 = var5;
                  var10001 = false;
                  break label44;
               } catch (IOException var6) {
                  var10000 = var6;
                  var10001 = false;
                  break;
               }
            }
         }

         IOException var11 = var10000;
         Log.e("ckChangeLog", var11.getMessage(), var11);
         return var4;
      }

      XmlPullParserException var12 = var13;
      Log.e("ckChangeLog", var12.getMessage(), var12);
      return var4;
   }

   protected final SparseArray readChangeLogFromResource(int var1, boolean var2) {
      XmlResourceParser var3 = this.mContext.getResources().getXml(var1);

      SparseArray var4;
      try {
         var4 = this.readChangeLog(var3, var2);
      } finally {
         var3.close();
      }

      return var4;
   }

   public void skipLogDialog() {
      this.updateVersionInPreferences();
   }

   protected void updateVersionInPreferences() {
      Editor var1 = PreferenceManager.getDefaultSharedPreferences(this.mContext).edit();
      var1.putInt("ckChangeLog_last_version_code", this.mCurrentVersionCode);
      var1.commit();
   }

   protected interface ChangeLogTag {
      String NAME = "changelog";
   }

   protected interface ChangeTag {
      String NAME = "change";
   }

   public static class ReleaseItem {
      public final List changes;
      public final int versionCode;
      public final String versionName;

      ReleaseItem(int var1, String var2, List var3) {
         this.versionCode = var1;
         this.versionName = var2;
         this.changes = var3;
      }
   }

   protected interface ReleaseTag {
      String ATTRIBUTE_VERSION = "version";
      String ATTRIBUTE_VERSION_CODE = "versioncode";
      String NAME = "release";
   }
}
