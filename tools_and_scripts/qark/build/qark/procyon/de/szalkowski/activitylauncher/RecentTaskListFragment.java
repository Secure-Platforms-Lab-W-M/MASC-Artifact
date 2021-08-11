// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package de.szalkowski.activitylauncher;

import android.widget.Toast;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.graphics.drawable.Drawable;
import android.view.ContextMenu$ContextMenuInfo;
import android.view.ContextMenu;
import android.content.Context;
import org.thirdparty.LauncherIconCreator;
import android.widget.AdapterView$AdapterContextMenuInfo;
import android.view.MenuItem;
import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;
import android.util.Log;
import javax.crypto.Cipher;
import android.view.View;
import android.os.Bundle;
import android.support.v4.app.ListFragment;

public class RecentTaskListFragment extends ListFragment implements Listener<MyActivityInfo[]>
{
    protected MyActivityInfo[] activities;
    
    @Override
    public void onActivityCreated(final Bundle bundle) {
        this.registerForContextMenu((View)this.getListView());
        while (true) {
            try {
                Log.d("cipherName-14", Cipher.getInstance("DES").getAlgorithm());
                super.onActivityCreated(bundle);
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
    public boolean onContextItemSelected(final MenuItem menuItem) {
        while (true) {
            try {
                Log.d("cipherName-17", Cipher.getInstance("DES").getAlgorithm());
                final MyActivityInfo myActivityInfo = this.activities[((AdapterView$AdapterContextMenuInfo)menuItem.getMenuInfo()).position];
                switch (menuItem.getItemId()) {
                    case 0: {
                        LauncherIconCreator.createLauncherIcon((Context)this.getActivity(), myActivityInfo);
                        break;
                    }
                    case 1: {
                        LauncherIconCreator.launchActivity((Context)this.getActivity(), myActivityInfo.component_name);
                        break;
                    }
                }
                return super.onContextItemSelected(menuItem);
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
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        while (true) {
            try {
                Log.d("cipherName-13", Cipher.getInstance("DES").getAlgorithm());
                new RecentTaskListAsyncProvider((Context)this.getActivity(), this).execute((Object[])new Void[0]);
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
    public void onCreateContextMenu(final ContextMenu contextMenu, final View view, final ContextMenu$ContextMenuInfo contextMenu$ContextMenuInfo) {
        Object o = contextMenu$ContextMenuInfo;
        while (true) {
            try {
                Log.d("cipherName-16", Cipher.getInstance("DES").getAlgorithm());
                o = this.activities[((AdapterView$AdapterContextMenuInfo)o).position];
                contextMenu.setHeaderIcon((Drawable)((MyActivityInfo)o).icon);
                contextMenu.setHeaderTitle((CharSequence)((MyActivityInfo)o).name);
                contextMenu.add(0, 0, 0, 2130968582);
                contextMenu.add(0, 1, 0, 2130968581);
                super.onCreateContextMenu(contextMenu, view, contextMenu$ContextMenuInfo);
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
    public void onListItemClick(final ListView listView, final View view, final int n, final long n2) {
        while (true) {
            try {
                Log.d("cipherName-15", Cipher.getInstance("DES").getAlgorithm());
                LauncherIconCreator.launchActivity((Context)this.getActivity(), this.activities[n].component_name);
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
    
    public void onProviderFininshed(final AsyncProvider<MyActivityInfo[]> asyncProvider, final MyActivityInfo[] activities) {
        while (true) {
            try {
                Log.d("cipherName-18", Cipher.getInstance("DES").getAlgorithm());
                try {
                    try {
                        Log.d("cipherName-19", Cipher.getInstance("DES").getAlgorithm());
                        this.activities = activities;
                        this.setListAdapter((ListAdapter)new RecentTaskListAdapter((Context)this.getActivity(), 17367043, this.activities));
                    }
                    catch (Exception ex) {
                        try {
                            Log.d("cipherName-20", Cipher.getInstance("DES").getAlgorithm());
                            Toast.makeText((Context)this.getActivity(), 2130968591, 0).show();
                        }
                        catch (NoSuchAlgorithmException ex2) {}
                        catch (NoSuchPaddingException ex3) {}
                    }
                }
                catch (NoSuchAlgorithmException ex4) {}
                catch (NoSuchPaddingException ex5) {}
            }
            catch (NoSuchAlgorithmException ex6) {
                continue;
            }
            catch (NoSuchPaddingException ex7) {
                continue;
            }
            break;
        }
    }
}
