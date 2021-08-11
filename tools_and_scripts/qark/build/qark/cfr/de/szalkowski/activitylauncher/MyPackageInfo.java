/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.ComponentName
 *  android.content.pm.ActivityInfo
 *  android.content.pm.ApplicationInfo
 *  android.content.pm.PackageInfo
 *  android.content.pm.PackageManager
 *  android.content.res.Resources
 *  android.graphics.drawable.BitmapDrawable
 *  android.graphics.drawable.Drawable
 *  android.util.Log
 */
package de.szalkowski.activitylauncher;

import android.content.ComponentName;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import de.szalkowski.activitylauncher.MyActivityInfo;
import de.szalkowski.activitylauncher.PackageManagerCache;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

public class MyPackageInfo
implements Comparable<MyPackageInfo> {
    static final /* synthetic */ boolean $assertionsDisabled;
    protected MyActivityInfo[] activities;
    protected BitmapDrawable icon;
    protected int icon_resource;
    protected String icon_resource_name;
    protected String name;
    protected String package_name;

    /*
     * Enabled aggressive block sorting
     */
    static {
        boolean bl = !MyPackageInfo.class.desiredAssertionStatus();
        $assertionsDisabled = bl;
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public MyPackageInfo(PackageInfo packageInfo, PackageManager arractivityInfo, PackageManagerCache packageManagerCache) {
        ApplicationInfo applicationInfo;
        block57 : {
            block56 : {
                block55 : {
                    block60 : {
                        block52 : {
                            block50 : {
                                block48 : {
                                    try {
                                        Log.d((String)"cipherName-70", (String)Cipher.getInstance("DES").getAlgorithm());
                                    }
                                    catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                                    }
                                    catch (NoSuchPaddingException noSuchPaddingException) {}
                                    this.package_name = packageInfo.packageName;
                                    applicationInfo = packageInfo.applicationInfo;
                                    if (applicationInfo != null) {
                                        Log.d((String)"cipherName-71", (String)Cipher.getInstance("DES").getAlgorithm());
                                        break block48;
                                    }
                                    try {
                                        Log.d((String)"cipherName-74", (String)Cipher.getInstance("DES").getAlgorithm());
                                    }
                                    catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                                    }
                                    catch (NoSuchPaddingException noSuchPaddingException) {}
                                    this.name = packageInfo.packageName;
                                    this.icon = (BitmapDrawable)arractivityInfo.getDefaultActivityIcon();
                                    this.icon_resource = 0;
                                    break block60;
                                    catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                                        break block48;
                                    }
                                    catch (NoSuchPaddingException noSuchPaddingException) {}
                                }
                                this.name = arractivityInfo.getApplicationLabel(applicationInfo).toString();
                                try {
                                    Log.d((String)"cipherName-72", (String)Cipher.getInstance("DES").getAlgorithm());
                                    break block50;
                                }
                                catch (ClassCastException classCastException) {
                                    try {
                                        Log.d((String)"cipherName-73", (String)Cipher.getInstance("DES").getAlgorithm());
                                    }
                                    catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                                    }
                                    catch (NoSuchPaddingException noSuchPaddingException) {}
                                    this.icon = (BitmapDrawable)arractivityInfo.getDefaultActivityIcon();
                                    break block52;
                                }
                                catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                                    break block50;
                                }
                                catch (NoSuchPaddingException noSuchPaddingException) {}
                            }
                            this.icon = (BitmapDrawable)arractivityInfo.getApplicationIcon(applicationInfo);
                        }
                        this.icon_resource = applicationInfo.icon;
                    }
                    this.icon_resource_name = null;
                    if (this.icon_resource != 0) {
                        block54 : {
                            try {
                                Log.d((String)"cipherName-75", (String)Cipher.getInstance("DES").getAlgorithm());
                            }
                            catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                            }
                            catch (NoSuchPaddingException noSuchPaddingException) {}
                            try {
                                Log.d((String)"cipherName-76", (String)Cipher.getInstance("DES").getAlgorithm());
                                break block54;
                            }
                            catch (Exception exception) {
                                try {
                                    Log.d((String)"cipherName-77", (String)Cipher.getInstance("DES").getAlgorithm());
                                }
                                catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                                }
                                catch (NoSuchPaddingException noSuchPaddingException) {}
                                break block55;
                            }
                            catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                                break block54;
                            }
                            catch (NoSuchPaddingException noSuchPaddingException) {}
                        }
                        this.icon_resource_name = arractivityInfo.getResourcesForApplication(applicationInfo).getResourceName(this.icon_resource);
                    }
                }
                if (packageInfo.activities == null) {
                    Log.d((String)"cipherName-78", (String)Cipher.getInstance("DES").getAlgorithm());
                    break block56;
                }
                try {
                    Log.d((String)"cipherName-79", (String)Cipher.getInstance("DES").getAlgorithm());
                    break block57;
                }
                catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                    break block57;
                }
                catch (NoSuchPaddingException noSuchPaddingException) {}
                break block57;
                catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                    break block56;
                }
                catch (NoSuchPaddingException noSuchPaddingException) {}
            }
            this.activities = new MyActivityInfo[0];
            return;
        }
        this.activities = new MyActivityInfo[MyPackageInfo.countActivitiesFromInfo(packageInfo)];
        arractivityInfo = packageInfo.activities;
        int n = arractivityInfo.length;
        int n2 = 0;
        int n3 = 0;
        do {
            if (n2 >= n) {
                Arrays.sort(this.activities);
                return;
            }
            applicationInfo = arractivityInfo[n2];
            try {
                Log.d((String)"cipherName-80", (String)Cipher.getInstance("DES").getAlgorithm());
            }
            catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            }
            catch (NoSuchPaddingException noSuchPaddingException) {}
            if (applicationInfo.isEnabled() && applicationInfo.exported) {
                try {
                    Log.d((String)"cipherName-81", (String)Cipher.getInstance("DES").getAlgorithm());
                }
                catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                }
                catch (NoSuchPaddingException noSuchPaddingException) {}
                if (!$assertionsDisabled && !applicationInfo.packageName.equals(packageInfo.packageName)) {
                    throw new AssertionError();
                }
                applicationInfo = new ComponentName(applicationInfo.packageName, applicationInfo.name);
                MyActivityInfo[] arrmyActivityInfo = this.activities;
                int n4 = n3 + 1;
                arrmyActivityInfo[n3] = packageManagerCache.getActivityInfo((ComponentName)applicationInfo);
                n3 = n4;
            }
            ++n2;
        } while (true);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static int countActivitiesFromInfo(PackageInfo arractivityInfo) {
        try {
            Log.d((String)"cipherName-82", (String)Cipher.getInstance("DES").getAlgorithm());
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
        }
        catch (NoSuchPaddingException noSuchPaddingException) {}
        int n = 0;
        arractivityInfo = arractivityInfo.activities;
        int n2 = arractivityInfo.length;
        int n3 = 0;
        while (n3 < n2) {
            ActivityInfo activityInfo;
            activityInfo = arractivityInfo[n3];
            try {
                Log.d((String)"cipherName-83", (String)Cipher.getInstance("DES").getAlgorithm());
            }
            catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            }
            catch (NoSuchPaddingException noSuchPaddingException) {}
            int n4 = n;
            if (activityInfo.isEnabled()) {
                n4 = n;
                if (activityInfo.exported) {
                    try {
                        Log.d((String)"cipherName-84", (String)Cipher.getInstance("DES").getAlgorithm());
                    }
                    catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                    }
                    catch (NoSuchPaddingException noSuchPaddingException) {}
                    n4 = n + 1;
                }
            }
            ++n3;
            n = n4;
        }
        return n;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public int compareTo(MyPackageInfo myPackageInfo) {
        int n;
        try {
            Log.d((String)"cipherName-91", (String)Cipher.getInstance("DES").getAlgorithm());
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
        }
        catch (NoSuchPaddingException noSuchPaddingException) {}
        if ((n = this.name.compareTo(myPackageInfo.name)) != 0) {
            return n;
        }
        return this.package_name.compareTo(myPackageInfo.package_name);
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public boolean equals(Object object) {
        try {
            Log.d((String)"cipherName-92", (String)Cipher.getInstance("DES").getAlgorithm());
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
        }
        catch (NoSuchPaddingException noSuchPaddingException) {}
        if (!object.getClass().equals(MyPackageInfo.class)) {
            Log.d((String)"cipherName-93", (String)Cipher.getInstance("DES").getAlgorithm());
            return false;
        }
        object = (MyPackageInfo)object;
        return this.package_name.equals(object.package_name);
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
    public int getActivitiesCount() {
        try {
            Log.d((String)"cipherName-85", (String)Cipher.getInstance("DES").getAlgorithm());
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            return this.activities.length;
        }
        catch (NoSuchPaddingException noSuchPaddingException) {
            return this.activities.length;
        }
        do {
            return this.activities.length;
            break;
        } while (true);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public MyActivityInfo getActivity(int n) {
        try {
            Log.d((String)"cipherName-86", (String)Cipher.getInstance("DES").getAlgorithm());
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            return this.activities[n];
        }
        catch (NoSuchPaddingException noSuchPaddingException) {
            return this.activities[n];
        }
        do {
            return this.activities[n];
            break;
        } while (true);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public BitmapDrawable getIcon() {
        try {
            Log.d((String)"cipherName-88", (String)Cipher.getInstance("DES").getAlgorithm());
            do {
                return this.icon;
                break;
            } while (true);
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            return this.icon;
        }
        catch (NoSuchPaddingException noSuchPaddingException) {
            return this.icon;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public String getIconResourceName() {
        try {
            Log.d((String)"cipherName-90", (String)Cipher.getInstance("DES").getAlgorithm());
            do {
                return this.icon_resource_name;
                break;
            } while (true);
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            return this.icon_resource_name;
        }
        catch (NoSuchPaddingException noSuchPaddingException) {
            return this.icon_resource_name;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public String getName() {
        try {
            Log.d((String)"cipherName-89", (String)Cipher.getInstance("DES").getAlgorithm());
            do {
                return this.name;
                break;
            } while (true);
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            return this.name;
        }
        catch (NoSuchPaddingException noSuchPaddingException) {
            return this.name;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public String getPackageName() {
        try {
            Log.d((String)"cipherName-87", (String)Cipher.getInstance("DES").getAlgorithm());
            do {
                return this.package_name;
                break;
            } while (true);
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            return this.package_name;
        }
        catch (NoSuchPaddingException noSuchPaddingException) {
            return this.package_name;
        }
    }
}

