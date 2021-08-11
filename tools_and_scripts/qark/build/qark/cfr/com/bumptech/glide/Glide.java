/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.app.Fragment
 *  android.content.ComponentCallbacks
 *  android.content.ComponentCallbacks2
 *  android.content.ContentResolver
 *  android.content.Context
 *  android.content.res.AssetFileDescriptor
 *  android.content.res.AssetManager
 *  android.content.res.Configuration
 *  android.content.res.Resources
 *  android.graphics.Bitmap
 *  android.graphics.drawable.BitmapDrawable
 *  android.graphics.drawable.Drawable
 *  android.net.Uri
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.ParcelFileDescriptor
 *  android.util.DisplayMetrics
 *  android.util.Log
 *  android.view.View
 */
package com.bumptech.glide;

import android.app.Activity;
import android.app.Fragment;
import android.content.ComponentCallbacks;
import android.content.ComponentCallbacks2;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import androidx.fragment.app.FragmentActivity;
import com.bumptech.glide.GeneratedAppGlideModule;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.GlideContext;
import com.bumptech.glide.MemoryCategory;
import com.bumptech.glide.Registry;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.TransitionOptions;
import com.bumptech.glide.gifdecoder.GifDecoder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.Encoder;
import com.bumptech.glide.load.ImageHeaderParser;
import com.bumptech.glide.load.Option;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.ResourceEncoder;
import com.bumptech.glide.load.data.DataRewinder;
import com.bumptech.glide.load.data.InputStreamRewinder;
import com.bumptech.glide.load.data.ParcelFileDescriptorRewinder;
import com.bumptech.glide.load.engine.Engine;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.engine.cache.MemoryCache;
import com.bumptech.glide.load.engine.prefill.BitmapPreFiller;
import com.bumptech.glide.load.engine.prefill.PreFillType;
import com.bumptech.glide.load.model.AssetUriLoader;
import com.bumptech.glide.load.model.ByteArrayLoader;
import com.bumptech.glide.load.model.ByteBufferEncoder;
import com.bumptech.glide.load.model.ByteBufferFileLoader;
import com.bumptech.glide.load.model.DataUrlLoader;
import com.bumptech.glide.load.model.FileLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.MediaStoreFileLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.ResourceLoader;
import com.bumptech.glide.load.model.StreamEncoder;
import com.bumptech.glide.load.model.StringLoader;
import com.bumptech.glide.load.model.UnitModelLoader;
import com.bumptech.glide.load.model.UriLoader;
import com.bumptech.glide.load.model.UrlUriLoader;
import com.bumptech.glide.load.model.stream.HttpGlideUrlLoader;
import com.bumptech.glide.load.model.stream.HttpUriLoader;
import com.bumptech.glide.load.model.stream.MediaStoreImageThumbLoader;
import com.bumptech.glide.load.model.stream.MediaStoreVideoThumbLoader;
import com.bumptech.glide.load.model.stream.QMediaStoreUriLoader;
import com.bumptech.glide.load.model.stream.UrlLoader;
import com.bumptech.glide.load.resource.bitmap.BitmapDrawableDecoder;
import com.bumptech.glide.load.resource.bitmap.BitmapDrawableEncoder;
import com.bumptech.glide.load.resource.bitmap.BitmapEncoder;
import com.bumptech.glide.load.resource.bitmap.ByteBufferBitmapDecoder;
import com.bumptech.glide.load.resource.bitmap.ByteBufferBitmapImageDecoderResourceDecoder;
import com.bumptech.glide.load.resource.bitmap.DefaultImageHeaderParser;
import com.bumptech.glide.load.resource.bitmap.Downsampler;
import com.bumptech.glide.load.resource.bitmap.ExifInterfaceImageHeaderParser;
import com.bumptech.glide.load.resource.bitmap.InputStreamBitmapImageDecoderResourceDecoder;
import com.bumptech.glide.load.resource.bitmap.ParcelFileDescriptorBitmapDecoder;
import com.bumptech.glide.load.resource.bitmap.ResourceBitmapDecoder;
import com.bumptech.glide.load.resource.bitmap.StreamBitmapDecoder;
import com.bumptech.glide.load.resource.bitmap.UnitBitmapDecoder;
import com.bumptech.glide.load.resource.bitmap.VideoDecoder;
import com.bumptech.glide.load.resource.bytes.ByteBufferRewinder;
import com.bumptech.glide.load.resource.drawable.ResourceDrawableDecoder;
import com.bumptech.glide.load.resource.drawable.UnitDrawableDecoder;
import com.bumptech.glide.load.resource.file.FileDecoder;
import com.bumptech.glide.load.resource.gif.ByteBufferGifDecoder;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.load.resource.gif.GifDrawableEncoder;
import com.bumptech.glide.load.resource.gif.GifFrameResourceDecoder;
import com.bumptech.glide.load.resource.gif.StreamGifDecoder;
import com.bumptech.glide.load.resource.transcode.BitmapBytesTranscoder;
import com.bumptech.glide.load.resource.transcode.BitmapDrawableTranscoder;
import com.bumptech.glide.load.resource.transcode.DrawableBytesTranscoder;
import com.bumptech.glide.load.resource.transcode.GifDrawableBytesTranscoder;
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder;
import com.bumptech.glide.manager.ConnectivityMonitorFactory;
import com.bumptech.glide.manager.RequestManagerRetriever;
import com.bumptech.glide.module.GlideModule;
import com.bumptech.glide.module.ManifestParser;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.ImageViewTargetFactory;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.util.Preconditions;
import com.bumptech.glide.util.Util;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Glide
implements ComponentCallbacks2 {
    private static final String DEFAULT_DISK_CACHE_DIR = "image_manager_disk_cache";
    private static final String TAG = "Glide";
    private static volatile Glide glide;
    private static volatile boolean isInitializing;
    private final ArrayPool arrayPool;
    private final BitmapPool bitmapPool;
    private BitmapPreFiller bitmapPreFiller;
    private final ConnectivityMonitorFactory connectivityMonitorFactory;
    private final RequestOptionsFactory defaultRequestOptionsFactory;
    private final Engine engine;
    private final GlideContext glideContext;
    private final List<RequestManager> managers = new ArrayList<RequestManager>();
    private final MemoryCache memoryCache;
    private MemoryCategory memoryCategory = MemoryCategory.NORMAL;
    private final Registry registry;
    private final RequestManagerRetriever requestManagerRetriever;

    Glide(Context context, Engine engine, MemoryCache object, BitmapPool bitmapPool, ArrayPool arrayPool, RequestManagerRetriever object2, ConnectivityMonitorFactory object3, int n, RequestOptionsFactory requestOptionsFactory, Map<Class<?>, TransitionOptions<?, ?>> map, List<RequestListener<Object>> list, boolean bl, boolean bl2) {
        this.engine = engine;
        this.bitmapPool = bitmapPool;
        this.arrayPool = arrayPool;
        this.memoryCache = object;
        this.requestManagerRetriever = object2;
        this.connectivityMonitorFactory = object3;
        this.defaultRequestOptionsFactory = requestOptionsFactory;
        Resources resources = context.getResources();
        this.registry = object = new Registry();
        object.register(new DefaultImageHeaderParser());
        if (Build.VERSION.SDK_INT >= 27) {
            this.registry.register(new ExifInterfaceImageHeaderParser());
        }
        List<ImageHeaderParser> list2 = this.registry.getImageHeaderParsers();
        ByteBufferGifDecoder byteBufferGifDecoder = new ByteBufferGifDecoder(context, list2, bitmapPool, arrayPool);
        ResourceDecoder<ParcelFileDescriptor, Bitmap> resourceDecoder = VideoDecoder.parcel(bitmapPool);
        Downsampler downsampler = new Downsampler(this.registry.getImageHeaderParsers(), resources.getDisplayMetrics(), bitmapPool, arrayPool);
        if (bl2 && Build.VERSION.SDK_INT >= 28) {
            object = new InputStreamBitmapImageDecoderResourceDecoder();
            object2 = new ByteBufferBitmapImageDecoderResourceDecoder();
        } else {
            object2 = new ByteBufferBitmapDecoder(downsampler);
            object = new StreamBitmapDecoder(downsampler, arrayPool);
        }
        object3 = new ResourceDrawableDecoder(context);
        ResourceLoader.StreamFactory streamFactory = new ResourceLoader.StreamFactory(resources);
        ResourceLoader.UriFactory uriFactory = new ResourceLoader.UriFactory(resources);
        ResourceLoader.FileDescriptorFactory fileDescriptorFactory = new ResourceLoader.FileDescriptorFactory(resources);
        ResourceLoader.AssetFileDescriptorFactory assetFileDescriptorFactory = new ResourceLoader.AssetFileDescriptorFactory(resources);
        BitmapEncoder bitmapEncoder = new BitmapEncoder(arrayPool);
        BitmapBytesTranscoder bitmapBytesTranscoder = new BitmapBytesTranscoder();
        GifDrawableBytesTranscoder gifDrawableBytesTranscoder = new GifDrawableBytesTranscoder();
        ContentResolver contentResolver = context.getContentResolver();
        this.registry.append(ByteBuffer.class, new ByteBufferEncoder()).append(InputStream.class, new StreamEncoder(arrayPool)).append("Bitmap", ByteBuffer.class, Bitmap.class, object2).append("Bitmap", InputStream.class, Bitmap.class, object);
        if (ParcelFileDescriptorRewinder.isSupported()) {
            this.registry.append("Bitmap", ParcelFileDescriptor.class, Bitmap.class, new ParcelFileDescriptorBitmapDecoder(downsampler));
        }
        this.registry.append("Bitmap", ParcelFileDescriptor.class, Bitmap.class, resourceDecoder).append("Bitmap", AssetFileDescriptor.class, Bitmap.class, VideoDecoder.asset(bitmapPool)).append(Bitmap.class, Bitmap.class, UnitModelLoader.Factory.getInstance()).append("Bitmap", Bitmap.class, Bitmap.class, new UnitBitmapDecoder()).append(Bitmap.class, bitmapEncoder).append("BitmapDrawable", ByteBuffer.class, BitmapDrawable.class, new BitmapDrawableDecoder(resources, object2)).append("BitmapDrawable", InputStream.class, BitmapDrawable.class, new BitmapDrawableDecoder(resources, object)).append("BitmapDrawable", ParcelFileDescriptor.class, BitmapDrawable.class, new BitmapDrawableDecoder<ParcelFileDescriptor>(resources, resourceDecoder)).append(BitmapDrawable.class, new BitmapDrawableEncoder(bitmapPool, bitmapEncoder)).append("Gif", InputStream.class, GifDrawable.class, new StreamGifDecoder(list2, byteBufferGifDecoder, arrayPool)).append("Gif", ByteBuffer.class, GifDrawable.class, byteBufferGifDecoder).append(GifDrawable.class, new GifDrawableEncoder()).append(GifDecoder.class, GifDecoder.class, UnitModelLoader.Factory.getInstance()).append("Bitmap", GifDecoder.class, Bitmap.class, new GifFrameResourceDecoder(bitmapPool)).append(Uri.class, Drawable.class, object3).append(Uri.class, Bitmap.class, new ResourceBitmapDecoder((ResourceDrawableDecoder)object3, bitmapPool)).register(new ByteBufferRewinder.Factory()).append(File.class, ByteBuffer.class, new ByteBufferFileLoader.Factory()).append(File.class, InputStream.class, new FileLoader.StreamFactory()).append(File.class, File.class, new FileDecoder()).append(File.class, ParcelFileDescriptor.class, new FileLoader.FileDescriptorFactory()).append(File.class, File.class, UnitModelLoader.Factory.getInstance()).register(new InputStreamRewinder.Factory(arrayPool));
        if (ParcelFileDescriptorRewinder.isSupported()) {
            this.registry.register(new ParcelFileDescriptorRewinder.Factory());
        }
        this.registry.append(Integer.TYPE, InputStream.class, streamFactory).append(Integer.TYPE, ParcelFileDescriptor.class, fileDescriptorFactory).append(Integer.class, InputStream.class, streamFactory).append(Integer.class, ParcelFileDescriptor.class, fileDescriptorFactory).append(Integer.class, Uri.class, uriFactory).append(Integer.TYPE, AssetFileDescriptor.class, assetFileDescriptorFactory).append(Integer.class, AssetFileDescriptor.class, assetFileDescriptorFactory).append(Integer.TYPE, Uri.class, uriFactory).append(String.class, InputStream.class, new DataUrlLoader.StreamFactory()).append(Uri.class, InputStream.class, new DataUrlLoader.StreamFactory()).append(String.class, InputStream.class, new StringLoader.StreamFactory()).append(String.class, ParcelFileDescriptor.class, new StringLoader.FileDescriptorFactory()).append(String.class, AssetFileDescriptor.class, new StringLoader.AssetFileDescriptorFactory()).append(Uri.class, InputStream.class, new HttpUriLoader.Factory()).append(Uri.class, InputStream.class, new AssetUriLoader.StreamFactory(context.getAssets())).append(Uri.class, ParcelFileDescriptor.class, new AssetUriLoader.FileDescriptorFactory(context.getAssets())).append(Uri.class, InputStream.class, new MediaStoreImageThumbLoader.Factory(context)).append(Uri.class, InputStream.class, new MediaStoreVideoThumbLoader.Factory(context));
        if (Build.VERSION.SDK_INT >= 29) {
            this.registry.append(Uri.class, InputStream.class, new QMediaStoreUriLoader.InputStreamFactory(context));
            this.registry.append(Uri.class, ParcelFileDescriptor.class, new QMediaStoreUriLoader.FileDescriptorFactory(context));
        }
        this.registry.append(Uri.class, InputStream.class, new UriLoader.StreamFactory(contentResolver)).append(Uri.class, ParcelFileDescriptor.class, new UriLoader.FileDescriptorFactory(contentResolver)).append(Uri.class, AssetFileDescriptor.class, new UriLoader.AssetFileDescriptorFactory(contentResolver)).append(Uri.class, InputStream.class, new UrlUriLoader.StreamFactory()).append(URL.class, InputStream.class, new UrlLoader.StreamFactory()).append(Uri.class, File.class, new MediaStoreFileLoader.Factory(context)).append(GlideUrl.class, InputStream.class, new HttpGlideUrlLoader.Factory()).append(byte[].class, ByteBuffer.class, new ByteArrayLoader.ByteBufferFactory()).append(byte[].class, InputStream.class, new ByteArrayLoader.StreamFactory()).append(Uri.class, Uri.class, UnitModelLoader.Factory.getInstance()).append(Drawable.class, Drawable.class, UnitModelLoader.Factory.getInstance()).append(Drawable.class, Drawable.class, new UnitDrawableDecoder()).register(Bitmap.class, BitmapDrawable.class, new BitmapDrawableTranscoder(resources)).register(Bitmap.class, byte[].class, bitmapBytesTranscoder).register(Drawable.class, byte[].class, new DrawableBytesTranscoder(bitmapPool, bitmapBytesTranscoder, gifDrawableBytesTranscoder)).register(GifDrawable.class, byte[].class, gifDrawableBytesTranscoder);
        if (Build.VERSION.SDK_INT >= 23) {
            object = VideoDecoder.byteBuffer(bitmapPool);
            this.registry.append(ByteBuffer.class, Bitmap.class, object);
            this.registry.append(ByteBuffer.class, BitmapDrawable.class, new BitmapDrawableDecoder(resources, object));
        }
        object = new ImageViewTargetFactory();
        this.glideContext = new GlideContext(context, arrayPool, this.registry, (ImageViewTargetFactory)object, requestOptionsFactory, map, list, engine, bl, n);
    }

    private static void checkAndInitializeGlide(Context context, GeneratedAppGlideModule generatedAppGlideModule) {
        if (!isInitializing) {
            isInitializing = true;
            Glide.initializeGlide(context, generatedAppGlideModule);
            isInitializing = false;
            return;
        }
        throw new IllegalStateException("You cannot call Glide.get() in registerComponents(), use the provided Glide instance instead");
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static Glide get(Context context) {
        if (glide == null) {
            GeneratedAppGlideModule generatedAppGlideModule = Glide.getAnnotationGeneratedGlideModules(context.getApplicationContext());
            synchronized (Glide.class) {
                if (glide == null) {
                    Glide.checkAndInitializeGlide(context, generatedAppGlideModule);
                }
            }
        }
        return glide;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private static GeneratedAppGlideModule getAnnotationGeneratedGlideModules(Context object) {
        Object var1_6 = null;
        try {
            return (GeneratedAppGlideModule)Class.forName("com.bumptech.glide.GeneratedAppGlideModuleImpl").getDeclaredConstructor(Context.class).newInstance(new Object[]{object.getApplicationContext()});
        }
        catch (InvocationTargetException invocationTargetException) {
            Glide.throwIncorrectGlideModule(invocationTargetException);
            return null;
        }
        catch (NoSuchMethodException noSuchMethodException) {
            Glide.throwIncorrectGlideModule(noSuchMethodException);
            return null;
        }
        catch (IllegalAccessException illegalAccessException) {
            Glide.throwIncorrectGlideModule(illegalAccessException);
            return null;
        }
        catch (InstantiationException instantiationException) {
            Glide.throwIncorrectGlideModule(instantiationException);
            return null;
        }
        catch (ClassNotFoundException classNotFoundException) {
            object = var1_6;
            if (!Log.isLoggable((String)"Glide", (int)5)) return object;
            Log.w((String)"Glide", (String)"Failed to find GeneratedAppGlideModule. You should include an annotationProcessor compile dependency on com.github.bumptech.glide:compiler in your application and a @GlideModule annotated AppGlideModule implementation or LibraryGlideModules will be silently ignored");
            return null;
        }
    }

    public static File getPhotoCacheDir(Context context) {
        return Glide.getPhotoCacheDir(context, "image_manager_disk_cache");
    }

    public static File getPhotoCacheDir(Context object, String string2) {
        if ((object = object.getCacheDir()) != null) {
            if (!((object = new File((File)object, string2)).mkdirs() || object.exists() && object.isDirectory())) {
                return null;
            }
            return object;
        }
        if (Log.isLoggable((String)"Glide", (int)6)) {
            Log.e((String)"Glide", (String)"default disk cache dir is null");
        }
        return null;
    }

    private static RequestManagerRetriever getRetriever(Context context) {
        Preconditions.checkNotNull(context, "You cannot start a load on a not yet attached View or a Fragment where getActivity() returns null (which usually occurs when getActivity() is called before the Fragment is attached or after the Fragment is destroyed).");
        return Glide.get(context).getRequestManagerRetriever();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static void init(Context context, GlideBuilder glideBuilder) {
        GeneratedAppGlideModule generatedAppGlideModule = Glide.getAnnotationGeneratedGlideModules(context);
        synchronized (Glide.class) {
            if (glide != null) {
                Glide.tearDown();
            }
            Glide.initializeGlide(context, glideBuilder, generatedAppGlideModule);
            return;
        }
    }

    @Deprecated
    public static void init(Glide glide) {
        synchronized (Glide.class) {
            if (Glide.glide != null) {
                Glide.tearDown();
            }
            Glide.glide = glide;
            return;
        }
    }

    private static void initializeGlide(Context context, GeneratedAppGlideModule generatedAppGlideModule) {
        Glide.initializeGlide(context, new GlideBuilder(), generatedAppGlideModule);
    }

    private static void initializeGlide(Context object, GlideBuilder object2, GeneratedAppGlideModule object3) {
        Object object4;
        Object object5;
        Object object6;
        Context context = object.getApplicationContext();
        object = Collections.emptyList();
        if (object3 == null || object3.isManifestParsingEnabled()) {
            object = new ManifestParser(context).parse();
        }
        if (object3 != null && !object3.getExcludedModuleClasses().isEmpty()) {
            object5 = object3.getExcludedModuleClasses();
            object6 = object.iterator();
            while (object6.hasNext()) {
                object4 = object6.next();
                if (!object5.contains(object4.getClass())) continue;
                if (Log.isLoggable((String)"Glide", (int)3)) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("AppGlideModule excludes manifest GlideModule: ");
                    stringBuilder.append(object4);
                    Log.d((String)"Glide", (String)stringBuilder.toString());
                }
                object6.remove();
            }
        }
        if (Log.isLoggable((String)"Glide", (int)3)) {
            object5 = object.iterator();
            while (object5.hasNext()) {
                object6 = (GlideModule)object5.next();
                object4 = new StringBuilder();
                object4.append("Discovered GlideModule from manifest: ");
                object4.append(object6.getClass());
                Log.d((String)"Glide", (String)object4.toString());
            }
        }
        object5 = object3 != null ? object3.getRequestManagerFactory() : null;
        object2.setRequestManagerFactory((RequestManagerRetriever.RequestManagerFactory)object5);
        object5 = object.iterator();
        while (object5.hasNext()) {
            object5.next().applyOptions(context, (GlideBuilder)object2);
        }
        if (object3 != null) {
            object3.applyOptions(context, (GlideBuilder)object2);
        }
        object2 = object2.build(context);
        object5 = object.iterator();
        while (object5.hasNext()) {
            object = object5.next();
            try {
                object.registerComponents(context, (Glide)object2, object2.registry);
            }
            catch (AbstractMethodError abstractMethodError) {
                object3 = new StringBuilder();
                object3.append("Attempting to register a Glide v3 module. If you see this, you or one of your dependencies may be including Glide v3 even though you're using Glide v4. You'll need to find and remove (or update) the offending dependency. The v3 module name is: ");
                object3.append(object.getClass().getName());
                throw new IllegalStateException(object3.toString(), abstractMethodError);
            }
        }
        if (object3 != null) {
            object3.registerComponents(context, (Glide)object2, object2.registry);
        }
        context.registerComponentCallbacks((ComponentCallbacks)object2);
        glide = object2;
    }

    public static void tearDown() {
        synchronized (Glide.class) {
            if (glide != null) {
                glide.getContext().getApplicationContext().unregisterComponentCallbacks((ComponentCallbacks)glide);
                Glide.glide.engine.shutdown();
            }
            glide = null;
            return;
        }
    }

    private static void throwIncorrectGlideModule(Exception exception) {
        throw new IllegalStateException("GeneratedAppGlideModuleImpl is implemented incorrectly. If you've manually implemented this class, remove your implementation. The Annotation processor will generate a correct implementation.", exception);
    }

    public static RequestManager with(Activity activity) {
        return Glide.getRetriever((Context)activity).get(activity);
    }

    @Deprecated
    public static RequestManager with(Fragment fragment) {
        return Glide.getRetriever((Context)fragment.getActivity()).get(fragment);
    }

    public static RequestManager with(Context context) {
        return Glide.getRetriever(context).get(context);
    }

    public static RequestManager with(View view) {
        return Glide.getRetriever(view.getContext()).get(view);
    }

    public static RequestManager with(androidx.fragment.app.Fragment fragment) {
        return Glide.getRetriever(fragment.getContext()).get(fragment);
    }

    public static RequestManager with(FragmentActivity fragmentActivity) {
        return Glide.getRetriever((Context)fragmentActivity).get(fragmentActivity);
    }

    public void clearDiskCache() {
        Util.assertBackgroundThread();
        this.engine.clearDiskCache();
    }

    public void clearMemory() {
        Util.assertMainThread();
        this.memoryCache.clearMemory();
        this.bitmapPool.clearMemory();
        this.arrayPool.clearMemory();
    }

    public ArrayPool getArrayPool() {
        return this.arrayPool;
    }

    public BitmapPool getBitmapPool() {
        return this.bitmapPool;
    }

    ConnectivityMonitorFactory getConnectivityMonitorFactory() {
        return this.connectivityMonitorFactory;
    }

    public Context getContext() {
        return this.glideContext.getBaseContext();
    }

    GlideContext getGlideContext() {
        return this.glideContext;
    }

    public Registry getRegistry() {
        return this.registry;
    }

    public RequestManagerRetriever getRequestManagerRetriever() {
        return this.requestManagerRetriever;
    }

    public void onConfigurationChanged(Configuration configuration) {
    }

    public void onLowMemory() {
        this.clearMemory();
    }

    public void onTrimMemory(int n) {
        this.trimMemory(n);
    }

    public /* varargs */ void preFillBitmapPool(PreFillType.Builder ... arrbuilder) {
        synchronized (this) {
            if (this.bitmapPreFiller == null) {
                DecodeFormat decodeFormat = this.defaultRequestOptionsFactory.build().getOptions().get(Downsampler.DECODE_FORMAT);
                this.bitmapPreFiller = new BitmapPreFiller(this.memoryCache, this.bitmapPool, decodeFormat);
            }
            this.bitmapPreFiller.preFill(arrbuilder);
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    void registerRequestManager(RequestManager requestManager) {
        List<RequestManager> list = this.managers;
        synchronized (list) {
            if (!this.managers.contains(requestManager)) {
                this.managers.add(requestManager);
                return;
            }
            throw new IllegalStateException("Cannot register already registered manager");
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    boolean removeFromManagers(Target<?> target) {
        List<RequestManager> list = this.managers;
        synchronized (list) {
            Iterator<RequestManager> iterator = this.managers.iterator();
            do {
                if (iterator.hasNext()) continue;
                return false;
            } while (!iterator.next().untrack(target));
            return true;
        }
    }

    public MemoryCategory setMemoryCategory(MemoryCategory memoryCategory) {
        Util.assertMainThread();
        this.memoryCache.setSizeMultiplier(memoryCategory.getMultiplier());
        this.bitmapPool.setSizeMultiplier(memoryCategory.getMultiplier());
        MemoryCategory memoryCategory2 = this.memoryCategory;
        this.memoryCategory = memoryCategory;
        return memoryCategory2;
    }

    public void trimMemory(int n) {
        Util.assertMainThread();
        Iterator<RequestManager> iterator = this.managers.iterator();
        while (iterator.hasNext()) {
            iterator.next().onTrimMemory(n);
        }
        this.memoryCache.trimMemory(n);
        this.bitmapPool.trimMemory(n);
        this.arrayPool.trimMemory(n);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    void unregisterRequestManager(RequestManager requestManager) {
        List<RequestManager> list = this.managers;
        synchronized (list) {
            if (this.managers.contains(requestManager)) {
                this.managers.remove(requestManager);
                return;
            }
            throw new IllegalStateException("Cannot unregister not yet registered manager");
        }
    }

    public static interface RequestOptionsFactory {
        public RequestOptions build();
    }

}

