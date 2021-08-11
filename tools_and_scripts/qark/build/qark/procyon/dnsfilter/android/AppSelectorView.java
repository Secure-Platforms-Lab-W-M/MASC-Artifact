// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package dnsfilter.android;

import java.util.List;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import java.util.TreeSet;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager$NameNotFoundException;
import android.view.View;
import android.widget.TextView;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.Canvas;
import android.graphics.Bitmap;
import android.graphics.Bitmap$Config;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.content.Context;
import android.content.pm.PackageManager;
import android.widget.LinearLayout;

public class AppSelectorView extends LinearLayout
{
    private int corIconSize;
    private int iconSize;
    private boolean loaded;
    private PackageManager pm;
    private AsyncLoader runningUpdate;
    private String selectedApps;
    private ComparableAppInfoWrapper[] wrappers;
    
    public AppSelectorView(final Context context) {
        super(context);
        this.pm = this.getContext().getPackageManager();
        this.loaded = false;
        this.selectedApps = "";
        this.iconSize = 0;
        this.corIconSize = 0;
        this.runningUpdate = null;
        this.wrappers = null;
    }
    
    public AppSelectorView(final Context context, final AttributeSet set) {
        super(context, set);
        this.pm = this.getContext().getPackageManager();
        this.loaded = false;
        this.selectedApps = "";
        this.iconSize = 0;
        this.corIconSize = 0;
        this.runningUpdate = null;
        this.wrappers = null;
    }
    
