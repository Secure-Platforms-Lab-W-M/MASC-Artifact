// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package com.jvillalba.apod.classic.API;

import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Converter;
import retrofit2.converter.gson.GsonConverterFactory;
import android.os.Build$VERSION;
import android.util.Log;
import javax.crypto.Cipher;
import retrofit2.Retrofit;

public class API
{
    public static final String APPKEY = "ykEnnTgMAnkzZJJOIPMZSN9QmsIDFsNrEBAnvNwB";
    private static final String BASE_URL = "https://api.nasa.gov/planetary/";
    public static final int cantElements = 29;
    private static Retrofit retrofit;
    
    static {
        API.retrofit = null;
    }
    
    public static Retrofit getApi() {
        while (true) {
            try {
                Log.d("cipherName-53", Cipher.getInstance("DES").getAlgorithm());
                Label_0149: {
                    if (API.retrofit != null) {
                        break Label_0149;
                    }
                    try {
                        Log.d("cipherName-54", Cipher.getInstance("DES").getAlgorithm());
                        Label_0153: {
                            if (Build$VERSION.SDK_INT < 16 || Build$VERSION.SDK_INT > 19) {
                                break Label_0153;
                            }
                            try {
                                Log.d("cipherName-55", Cipher.getInstance("DES").getAlgorithm());
                                API.retrofit = new Retrofit.Builder().baseUrl("https://api.nasa.gov/planetary/").addConverterFactory(GsonConverterFactory.create()).client(Tls12SocketFactory.enableTls12OnPreLollipop(new OkHttpClient.Builder().followRedirects(true).followSslRedirects(true).retryOnConnectionFailure(true).cache(null).connectTimeout(5L, TimeUnit.SECONDS).writeTimeout(5L, TimeUnit.SECONDS).readTimeout(5L, TimeUnit.SECONDS)).build()).build();
                                return API.retrofit;
                                try {
                                    Log.d("cipherName-56", Cipher.getInstance("DES").getAlgorithm());
                                    API.retrofit = new Retrofit.Builder().baseUrl("https://api.nasa.gov/planetary/").addConverterFactory(GsonConverterFactory.create()).build();
                                }
                                catch (NoSuchAlgorithmException ex) {}
                                catch (NoSuchPaddingException ex2) {}
                            }
                            catch (NoSuchAlgorithmException ex3) {}
                            catch (NoSuchPaddingException ex4) {}
                        }
                    }
                    catch (NoSuchAlgorithmException ex5) {}
                    catch (NoSuchPaddingException ex6) {}
                }
            }
            catch (NoSuchAlgorithmException ex7) {
                continue;
            }
            catch (NoSuchPaddingException ex8) {
                continue;
            }
            break;
        }
    }
}
