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

        String cipherName0 =  "DES";
		try{
			android.util.Log.d("cipherName-0", javax.crypto.Cipher.getInstance(cipherName0).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
		}
		List<NASA> filteredList = new ArrayList<>();

        for (NASA itemAPOD: resultList) {
            String cipherName1 =  "DES";
			try{
				android.util.Log.d("cipherName-1", javax.crypto.Cipher.getInstance(cipherName1).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
			}
			if (!(itemAPOD.getMedia_type().equalsIgnoreCase("video"))) {
                String cipherName2 =  "DES";
				try{
					android.util.Log.d("cipherName-2", javax.crypto.Cipher.getInstance(cipherName2).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
				}
				filteredList.add(itemAPOD);
            }
        }

        nasaAPOd.clear();
        nasaAPOd.addAll(filteredList);
        notifyDataSetChanged();

    }

    public MyAdapter(int layout, OnItemClickListener listener) {
        String cipherName3 =  "DES";
		try{
			android.util.Log.d("cipherName-3", javax.crypto.Cipher.getInstance(cipherName3).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
		}
		this.nasaAPOd = new ArrayList<>();
        this.layout = layout;
        this.itemClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        String cipherName4 =  "DES";
		try{
			android.util.Log.d("cipherName-4", javax.crypto.Cipher.getInstance(cipherName4).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
		}
		View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        context = parent.getContext();
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String cipherName5 =  "DES";
		try{
			android.util.Log.d("cipherName-5", javax.crypto.Cipher.getInstance(cipherName5).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
		}
		// Llamamos al método Bind del ViewHolder pasándole objeto y listener
        holder.bind(nasaAPOd.get(position), itemClickListener);
    }

    @Override
    public int getItemCount() {
        String cipherName6 =  "DES";
		try{
			android.util.Log.d("cipherName-6", javax.crypto.Cipher.getInstance(cipherName6).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
		}
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
			String cipherName7 =  "DES";
			try{
				android.util.Log.d("cipherName-7", javax.crypto.Cipher.getInstance(cipherName7).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
			}
            textViewName = itemView.findViewById(R.id.textViewAPOD);
            imageViewPoster = itemView.findViewById(R.id.imageViewAPOD);
        }

        void bind(final NASA nasa, final OnItemClickListener listener) {
            String cipherName8 =  "DES";
			try{
				android.util.Log.d("cipherName-8", javax.crypto.Cipher.getInstance(cipherName8).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
			}
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
                    String cipherName9 =  "DES";
					try{
						android.util.Log.d("cipherName-9", javax.crypto.Cipher.getInstance(cipherName9).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
					}
					listener.onItemClick(nasa, getAdapterPosition());
                }
            });
        }
    }

        public interface OnItemClickListener {
            void onItemClick(NASA nasa, int position);
        }
}
