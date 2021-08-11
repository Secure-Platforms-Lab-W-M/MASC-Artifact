// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package com.example.recyclerviewminimal;

import android.widget.Toast;
import android.view.View;
import android.support.v7.widget.DefaultItemAnimator;
import android.content.Context;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.os.Bundle;
import java.util.ArrayList;
import android.support.v7.widget.RecyclerView;
import java.util.List;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity
{
    private List<ListEntity> entitiesList;
    private ListEntityAdapter listAdapter;
    private RecyclerView recyclerView;
    
    public MainActivity() {
        this.entitiesList = new ArrayList<ListEntity>();
    }
    
    private void prepareEntitiesData() {
        this.entitiesList.add(new ListEntity("A", "1"));
        this.entitiesList.add(new ListEntity("B", "2"));
        this.entitiesList.add(new ListEntity("C", "3"));
        this.entitiesList.add(new ListEntity("D", "4"));
        this.entitiesList.add(new ListEntity("E", "5"));
        ((RecyclerView.Adapter)this.listAdapter).notifyDataSetChanged();
    }
    
    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(2131361819);
        this.recyclerView = this.findViewById(2131230838);
        this.listAdapter = new ListEntityAdapter(this.entitiesList);
        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.setLayoutManager((RecyclerView.LayoutManager)new LinearLayoutManager(this.getApplicationContext()));
        this.recyclerView.addItemDecoration((RecyclerView.ItemDecoration)new DividerItemDecoration((Context)this, 1));
        this.recyclerView.setItemAnimator((RecyclerView.ItemAnimator)new DefaultItemAnimator());
        this.recyclerView.setAdapter((RecyclerView.Adapter)this.listAdapter);
        this.recyclerView.addOnItemTouchListener((RecyclerView.OnItemTouchListener)new RecyclerTouchListener(this.getApplicationContext(), this.recyclerView, (RecyclerTouchListener.ClickListener)new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(final View view, final int n) {
                final ListEntity listEntity = MainActivity.this.entitiesList.get(n);
                final Context applicationContext = MainActivity.this.getApplicationContext();
                final StringBuilder sb = new StringBuilder();
                sb.append(listEntity.getTitle());
                sb.append(" is selected!");
                Toast.makeText(applicationContext, (CharSequence)sb.toString(), 0).show();
            }
            
            @Override
            public void onLongClick(final View view, final int n) {
            }
        }));
        this.prepareEntitiesData();
    }
}
