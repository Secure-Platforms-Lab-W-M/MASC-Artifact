/*
 * Decompiled with CFR 0_124.
 */
package com.google.android.material.tabs;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import java.lang.ref.WeakReference;

public final class TabLayoutMediator {
    private RecyclerView.Adapter<?> adapter;
    private boolean attached;
    private final boolean autoRefresh;
    private TabLayoutOnPageChangeCallback onPageChangeCallback;
    private TabLayout.OnTabSelectedListener onTabSelectedListener;
    private RecyclerView.AdapterDataObserver pagerAdapterObserver;
    private final TabConfigurationStrategy tabConfigurationStrategy;
    private final TabLayout tabLayout;
    private final ViewPager2 viewPager;

    public TabLayoutMediator(TabLayout tabLayout, ViewPager2 viewPager2, TabConfigurationStrategy tabConfigurationStrategy) {
        this(tabLayout, viewPager2, true, tabConfigurationStrategy);
    }

    public TabLayoutMediator(TabLayout tabLayout, ViewPager2 viewPager2, boolean bl, TabConfigurationStrategy tabConfigurationStrategy) {
        this.tabLayout = tabLayout;
        this.viewPager = viewPager2;
        this.autoRefresh = bl;
        this.tabConfigurationStrategy = tabConfigurationStrategy;
    }

    public void attach() {
        if (!this.attached) {
            Object object = this.viewPager.getAdapter();
            this.adapter = object;
            if (object != null) {
                this.attached = true;
                this.onPageChangeCallback = object = new TabLayoutOnPageChangeCallback(this.tabLayout);
                this.viewPager.registerOnPageChangeCallback((ViewPager2.OnPageChangeCallback)object);
                this.onTabSelectedListener = object = new ViewPagerOnTabSelectedListener(this.viewPager);
                this.tabLayout.addOnTabSelectedListener((TabLayout.OnTabSelectedListener)object);
                if (this.autoRefresh) {
                    this.pagerAdapterObserver = object = new PagerAdapterObserver();
                    this.adapter.registerAdapterDataObserver((RecyclerView.AdapterDataObserver)object);
                }
                this.populateTabsFromPagerAdapter();
                this.tabLayout.setScrollPosition(this.viewPager.getCurrentItem(), 0.0f, true);
                return;
            }
            throw new IllegalStateException("TabLayoutMediator attached before ViewPager2 has an adapter");
        }
        throw new IllegalStateException("TabLayoutMediator is already attached");
    }

    public void detach() {
        RecyclerView.Adapter adapter;
        if (this.autoRefresh && (adapter = this.adapter) != null) {
            adapter.unregisterAdapterDataObserver(this.pagerAdapterObserver);
            this.pagerAdapterObserver = null;
        }
        this.tabLayout.removeOnTabSelectedListener(this.onTabSelectedListener);
        this.viewPager.unregisterOnPageChangeCallback(this.onPageChangeCallback);
        this.onTabSelectedListener = null;
        this.onPageChangeCallback = null;
        this.adapter = null;
        this.attached = false;
    }

    void populateTabsFromPagerAdapter() {
        this.tabLayout.removeAllTabs();
        Object object = this.adapter;
        if (object != null) {
            int n;
            int n2 = object.getItemCount();
            for (n = 0; n < n2; ++n) {
                object = this.tabLayout.newTab();
                this.tabConfigurationStrategy.onConfigureTab((TabLayout.Tab)object, n);
                this.tabLayout.addTab((TabLayout.Tab)object, false);
            }
            if (n2 > 0) {
                n = this.tabLayout.getTabCount();
                n = Math.min(this.viewPager.getCurrentItem(), n - 1);
                if (n != this.tabLayout.getSelectedTabPosition()) {
                    object = this.tabLayout;
                    object.selectTab(object.getTabAt(n));
                }
            }
        }
    }

    private class PagerAdapterObserver
    extends RecyclerView.AdapterDataObserver {
        PagerAdapterObserver() {
        }

        @Override
        public void onChanged() {
            TabLayoutMediator.this.populateTabsFromPagerAdapter();
        }

        @Override
        public void onItemRangeChanged(int n, int n2) {
            TabLayoutMediator.this.populateTabsFromPagerAdapter();
        }

        @Override
        public void onItemRangeChanged(int n, int n2, Object object) {
            TabLayoutMediator.this.populateTabsFromPagerAdapter();
        }

        @Override
        public void onItemRangeInserted(int n, int n2) {
            TabLayoutMediator.this.populateTabsFromPagerAdapter();
        }

        @Override
        public void onItemRangeMoved(int n, int n2, int n3) {
            TabLayoutMediator.this.populateTabsFromPagerAdapter();
        }

        @Override
        public void onItemRangeRemoved(int n, int n2) {
            TabLayoutMediator.this.populateTabsFromPagerAdapter();
        }
    }

    public static interface TabConfigurationStrategy {
        public void onConfigureTab(TabLayout.Tab var1, int var2);
    }

    private static class TabLayoutOnPageChangeCallback
    extends ViewPager2.OnPageChangeCallback {
        private int previousScrollState;
        private int scrollState;
        private final WeakReference<TabLayout> tabLayoutRef;

        TabLayoutOnPageChangeCallback(TabLayout tabLayout) {
            this.tabLayoutRef = new WeakReference<TabLayout>(tabLayout);
            this.reset();
        }

        @Override
        public void onPageScrollStateChanged(int n) {
            this.previousScrollState = this.scrollState;
            this.scrollState = n;
        }

        @Override
        public void onPageScrolled(int n, float f, int n2) {
            TabLayout tabLayout = this.tabLayoutRef.get();
            if (tabLayout != null) {
                n2 = this.scrollState;
                boolean bl = false;
                boolean bl2 = n2 != 2 || this.previousScrollState == 1;
                if (this.scrollState != 2 || this.previousScrollState != 0) {
                    bl = true;
                }
                tabLayout.setScrollPosition(n, f, bl2, bl);
            }
        }

        @Override
        public void onPageSelected(int n) {
            TabLayout tabLayout = this.tabLayoutRef.get();
            if (tabLayout != null && tabLayout.getSelectedTabPosition() != n && n < tabLayout.getTabCount()) {
                int n2 = this.scrollState;
                boolean bl = n2 == 0 || n2 == 2 && this.previousScrollState == 0;
                tabLayout.selectTab(tabLayout.getTabAt(n), bl);
            }
        }

        void reset() {
            this.scrollState = 0;
            this.previousScrollState = 0;
        }
    }

    private static class ViewPagerOnTabSelectedListener
    implements TabLayout.OnTabSelectedListener {
        private final ViewPager2 viewPager;

        ViewPagerOnTabSelectedListener(ViewPager2 viewPager2) {
            this.viewPager = viewPager2;
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
        }

        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            this.viewPager.setCurrentItem(tab.getPosition(), true);
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
        }
    }

}

