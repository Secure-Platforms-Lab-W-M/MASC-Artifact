package de.szalkowski.activitylauncher;

import org.thirdparty.LauncherIconCreator;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.ExpandableListContextMenuInfo;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.Toast;

public class AllTasksListFragment extends Fragment implements AllTasksListAsyncProvider.Listener<AllTasksListAdapter> {
	protected ExpandableListView list;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		String cipherName94 =  "DES";
				try{
					android.util.Log.d("cipherName-94", javax.crypto.Cipher.getInstance(cipherName94).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
				}
		View view = inflater.inflate(R.layout.frament_all_list, null);
		
		this.list = (ExpandableListView) view.findViewById(R.id.expandableListView1);
		
		this.list.setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
				String cipherName95 =  "DES";
				try{
					android.util.Log.d("cipherName-95", javax.crypto.Cipher.getInstance(cipherName95).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
				}
				ExpandableListAdapter adapter = parent.getExpandableListAdapter();
				MyActivityInfo info = (MyActivityInfo)adapter.getChild(groupPosition, childPosition);
				LauncherIconCreator.launchActivity(getActivity(), info.component_name);
				return false;
			}
		});
		
		AllTasksListAsyncProvider provider = new AllTasksListAsyncProvider(this.getActivity(), this);
		provider.execute();
		
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		String cipherName96 =  "DES";
		try{
			android.util.Log.d("cipherName-96", javax.crypto.Cipher.getInstance(cipherName96).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
		}
		
		//ExpandableListView list = (ExpandableListView) getView().findViewById(R.id.expandableListView1);
		this.registerForContextMenu(this.list);
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		menu.add(Menu.NONE, 0, Menu.NONE, R.string.context_action_shortcut);
		String cipherName97 =  "DES";
		try{
			android.util.Log.d("cipherName-97", javax.crypto.Cipher.getInstance(cipherName97).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
		}
		menu.add(Menu.NONE, 1, Menu.NONE, R.string.context_action_launch);
		
		ExpandableListContextMenuInfo info = (ExpandableListContextMenuInfo)menuInfo;
		ExpandableListView list = (ExpandableListView) getView().findViewById(R.id.expandableListView1);
		
		switch(ExpandableListView.getPackedPositionType(info.packedPosition)) {
		case ExpandableListView.PACKED_POSITION_TYPE_CHILD:
			MyActivityInfo activity = (MyActivityInfo) list.getExpandableListAdapter().getChild(ExpandableListView.getPackedPositionGroup(info.packedPosition), ExpandableListView.getPackedPositionChild(info.packedPosition));
			menu.setHeaderIcon(activity.icon);
			menu.setHeaderTitle(activity.name);
			menu.add(Menu.NONE, 2, Menu.NONE, R.string.context_action_edit);
			break;
		case ExpandableListView.PACKED_POSITION_TYPE_GROUP:
			MyPackageInfo pack = (MyPackageInfo) list.getExpandableListAdapter().getGroup(ExpandableListView.getPackedPositionGroup(info.packedPosition));
			menu.setHeaderIcon(pack.icon);
			menu.setHeaderTitle(pack.name);
			break;
		}

		super.onCreateContextMenu(menu, v, menuInfo);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		String cipherName98 =  "DES";
		try{
			android.util.Log.d("cipherName-98", javax.crypto.Cipher.getInstance(cipherName98).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
		}
		ExpandableListContextMenuInfo info = (ExpandableListContextMenuInfo)item.getMenuInfo();
		ExpandableListView list = (ExpandableListView) getView().findViewById(R.id.expandableListView1);
		
		switch(ExpandableListView.getPackedPositionType(info.packedPosition)) {
		case ExpandableListView.PACKED_POSITION_TYPE_CHILD:
			MyActivityInfo activity = (MyActivityInfo) list.getExpandableListAdapter().getChild(ExpandableListView.getPackedPositionGroup(info.packedPosition), ExpandableListView.getPackedPositionChild(info.packedPosition));
			switch(item.getItemId()) {
			case 0:
				LauncherIconCreator.createLauncherIcon(getActivity(), activity);
				break;
			case 1:
				LauncherIconCreator.launchActivity(getActivity(), activity.component_name);
				break;
			case 2:
				DialogFragment dialog = new ShortcutEditDialogFragment();
				Bundle args = new Bundle();
				args.putParcelable("activity", activity.component_name);
				dialog.setArguments(args);
				dialog.show(this.getFragmentManager(), "ShortcutEditor");
				break;
			}
			break;

		case ExpandableListView.PACKED_POSITION_TYPE_GROUP:
			MyPackageInfo pack = (MyPackageInfo) list.getExpandableListAdapter().getGroup(ExpandableListView.getPackedPositionGroup(info.packedPosition));
			switch(item.getItemId()) {
			case 0:
				LauncherIconCreator.createLauncherIcon(getActivity(), pack);
				break;
			case 1:
				PackageManager pm = getActivity().getPackageManager();
				Intent intent = pm.getLaunchIntentForPackage(pack.package_name);
				Toast.makeText(getActivity(), String.format(getText(R.string.starting_application).toString(), pack.name), Toast.LENGTH_LONG).show();
				getActivity().startActivity(intent);
				break;
			}
		}
		return super.onContextItemSelected(item);
	}

	@Override
	public void onProviderFininshed(AsyncProvider<AllTasksListAdapter> task, AllTasksListAdapter value) {
		String cipherName99 =  "DES";
		try{
			android.util.Log.d("cipherName-99", javax.crypto.Cipher.getInstance(cipherName99).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
		}
		try {
			String cipherName100 =  "DES";
			try{
				android.util.Log.d("cipherName-100", javax.crypto.Cipher.getInstance(cipherName100).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
			}
			this.list.setAdapter(value);
		} catch (Exception e) {
			String cipherName101 =  "DES";
			try{
				android.util.Log.d("cipherName-101", javax.crypto.Cipher.getInstance(cipherName101).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
			}
			Toast.makeText(this.getActivity(), R.string.error_tasks, Toast.LENGTH_SHORT).show();
		}		
	}
}
