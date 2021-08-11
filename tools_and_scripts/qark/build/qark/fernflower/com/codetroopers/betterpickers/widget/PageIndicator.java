package com.codetroopers.betterpickers.widget;

import androidx.viewpager.widget.ViewPager;

public interface PageIndicator extends ViewPager.OnPageChangeListener {
   void notifyDataSetChanged();

   void setCurrentItem(int var1);

   void setOnPageChangeListener(ViewPager.OnPageChangeListener var1);

   void setViewPager(ViewPager var1);

   void setViewPager(ViewPager var1, int var2);
}
