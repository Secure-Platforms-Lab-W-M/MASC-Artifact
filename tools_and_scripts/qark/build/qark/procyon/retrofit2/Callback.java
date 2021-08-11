// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package retrofit2;

public interface Callback<T>
{
    void onFailure(final Call<T> p0, final Throwable p1);
    
    void onResponse(final Call<T> p0, final Response<T> p1);
}
