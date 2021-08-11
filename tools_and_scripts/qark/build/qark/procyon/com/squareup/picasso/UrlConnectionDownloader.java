// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package com.squareup.picasso;

import java.io.File;
import android.net.http.HttpResponseCache;
import java.net.URL;
import java.net.HttpURLConnection;
import android.os.Build$VERSION;
import android.net.Uri;
import java.io.IOException;
import android.content.Context;

public class UrlConnectionDownloader implements Downloader
{
    private static final ThreadLocal<StringBuilder> CACHE_HEADER_BUILDER;
    private static final String FORCE_CACHE = "only-if-cached,max-age=2147483647";
    static final String RESPONSE_SOURCE = "X-Android-Response-Source";
    static volatile Object cache;
    private static final Object lock;
    private final Context context;
    
    static {
        lock = new Object();
        CACHE_HEADER_BUILDER = new ThreadLocal<StringBuilder>() {
            @Override
            protected StringBuilder initialValue() {
                return new StringBuilder();
            }
        };
    }
    
    public UrlConnectionDownloader(final Context context) {
        this.context = context.getApplicationContext();
    }
    
    private static void installCacheIfNeeded(final Context context) {
        if (UrlConnectionDownloader.cache == null) {
            try {
                synchronized (UrlConnectionDownloader.lock) {
                    if (UrlConnectionDownloader.cache == null) {
                        UrlConnectionDownloader.cache = ResponseCacheIcs.install(context);
                    }
                }
            }
            catch (IOException ex) {}
        }
    }
    
    @Override
    public Response load(final Uri uri, final int n) throws IOException {
        if (Build$VERSION.SDK_INT >= 14) {
            installCacheIfNeeded(this.context);
        }
        final HttpURLConnection openConnection = this.openConnection(uri);
        openConnection.setUseCaches(true);
        if (n != 0) {
            String string;
            if (NetworkPolicy.isOfflineOnly(n)) {
                string = "only-if-cached,max-age=2147483647";
            }
            else {
                final StringBuilder sb = UrlConnectionDownloader.CACHE_HEADER_BUILDER.get();
                sb.setLength(0);
                if (!NetworkPolicy.shouldReadFromDiskCache(n)) {
                    sb.append("no-cache");
                }
                if (!NetworkPolicy.shouldWriteToDiskCache(n)) {
                    if (sb.length() > 0) {
                        sb.append(',');
                    }
                    sb.append("no-store");
                }
                string = sb.toString();
            }
            openConnection.setRequestProperty("Cache-Control", string);
        }
        final int responseCode = openConnection.getResponseCode();
        if (responseCode >= 300) {
            openConnection.disconnect();
            throw new ResponseException(responseCode + " " + openConnection.getResponseMessage(), n, responseCode);
        }
        return new Response(openConnection.getInputStream(), Utils.parseResponseSourceHeader(openConnection.getHeaderField("X-Android-Response-Source")), openConnection.getHeaderFieldInt("Content-Length", -1));
    }
    
    protected HttpURLConnection openConnection(final Uri uri) throws IOException {
        final HttpURLConnection httpURLConnection = (HttpURLConnection)new URL(uri.toString()).openConnection();
        httpURLConnection.setConnectTimeout(15000);
        httpURLConnection.setReadTimeout(20000);
        return httpURLConnection;
    }
    
    @Override
    public void shutdown() {
        if (Build$VERSION.SDK_INT >= 14 && UrlConnectionDownloader.cache != null) {
            ResponseCacheIcs.close(UrlConnectionDownloader.cache);
        }
    }
    
    private static class ResponseCacheIcs
    {
        static void close(final Object o) {
            try {
                ((HttpResponseCache)o).close();
            }
            catch (IOException ex) {}
        }
        
        static Object install(final Context context) throws IOException {
            final File defaultCacheDir = Utils.createDefaultCacheDir(context);
            HttpResponseCache httpResponseCache;
            if ((httpResponseCache = HttpResponseCache.getInstalled()) == null) {
                httpResponseCache = HttpResponseCache.install(defaultCacheDir, Utils.calculateDiskCacheSize(defaultCacheDir));
            }
            return httpResponseCache;
        }
    }
}
