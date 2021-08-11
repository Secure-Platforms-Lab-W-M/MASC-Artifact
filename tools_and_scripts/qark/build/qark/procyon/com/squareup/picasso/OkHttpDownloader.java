// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package com.squareup.picasso;

import com.squareup.okhttp.ResponseBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.Request$Builder;
import com.squareup.okhttp.CacheControl$Builder;
import com.squareup.okhttp.CacheControl;
import android.net.Uri;
import java.util.concurrent.TimeUnit;
import java.io.IOException;
import com.squareup.okhttp.Cache;
import java.io.File;
import android.content.Context;
import com.squareup.okhttp.OkHttpClient;

public class OkHttpDownloader implements Downloader
{
    private final OkHttpClient client;
    
    public OkHttpDownloader(final Context context) {
        this(Utils.createDefaultCacheDir(context));
    }
    
    public OkHttpDownloader(final Context context, final long n) {
        this(Utils.createDefaultCacheDir(context), n);
    }
    
    public OkHttpDownloader(final OkHttpClient client) {
        this.client = client;
    }
    
    public OkHttpDownloader(final File file) {
        this(file, Utils.calculateDiskCacheSize(file));
    }
    
    public OkHttpDownloader(final File file, final long n) {
        this(defaultOkHttpClient());
        try {
            this.client.setCache(new Cache(file, n));
        }
        catch (IOException ex) {}
    }
    
    private static OkHttpClient defaultOkHttpClient() {
        final OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(15000L, TimeUnit.MILLISECONDS);
        okHttpClient.setReadTimeout(20000L, TimeUnit.MILLISECONDS);
        okHttpClient.setWriteTimeout(20000L, TimeUnit.MILLISECONDS);
        return okHttpClient;
    }
    
    protected final OkHttpClient getClient() {
        return this.client;
    }
    
    @Override
    public Response load(final Uri uri, final int n) throws IOException {
        CacheControl cacheControl = null;
        if (n != 0) {
            if (NetworkPolicy.isOfflineOnly(n)) {
                cacheControl = CacheControl.FORCE_CACHE;
            }
            else {
                final CacheControl$Builder cacheControl$Builder = new CacheControl$Builder();
                if (!NetworkPolicy.shouldReadFromDiskCache(n)) {
                    cacheControl$Builder.noCache();
                }
                if (!NetworkPolicy.shouldWriteToDiskCache(n)) {
                    cacheControl$Builder.noStore();
                }
                cacheControl = cacheControl$Builder.build();
            }
        }
        final Request$Builder url = new Request$Builder().url(uri.toString());
        if (cacheControl != null) {
            url.cacheControl(cacheControl);
        }
        final com.squareup.okhttp.Response execute = this.client.newCall(url.build()).execute();
        final int code = execute.code();
        if (code >= 300) {
            execute.body().close();
            throw new ResponseException(code + " " + execute.message(), n, code);
        }
        final boolean b = execute.cacheResponse() != null;
        final ResponseBody body = execute.body();
        return new Response(body.byteStream(), b, body.contentLength());
    }
    
    @Override
    public void shutdown() {
        final Cache cache = this.client.getCache();
        if (cache == null) {
            return;
        }
        try {
            cache.close();
        }
        catch (IOException ex) {}
    }
}
