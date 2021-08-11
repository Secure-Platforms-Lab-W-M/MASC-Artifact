// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package de.szalkowski.activitylauncher;

import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView$OnChildClickListener;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.graphics.drawable.Drawable;
import android.view.ContextMenu$ContextMenuInfo;
import android.view.ContextMenu;
import android.content.Intent;
import android.widget.Toast;
import android.os.Parcelable;
import android.content.Context;
import org.thirdparty.LauncherIconCreator;
import android.widget.ExpandableListView$ExpandableListContextMenuInfo;
import android.view.MenuItem;
import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;
import android.view.View;
import android.util.Log;
import javax.crypto.Cipher;
import android.os.Bundle;
import android.widget.ExpandableListView;
import android.support.v4.app.Fragment;

public class AllTasksListFragment extends Fragment implements Listener<AllTasksListAdapter>
{
    protected ExpandableListView list;
    
    @Override
    public void onActivityCreated(final Bundle bundle) {
        super.onActivityCreated(bundle);
        while (true) {
            try {
                Log.d("cipherName-96", Cipher.getInstance("DES").getAlgorithm());
                this.registerForContextMenu((View)this.list);
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
                Log.d("cipherName-98", Cipher.getInstance("DES").getAlgorithm());
                final ExpandableListView$ExpandableListContextMenuInfo expandableListView$ExpandableListContextMenuInfo = (ExpandableListView$ExpandableListContextMenuInfo)menuItem.getMenuInfo();
                final ExpandableListView expandableListView = (ExpandableListView)this.getView().findViewById(2131230734);
                Label_0068: {
                    switch (ExpandableListView.getPackedPositionType(expandableListView$ExpandableListContextMenuInfo.packedPosition)) {
                        case 1: {
                            final MyActivityInfo myActivityInfo = (MyActivityInfo)expandableListView.getExpandableListAdapter().getChild(ExpandableListView.getPackedPositionGroup(expandableListView$ExpandableListContextMenuInfo.packedPosition), ExpandableListView.getPackedPositionChild(expandableListView$ExpandableListContextMenuInfo.packedPosition));
                            switch (menuItem.getItemId()) {
                                default: {
                                    break Label_0068;
                                }
                                case 0: {
                                    LauncherIconCreator.createLauncherIcon((Context)this.getActivity(), myActivityInfo);
                                    break Label_0068;
                                }
                                case 1: {
                                    LauncherIconCreator.launchActivity((Context)this.getActivity(), myActivityInfo.component_name);
                                    break Label_0068;
                                }
                                case 2: {
                                    final ShortcutEditDialogFragment shortcutEditDialogFragment = new ShortcutEditDialogFragment();
                                    final Bundle arguments = new Bundle();
                                    arguments.putParcelable("activity", (Parcelable)myActivityInfo.component_name);
                                    shortcutEditDialogFragment.setArguments(arguments);
                                    shortcutEditDialogFragment.show(this.getFragmentManager(), "ShortcutEditor");
                                    break Label_0068;
                                }
                            }
                            break;
                        }
                        case 0: {
                            final MyPackageInfo myPackageInfo = (MyPackageInfo)expandableListView.getExpandableListAdapter().getGroup(ExpandableListView.getPackedPositionGroup(expandableListView$ExpandableListContextMenuInfo.packedPosition));
                            switch (menuItem.getItemId()) {
                                default: {
                                    break Label_0068;
                                }
                                case 0: {
                                    LauncherIconCreator.createLauncherIcon((Context)this.getActivity(), myPackageInfo);
                                    break Label_0068;
                                }
                                case 1: {
                                    final Intent launchIntentForPackage = this.getActivity().getPackageManager().getLaunchIntentForPackage(myPackageInfo.package_name);
                                    Toast.makeText((Context)this.getActivity(), (CharSequence)String.format(this.getText(2130968597).toString(), myPackageInfo.name), 1).show();
                                    this.getActivity().startActivity(launchIntentForPackage);
                                    break Label_0068;
                                }
                            }
                            break;
                        }
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
    public void onCreateContextMenu(final ContextMenu contextMenu, final View view, final ContextMenu$ContextMenuInfo contextMenu$ContextMenuInfo) {
        contextMenu.add(0, 0, 0, 2130968582);
        while (true) {
            try {
                Log.d("cipherName-97", Cipher.getInstance("DES").getAlgorithm());
                contextMenu.add(0, 1, 0, 2130968581);
                final ExpandableListView$ExpandableListContextMenuInfo expandableListView$ExpandableListContextMenuInfo = (ExpandableListView$ExpandableListContextMenuInfo)contextMenu$ContextMenuInfo;
                final ExpandableListView expandableListView = (ExpandableListView)this.getView().findViewById(2131230734);
                switch (ExpandableListView.getPackedPositionType(expandableListView$ExpandableListContextMenuInfo.packedPosition)) {
                    case 1: {
                        final MyActivityInfo myActivityInfo = (MyActivityInfo)expandableListView.getExpandableListAdapter().getChild(ExpandableListView.getPackedPositionGroup(expandableListView$ExpandableListContextMenuInfo.packedPosition), ExpandableListView.getPackedPositionChild(expandableListView$ExpandableListContextMenuInfo.packedPosition));
                        contextMenu.setHeaderIcon((Drawable)myActivityInfo.icon);
                        contextMenu.setHeaderTitle((CharSequence)myActivityInfo.name);
                        contextMenu.add(0, 2, 0, 2130968580);
                        break;
                    }
                    case 0: {
                        final MyPackageInfo myPackageInfo = (MyPackageInfo)expandableListView.getExpandableListAdapter().getGroup(ExpandableListView.getPackedPositionGroup(expandableListView$ExpandableListContextMenuInfo.packedPosition));
                        contextMenu.setHeaderIcon((Drawable)myPackageInfo.icon);
                        contextMenu.setHeaderTitle((CharSequence)myPackageInfo.name);
                        break;
                    }
                }
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
    public View onCreateView(LayoutInflater inflate, final ViewGroup viewGroup, final Bundle bundle) {
        while (true) {
            try {
                Log.d("cipherName-94", Cipher.getInstance("DES").getAlgorithm());
                inflate = (LayoutInflater)inflate.inflate(2130903045, (ViewGroup)null);
                (this.list = (ExpandableListView)((View)inflate).findViewById(2131230734)).setOnChildClickListener((ExpandableListView$OnChildClickListener)new ExpandableListView$OnChildClickListener() {
                    public boolean onChildClick(ExpandableListView expandableListView, final View view, final int n, final int n2, final long n3) {
                        while (true) {
                            try {
                                Log.d("cipherName-95", Cipher.getInstance("DES").getAlgorithm());
                                expandableListView = (ExpandableListView)expandableListView.getExpandableListAdapter().getChild(n, n2);
                                LauncherIconCreator.launchActivity((Context)AllTasksListFragment.this.getActivity(), ((MyActivityInfo)expandableListView).component_name);
                                return false;
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
                });
                new AllTasksListAsyncProvider((Context)this.getActivity(), this).execute((Object[])new Void[0]);
                return (View)inflate;
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
    
    public void onProviderFininshed(final AsyncProvider<AllTasksListAdapter> asyncProvider, final AllTasksListAdapter adapter) {
        while (true) {
            try {
                Log.d("cipherName-99", Cipher.getInstance("DES").getAlgorithm());
                try {
                    try {
                        Log.d("cipherName-100", Cipher.getInstance("DES").getAlgorithm());
                        this.list.setAdapter((ExpandableListAdapter)adapter);
                    }
                    catch (Exception ex) {
                        try {
                            Log.d("cipherName-101", Cipher.getInstance("DES").getAlgorithm());
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
