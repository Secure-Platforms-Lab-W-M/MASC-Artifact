// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package com.example.recyclerviewminimal;

import java.util.Random;
import android.view.View;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import java.util.List;
import android.support.v7.widget.RecyclerView;

public class ListEntityAdapter extends Adapter<ListViewHolder>
{
    private List<ListEntity> entitiesList;
    
    public ListEntityAdapter(final List<ListEntity> entitiesList) {
        this.entitiesList = entitiesList;
    }
    
    @Override
    public int getItemCount() {
        return this.entitiesList.size();
    }
    
    public void onBindViewHolder(final ListViewHolder listViewHolder, final int n) {
        final ListEntity listEntity = this.entitiesList.get(n);
        listViewHolder.id.setText((CharSequence)listEntity.getId());
        listViewHolder.title.setText((CharSequence)listEntity.getTitle());
    }
    
    public ListViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
        return new ListViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(2131361834, viewGroup, false));
    }
    
    public class ListViewHolder extends ViewHolder
    {
        public TextView id;
        public TextView title;
        
        public ListViewHolder(final View view) {
            super(view);
            new Random().nextInt();
            this.id = (TextView)view.findViewById(2131230802);
            this.title = (TextView)view.findViewById(2131230891);
        }
    }
}
