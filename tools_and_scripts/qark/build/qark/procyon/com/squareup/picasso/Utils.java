// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package com.squareup.picasso;

import java.util.concurrent.ThreadFactory;
import android.os.Process;
import android.annotation.TargetApi;
import java.io.ByteArrayOutputStream;
import android.util.Log;
import android.content.ContentResolver;
import android.provider.Settings$System;
import android.content.pm.PackageManager$NameNotFoundException;
import java.io.FileNotFoundException;
import android.content.res.Resources;
import java.util.List;
import android.graphics.Bitmap;
import android.os.Message;
import android.os.Handler;
import android.os.Looper;
import java.io.IOException;
import java.io.InputStream;
import android.os.Build$VERSION;
import android.app.ActivityManager;
import android.content.Context;
import android.os.StatFs;
import java.io.File;

final class Utils
{
    static final int DEFAULT_CONNECT_TIMEOUT_MILLIS = 15000;
    static final int DEFAULT_READ_TIMEOUT_MILLIS = 20000;
    static final int DEFAULT_WRITE_TIMEOUT_MILLIS = 20000;
    private static final int KEY_PADDING = 50;
    static final char KEY_SEPARATOR = '\n';
    static final StringBuilder MAIN_THREAD_KEY_BUILDER;
    private static final int MAX_DISK_CACHE_SIZE = 52428800;
    private static final int MIN_DISK_CACHE_SIZE = 5242880;
    static final String OWNER_DISPATCHER = "Dispatcher";
    static final String OWNER_HUNTER = "Hunter";
    static final String OWNER_MAIN = "Main";
    private static final String PICASSO_CACHE = "picasso-cache";
    static final String THREAD_IDLE_NAME = "Picasso-Idle";
    static final int THREAD_LEAK_CLEANING_MS = 1000;
    static final String THREAD_PREFIX = "Picasso-";
    static final String VERB_BATCHED = "batched";
    static final String VERB_CANCELED = "canceled";
    static final String VERB_CHANGED = "changed";
    static final String VERB_COMPLETED = "completed";
    static final String VERB_CREATED = "created";
    static final String VERB_DECODED = "decoded";
    static final String VERB_DELIVERED = "delivered";
    static final String VERB_ENQUEUED = "enqueued";
    static final String VERB_ERRORED = "errored";
    static final String VERB_EXECUTING = "executing";
    static final String VERB_IGNORED = "ignored";
    static final String VERB_JOINED = "joined";
    static final String VERB_PAUSED = "paused";
    static final String VERB_REMOVED = "removed";
    static final String VERB_REPLAYING = "replaying";
    static final String VERB_RESUMED = "resumed";
    static final String VERB_RETRYING = "retrying";
    static final String VERB_TRANSFORMED = "transformed";
    private static final String WEBP_FILE_HEADER_RIFF = "RIFF";
    private static final int WEBP_FILE_HEADER_SIZE = 12;
    private static final String WEBP_FILE_HEADER_WEBP = "WEBP";
    
    static {
        MAIN_THREAD_KEY_BUILDER = new StringBuilder();
    }
    
    private Utils() {
    }
    
    static long calculateDiskCacheSize(final File file) {
        long n = 5242880L;
        try {
            final StatFs statFs = new StatFs(file.getAbsolutePath());
            n = statFs.getBlockCount() * (long)statFs.getBlockSize() / 50L;
            return Math.max(Math.min(n, 52428800L), 5242880L);
        }
        catch (IllegalArgumentException ex) {
            return Math.max(Math.min(n, 52428800L), 5242880L);
        }
    }
    
    static int calculateMemoryCacheSize(final Context context) {
        final ActivityManager activityManager = getService(context, "activity");
        int n;
        if ((context.getApplicationInfo().flags & 0x100000) != 0x0) {
            n = 1;
        }
        else {
            n = 0;
        }
        int n2 = activityManager.getMemoryClass();
        if (n != 0) {
            n2 = n2;
            if (Build$VERSION.SDK_INT >= 11) {
                n2 = ActivityManagerHoneycomb.getLargeMemoryClass(activityManager);
            }
        }
        return 1048576 * n2 / 7;
    }
    
    static void checkMain() {
        if (!isMain()) {
            throw new IllegalStateException("Method call should happen from the main thread.");
        }
    }
    
