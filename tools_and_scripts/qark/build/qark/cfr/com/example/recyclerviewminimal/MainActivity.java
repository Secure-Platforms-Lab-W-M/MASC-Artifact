/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Bundle
 *  android.view.View
 *  android.widget.Toast
 */
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
import com.example.recyclerviewminimal.ListEntity;
import com.example.recyclerviewminimal.ListEntityAdapter;
import com.example.recyclerviewminimal.RecyclerTouchListener;
import java.util.ArrayList;
import java.util.List;

public class MainActivity
extends AppCompatActivity {
    private List<ListEntity> entitiesList = new ArrayList<ListEntity>();
    private ListEntityAdapter listAdapter;
    private RecyclerView recyclerView;

    private void prepareEntitiesData() {
        ListEntity listEntity = new ListEntity("A", "1");
        this.entitiesList.add(listEntity);
        listEntity = new ListEntity("B", "2");
        this.entitiesList.add(listEntity);
        listEntity = new ListEntity("C", "3");
        this.entitiesList.add(listEntity);
        listEntity = new ListEntity("D", "4");
        this.entitiesList.add(listEntity);
        listEntity = new ListEntity("E", "5");
        this.entitiesList.add(listEntity);
        this.listAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle object) {
        super.onCreate((Bundle)object);
        this.setContentView(2131361819);
        this.recyclerView = (RecyclerView)this.findViewById(2131230838);
        this.listAdapter = new ListEntityAdapter(this.entitiesList);
        this.recyclerView.setHasFixedSize(true);
        object = new LinearLayoutManager(this.getApplicationContext());
        this.recyclerView.setLayoutManager((RecyclerView.LayoutManager)object);
        this.recyclerView.addItemDecoration(new DividerItemDecoration((Context)this, 1));
        this.recyclerView.setItemAnimator(new DefaultItemAnimator());
        this.recyclerView.setAdapter(this.listAdapter);
        this.recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this.getApplicationContext(), this.recyclerView, new RecyclerTouchListener.ClickListener(){

            @Override
            public void onClick(View object, int n) {
                object = (ListEntity)MainActivity.this.entitiesList.get(n);
                Context context = MainActivity.this.getApplicationContext();
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(object.getTitle());
                stringBuilder.append(" is selected!");
                Toast.makeText((Context)context, (CharSequence)stringBuilder.toString(), (int)0).show();
            }

            @Override
            public void onLongClick(View view, int n) {
            }
        }));
        this.prepareEntitiesData();
    }

}

