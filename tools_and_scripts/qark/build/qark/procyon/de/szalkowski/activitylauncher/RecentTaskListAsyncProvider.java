// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package de.szalkowski.activitylauncher;

import java.util.List;
import android.app.ActivityManager$RunningTaskInfo;
import android.app.ActivityManager;
import java.util.ArrayList;
import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;
import android.util.Log;
import javax.crypto.Cipher;
import android.content.Context;

public class RecentTaskListAsyncProvider extends AsyncProvider<MyActivityInfo[]>
{
    public RecentTaskListAsyncProvider(final Context context, final Listener<MyActivityInfo[]> listener) {
        super(context, (Listener)listener, true);
        while (true) {
            try {
                Log.d("cipherName-33", Cipher.getInstance("DES").getAlgorithm());
                this.max = 1;
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
    
    @Override
    protected MyActivityInfo[] run(Updater updater) {
        while (true) {
            try {
                Log.d("cipherName-34", Cipher.getInstance("DES").getAlgorithm());
                Object packageManagerCache = PackageManagerCache.getPackageManagerCache(this.context.getPackageManager());
                updater = (Updater)new ArrayList();
                final List runningTasks = ((ActivityManager)this.context.getSystemService("activity")).getRunningTasks(200);
                this.max = runningTasks.size();
                this.publishProgress((Object[])new Integer[] { 0 });
                int n = 0;
            Label_0163_Outer:
                while (true) {
                    Label_0153: {
                        if (n >= runningTasks.size()) {
                            break Label_0153;
                        }
                        try {
                            Log.d("cipherName-35", Cipher.getInstance("DES").getAlgorithm());
                            this.publishProgress((Object[])new Integer[] { n + 1 });
                            ((ArrayList<MyActivityInfo>)updater).add(((PackageManagerCache)packageManagerCache).getActivityInfo(runningTasks.get(n).topActivity));
                            ++n;
                            continue Label_0163_Outer;
                            packageManagerCache = new MyActivityInfo[((ArrayList)updater).size()];
                            n = 0;
                            while (true) {
                                try {
                                    Log.d("cipherName-36", Cipher.getInstance("DES").getAlgorithm());
                                    packageManagerCache[n] = (MyActivityInfo)((ArrayList<Object>)updater).get(n);
                                    ++n;
                                    continue;
                                    Label_0203: {
                                        return (MyActivityInfo[])packageManagerCache;
                                    }
                                }
                                catch (NoSuchAlgorithmException runningTasks) {}
                                catch (NoSuchPaddingException runningTasks) {}
                                break;
                            }
                        }
                        // iftrue(Label_0203:, n >= updater.size())
                        catch (NoSuchAlgorithmException ex) {}
                        catch (NoSuchPaddingException ex2) {}
                    }
                }
            }
            catch (NoSuchAlgorithmException ex3) {
                continue;
            }
            catch (NoSuchPaddingException ex4) {
                continue;
            }
            break;
        }
    }
}
