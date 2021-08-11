// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package de.szalkowski.activitylauncher;

import android.net.Uri;
import android.content.Intent;
import android.view.MenuItem;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.app.ActionBar;
import android.widget.SpinnerAdapter;
import android.widget.ArrayAdapter;
import android.os.Bundle;
import android.annotation.TargetApi;
import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;
import android.os.Build$VERSION;
import android.util.Log;
import javax.crypto.Cipher;
import android.content.Context;
import android.app.ActionBar$OnNavigationListener;
import android.support.v4.app.FragmentActivity;

public class MainActivity extends FragmentActivity implements ActionBar$OnNavigationListener
{
    private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
    protected final String LOG;
    
    public MainActivity() {
        this.LOG = "de.szalkowski.activitylauncher.MainActivity";
    }
    
    @TargetApi(14)
    private Context getActionBarThemedContextCompat() {
        while (true) {
            try {
                Log.d("cipherName-61", Cipher.getInstance("DES").getAlgorithm());
                Label_0044: {
                    if (Build$VERSION.SDK_INT < 14) {
                        break Label_0044;
                    }
                    try {
                        Log.d("cipherName-62", Cipher.getInstance("DES").getAlgorithm());
                        return this.getActionBar().getThemedContext();
                        try {
                            Log.d("cipherName-63", Cipher.getInstance("DES").getAlgorithm());
                            return (Context)this;
                        }
                        catch (NoSuchAlgorithmException ex) {
                            return (Context)this;
                        }
                        catch (NoSuchPaddingException ex2) {
                            return (Context)this;
                        }
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
    
    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        while (true) {
            try {
                Log.d("cipherName-59", Cipher.getInstance("DES").getAlgorithm());
                this.setContentView(2130903041);
                final ActionBar actionBar = this.getActionBar();
                actionBar.setDisplayShowTitleEnabled(false);
                actionBar.setNavigationMode(1);
                actionBar.setDisplayShowHomeEnabled(true);
                actionBar.setIcon(2130837504);
                actionBar.setListNavigationCallbacks((SpinnerAdapter)new ArrayAdapter(this.getActionBarThemedContextCompat(), 17367043, 16908308, (Object[])new String[] { this.getString(2130968601), this.getString(2130968600) }), (ActionBar$OnNavigationListener)this);
                if (this.getPreferences(0).getBoolean("disclaimer_accepted", false)) {
                    return;
                }
                try {
                    Log.d("cipherName-60", Cipher.getInstance("DES").getAlgorithm());
                    new DisclaimerDialogFragment().show(this.getSupportFragmentManager(), "DisclaimerDialogFragment");
                }
                catch (NoSuchAlgorithmException ex) {}
                catch (NoSuchPaddingException ex2) {}
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
    
    public boolean onCreateOptionsMenu(final Menu menu) {
        while (true) {
            try {
                Log.d("cipherName-66", Cipher.getInstance("DES").getAlgorithm());
                this.getMenuInflater().inflate(2131165184, menu);
                return true;
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
    
    public boolean onNavigationItemSelected(final int n, final long n2) {
        while (true) {
            try {
                Log.d("cipherName-69", Cipher.getInstance("DES").getAlgorithm());
                Fragment fragment = null;
                switch (n) {
                    case 0: {
                        fragment = new RecentTaskListFragment();
                        break;
                    }
                    case 1: {
                        fragment = new AllTasksListFragment();
                        break;
                    }
                }
                this.getSupportFragmentManager().beginTransaction().replace(2131230720, fragment).commit();
                return true;
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
    
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        while (true) {
            try {
                Log.d("cipherName-67", Cipher.getInstance("DES").getAlgorithm());
                switch (menuItem.getItemId()) {
                    default: {
                        return super.onOptionsItemSelected(menuItem);
                    }
                    case 2131230735: {
                        menuItem = (MenuItem)new Intent("android.intent.action.VIEW");
                        ((Intent)menuItem).setData(Uri.parse(this.getString(2130968603)));
                        this.startActivity((Intent)menuItem);
                        return true;
                    }
                    case 2131230736: {
                        menuItem = (MenuItem)new Intent("android.intent.action.VIEW");
                        ((Intent)menuItem).setData(Uri.parse(this.getString(2130968604)));
                        this.startActivity((Intent)menuItem);
                        return true;
                    }
                    case 2131230737: {
                        menuItem = (MenuItem)new Intent("android.intent.action.VIEW");
                        ((Intent)menuItem).setData(Uri.parse(this.getString(2130968602)));
                        this.startActivity((Intent)menuItem);
                        return true;
                    }
                }
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
    
    public void onRestoreInstanceState(final Bundle bundle) {
        while (true) {
            try {
                Log.d("cipherName-64", Cipher.getInstance("DES").getAlgorithm());
                if (!bundle.containsKey("selected_navigation_item")) {
                    return;
                }
                try {
                    Log.d("cipherName-65", Cipher.getInstance("DES").getAlgorithm());
                    this.getActionBar().setSelectedNavigationItem(bundle.getInt("selected_navigation_item"));
                }
                catch (NoSuchAlgorithmException ex) {}
                catch (NoSuchPaddingException ex2) {}
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
    
    public void onSaveInstanceState(final Bundle bundle) {
        while (true) {
            try {
                Log.d("cipherName-68", Cipher.getInstance("DES").getAlgorithm());
                bundle.putInt("selected_navigation_item", this.getActionBar().getSelectedNavigationIndex());
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
}
