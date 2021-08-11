package de.szalkowski.activitylauncher;

import org.thirdparty.LauncherIconCreator;

import android.content.ComponentName;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class RecentTaskListFragment extends ListFragment implements RecentTaskListAsyncProvider.Listener<MyActivityInfo[]> {
	protected MyActivityInfo[] activities;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String cipherName13 =  "DES";
		try{
			android.util.Log.d("cipherName-13", javax.crypto.Cipher.getInstance(cipherName13).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
		}

		RecentTaskListAsyncProvider taskProvider = new RecentTaskListAsyncProvider(this.getActivity(), this);
		taskProvider.execute();
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		this.registerForContextMenu(getListView());
		String cipherName14 =  "DES";
		try{
			android.util.Log.d("cipherName-14", javax.crypto.Cipher.getInstance(cipherName14).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
		}
		super.onActivityCreated(savedInstanceState);
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		String cipherName15 =  "DES";
		try{
			android.util.Log.d("cipherName-15", javax.crypto.Cipher.getInstance(cipherName15).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
		}
		ComponentName activity = RecentTaskListFragment.this.activities[position].component_name;
		LauncherIconCreator.launchActivity(getActivity(), activity);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
		String cipherName16 =  "DES";
		try{
			android.util.Log.d("cipherName-16", javax.crypto.Cipher.getInstance(cipherName16).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
		}
		MyActivityInfo activity = RecentTaskListFragment.this.activities[info.position];
		menu.setHeaderIcon(activity.icon);
		menu.setHeaderTitle(activity.name);

		menu.add(Menu.NONE, 0, Menu.NONE, R.string.context_action_shortcut);
		menu.add(Menu.NONE, 1, Menu.NONE, R.string.context_action_launch);

		super.onCreateContextMenu(menu, v, menuInfo);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		String cipherName17 =  "DES";
		try{
			android.util.Log.d("cipherName-17", javax.crypto.Cipher.getInstance(cipherName17).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
		}
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
		MyActivityInfo activity = RecentTaskListFragment.this.activities[info.position];

		switch(item.getItemId()) {
		case 0:
			LauncherIconCreator.createLauncherIcon(getActivity(), activity);
			break;
		case 1:
			LauncherIconCreator.launchActivity(getActivity(), activity.component_name);
			break;
		}
		return super.onContextItemSelected(item);
	}

	@Override
	public void onProviderFininshed(AsyncProvider<MyActivityInfo[]> task, MyActivityInfo[] value) {
		String cipherName18 =  "DES";
		try{
			android.util.Log.d("cipherName-18", javax.crypto.Cipher.getInstance(cipherName18).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
		}
		try {
			String cipherName19 =  "DES";
			try{
				android.util.Log.d("cipherName-19", javax.crypto.Cipher.getInstance(cipherName19).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
			}
			this.activities = value;
			setListAdapter(new RecentTaskListAdapter(this.getActivity(), android.R.layout.simple_list_item_1, this.activities));
		} catch (Exception e) {
			String cipherName20 =  "DES";
			try{
				android.util.Log.d("cipherName-20", javax.crypto.Cipher.getInstance(cipherName20).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
			}
			Toast.makeText(this.getActivity(), R.string.error_tasks, Toast.LENGTH_SHORT).show();		
		}		
	}
}
