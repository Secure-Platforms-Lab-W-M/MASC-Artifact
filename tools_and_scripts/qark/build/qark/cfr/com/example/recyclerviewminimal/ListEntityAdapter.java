/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.ViewGroup
 *  android.widget.TextView
 */
package com.example.recyclerviewminimal;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.recyclerviewminimal.ListEntity;
import java.util.List;
import java.util.Random;

public class ListEntityAdapter
extends RecyclerView.Adapter<ListViewHolder> {
    private List<ListEntity> entitiesList;

    public ListEntityAdapter(List<ListEntity> list) {
        this.entitiesList = list;
    }

    @Override
    public int getItemCount() {
        return this.entitiesList.size();
    }

    @Override
    public void onBindViewHolder(ListViewHolder listViewHolder, int n) {
        ListEntity listEntity = this.entitiesList.get(n);
        listViewHolder.id.setText((CharSequence)listEntity.getId());
        listViewHolder.title.setText((CharSequence)listEntity.getTitle());
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup viewGroup, int n) {
        return new ListViewHolder(LayoutInflater.from((Context)viewGroup.getContext()).inflate(2131361834, viewGroup, false));
    }

    public class ListViewHolder
    extends RecyclerView.ViewHolder {
        public TextView id;
        public TextView title;

        public ListViewHolder(View view) {
            super(view);
            new Random().nextInt();
            this.id = (TextView)view.findViewById(2131230802);
            this.title = (TextView)view.findViewById(2131230891);
        }
    }

}

