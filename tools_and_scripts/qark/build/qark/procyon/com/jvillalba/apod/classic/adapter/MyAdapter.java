// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package com.jvillalba.apod.classic.adapter;

import android.view.View$OnClickListener;
import com.squareup.picasso.Picasso;
import android.widget.TextView;
import android.widget.ImageView;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import java.util.Collection;
import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import android.util.Log;
import javax.crypto.Cipher;
import com.jvillalba.apod.classic.model.NASA;
import java.util.List;
import android.content.Context;
import android.support.v7.widget.RecyclerView;

public class MyAdapter extends Adapter<ViewHolder>
{
    private Context context;
    private OnItemClickListener itemClickListener;
    private int layout;
    private List<NASA> nasaAPOd;
    
    public MyAdapter(final int layout, final OnItemClickListener itemClickListener) {
        while (true) {
            try {
                Log.d("cipherName-3", Cipher.getInstance("DES").getAlgorithm());
                this.nasaAPOd = new ArrayList<NASA>();
                this.layout = layout;
                this.itemClickListener = itemClickListener;
            }
            catch (NoSuchAlgorithmException ex) {
                continue;
            }
            catch (NoSuchPaddingException ex2) {
                continue;
            }
            break;
        }
    }
    
    public void addAll(List<NASA> iterator) {
        while (true) {
            try {
                Log.d("cipherName-0", Cipher.getInstance("DES").getAlgorithm());
                final ArrayList<NASA> list = new ArrayList<NASA>();
                iterator = ((List<NASA>)iterator).iterator();
                while (true) {
                    Label_0099: {
                        if (!iterator.hasNext()) {
                            break Label_0099;
                        }
                        final NASA nasa = iterator.next();
                        try {
                            Log.d("cipherName-1", Cipher.getInstance("DES").getAlgorithm());
                            if (nasa.getMedia_type().equalsIgnoreCase("video")) {
                                continue;
                            }
                            try {
                                Log.d("cipherName-2", Cipher.getInstance("DES").getAlgorithm());
                                list.add(nasa);
                                continue;
                                this.nasaAPOd.clear();
                                this.nasaAPOd.addAll(list);
                                ((RecyclerView.Adapter)this).notifyDataSetChanged();
                            }
                            catch (NoSuchAlgorithmException ex) {}
                            catch (NoSuchPaddingException ex2) {}
                        }
                        catch (NoSuchAlgorithmException ex3) {}
                        catch (NoSuchPaddingException ex4) {}
                    }
                }
            }
            catch (NoSuchAlgorithmException ex5) {
                continue;
            }
            catch (NoSuchPaddingException ex6) {
                continue;
            }
            break;
        }
    }
    
    @Override
    public int getItemCount() {
        try {
            Log.d("cipherName-6", Cipher.getInstance("DES").getAlgorithm());
            return this.nasaAPOd.size();
        }
        catch (NoSuchAlgorithmException ex) {
            return this.nasaAPOd.size();
        }
        catch (NoSuchPaddingException ex2) {
            return this.nasaAPOd.size();
        }
    }
    
    public void onBindViewHolder(final ViewHolder viewHolder, final int n) {
        while (true) {
            try {
                Log.d("cipherName-5", Cipher.getInstance("DES").getAlgorithm());
                viewHolder.bind(this.nasaAPOd.get(n), this.itemClickListener);
            }
            catch (NoSuchAlgorithmException ex) {
                continue;
            }
            catch (NoSuchPaddingException ex2) {
                continue;
            }
            break;
        }
    }
    
    public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
        while (true) {
            try {
                Log.d("cipherName-4", Cipher.getInstance("DES").getAlgorithm());
                final View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(this.layout, viewGroup, false);
                this.context = viewGroup.getContext();
                return new ViewHolder(inflate);
            }
            catch (NoSuchAlgorithmException ex) {
                continue;
            }
            catch (NoSuchPaddingException ex2) {
                continue;
            }
            break;
        }
    }
    
    public interface OnItemClickListener
    {
        void onItemClick(final NASA p0, final int p1);
    }
    
    class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imageViewPoster;
        TextView textViewName;
        
        ViewHolder(final View view) {
            super(view);
            while (true) {
                try {
                    Log.d("cipherName-7", Cipher.getInstance("DES").getAlgorithm());
                    this.textViewName = (TextView)view.findViewById(2131230890);
                    this.imageViewPoster = (ImageView)view.findViewById(2131230807);
                }
                catch (NoSuchAlgorithmException ex) {
                    continue;
                }
                catch (NoSuchPaddingException ex2) {
                    continue;
                }
                break;
            }
        }
        
        void bind(final NASA nasa, final OnItemClickListener onItemClickListener) {
            while (true) {
                try {
                    Log.d("cipherName-8", Cipher.getInstance("DES").getAlgorithm());
                    this.textViewName.setText((CharSequence)nasa.getTitle());
                    Picasso.with(MyAdapter.this.context).load(nasa.getUrl()).error(2131492865).fit().into(this.imageViewPoster);
                    this.itemView.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                        public void onClick(final View view) {
                            while (true) {
                                try {
                                    Log.d("cipherName-9", Cipher.getInstance("DES").getAlgorithm());
                                    onItemClickListener.onItemClick(nasa, ((RecyclerView.ViewHolder)ViewHolder.this).getAdapterPosition());
                                }
                                catch (NoSuchAlgorithmException ex) {
                                    continue;
                                }
                                catch (NoSuchPaddingException ex2) {
                                    continue;
                                }
                                break;
                            }
                        }
                    });
                }
                catch (NoSuchAlgorithmException ex) {
                    continue;
                }
                catch (NoSuchPaddingException ex2) {
                    continue;
                }
                break;
            }
        }
    }
}
