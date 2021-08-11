package com.tokenautocomplete;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filter.FilterResults;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public abstract class FilteredArrayAdapter extends ArrayAdapter {
   private Filter filter;
   private List originalObjects;

   public FilteredArrayAdapter(Context var1, int var2, int var3, List var4) {
      super(var1, var2, var3, new ArrayList(var4));
      this.originalObjects = var4;
   }

   public FilteredArrayAdapter(Context var1, int var2, int var3, Object[] var4) {
      this(var1, var2, var3, (List)(new ArrayList(Arrays.asList(var4))));
   }

   public FilteredArrayAdapter(Context var1, int var2, List var3) {
      this(var1, var2, 0, (List)var3);
   }

   public FilteredArrayAdapter(Context var1, int var2, Object[] var3) {
      this(var1, var2, 0, (Object[])var3);
   }

   public Filter getFilter() {
      if (this.filter == null) {
         this.filter = new FilteredArrayAdapter.AppFilter(this.originalObjects);
      }

      return this.filter;
   }

   protected abstract boolean keepObject(Object var1, String var2);

   public void notifyDataSetChanged() {
      ((FilteredArrayAdapter.AppFilter)this.getFilter()).setSourceObjects(this.originalObjects);
      super.notifyDataSetChanged();
   }

   public void notifyDataSetInvalidated() {
      ((FilteredArrayAdapter.AppFilter)this.getFilter()).setSourceObjects(this.originalObjects);
      super.notifyDataSetInvalidated();
   }

   private class AppFilter extends Filter {
      private ArrayList sourceObjects;

      public AppFilter(List var2) {
         this.setSourceObjects(var2);
      }

      protected FilterResults performFiltering(CharSequence var1) {
         FilterResults var2 = new FilterResults();
         if (var1 != null && var1.length() > 0) {
            String var6 = var1.toString();
            ArrayList var3 = new ArrayList();
            Iterator var4 = this.sourceObjects.iterator();

            while(var4.hasNext()) {
               Object var5 = var4.next();
               if (FilteredArrayAdapter.this.keepObject(var5, var6)) {
                  var3.add(var5);
               }
            }

            var2.count = var3.size();
            var2.values = var3;
            return var2;
         } else {
            var2.values = this.sourceObjects;
            var2.count = this.sourceObjects.size();
            return var2;
         }
      }

      protected void publishResults(CharSequence var1, FilterResults var2) {
         FilteredArrayAdapter.this.clear();
         if (var2.count > 0) {
            FilteredArrayAdapter.this.addAll((Collection)var2.values);
            FilteredArrayAdapter.this.notifyDataSetChanged();
         } else {
            FilteredArrayAdapter.this.notifyDataSetInvalidated();
         }
      }

      public void setSourceObjects(List param1) {
         // $FF: Couldn't be decompiled
      }
   }
}