    static void checkNotMain() {
        if (isMain()) {
            throw new IllegalStateException("Method call should not happen from the main thread.");
        }
    }
    
    static <T> T checkNotNull(final T t, final String s) {
        if (t == null) {
            throw new NullPointerException(s);
        }
        return t;
    }
    
    static void closeQuietly(final InputStream inputStream) {
        if (inputStream == null) {
            return;
        }
        try {
            inputStream.close();
        }
        catch (IOException ex) {}
    }
    
    static File createDefaultCacheDir(final Context context) {
        final File file = new File(context.getApplicationContext().getCacheDir(), "picasso-cache");
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }
    
    static Downloader createDefaultDownloader(final Context context) {
        try {
            Class.forName("com.squareup.okhttp.OkHttpClient");
            return OkHttpLoaderCreator.create(context);
        }
        catch (ClassNotFoundException ex) {
            return new UrlConnectionDownloader(context);
        }
    }
    
    static String createKey(final Request request) {
        final String key = createKey(request, Utils.MAIN_THREAD_KEY_BUILDER);
        Utils.MAIN_THREAD_KEY_BUILDER.setLength(0);
        return key;
    }
    
    static String createKey(final Request request, final StringBuilder sb) {
        if (request.stableKey != null) {
            sb.ensureCapacity(request.stableKey.length() + 50);
            sb.append(request.stableKey);
        }
        else if (request.uri != null) {
            final String string = request.uri.toString();
            sb.ensureCapacity(string.length() + 50);
            sb.append(string);
        }
        else {
            sb.ensureCapacity(50);
            sb.append(request.resourceId);
        }
        sb.append('\n');
        if (request.rotationDegrees != 0.0f) {
            sb.append("rotation:").append(request.rotationDegrees);
            if (request.hasRotationPivot) {
                sb.append('@').append(request.rotationPivotX).append('x').append(request.rotationPivotY);
            }
            sb.append('\n');
        }
        if (request.hasSize()) {
            sb.append("resize:").append(request.targetWidth).append('x').append(request.targetHeight);
            sb.append('\n');
        }
        if (request.centerCrop) {
            sb.append("centerCrop").append('\n');
        }
        else if (request.centerInside) {
            sb.append("centerInside").append('\n');
        }
        if (request.transformations != null) {
            for (int i = 0; i < request.transformations.size(); ++i) {
                sb.append(request.transformations.get(i).key());
                sb.append('\n');
            }
        }
        return sb.toString();
    }
    
    static void flushStackLocalLeaks(final Looper looper) {
        final Handler handler = new Handler(looper) {
            public void handleMessage(final Message message) {
                this.sendMessageDelayed(this.obtainMessage(), 1000L);
            }
        };
        handler.sendMessageDelayed(handler.obtainMessage(), 1000L);
    }
    
    static int getBitmapBytes(final Bitmap bitmap) {
        int byteCount;
        if (Build$VERSION.SDK_INT >= 12) {
            byteCount = BitmapHoneycombMR1.getByteCount(bitmap);
        }
        else {
            byteCount = bitmap.getRowBytes() * bitmap.getHeight();
        }
        if (byteCount < 0) {
            throw new IllegalStateException("Negative size: " + bitmap);
        }
        return byteCount;
    }
    
    static String getLogIdsForHunter(final BitmapHunter bitmapHunter) {
        return getLogIdsForHunter(bitmapHunter, "");
    }
    
    static String getLogIdsForHunter(final BitmapHunter bitmapHunter, final String s) {
        final StringBuilder sb = new StringBuilder(s);
        final Action action = bitmapHunter.getAction();
        if (action != null) {
            sb.append(action.request.logId());
        }
        final List<Action> actions = bitmapHunter.getActions();
        if (actions != null) {
            for (int i = 0; i < actions.size(); ++i) {
                if (i > 0 || action != null) {
                    sb.append(", ");
                }
                sb.append(actions.get(i).request.logId());
            }
        }
        return sb.toString();
    }
    
