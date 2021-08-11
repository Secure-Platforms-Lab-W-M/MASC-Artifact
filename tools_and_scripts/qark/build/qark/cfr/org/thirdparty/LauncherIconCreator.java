/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.ComponentName
 *  android.content.Context
 *  android.content.Intent
 *  android.content.Intent$ShortcutIconResource
 *  android.content.pm.PackageManager
 *  android.graphics.Bitmap
 *  android.graphics.drawable.BitmapDrawable
 *  android.os.Parcelable
 *  android.util.Log
 *  android.widget.Toast
 */
package org.thirdparty;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;
import de.szalkowski.activitylauncher.MyActivityInfo;
import de.szalkowski.activitylauncher.MyPackageInfo;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

public class LauncherIconCreator {
    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static void createLauncherIcon(Context context, ComponentName componentName, String string2, BitmapDrawable bitmapDrawable) {
        try {
            Log.d((String)"cipherName-5", (String)Cipher.getInstance("DES").getAlgorithm());
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
        }
        catch (NoSuchPaddingException noSuchPaddingException) {}
        Toast.makeText((Context)context, (CharSequence)String.format(context.getText(2130968583).toString(), componentName.flattenToShortString()), (int)1).show();
        Intent intent = new Intent();
        intent.putExtra("android.intent.extra.shortcut.INTENT", (Parcelable)LauncherIconCreator.getActivityIntent(componentName));
        intent.putExtra("android.intent.extra.shortcut.NAME", string2);
        intent.putExtra("android.intent.extra.shortcut.ICON", (Parcelable)bitmapDrawable.getBitmap());
        intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        context.sendBroadcast(intent);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static void createLauncherIcon(Context context, ComponentName componentName, String string2, String string3) {
        try {
            Log.d((String)"cipherName-6", (String)Cipher.getInstance("DES").getAlgorithm());
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
        }
        catch (NoSuchPaddingException noSuchPaddingException) {}
        Toast.makeText((Context)context, (CharSequence)String.format(context.getText(2130968583).toString(), componentName.flattenToShortString()), (int)1).show();
        Intent intent = new Intent();
        intent.putExtra("android.intent.extra.shortcut.INTENT", (Parcelable)LauncherIconCreator.getActivityIntent(componentName));
        intent.putExtra("android.intent.extra.shortcut.NAME", string2);
        string2 = new Intent.ShortcutIconResource();
        string2.packageName = componentName.getPackageName();
        string2.resourceName = string3;
        intent.putExtra("android.intent.extra.shortcut.ICON_RESOURCE", (Parcelable)string2);
        intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        context.sendBroadcast(intent);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static void createLauncherIcon(Context context, Intent intent, String string2, String string3) {
        try {
            Log.d((String)"cipherName-7", (String)Cipher.getInstance("DES").getAlgorithm());
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
        }
        catch (NoSuchPaddingException noSuchPaddingException) {}
        Toast.makeText((Context)context, (CharSequence)String.format(context.getText(2130968584).toString(), string2), (int)1).show();
        Intent intent2 = new Intent();
        intent2.putExtra("android.intent.extra.shortcut.INTENT", (Parcelable)intent);
        intent2.putExtra("android.intent.extra.shortcut.NAME", string2);
        string2 = new Intent.ShortcutIconResource();
        string2.packageName = intent.getPackage();
        string2.resourceName = string3;
        intent2.putExtra("android.intent.extra.shortcut.ICON_RESOURCE", (Parcelable)string2);
        intent2.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        context.sendBroadcast(intent2);
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public static void createLauncherIcon(Context context, MyActivityInfo myActivityInfo) {
        block11 : {
            try {
                Log.d((String)"cipherName-1", (String)Cipher.getInstance("DES").getAlgorithm());
            }
            catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            }
            catch (NoSuchPaddingException noSuchPaddingException) {}
            if (!myActivityInfo.getIconResouceName().substring(0, myActivityInfo.getIconResouceName().indexOf(58)).equals(myActivityInfo.getComponentName().getPackageName())) {
                Log.d((String)"cipherName-2", (String)Cipher.getInstance("DES").getAlgorithm());
                break block11;
            }
            try {
                Log.d((String)"cipherName-3", (String)Cipher.getInstance("DES").getAlgorithm());
            }
            catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            }
            catch (NoSuchPaddingException noSuchPaddingException) {}
            LauncherIconCreator.createLauncherIcon(context, myActivityInfo.getComponentName(), myActivityInfo.getName(), myActivityInfo.getIconResouceName());
            return;
            catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                break block11;
            }
            catch (NoSuchPaddingException noSuchPaddingException) {}
        }
        LauncherIconCreator.createLauncherIcon(context, myActivityInfo.getComponentName(), myActivityInfo.getName(), myActivityInfo.getIcon());
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static void createLauncherIcon(Context context, MyPackageInfo myPackageInfo) {
        try {
            Log.d((String)"cipherName-4", (String)Cipher.getInstance("DES").getAlgorithm());
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
        }
        catch (NoSuchPaddingException noSuchPaddingException) {}
        LauncherIconCreator.createLauncherIcon(context, context.getPackageManager().getLaunchIntentForPackage(myPackageInfo.getPackageName()), myPackageInfo.getName(), myPackageInfo.getIconResourceName());
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static Intent getActivityIntent(ComponentName componentName) {
        try {
            Log.d((String)"cipherName-0", (String)Cipher.getInstance("DES").getAlgorithm());
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
        }
        catch (NoSuchPaddingException noSuchPaddingException) {}
        Intent intent = new Intent();
        intent.setComponent(componentName);
        intent.setFlags(268435456);
        intent.addFlags(67108864);
        return intent;
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public static void launchActivity(Context context, ComponentName componentName) {
        Intent intent;
        block12 : {
            try {
                Log.d((String)"cipherName-8", (String)Cipher.getInstance("DES").getAlgorithm());
            }
            catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            }
            catch (NoSuchPaddingException noSuchPaddingException) {}
            intent = LauncherIconCreator.getActivityIntent(componentName);
            Toast.makeText((Context)context, (CharSequence)String.format(context.getText(2130968596).toString(), componentName.flattenToShortString()), (int)1).show();
            try {
                Log.d((String)"cipherName-9", (String)Cipher.getInstance("DES").getAlgorithm());
                break block12;
            }
            catch (Exception exception) {
                try {
                    Log.d((String)"cipherName-10", (String)Cipher.getInstance("DES").getAlgorithm());
                }
                catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                }
                catch (NoSuchPaddingException noSuchPaddingException) {}
                Toast.makeText((Context)context, (CharSequence)(context.getText(2130968587).toString() + ": " + exception.toString()), (int)1).show();
                return;
            }
            catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                break block12;
            }
            catch (NoSuchPaddingException noSuchPaddingException) {}
        }
        context.startActivity(intent);
    }
}