    public AppSelectorView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.pm = this.getContext().getPackageManager();
        this.loaded = false;
        this.selectedApps = "";
        this.iconSize = 0;
        this.corIconSize = 0;
        this.runningUpdate = null;
        this.wrappers = null;
    }
    
    public AppSelectorView(final Context context, final AttributeSet set, final int n, final int n2) {
        super(context, set, n);
        this.pm = this.getContext().getPackageManager();
        this.loaded = false;
        this.selectedApps = "";
        this.iconSize = 0;
        this.corIconSize = 0;
        this.runningUpdate = null;
        this.wrappers = null;
    }
    
    private Drawable resizeDrawable(final Drawable drawable, final int n) {
        final Bitmap bitmap = Bitmap.createBitmap(n, n, Bitmap$Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, n, n);
        drawable.draw(canvas);
        return (Drawable)new BitmapDrawable(bitmap);
    }
    
    public void clear() {
        final AsyncLoader runningUpdate = this.runningUpdate;
        if (runningUpdate != null) {
            runningUpdate.abort();
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
            final ComparableAppInfoWrapper[] wrappers = this.wrappers;
            String s = "";
            String s2 = "";
            String string;
            String s3;
            for (int i = 0; i < wrappers.length; ++i, s = string, s2 = s3) {
                string = s;
                s3 = s2;
                if (wrappers[i].checkBox.isChecked()) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append(s);
                    sb.append(s2);
                    sb.append(wrappers[i].wrapped.packageName);
                    string = sb.toString();
                    s3 = ", ";
                }
            }
            return s;
        }
        return this.selectedApps;
    }
    
    public void loadAppList() {
        if (this.loaded) {
            return;
        }
        if (this.runningUpdate != null) {
            return;
        }
        this.runningUpdate = new AsyncLoader();
        new Thread(this.runningUpdate).start();
    }
    
    public void setSelectedApps(final String selectedApps) {
        this.selectedApps = selectedApps;
    }
    
    private class AsyncLoader implements Runnable
    {
        private boolean abort;
        
        private AsyncLoader() {
            this.abort = false;
        }
        
        public void abort() {
            this.abort = true;
            synchronized (this) {
            Label_0023_Outer:
                while (AppSelectorView.this.runningUpdate != null) {
                    while (true) {
                        try {
                            this.wait();
                            continue Label_0023_Outer;
                        }
                        catch (InterruptedException ex) {
                            ex.printStackTrace();
                            continue;
                        }
                        break;
                    }
                    break;
                }
            }
        }
        
        @Override
        public void run() {
            synchronized (this) {
                if (this.abort) {
                    return;
                }
                try {
                    final TextView textView = new TextView(AppSelectorView.this.getContext());
                    textView.setTextColor(-16777216);
                    textView.setText((CharSequence)"Loading Apps...");
                    AppSelectorView.this.post((Runnable)new Runnable() {
                        @Override
                        public void run() {
                            AppSelectorView.this.addView((View)textView);
                        }
                    });
                    if (AppSelectorView.this.iconSize == 0) {
                        try {
                            final Drawable applicationIcon = AppSelectorView.this.pm.getApplicationIcon("dnsfilter.android");
                            AppSelectorView.this.iconSize = applicationIcon.getIntrinsicWidth();
                            AppSelectorView.this.corIconSize = (int)(AppSelectorView.this.iconSize * (AppSelectorView.this.iconSize / (float)AppSelectorView.this.resizeDrawable(applicationIcon, AppSelectorView.this.iconSize).getIntrinsicWidth()));
                        }
                        catch (PackageManager$NameNotFoundException ex) {
                            ex.printStackTrace();
                        }
                    }
                    final StringBuilder sb = new StringBuilder();
                    sb.append(",");
                    sb.append(AppSelectorView.this.selectedApps);
                    sb.append(",");
                    final String replace = sb.toString().replace(" ", "");
                    final List installedApplications = AppSelectorView.this.pm.getInstalledApplications(128);
                    final int n = 0;
                    final ApplicationInfo[] array = installedApplications.toArray(new ApplicationInfo[0]);
                    final TreeSet<ComparableAppInfoWrapper> set = new TreeSet<ComparableAppInfoWrapper>();
                    for (int n2 = 0; n2 < array.length && !this.abort; ++n2) {
                        final CheckBox checkBox = (CheckBox)LayoutInflater.from(AppSelectorView.this.getContext()).inflate(2130903040, (ViewGroup)null);
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append(",");
                        sb2.append(array[n2].packageName);
                        sb2.append(",");
                        checkBox.setChecked(replace.contains(sb2.toString()));
                        final StringBuilder sb3 = new StringBuilder();
                        sb3.append((Object)array[n2].loadLabel(AppSelectorView.this.pm));
                        sb3.append("\n");
                        sb3.append(array[n2].packageName);
                        checkBox.setText((CharSequence)sb3.toString());
                        set.add(new ComparableAppInfoWrapper(array[n2], checkBox));
                    }
                    AppSelectorView.this.post((Runnable)new Runnable() {
                        @Override
                        public void run() {
                            AppSelectorView.this.removeView((View)textView);
                        }
                    });
                    AppSelectorView.this.wrappers = (ComparableAppInfoWrapper[])set.toArray(new ComparableAppInfoWrapper[0]);
                    for (int n3 = n; n3 < AppSelectorView.this.wrappers.length && !this.abort; ++n3) {
                        Drawable drawable2;
                        final Drawable drawable = drawable2 = AppSelectorView.this.wrappers[n3].wrapped.loadIcon(AppSelectorView.this.pm);
                        if (drawable.getIntrinsicWidth() != AppSelectorView.this.iconSize) {
                            drawable2 = AppSelectorView.this.resizeDrawable(drawable, AppSelectorView.this.corIconSize);
                        }
                        AppSelectorView.this.post((Runnable)new UIUpdate(AppSelectorView.this.wrappers[n3].checkBox, drawable2, this));
                    }
                    AppSelectorView.this.loaded = (this.abort ^ true);
                }
                finally {
                    AppSelectorView.this.runningUpdate = null;
                    this.notifyAll();
                }
            }
        }
    }
    
    private class ComparableAppInfoWrapper implements Comparable<ComparableAppInfoWrapper>
    {
        private String appName;
        CheckBox checkBox;
        private ApplicationInfo wrapped;
        
        private ComparableAppInfoWrapper(final ApplicationInfo wrapped, final CheckBox checkBox) {
            this.appName = null;
            this.wrapped = null;
            this.checkBox = null;
            this.appName = checkBox.getText().toString();
            this.wrapped = wrapped;
            this.checkBox = checkBox;
        }
        
        @Override
        public int compareTo(final ComparableAppInfoWrapper comparableAppInfoWrapper) {
            return this.appName.toUpperCase().compareTo(comparableAppInfoWrapper.appName.toUpperCase());
        }
    }
    
    private class UIUpdate implements Runnable
    {
        private CheckBox checkBox;
        private Drawable icon;
        AsyncLoader update;
        
        private UIUpdate(final CheckBox checkBox, final Drawable icon, final AsyncLoader update) {
            this.checkBox = checkBox;
            this.icon = icon;
            this.update = update;
        }
        
        @Override
        public void run() {
            if (this.update.abort) {
                return;
            }
            this.checkBox.setCompoundDrawablesWithIntrinsicBounds((Drawable)null, (Drawable)null, this.icon, (Drawable)null);
            AppSelectorView.this.addView((View)this.checkBox);
        }
    }
}
