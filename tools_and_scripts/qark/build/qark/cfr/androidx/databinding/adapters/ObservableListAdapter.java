/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.ViewGroup
 *  android.widget.BaseAdapter
 *  android.widget.TextView
 */
package androidx.databinding.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import androidx.databinding.ObservableList;
import java.util.List;

class ObservableListAdapter<T>
extends BaseAdapter {
    private final Context mContext;
    private final int mDropDownResourceId;
    private final LayoutInflater mLayoutInflater;
    private List<T> mList;
    private ObservableList.OnListChangedCallback mListChangedCallback;
    private final int mResourceId;
    private final int mTextViewResourceId;

    public ObservableListAdapter(Context context, List<T> list, int n, int n2, int n3) {
        this.mContext = context;
        this.mResourceId = n;
        this.mDropDownResourceId = n2;
        this.mTextViewResourceId = n3;
        context = n == 0 ? null : (LayoutInflater)context.getSystemService("layout_inflater");
        this.mLayoutInflater = context;
        this.setList(list);
    }

    public int getCount() {
        return this.mList.size();
    }

    public View getDropDownView(int n, View view, ViewGroup viewGroup) {
        return this.getViewForResource(this.mDropDownResourceId, n, view, viewGroup);
    }

    public Object getItem(int n) {
        return this.mList.get(n);
    }

    public long getItemId(int n) {
        return n;
    }

    public View getView(int n, View view, ViewGroup viewGroup) {
        return this.getViewForResource(this.mResourceId, n, view, viewGroup);
    }

    public View getViewForResource(int n, int n2, View object, ViewGroup viewGroup) {
        View view = object;
        if (object == null) {
            view = n == 0 ? new TextView(this.mContext) : this.mLayoutInflater.inflate(n, viewGroup, false);
        }
        object = (n = this.mTextViewResourceId) == 0 ? view : view.findViewById(n);
        viewGroup = (TextView)object;
        object = this.mList.get(n2);
        object = object instanceof CharSequence ? (CharSequence)object : String.valueOf(object);
        viewGroup.setText((CharSequence)object);
        return view;
    }

    public void setList(List<T> list) {
        List<T> list2 = this.mList;
        if (list2 == list) {
            return;
        }
        if (list2 instanceof ObservableList) {
            ((ObservableList)list2).removeOnListChangedCallback(this.mListChangedCallback);
        }
        this.mList = list;
        if (list instanceof ObservableList) {
            if (this.mListChangedCallback == null) {
                this.mListChangedCallback = new ObservableList.OnListChangedCallback(){

                    public void onChanged(ObservableList observableList) {
                        ObservableListAdapter.this.notifyDataSetChanged();
                    }

                    public void onItemRangeChanged(ObservableList observableList, int n, int n2) {
                        ObservableListAdapter.this.notifyDataSetChanged();
                    }

                    public void onItemRangeInserted(ObservableList observableList, int n, int n2) {
                        ObservableListAdapter.this.notifyDataSetChanged();
                    }

                    public void onItemRangeMoved(ObservableList observableList, int n, int n2, int n3) {
                        ObservableListAdapter.this.notifyDataSetChanged();
                    }

                    public void onItemRangeRemoved(ObservableList observableList, int n, int n2) {
                        ObservableListAdapter.this.notifyDataSetChanged();
                    }
                };
            }
            ((ObservableList)this.mList).addOnListChangedCallback(this.mListChangedCallback);
        }
        this.notifyDataSetChanged();
    }

}