    static int getResourceId(final Resources resources, final Request request) throws FileNotFoundException {
        if (request.resourceId != 0 || request.uri == null) {
            return request.resourceId;
        }
        final String authority = request.uri.getAuthority();
        if (authority == null) {
            throw new FileNotFoundException("No package provided: " + request.uri);
        }
        final List pathSegments = request.uri.getPathSegments();
        if (pathSegments == null || pathSegments.isEmpty()) {
            throw new FileNotFoundException("No path segments: " + request.uri);
        }
        if (pathSegments.size() == 1) {
            try {
                return Integer.parseInt(pathSegments.get(0));
            }
            catch (NumberFormatException ex) {
                throw new FileNotFoundException("Last path segment is not a resource ID: " + request.uri);
            }
        }
        if (pathSegments.size() == 2) {
            return resources.getIdentifier((String)pathSegments.get(1), (String)pathSegments.get(0), authority);
        }
        throw new FileNotFoundException("More than two path segments: " + request.uri);
    }
    
    static Resources getResources(final Context context, final Request request) throws FileNotFoundException {
        if (request.resourceId != 0 || request.uri == null) {
            return context.getResources();
        }
        final String authority = request.uri.getAuthority();
        if (authority == null) {
            throw new FileNotFoundException("No package provided: " + request.uri);
        }
        try {
            return context.getPackageManager().getResourcesForApplication(authority);
        }
        catch (PackageManager$NameNotFoundException ex) {
            throw new FileNotFoundException("Unable to obtain resources for package: " + request.uri);
        }
    }
    
    static <T> T getService(final Context context, final String s) {
        return (T)context.getSystemService(s);
    }
    
    static boolean hasPermission(final Context context, final String s) {
        return context.checkCallingOrSelfPermission(s) == 0;
    }
    
    static boolean isAirplaneModeOn(final Context context) {
        boolean b = false;
        final ContentResolver contentResolver = context.getContentResolver();
        try {
            if (Settings$System.getInt(contentResolver, "airplane_mode_on", 0) != 0) {
                b = true;
            }
            return b;
        }
        catch (NullPointerException ex) {
            return false;
        }
    }
    
    static boolean isMain() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }
    
    static boolean isWebPFile(final InputStream inputStream) throws IOException {
        final byte[] array = new byte[12];
        boolean b = false;
        if (inputStream.read(array, 0, 12) == 12) {
            if (!"RIFF".equals(new String(array, 0, 4, "US-ASCII")) || !"WEBP".equals(new String(array, 8, 4, "US-ASCII"))) {
                return false;
            }
            b = true;
        }
        return b;
    }
    
    static void log(final String s, final String s2, final String s3) {
        log(s, s2, s3, "");
    }
    
    static void log(final String s, final String s2, final String s3, final String s4) {
        Log.d("Picasso", String.format("%1$-11s %2$-12s %3$s %4$s", s, s2, s3, s4));
    }
    
    static boolean parseResponseSourceHeader(final String s) {
        boolean b = true;
        if (s != null) {
            final String[] split = s.split(" ", 2);
            if ("CACHE".equals(split[0])) {
                return true;
            }
            if (split.length != 1) {
                try {
                    if (!"CONDITIONAL_CACHE".equals(split[0]) || Integer.parseInt(split[1]) != 304) {
                        b = false;
                    }
                    return b;
                }
                catch (NumberFormatException ex) {
                    return false;
                }
            }
        }
        return false;
    }
    
    static byte[] toByteArray(final InputStream inputStream) throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final byte[] array = new byte[4096];
        while (true) {
            final int read = inputStream.read(array);
            if (-1 == read) {
                break;
            }
            byteArrayOutputStream.write(array, 0, read);
        }
        return byteArrayOutputStream.toByteArray();
    }
    
    @TargetApi(11)
    private static class ActivityManagerHoneycomb
    {
        static int getLargeMemoryClass(final ActivityManager activityManager) {
            return activityManager.getLargeMemoryClass();
        }
    }
    
    @TargetApi(12)
    private static class BitmapHoneycombMR1
    {
        static int getByteCount(final Bitmap bitmap) {
            return bitmap.getByteCount();
        }
    }
    
    private static class OkHttpLoaderCreator
    {
        static Downloader create(final Context context) {
            return new OkHttpDownloader(context);
        }
    }
    
    private static class PicassoThread extends Thread
    {
        public PicassoThread(final Runnable runnable) {
            super(runnable);
        }
        
        @Override
        public void run() {
            Process.setThreadPriority(10);
            super.run();
        }
    }
    
    static class PicassoThreadFactory implements ThreadFactory
    {
        @Override
        public Thread newThread(final Runnable runnable) {
            return new PicassoThread(runnable);
        }
    }
}
