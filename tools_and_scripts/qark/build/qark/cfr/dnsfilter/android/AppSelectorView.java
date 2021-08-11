/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.pm.ApplicationInfo
 *  android.content.pm.PackageManager
 *  android.content.pm.PackageManager$NameNotFoundException
 *  android.graphics.Bitmap
 *  android.graphics.Bitmap$Config
 *  android.graphics.Canvas
 *  android.graphics.drawable.BitmapDrawable
 *  android.graphics.drawable.Drawable
 *  android.util.AttributeSet
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.ViewGroup
 *  android.widget.CheckBox
 *  android.widget.LinearLayout
 *  android.widget.TextView
 */
package dnsfilter.android;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.List;
import java.util.TreeSet;

public class AppSelectorView
extends LinearLayout {
    private int corIconSize;
    private int iconSize;
    private boolean loaded;
    private PackageManager pm;
    private AsyncLoader runningUpdate;
    private String selectedApps;
    private ComparableAppInfoWrapper[] wrappers;

    public AppSelectorView(Context context) {
        super(context);
        this.pm = this.getContext().getPackageManager();
        this.loaded = false;
        this.selectedApps = "";
        this.iconSize = 0;
        this.corIconSize = 0;
        this.runningUpdate = null;
        this.wrappers = null;
    }

    public AppSelectorView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.pm = this.getContext().getPackageManager();
        this.loaded = false;
        this.selectedApps = "";
        this.iconSize = 0;
        this.corIconSize = 0;
        this.runningUpdate = null;
        this.wrappers = null;
    }

    public AppSelectorView(Context context, AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
        this.pm = this.getContext().getPackageManager();
        this.loaded = false;
        this.selectedApps = "";
        this.iconSize = 0;
        this.corIconSize = 0;
        this.runningUpdate = null;
        this.wrappers = null;
    }

    public AppSelectorView(Context context, AttributeSet attributeSet, int n, int n2) {
        super(context, attributeSet, n);
        this.pm = this.getContext().getPackageManager();
        this.loaded = false;
        this.selectedApps = "";
        this.iconSize = 0;
        this.corIconSize = 0;
        this.runningUpdate = null;
        this.wrappers = null;
    }

    static /* synthetic */ int access$100(AppSelectorView appSelectorView) {
        return appSelectorView.iconSize;
    }

    static /* synthetic */ boolean access$1002(AppSelectorView appSelectorView, boolean bl) {
        appSelectorView.loaded = bl;
        return bl;
    }

    static /* synthetic */ int access$102(AppSelectorView appSelectorView, int n) {
        appSelectorView.iconSize = n;
        return n;
    }

    static /* synthetic */ AsyncLoader access$1102(AppSelectorView appSelectorView, AsyncLoader asyncLoader) {
        appSelectorView.runningUpdate = asyncLoader;
        return asyncLoader;
    }

    static /* synthetic */ PackageManager access$200(AppSelectorView appSelectorView) {
        return appSelectorView.pm;
    }

    static /* synthetic */ Drawable access$300(AppSelectorView appSelectorView, Drawable drawable2, int n) {
        return appSelectorView.resizeDrawable(drawable2, n);
    }

    static /* synthetic */ int access$400(AppSelectorView appSelectorView) {
        return appSelectorView.corIconSize;
    }

    static /* synthetic */ int access$402(AppSelectorView appSelectorView, int n) {
        appSelectorView.corIconSize = n;
        return n;
    }

    static /* synthetic */ String access$500(AppSelectorView appSelectorView) {
        return appSelectorView.selectedApps;
    }

    static /* synthetic */ ComparableAppInfoWrapper[] access$700(AppSelectorView appSelectorView) {
        return appSelectorView.wrappers;
    }

    static /* synthetic */ ComparableAppInfoWrapper[] access$702(AppSelectorView appSelectorView, ComparableAppInfoWrapper[] arrcomparableAppInfoWrapper) {
        appSelectorView.wrappers = arrcomparableAppInfoWrapper;
        return arrcomparableAppInfoWrapper;
    }

    private Drawable resizeDrawable(Drawable drawable2, int n) {
        Bitmap bitmap = Bitmap.createBitmap((int)n, (int)n, (Bitmap.Config)Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable2.setBounds(0, 0, n, n);
        drawable2.draw(canvas);
        return new BitmapDrawable(bitmap);
    }

    public void clear() {
        AsyncLoader asyncLoader = this.runningUpdate;
        if (asyncLoader != null) {
            asyncLoader.abort();
            this.loaded = false;
            this.wrappers = null;
        }
        this.selectedApps = this.getSelectedAppPackages();
        this.wrappers = null;
        this.removeAllViews();
        this.loaded = false;
    }

    public String getSelectedAppPackages() {
        if (this.loaded && this.runningUpdate == null) {
            ComparableAppInfoWrapper[] arrcomparableAppInfoWrapper = this.wrappers;
            String string2 = "";
            String string3 = "";
            for (int i = 0; i < arrcomparableAppInfoWrapper.length; ++i) {
                String string4 = string2;
                CharSequence charSequence = string3;
                if (arrcomparableAppInfoWrapper[i].checkBox.isChecked()) {
                    charSequence = new StringBuilder();
                    charSequence.append(string2);
                    charSequence.append(string3);
                    charSequence.append(ComparableAppInfoWrapper.access$800((ComparableAppInfoWrapper)arrcomparableAppInfoWrapper[i]).packageName);
                    string4 = charSequence.toString();
                    charSequence = ", ";
                }
                string2 = string4;
                string3 = charSequence;
            }
            return string2;
        }
        return this.selectedApps;
    }

    public void loadAppList() {
        if (!this.loaded) {
            if (this.runningUpdate != null) {
                return;
            }
            this.runningUpdate = new AsyncLoader();
            new Thread(this.runningUpdate).start();
            return;
        }
    }

    public void setSelectedApps(String string2) {
        this.selectedApps = string2;
    }

    private class AsyncLoader
    implements Runnable {
        private boolean abort;

        private AsyncLoader() {
            this.abort = false;
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        public void abort() {
            this.abort = true;
            synchronized (this) {
                AsyncLoader asyncLoader;
                while ((asyncLoader = AppSelectorView.this.runningUpdate) != null) {
                    try {
                        this.wait();
                    }
                    catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                        continue;
                    }
                    break;
                }
                return;
            }
        }

        /*
         * Unable to fully structure code
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         * Converted monitor instructions to comments
         * Lifted jumps to return sites
         */
        @Override
        public void run() {
            block15 : {
                // MONITORENTER : this
                var4_1 = this.abort;
                if (var4_1) {
                    // MONITOREXIT : this
                    return;
                }
                try {
                    var5_2 = new TextView(AppSelectorView.this.getContext());
                    var5_2.setTextColor(-16777216);
                    var5_2.setText((CharSequence)"Loading Apps...");
                    AppSelectorView.this.post(new Runnable((TextView)var5_2){
                        final /* synthetic */ TextView val$infoText;
                        {
                            this.val$infoText = textView;
                        }

                        @Override
                        public void run() {
                            AppSelectorView.this.addView((View)this.val$infoText);
                        }
                    });
                    var2_4 = AppSelectorView.access$100(AppSelectorView.this);
                    if (var2_4 == 0) {
                        try {
                            var6_5 /* !! */  = AppSelectorView.access$200(AppSelectorView.this).getApplicationIcon("dnsfilter.android");
                            AppSelectorView.access$102(AppSelectorView.this, var6_5 /* !! */ .getIntrinsicWidth());
                            var6_5 /* !! */  = AppSelectorView.access$300(AppSelectorView.this, (Drawable)var6_5 /* !! */ , AppSelectorView.access$100(AppSelectorView.this));
                            var1_7 = (float)AppSelectorView.access$100(AppSelectorView.this) / (float)var6_5 /* !! */ .getIntrinsicWidth();
                            AppSelectorView.access$402(AppSelectorView.this, (int)((float)AppSelectorView.access$100(AppSelectorView.this) * var1_7));
                        }
                        catch (PackageManager.NameNotFoundException var6_6) {
                            var6_6.printStackTrace();
                        }
                    }
                    var6_5 /* !! */  = new StringBuilder();
                    var6_5 /* !! */ .append(",");
                    var6_5 /* !! */ .append(AppSelectorView.access$500(AppSelectorView.this));
                    var6_5 /* !! */ .append(",");
                    var6_5 /* !! */  = var6_5 /* !! */ .toString().replace(" ", "");
                    var7_8 = AppSelectorView.access$200(AppSelectorView.this).getInstalledApplications(128);
                    var3_9 = 0;
                    var7_8 = var7_8.toArray((T[])new ApplicationInfo[0]);
                    var8_10 = new TreeSet<ComparableAppInfoWrapper>();
                    for (var2_4 = 0; var2_4 < var7_8.length && !this.abort; ++var2_4) {
                        var9_11 = (CheckBox)LayoutInflater.from((Context)AppSelectorView.this.getContext()).inflate(2130903040, null);
                        var10_12 = new StringBuilder();
                        var10_12.append(",");
                        var10_12.append(var7_8[var2_4].packageName);
                        var10_12.append(",");
                        var9_11.setChecked(var6_5 /* !! */ .contains(var10_12.toString()));
                        var10_12 = new StringBuilder();
                        var10_12.append((Object)var7_8[var2_4].loadLabel(AppSelectorView.access$200(AppSelectorView.this)));
                        var10_12.append("\n");
                        var10_12.append(var7_8[var2_4].packageName);
                        var9_11.setText((CharSequence)var10_12.toString());
                        var8_10.add(new ComparableAppInfoWrapper(var7_8[var2_4], var9_11));
                    }
                    AppSelectorView.this.post(new Runnable((TextView)var5_2){
                        final /* synthetic */ TextView val$infoText;
                        {
                            this.val$infoText = textView;
                        }

                        @Override
                        public void run() {
                            AppSelectorView.this.removeView((View)this.val$infoText);
                        }
                    });
                    AppSelectorView.access$702(AppSelectorView.this, var8_10.toArray(new ComparableAppInfoWrapper[0]));
                    break block15;
                }
                catch (Throwable var5_3) {}
                throw var5_3;
            }
            for (var2_4 = var3_9; var2_4 < AppSelectorView.access$700(AppSelectorView.this).length && !this.abort; ++var2_4) {
                var6_5 /* !! */  = ComparableAppInfoWrapper.access$800(AppSelectorView.access$700(AppSelectorView.this)[var2_4]).loadIcon(AppSelectorView.access$200(AppSelectorView.this));
                var5_2 = var6_5 /* !! */ ;
                if (var6_5 /* !! */ .getIntrinsicWidth() != AppSelectorView.access$100(AppSelectorView.this)) {
                    var5_2 = AppSelectorView.access$300(AppSelectorView.this, (Drawable)var6_5 /* !! */ , AppSelectorView.access$400(AppSelectorView.this));
                }
                AppSelectorView.this.post((Runnable)new UIUpdate(AppSelectorView.access$700((AppSelectorView)AppSelectorView.this)[var2_4].checkBox, (Drawable)var5_2, this));
            }
            ** try [egrp 4[TRYBLOCK] [12 : 607->621)] { 
lbl59: // 1 sources:
            AppSelectorView.access$1002(AppSelectorView.this, this.abort ^ true);
            return;
lbl61: // 1 sources:
            finally {
                AppSelectorView.access$1102(AppSelectorView.this, null);
                this.notifyAll();
            }
        }

    }

    private class ComparableAppInfoWrapper
    implements Comparable<ComparableAppInfoWrapper> {
        private String appName;
        CheckBox checkBox;
        private ApplicationInfo wrapped;

        private ComparableAppInfoWrapper(ApplicationInfo applicationInfo, CheckBox checkBox) {
            this.appName = null;
            this.wrapped = null;
            this.checkBox = null;
            this.appName = checkBox.getText().toString();
            this.wrapped = applicationInfo;
            this.checkBox = checkBox;
        }

        static /* synthetic */ ApplicationInfo access$800(ComparableAppInfoWrapper comparableAppInfoWrapper) {
            return comparableAppInfoWrapper.wrapped;
        }

        @Override
        public int compareTo(ComparableAppInfoWrapper comparableAppInfoWrapper) {
            return this.appName.toUpperCase().compareTo(comparableAppInfoWrapper.appName.toUpperCase());
        }
    }

    private class UIUpdate
    implements Runnable {
        private CheckBox checkBox;
        private Drawable icon;
        AsyncLoader update;

        private UIUpdate(CheckBox checkBox, Drawable drawable2, AsyncLoader asyncLoader) {
            this.checkBox = checkBox;
            this.icon = drawable2;
            this.update = asyncLoader;
        }

        @Override
        public void run() {
            if (this.update.abort) {
                return;
            }
            this.checkBox.setCompoundDrawablesWithIntrinsicBounds(null, null, this.icon, null);
            AppSelectorView.this.addView((View)this.checkBox);
        }
    }

}

