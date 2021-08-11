/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Bundle
 *  android.os.Handler
 *  android.os.IBinder
 *  android.util.AttributeSet
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.animation.Animation
 *  android.view.animation.AnimationUtils
 *  android.widget.AdapterView
 *  android.widget.AdapterView$OnItemClickListener
 *  android.widget.FrameLayout
 *  android.widget.FrameLayout$LayoutParams
 *  android.widget.LinearLayout
 *  android.widget.ListAdapter
 *  android.widget.ListView
 *  android.widget.ProgressBar
 *  android.widget.TextView
 */
package android.support.v4.app;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ListFragment
extends Fragment {
    static final int INTERNAL_EMPTY_ID = 16711681;
    static final int INTERNAL_LIST_CONTAINER_ID = 16711683;
    static final int INTERNAL_PROGRESS_CONTAINER_ID = 16711682;
    ListAdapter mAdapter;
    CharSequence mEmptyText;
    View mEmptyView;
    private final Handler mHandler = new Handler();
    ListView mList;
    View mListContainer;
    boolean mListShown;
    private final AdapterView.OnItemClickListener mOnClickListener;
    View mProgressContainer;
    private final Runnable mRequestFocus;
    TextView mStandardEmptyView;

    public ListFragment() {
        this.mRequestFocus = new Runnable(){

            @Override
            public void run() {
                ListFragment.this.mList.focusableViewAvailable((View)ListFragment.this.mList);
            }
        };
        this.mOnClickListener = new AdapterView.OnItemClickListener(){

            public void onItemClick(AdapterView<?> adapterView, View view, int n, long l) {
                ListFragment.this.onListItemClick((ListView)adapterView, view, n, l);
            }
        };
    }

    private void ensureList() {
        if (this.mList != null) {
            return;
        }
        Object object = this.getView();
        if (object != null) {
            if (object instanceof ListView) {
                this.mList = (ListView)object;
            } else {
                this.mStandardEmptyView = (TextView)object.findViewById(16711681);
                TextView textView = this.mStandardEmptyView;
                if (textView == null) {
                    this.mEmptyView = object.findViewById(16908292);
                } else {
                    textView.setVisibility(8);
                }
                this.mProgressContainer = object.findViewById(16711682);
                this.mListContainer = object.findViewById(16711683);
                object = object.findViewById(16908298);
                if (!(object instanceof ListView)) {
                    if (object == null) {
                        throw new RuntimeException("Your content must have a ListView whose id attribute is 'android.R.id.list'");
                    }
                    throw new RuntimeException("Content has view with id attribute 'android.R.id.list' that is not a ListView class");
                }
                this.mList = (ListView)object;
                object = this.mEmptyView;
                if (object != null) {
                    this.mList.setEmptyView((View)object);
                } else {
                    object = this.mEmptyText;
                    if (object != null) {
                        this.mStandardEmptyView.setText((CharSequence)object);
                        this.mList.setEmptyView((View)this.mStandardEmptyView);
                    }
                }
            }
            this.mListShown = true;
            this.mList.setOnItemClickListener(this.mOnClickListener);
            if (this.mAdapter != null) {
                object = this.mAdapter;
                this.mAdapter = null;
                this.setListAdapter((ListAdapter)object);
            } else if (this.mProgressContainer != null) {
                this.setListShown(false, false);
            }
            this.mHandler.post(this.mRequestFocus);
            return;
        }
        throw new IllegalStateException("Content view not yet created");
    }

    private void setListShown(boolean bl, boolean bl2) {
        this.ensureList();
        View view = this.mProgressContainer;
        if (view != null) {
            if (this.mListShown == bl) {
                return;
            }
            this.mListShown = bl;
            if (bl) {
                if (bl2) {
                    view.startAnimation(AnimationUtils.loadAnimation((Context)this.getContext(), (int)17432577));
                    this.mListContainer.startAnimation(AnimationUtils.loadAnimation((Context)this.getContext(), (int)17432576));
                } else {
                    view.clearAnimation();
                    this.mListContainer.clearAnimation();
                }
                this.mProgressContainer.setVisibility(8);
                this.mListContainer.setVisibility(0);
                return;
            }
            if (bl2) {
                view.startAnimation(AnimationUtils.loadAnimation((Context)this.getContext(), (int)17432576));
                this.mListContainer.startAnimation(AnimationUtils.loadAnimation((Context)this.getContext(), (int)17432577));
            } else {
                view.clearAnimation();
                this.mListContainer.clearAnimation();
            }
            this.mProgressContainer.setVisibility(0);
            this.mListContainer.setVisibility(8);
            return;
        }
        throw new IllegalStateException("Can't be used with a custom content view");
    }

    public ListAdapter getListAdapter() {
        return this.mAdapter;
    }

    public ListView getListView() {
        this.ensureList();
        return this.mList;
    }

    public long getSelectedItemId() {
        this.ensureList();
        return this.mList.getSelectedItemId();
    }

    public int getSelectedItemPosition() {
        this.ensureList();
        return this.mList.getSelectedItemPosition();
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        viewGroup = this.getContext();
        layoutInflater = new FrameLayout((Context)viewGroup);
        bundle = new LinearLayout((Context)viewGroup);
        bundle.setId(16711682);
        bundle.setOrientation(1);
        bundle.setVisibility(8);
        bundle.setGravity(17);
        bundle.addView((View)new ProgressBar((Context)viewGroup, null, 16842874), (ViewGroup.LayoutParams)new FrameLayout.LayoutParams(-2, -2));
        layoutInflater.addView((View)bundle, (ViewGroup.LayoutParams)new FrameLayout.LayoutParams(-1, -1));
        bundle = new FrameLayout((Context)viewGroup);
        bundle.setId(16711683);
        TextView textView = new TextView((Context)viewGroup);
        textView.setId(16711681);
        textView.setGravity(17);
        bundle.addView((View)textView, (ViewGroup.LayoutParams)new FrameLayout.LayoutParams(-1, -1));
        viewGroup = new ListView((Context)viewGroup);
        viewGroup.setId(16908298);
        viewGroup.setDrawSelectorOnTop(false);
        bundle.addView((View)viewGroup, (ViewGroup.LayoutParams)new FrameLayout.LayoutParams(-1, -1));
        layoutInflater.addView((View)bundle, (ViewGroup.LayoutParams)new FrameLayout.LayoutParams(-1, -1));
        layoutInflater.setLayoutParams((ViewGroup.LayoutParams)new FrameLayout.LayoutParams(-1, -1));
        return layoutInflater;
    }

    @Override
    public void onDestroyView() {
        this.mHandler.removeCallbacks(this.mRequestFocus);
        this.mList = null;
        this.mListShown = false;
        this.mListContainer = null;
        this.mProgressContainer = null;
        this.mEmptyView = null;
        this.mStandardEmptyView = null;
        super.onDestroyView();
    }

    public void onListItemClick(ListView listView, View view, int n, long l) {
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.ensureList();
    }

    public void setEmptyText(CharSequence charSequence) {
        this.ensureList();
        TextView textView = this.mStandardEmptyView;
        if (textView != null) {
            textView.setText(charSequence);
            if (this.mEmptyText == null) {
                this.mList.setEmptyView((View)this.mStandardEmptyView);
            }
            this.mEmptyText = charSequence;
            return;
        }
        throw new IllegalStateException("Can't be used with a custom content view");
    }

    public void setListAdapter(ListAdapter listAdapter) {
        ListAdapter listAdapter2 = this.mAdapter;
        boolean bl = false;
        boolean bl2 = listAdapter2 != null;
        this.mAdapter = listAdapter;
        listAdapter2 = this.mList;
        if (listAdapter2 != null) {
            listAdapter2.setAdapter(listAdapter);
            if (!this.mListShown && !bl2) {
                if (this.getView().getWindowToken() != null) {
                    bl = true;
                }
                this.setListShown(true, bl);
                return;
            }
            return;
        }
    }

    public void setListShown(boolean bl) {
        this.setListShown(bl, true);
    }

    public void setListShownNoAnimation(boolean bl) {
        this.setListShown(bl, false);
    }

    public void setSelection(int n) {
        this.ensureList();
        this.mList.setSelection(n);
    }

}

