package com.jvillalba.apod.classic.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jvillalba.apod.classic.R;
import com.jvillalba.apod.classic.model.NASA;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>
{

    private List<NASA> nasaAPOd;
    private int layout;
    private OnItemClickListener itemClickListener;

    private Context context;

    public void addAll(List<NASA> resultList) {

        List<NASA> filteredList = new ArrayList<>();

        for (NASA itemAPOD: resultList) {
            if (!(itemAPOD.getMedia_type().equalsIgnoreCase("video"))) {
                filteredList.add(itemAPOD);
            }
        }

        nasaAPOd.clear();
        nasaAPOd.addAll(filteredList);
        notifyDataSetChanged();

    }

    public MyAdapter(int layout, OnItemClickListener listener) {
        this.nasaAPOd = new ArrayList<>();
        this.layout = layout;
        this.itemClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        context = parent.getContext();
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Llamamos al método Bind del ViewHolder pasándole objeto y listener
        holder.bind(nasaAPOd.get(position), itemClickListener);
    }

    @Override
    public int getItemCount() {
        return nasaAPOd.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        // Elementos UI a rellenar
        TextView textViewName;
        ImageView imageViewPoster;

        ViewHolder(View itemView) {
            // Recibe la View completa. La pasa al constructor padre y enlazamos referencias UI
            // con nuestras propiedades ViewHolder declaradas justo arriba.
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewAPOD);
            imageViewPoster = itemView.findViewById(R.id.imageViewAPOD);
        }

        void bind(final NASA nasa, final OnItemClickListener listener) {
            // Procesamos los datos a renderizar
            textViewName.setText(nasa.getTitle());
           Picasso.with(context)
                   .load(nasa.getUrl())
                   .error(R.mipmap.ic_launcher_foreground)
                   .fit()
                   .into(imageViewPoster);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(nasa, getAdapterPosition());
                }
            });
        }
    }

        public interface OnItemClickListener {
            void onItemClick(NASA nasa, int position);
        }
}
