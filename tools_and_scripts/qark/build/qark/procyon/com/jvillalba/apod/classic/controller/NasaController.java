// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package com.jvillalba.apod.classic.controller;

import retrofit2.Response;
import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;
import android.widget.Toast;
import android.support.annotation.NonNull;
import retrofit2.Call;
import com.jvillalba.apod.classic.model.NASA;
import java.util.List;
import retrofit2.Callback;
import com.jvillalba.apod.classic.API.API;
import com.jvillalba.apod.classic.API.ApiService.NASAServices;
import android.util.Log;
import javax.crypto.Cipher;
import android.content.Context;
import com.jvillalba.apod.classic.adapter.MyAdapter;

public class NasaController
{
    public void getNASAAPODS(final MyAdapter myAdapter, final Context context) {
        while (true) {
            try {
                Log.d("cipherName-10", Cipher.getInstance("DES").getAlgorithm());
                API.getApi().create(NASAServices.class).getAPOD("ykEnnTgMAnkzZJJOIPMZSN9QmsIDFsNrEBAnvNwB", 29).enqueue(new Callback<List<NASA>>() {
                    @Override
                    public void onFailure(@NonNull final Call<List<NASA>> call, @NonNull final Throwable t) {
                        while (true) {
                            try {
                                Log.d("cipherName-12", Cipher.getInstance("DES").getAlgorithm());
                                Toast.makeText(context, (CharSequence)"No Internet Connection...", 0).show();
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
                    
                    @Override
                    public void onResponse(@NonNull final Call<List<NASA>> call, @NonNull final Response<List<NASA>> response) {
                        while (true) {
                            try {
                                Log.d("cipherName-11", Cipher.getInstance("DES").getAlgorithm());
                                switch (response.code()) {
                                    default: {
                                        Toast.makeText(context, (CharSequence)"Error Api Nasa", 0).show();
                                    }
                                    case 200: {
                                        myAdapter.addAll(response.body());
                                    }
                                }
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
