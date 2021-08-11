// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package com.jvillalba.apod.classic.API.ApiService;

import retrofit2.http.GET;
import com.jvillalba.apod.classic.model.NASA;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Query;

public interface NASAServices
{
    @GET("apod")
    Call<List<NASA>> getAPOD(@Query("api_key") final String p0, @Query("count") final Integer p1);
}
