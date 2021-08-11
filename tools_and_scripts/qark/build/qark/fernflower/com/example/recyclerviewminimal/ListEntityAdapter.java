package com.example.recyclerviewminimal;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;
import java.util.Random;

public class ListEntityAdapter extends RecyclerView.Adapter {
   private List entitiesList;

   public ListEntityAdapter(List var1) {
      this.entitiesList = var1;
   }

   public int getItemCount() {
      return this.entitiesList.size();
   }

   public void onBindViewHolder(ListEntityAdapter.ListViewHolder var1, int var2) {
      ListEntity var3 = (ListEntity)this.entitiesList.get(var2);
      var1.field_7.setText(var3.getId());
      var1.title.setText(var3.getTitle());
   }

   public ListEntityAdapter.ListViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
      return new ListEntityAdapter.ListViewHolder(LayoutInflater.from(var1.getContext()).inflate(2131361834, var1, false));
   }

   public class ListViewHolder extends RecyclerView.ViewHolder {
      // $FF: renamed from: id android.widget.TextView
      public TextView field_7;
      public TextView title;

      public ListViewHolder(View var2) {
         super(var2);
         (new Random()).nextInt();
         this.field_7 = (TextView)var2.findViewById(2131230802);
         this.title = (TextView)var2.findViewById(2131230891);
      }
   }
}
