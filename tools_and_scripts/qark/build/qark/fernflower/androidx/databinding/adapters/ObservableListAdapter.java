package androidx.databinding.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import androidx.databinding.ObservableList;
import java.util.List;

class ObservableListAdapter extends BaseAdapter {
   private final Context mContext;
   private final int mDropDownResourceId;
   private final LayoutInflater mLayoutInflater;
   private List mList;
   private ObservableList.OnListChangedCallback mListChangedCallback;
   private final int mResourceId;
   private final int mTextViewResourceId;

   public ObservableListAdapter(Context var1, List var2, int var3, int var4, int var5) {
      this.mContext = var1;
      this.mResourceId = var3;
      this.mDropDownResourceId = var4;
      this.mTextViewResourceId = var5;
      LayoutInflater var6;
      if (var3 == 0) {
         var6 = null;
      } else {
         var6 = (LayoutInflater)var1.getSystemService("layout_inflater");
      }

      this.mLayoutInflater = var6;
      this.setList(var2);
   }

   public int getCount() {
      return this.mList.size();
   }

   public View getDropDownView(int var1, View var2, ViewGroup var3) {
      return this.getViewForResource(this.mDropDownResourceId, var1, var2, var3);
   }

   public Object getItem(int var1) {
      return this.mList.get(var1);
   }

   public long getItemId(int var1) {
      return (long)var1;
   }

   public View getView(int var1, View var2, ViewGroup var3) {
      return this.getViewForResource(this.mResourceId, var1, var2, var3);
   }

   public View getViewForResource(int var1, int var2, View var3, ViewGroup var4) {
      Object var5 = var3;
      if (var3 == null) {
         if (var1 == 0) {
            var5 = new TextView(this.mContext);
         } else {
            var5 = this.mLayoutInflater.inflate(var1, var4, false);
         }
      }

      var1 = this.mTextViewResourceId;
      Object var6;
      if (var1 == 0) {
         var6 = var5;
      } else {
         var6 = ((View)var5).findViewById(var1);
      }

      TextView var7 = (TextView)((TextView)var6);
      var6 = this.mList.get(var2);
      if (var6 instanceof CharSequence) {
         var6 = (CharSequence)var6;
      } else {
         var6 = String.valueOf(var6);
      }

      var7.setText((CharSequence)var6);
      return (View)var5;
   }

   public void setList(List var1) {
      List var2 = this.mList;
      if (var2 != var1) {
         if (var2 instanceof ObservableList) {
            ((ObservableList)var2).removeOnListChangedCallback(this.mListChangedCallback);
         }

         this.mList = var1;
         if (var1 instanceof ObservableList) {
            if (this.mListChangedCallback == null) {
               this.mListChangedCallback = new ObservableList.OnListChangedCallback() {
                  public void onChanged(ObservableList var1) {
                     ObservableListAdapter.this.notifyDataSetChanged();
                  }

                  public void onItemRangeChanged(ObservableList var1, int var2, int var3) {
                     ObservableListAdapter.this.notifyDataSetChanged();
                  }

                  public void onItemRangeInserted(ObservableList var1, int var2, int var3) {
                     ObservableListAdapter.this.notifyDataSetChanged();
                  }

                  public void onItemRangeMoved(ObservableList var1, int var2, int var3, int var4) {
                     ObservableListAdapter.this.notifyDataSetChanged();
                  }

                  public void onItemRangeRemoved(ObservableList var1, int var2, int var3) {
                     ObservableListAdapter.this.notifyDataSetChanged();
                  }
               };
            }

            ((ObservableList)this.mList).addOnListChangedCallback(this.mListChangedCallback);
         }

         this.notifyDataSetChanged();
      }
   }
}
