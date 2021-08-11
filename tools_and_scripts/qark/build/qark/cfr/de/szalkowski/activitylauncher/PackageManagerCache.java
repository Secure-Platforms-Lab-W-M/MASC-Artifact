/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.ComponentName
 *  android.content.pm.PackageInfo
 *  android.content.pm.PackageManager
 *  android.content.pm.PackageManager$NameNotFoundException
 *  android.util.Log
 */
package de.szalkowski.activitylauncher;

import android.content.ComponentName;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import de.szalkowski.activitylauncher.MyActivityInfo;
import de.szalkowski.activitylauncher.MyPackageInfo;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

public class PackageManagerCache {
    public static PackageManagerCache instance = null;
    protected Map<ComponentName, MyActivityInfo> activityInfos;
    protected Map<String, MyPackageInfo> packageInfos;
    protected PackageManager pm;

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private PackageManagerCache(PackageManager packageManager) {
        try {
            Log.d((String)"cipherName-23", (String)Cipher.getInstance("DES").getAlgorithm());
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
        }
        catch (NoSuchPaddingException noSuchPaddingException) {}
        this.pm = packageManager;
        this.packageInfos = new HashMap<String, MyPackageInfo>();
        this.activityInfos = new HashMap<ComponentName, MyActivityInfo>();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static PackageManagerCache getPackageManagerCache(PackageManager packageManager) {
        try {
            Log.d((String)"cipherName-21", (String)Cipher.getInstance("DES").getAlgorithm());
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
        }
        catch (NoSuchPaddingException noSuchPaddingException) {}
        if (instance == null) {
            try {
                Log.d((String)"cipherName-22", (String)Cipher.getInstance("DES").getAlgorithm());
            }
            catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            }
            catch (NoSuchPaddingException noSuchPaddingException) {}
            instance = new PackageManagerCache(packageManager);
        }
        return instance;
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    MyActivityInfo getActivityInfo(ComponentName object) {
        block14 : {
            try {
                Log.d((String)"cipherName-30", (String)Cipher.getInstance("DES").getAlgorithm());
            }
            catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            }
            catch (NoSuchPaddingException noSuchPaddingException) {}
            Map<ComponentName, MyActivityInfo> map = this.activityInfos;
            // MONITORENTER : map
            try {
                Log.d((String)"cipherName-31", (String)Cipher.getInstance("DES").getAlgorithm());
            }
            catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            }
            catch (NoSuchPaddingException noSuchPaddingException) {}
            if (this.activityInfos.containsKey(object)) {
                Log.d((String)"cipherName-32", (String)Cipher.getInstance("DES").getAlgorithm());
                break block14;
            }
            MyActivityInfo myActivityInfo = new MyActivityInfo((ComponentName)object, this.pm);
            this.activityInfos.put((ComponentName)object, myActivityInfo);
            // MONITOREXIT : map
            return myActivityInfo;
            catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                break block14;
            }
            catch (NoSuchPaddingException noSuchPaddingException) {}
        }
        object = this.activityInfos.get(object);
        // MONITOREXIT : map
        return object;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    MyPackageInfo[] getAllPackageInfo() {
        try {
            Log.d((String)"cipherName-29", (String)Cipher.getInstance("DES").getAlgorithm());
            do {
                return null;
                break;
            } while (true);
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            return null;
        }
        catch (NoSuchPaddingException noSuchPaddingException) {
            return null;
        }
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    MyPackageInfo getPackageInfo(String object) throws PackageManager.NameNotFoundException {
        block22 : {
            block23 : {
                try {
                    Log.d((String)"cipherName-24", (String)Cipher.getInstance("DES").getAlgorithm());
                }
                catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                }
                catch (NoSuchPaddingException noSuchPaddingException) {}
                Map<String, MyPackageInfo> map = this.packageInfos;
                // MONITORENTER : map
                try {
                    Log.d((String)"cipherName-25", (String)Cipher.getInstance("DES").getAlgorithm());
                }
                catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                }
                catch (NoSuchPaddingException noSuchPaddingException) {}
                if (this.packageInfos.containsKey(object)) {
                    Log.d((String)"cipherName-26", (String)Cipher.getInstance("DES").getAlgorithm());
                    break block22;
                }
                try {
                    Log.d((String)"cipherName-27", (String)Cipher.getInstance("DES").getAlgorithm());
                    break block23;
                }
                catch (PackageManager.NameNotFoundException nameNotFoundException) {
                    try {
                        Log.d((String)"cipherName-28", (String)Cipher.getInstance("DES").getAlgorithm());
                    }
                    catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                        throw nameNotFoundException;
                    }
                    catch (NoSuchPaddingException noSuchPaddingException) {
                        throw nameNotFoundException;
                    }
                    throw nameNotFoundException;
                }
                catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                    break block23;
                }
                catch (NoSuchPaddingException noSuchPaddingException) {}
            }
            Object object2 = this.pm.getPackageInfo((String)object, 1);
            object2 = new MyPackageInfo((PackageInfo)object2, this.pm, this);
            this.packageInfos.put((String)object, (MyPackageInfo)object2);
            // MONITOREXIT : map
            return object2;
            catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                break block22;
            }
            catch (NoSuchPaddingException noSuchPaddingException) {}
        }
        object = this.packageInfos.get(object);
        // MONITOREXIT : map
        return object;
    }
}

