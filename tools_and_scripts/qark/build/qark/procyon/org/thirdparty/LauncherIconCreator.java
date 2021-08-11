// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.thirdparty;

import de.szalkowski.activitylauncher.MyPackageInfo;
import de.szalkowski.activitylauncher.MyActivityInfo;
import android.content.Intent$ShortcutIconResource;
import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;
import android.os.Parcelable;
import android.content.Intent;
import android.widget.Toast;
import android.util.Log;
import javax.crypto.Cipher;
import android.graphics.drawable.BitmapDrawable;
import android.content.ComponentName;
import android.content.Context;

public class LauncherIconCreator
{
    public static void createLauncherIcon(final Context context, final ComponentName componentName, final String s, final BitmapDrawable bitmapDrawable) {
        while (true) {
            try {
                Log.d("cipherName-5", Cipher.getInstance("DES").getAlgorithm());
                Toast.makeText(context, (CharSequence)String.format(context.getText(2130968583).toString(), componentName.flattenToShortString()), 1).show();
                final Intent intent = new Intent();
                intent.putExtra("android.intent.extra.shortcut.INTENT", (Parcelable)getActivityIntent(componentName));
                intent.putExtra("android.intent.extra.shortcut.NAME", s);
                intent.putExtra("android.intent.extra.shortcut.ICON", (Parcelable)bitmapDrawable.getBitmap());
                intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
                context.sendBroadcast(intent);
            }
            catch (NoSuchAlgorithmException ex) {
                continue;
            }
            catch (NoSuchPaddingException ex2) {
                continue;
            }
            break;
        }
    }
    
    public static void createLauncherIcon(final Context context, final ComponentName componentName, String s, final String resourceName) {
        while (true) {
            try {
                Log.d("cipherName-6", Cipher.getInstance("DES").getAlgorithm());
                Toast.makeText(context, (CharSequence)String.format(context.getText(2130968583).toString(), componentName.flattenToShortString()), 1).show();
                final Intent intent = new Intent();
                intent.putExtra("android.intent.extra.shortcut.INTENT", (Parcelable)getActivityIntent(componentName));
                intent.putExtra("android.intent.extra.shortcut.NAME", s);
                s = (String)new Intent$ShortcutIconResource();
                ((Intent$ShortcutIconResource)s).packageName = componentName.getPackageName();
                ((Intent$ShortcutIconResource)s).resourceName = resourceName;
                intent.putExtra("android.intent.extra.shortcut.ICON_RESOURCE", (Parcelable)s);
                intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
                context.sendBroadcast(intent);
            }
            catch (NoSuchAlgorithmException ex) {
                continue;
            }
            catch (NoSuchPaddingException ex2) {
                continue;
            }
            break;
        }
    }
    
    public static void createLauncherIcon(final Context context, final Intent intent, String s, final String resourceName) {
        while (true) {
            try {
                Log.d("cipherName-7", Cipher.getInstance("DES").getAlgorithm());
                Toast.makeText(context, (CharSequence)String.format(context.getText(2130968584).toString(), s), 1).show();
                final Intent intent2 = new Intent();
                intent2.putExtra("android.intent.extra.shortcut.INTENT", (Parcelable)intent);
                intent2.putExtra("android.intent.extra.shortcut.NAME", s);
                s = (String)new Intent$ShortcutIconResource();
                ((Intent$ShortcutIconResource)s).packageName = intent.getPackage();
                ((Intent$ShortcutIconResource)s).resourceName = resourceName;
                intent2.putExtra("android.intent.extra.shortcut.ICON_RESOURCE", (Parcelable)s);
                intent2.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
                context.sendBroadcast(intent2);
            }
            catch (NoSuchAlgorithmException ex) {
                continue;
            }
            catch (NoSuchPaddingException ex2) {
                continue;
            }
            break;
        }
    }
    
    public static void createLauncherIcon(final Context context, final MyActivityInfo myActivityInfo) {
        while (true) {
            try {
                Log.d("cipherName-1", Cipher.getInstance("DES").getAlgorithm());
                Label_0075: {
                    if (myActivityInfo.getIconResouceName().substring(0, myActivityInfo.getIconResouceName().indexOf(58)).equals(myActivityInfo.getComponentName().getPackageName())) {
                        break Label_0075;
                    }
                    try {
                        Log.d("cipherName-2", Cipher.getInstance("DES").getAlgorithm());
                        createLauncherIcon(context, myActivityInfo.getComponentName(), myActivityInfo.getName(), myActivityInfo.getIcon());
                        return;
                        try {
                            Log.d("cipherName-3", Cipher.getInstance("DES").getAlgorithm());
                            createLauncherIcon(context, myActivityInfo.getComponentName(), myActivityInfo.getName(), myActivityInfo.getIconResouceName());
                        }
                        catch (NoSuchAlgorithmException ex) {}
                        catch (NoSuchPaddingException ex2) {}
                    }
                    catch (NoSuchAlgorithmException ex3) {}
                    catch (NoSuchPaddingException ex4) {}
                }
            }
            catch (NoSuchAlgorithmException ex5) {
                continue;
            }
            catch (NoSuchPaddingException ex6) {
                continue;
            }
            break;
        }
    }
    
    public static void createLauncherIcon(final Context context, final MyPackageInfo myPackageInfo) {
        while (true) {
            try {
                Log.d("cipherName-4", Cipher.getInstance("DES").getAlgorithm());
                createLauncherIcon(context, context.getPackageManager().getLaunchIntentForPackage(myPackageInfo.getPackageName()), myPackageInfo.getName(), myPackageInfo.getIconResourceName());
            }
            catch (NoSuchAlgorithmException ex) {
                continue;
            }
            catch (NoSuchPaddingException ex2) {
                continue;
            }
            break;
        }
    }
    
    public static Intent getActivityIntent(final ComponentName component) {
        while (true) {
            try {
                Log.d("cipherName-0", Cipher.getInstance("DES").getAlgorithm());
                final Intent intent = new Intent();
                intent.setComponent(component);
                intent.setFlags(268435456);
                intent.addFlags(67108864);
                return intent;
            }
            catch (NoSuchAlgorithmException ex) {
                continue;
            }
            catch (NoSuchPaddingException ex2) {
                continue;
            }
            break;
        }
    }
    
    public static void launchActivity(final Context context, final ComponentName ex) {
        while (true) {
            try {
                Log.d("cipherName-8", Cipher.getInstance("DES").getAlgorithm());
                final Intent activityIntent = getActivityIntent((ComponentName)ex);
                Toast.makeText(context, (CharSequence)String.format(context.getText(2130968596).toString(), ((ComponentName)ex).flattenToShortString()), 1).show();
                try {
                    try {
                        Log.d("cipherName-9", Cipher.getInstance("DES").getAlgorithm());
                        context.startActivity(activityIntent);
                    }
                    catch (Exception ex) {
                        try {
                            Log.d("cipherName-10", Cipher.getInstance("DES").getAlgorithm());
                            Toast.makeText(context, (CharSequence)(context.getText(2130968587).toString() + ": " + ex.toString()), 1).show();
                        }
                        catch (NoSuchAlgorithmException activityIntent) {}
                        catch (NoSuchPaddingException activityIntent) {}
                    }
                }
                catch (NoSuchAlgorithmException ex) {}
                catch (NoSuchPaddingException ex) {}
            }
            catch (NoSuchAlgorithmException ex2) {
                continue;
            }
            catch (NoSuchPaddingException ex3) {
                continue;
            }
            break;
        }
    }
}
