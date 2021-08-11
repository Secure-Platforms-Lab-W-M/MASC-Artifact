package com.example.recyclerviewminimal;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
   private List entitiesList = new ArrayList();
   private ListEntityAdapter listAdapter;
   private RecyclerView recyclerView;

   private void prepareEntitiesData() {
      ListEntity var1 = new ListEntity("A", "1");
      this.entitiesList.add(var1);
      var1 = new ListEntity("B", "2");
      this.entitiesList.add(var1);
      var1 = new ListEntity("C", "3");
      this.entitiesList.add(var1);
      var1 = new ListEntity("D", "4");
      this.entitiesList.add(var1);
      var1 = new ListEntity("E", "5");
      this.entitiesList.add(var1);
      this.listAdapter.notifyDataSetChanged();
   }

   protected void onCreate(Bundle var1) {
      super.onCreate(var1);
      this.setContentView(2131361819);
      this.recyclerView = (RecyclerView)this.findViewById(2131230838);
      this.listAdapter = new ListEntityAdapter(this.entitiesList);
      this.recyclerView.setHasFixedSize(true);
      LinearLayoutManager var2 = new LinearLayoutManager(this.getApplicationContext());
      this.recyclerView.setLayoutManager(var2);
      this.recyclerView.addItemDecoration(new DividerItemDecoration(this, 1));
      this.recyclerView.setItemAnimator(new DefaultItemAnimator());
      this.recyclerView.setAdapter(this.listAdapter);
      this.recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this.getApplicationContext(), this.recyclerView, new RecyclerTouchListener.ClickListener() {
         public void onClick(View var1, int var2) {
            ListEntity var5 = (ListEntity)MainActivity.this.entitiesList.get(var2);
            Context var3 = MainActivity.this.getApplicationContext();
            StringBuilder var4 = new StringBuilder();
            var4.append(var5.getTitle());
            var4.append(" is selected!");
            Toast.makeText(var3, var4.toString(), 0).show();
         }

         public void onLongClick(View var1, int var2) {
         }
      }));
      this.prepareEntitiesData();
   }
}
