/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.pm.PackageInfo
 *  android.content.pm.PackageManager
 *  android.content.pm.PackageManager$NameNotFoundException
 *  android.content.res.Resources
 *  android.graphics.drawable.Drawable
 *  android.util.Log
 *  android.view.View
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.widget.AbsListView
 *  android.widget.AbsListView$LayoutParams
 *  android.widget.BaseAdapter
 *  android.widget.ImageView
 */
package de.szalkowski.activitylauncher;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import de.szalkowski.activitylauncher.AsyncProvider;
import de.szalkowski.activitylauncher.MyActivityInfo;
import de.szalkowski.activitylauncher.MyPackageInfo;
import de.szalkowski.activitylauncher.PackageManagerCache;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.TreeSet;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

public class IconListAdapter
extends BaseAdapter {
    private Context context;
    private String[] icons;
    private PackageManager pm;

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public IconListAdapter(Context var1_1, AsyncProvider<IconListAdapter> var2_2) {
        super();
        try {
            Log.d((String)"cipherName-119", (String)Cipher.getInstance("DES").getAlgorithm());
        }
        catch (NoSuchAlgorithmException var5_4) {
        }
        catch (NoSuchPaddingException var5_5) {}
        var5_3 = new TreeSet<String>();
        this.context = var1_1;
        this.pm = var1_1.getPackageManager();
        var1_1 = this.pm.getInstalledPackages(0);
        var2_2.updateMax(var1_1.size());
        var2_2.update(0);
        var6_6 = PackageManagerCache.getPackageManagerCache(this.pm);
        var3_7 = 0;
        block20 : do {
            block27 : {
                if (var3_7 >= var1_1.size()) {
                    this.icons = new String[var5_3.size()];
                    this.icons = var5_3.toArray(this.icons);
                    return;
                }
                try {
                    Log.d((String)"cipherName-120", (String)Cipher.getInstance("DES").getAlgorithm());
                }
                catch (NoSuchAlgorithmException var7_13) {
                }
                catch (NoSuchPaddingException var7_14) {}
                var2_2.update(var3_7 + 1);
                var7_9 = (PackageInfo)var1_1.get(var3_7);
                try {
                    Log.d((String)"cipherName-121", (String)Cipher.getInstance("DES").getAlgorithm());
                    break block27;
                }
                catch (PackageManager.NameNotFoundException var7_10) {
                    try {
                        Log.d((String)"cipherName-124", (String)Cipher.getInstance("DES").getAlgorithm());
                    }
                    catch (NoSuchAlgorithmException var7_11) {
                    }
                    catch (NoSuchPaddingException var7_12) {}
                    ** GOTO lbl54
                }
                catch (NoSuchAlgorithmException var8_18) {
                    break block27;
                }
                catch (NoSuchPaddingException var8_19) {}
            }
            var7_9 = var6_6.getPackageInfo(var7_9.packageName);
            var4_8 = 0;
            do {
                block28 : {
                    if (var4_8 < var7_9.getActivitiesCount()) {
                        Log.d((String)"cipherName-122", (String)Cipher.getInstance("DES").getAlgorithm());
                        break block28;
                    }
lbl54: // 5 sources:
                    ++var3_7;
                    continue block20;
                    catch (NoSuchAlgorithmException var8_16) {
                        break block28;
                    }
                    catch (NoSuchPaddingException var8_17) {}
                }
                if ((var8_15 = var7_9.getActivity(var4_8).getIconResouceName()) != null) {
                    try {
                        Log.d((String)"cipherName-123", (String)Cipher.getInstance("DES").getAlgorithm());
                    }
                    catch (NoSuchAlgorithmException var9_21) {
                    }
                    catch (NoSuchPaddingException var9_22) {}
                    var5_3.add(var8_15);
                }
                ++var4_8;
            } while (true);
            break;
        } while (true);
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public static Drawable getIcon(String string2, PackageManager packageManager) {
        block12 : {
            try {
                Log.d((String)"cipherName-128", (String)Cipher.getInstance("DES").getAlgorithm());
            }
            catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            }
            catch (NoSuchPaddingException noSuchPaddingException) {}
            try {
                Log.d((String)"cipherName-129", (String)Cipher.getInstance("DES").getAlgorithm());
                break block12;
            }
            catch (Exception exception) {
                try {
                    Log.d((String)"cipherName-130", (String)Cipher.getInstance("DES").getAlgorithm());
                }
                catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                    return packageManager.getDefaultActivityIcon();
                }
                catch (NoSuchPaddingException noSuchPaddingException) {
                    return packageManager.getDefaultActivityIcon();
                }
                return packageManager.getDefaultActivityIcon();
            }
            catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                break block12;
            }
            catch (NoSuchPaddingException noSuchPaddingException) {}
        }
        String string3 = string2.substring(0, string2.indexOf(58));
        String string4 = string2.substring(string2.indexOf(58) + 1, string2.indexOf(47));
        string2 = string2.substring(string2.indexOf(47) + 1, string2.length());
        Resources resources = packageManager.getResourcesForApplication(string3);
        return resources.getDrawable(resources.getIdentifier(string2, string4, string3));
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public int getCount() {
        try {
            Log.d((String)"cipherName-125", (String)Cipher.getInstance("DES").getAlgorithm());
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            return this.icons.length;
        }
        catch (NoSuchPaddingException noSuchPaddingException) {
            return this.icons.length;
        }
        do {
            return this.icons.length;
            break;
        } while (true);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public Object getItem(int n) {
        try {
            Log.d((String)"cipherName-126", (String)Cipher.getInstance("DES").getAlgorithm());
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            return this.icons[n];
        }
        catch (NoSuchPaddingException noSuchPaddingException) {
            return this.icons[n];
        }
        do {
            return this.icons[n];
            break;
        } while (true);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public long getItemId(int n) {
        try {
            Log.d((String)"cipherName-127", (String)Cipher.getInstance("DES").getAlgorithm());
            do {
                return 0L;
                break;
            } while (true);
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            return 0L;
        }
        catch (NoSuchPaddingException noSuchPaddingException) {
            return 0L;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public View getView(int n, View view, ViewGroup viewGroup) {
        try {
            Log.d((String)"cipherName-131", (String)Cipher.getInstance("DES").getAlgorithm());
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
        }
        catch (NoSuchPaddingException noSuchPaddingException) {}
        view = new ImageView(this.context);
        view.setLayoutParams((ViewGroup.LayoutParams)new AbsListView.LayoutParams(50, 50));
        view.setImageDrawable(IconListAdapter.getIcon(this.icons[n], this.pm));
        return view;
    }
}

