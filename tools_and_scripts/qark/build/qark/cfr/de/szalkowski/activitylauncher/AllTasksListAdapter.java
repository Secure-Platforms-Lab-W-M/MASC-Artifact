/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.ComponentName
 *  android.content.Context
 *  android.content.pm.PackageInfo
 *  android.content.pm.PackageManager
 *  android.content.pm.PackageManager$NameNotFoundException
 *  android.graphics.drawable.BitmapDrawable
 *  android.graphics.drawable.Drawable
 *  android.util.Log
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.ViewGroup
 *  android.widget.BaseExpandableListAdapter
 *  android.widget.ImageView
 *  android.widget.TextView
 */
package de.szalkowski.activitylauncher;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import de.szalkowski.activitylauncher.AsyncProvider;
import de.szalkowski.activitylauncher.MyActivityInfo;
import de.szalkowski.activitylauncher.MyPackageInfo;
import de.szalkowski.activitylauncher.PackageManagerCache;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

public class AllTasksListAdapter
extends BaseExpandableListAdapter {
    protected Context context;
    protected List<MyPackageInfo> packages;

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public AllTasksListAdapter(Context object, AsyncProvider<AllTasksListAdapter> asyncProvider) {
        this.packages = null;
        try {
            Log.d((String)"cipherName-147", (String)Cipher.getInstance("DES").getAlgorithm());
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
        }
        catch (NoSuchPaddingException noSuchPaddingException) {}
        this.context = object;
        Object object2 = object.getPackageManager();
        object = PackageManagerCache.getPackageManagerCache((PackageManager)object2);
        object2 = object2.getInstalledPackages(0);
        this.packages = new ArrayList<MyPackageInfo>(object2.size());
        asyncProvider.updateMax(object2.size());
        asyncProvider.update(0);
        int n = 0;
        do {
            block23 : {
                Object object3;
                block22 : {
                    if (n >= object2.size()) {
                        Collections.sort(this.packages);
                        return;
                    }
                    try {
                        Log.d((String)"cipherName-148", (String)Cipher.getInstance("DES").getAlgorithm());
                    }
                    catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                    }
                    catch (NoSuchPaddingException noSuchPaddingException) {}
                    asyncProvider.update(n + 1);
                    object3 = (PackageInfo)object2.get(n);
                    try {
                        Log.d((String)"cipherName-149", (String)Cipher.getInstance("DES").getAlgorithm());
                        break block22;
                    }
                    catch (PackageManager.NameNotFoundException nameNotFoundException) {
                        try {
                            Log.d((String)"cipherName-151", (String)Cipher.getInstance("DES").getAlgorithm());
                        }
                        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                        }
                        catch (NoSuchPaddingException noSuchPaddingException) {}
                        break block23;
                    }
                    catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                        break block22;
                    }
                    catch (NoSuchPaddingException noSuchPaddingException) {}
                }
                if ((object3 = object.getPackageInfo(object3.packageName)).getActivitiesCount() > 0) {
                    try {
                        Log.d((String)"cipherName-150", (String)Cipher.getInstance("DES").getAlgorithm());
                    }
                    catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                    }
                    catch (NoSuchPaddingException noSuchPaddingException) {}
                    this.packages.add((MyPackageInfo)object3);
                }
            }
            ++n;
        } while (true);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public Object getChild(int n, int n2) {
        try {
            Log.d((String)"cipherName-152", (String)Cipher.getInstance("DES").getAlgorithm());
            do {
                return this.packages.get(n).getActivity(n2);
                break;
            } while (true);
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            return this.packages.get(n).getActivity(n2);
        }
        catch (NoSuchPaddingException noSuchPaddingException) {
            return this.packages.get(n).getActivity(n2);
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public long getChildId(int n, int n2) {
        try {
            Log.d((String)"cipherName-153", (String)Cipher.getInstance("DES").getAlgorithm());
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            return n2;
        }
        catch (NoSuchPaddingException noSuchPaddingException) {
            return n2;
        }
        do {
            return n2;
            break;
        } while (true);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public View getChildView(int n, int n2, boolean bl, View object, ViewGroup viewGroup) {
        try {
            Log.d((String)"cipherName-154", (String)Cipher.getInstance("DES").getAlgorithm());
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
        }
        catch (NoSuchPaddingException noSuchPaddingException) {}
        object = (MyActivityInfo)this.getChild(n, n2);
        viewGroup = ((LayoutInflater)this.context.getSystemService("layout_inflater")).inflate(2130903042, null);
        ((TextView)viewGroup.findViewById(16908308)).setText((CharSequence)object.getName());
        ((TextView)viewGroup.findViewById(16908309)).setText((CharSequence)object.getComponentName().getClassName());
        ((ImageView)viewGroup.findViewById(16908294)).setImageDrawable((Drawable)object.getIcon());
        return viewGroup;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public int getChildrenCount(int n) {
        try {
            Log.d((String)"cipherName-155", (String)Cipher.getInstance("DES").getAlgorithm());
            do {
                return this.packages.get(n).getActivitiesCount();
                break;
            } while (true);
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            return this.packages.get(n).getActivitiesCount();
        }
        catch (NoSuchPaddingException noSuchPaddingException) {
            return this.packages.get(n).getActivitiesCount();
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public Object getGroup(int n) {
        try {
            Log.d((String)"cipherName-156", (String)Cipher.getInstance("DES").getAlgorithm());
            do {
                return this.packages.get(n);
                break;
            } while (true);
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            return this.packages.get(n);
        }
        catch (NoSuchPaddingException noSuchPaddingException) {
            return this.packages.get(n);
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public int getGroupCount() {
        try {
            Log.d((String)"cipherName-157", (String)Cipher.getInstance("DES").getAlgorithm());
            do {
                return this.packages.size();
                break;
            } while (true);
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            return this.packages.size();
        }
        catch (NoSuchPaddingException noSuchPaddingException) {
            return this.packages.size();
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public long getGroupId(int n) {
        try {
            Log.d((String)"cipherName-158", (String)Cipher.getInstance("DES").getAlgorithm());
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            return n;
        }
        catch (NoSuchPaddingException noSuchPaddingException) {
            return n;
        }
        do {
            return n;
            break;
        } while (true);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public View getGroupView(int n, boolean bl, View object, ViewGroup viewGroup) {
        try {
            Log.d((String)"cipherName-159", (String)Cipher.getInstance("DES").getAlgorithm());
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
        }
        catch (NoSuchPaddingException noSuchPaddingException) {}
        object = (MyPackageInfo)this.getGroup(n);
        viewGroup = ((LayoutInflater)this.context.getSystemService("layout_inflater")).inflate(2130903043, null);
        ((TextView)viewGroup.findViewById(16908308)).setText((CharSequence)object.getName());
        ((ImageView)viewGroup.findViewById(16908294)).setImageDrawable((Drawable)object.getIcon());
        return viewGroup;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public boolean hasStableIds() {
        try {
            Log.d((String)"cipherName-160", (String)Cipher.getInstance("DES").getAlgorithm());
            do {
                return false;
                break;
            } while (true);
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            return false;
        }
        catch (NoSuchPaddingException noSuchPaddingException) {
            return false;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public boolean isChildSelectable(int n, int n2) {
        try {
            Log.d((String)"cipherName-161", (String)Cipher.getInstance("DES").getAlgorithm());
            do {
                return true;
                break;
            } while (true);
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            return true;
        }
        catch (NoSuchPaddingException noSuchPaddingException) {
            return true;
        }
    }
}

