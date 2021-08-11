package com.bumptech.glide;

import android.app.Activity;
import android.app.Fragment;
import android.content.ComponentCallbacks2;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.os.Build.VERSION;
import android.util.Log;
import android.view.View;
import androidx.fragment.app.FragmentActivity;
import com.bumptech.glide.gifdecoder.GifDecoder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.Encoder;
import com.bumptech.glide.load.ImageHeaderParser;
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
import com.bumptech.glide.manager.ConnectivityMonitorFactory;
import com.bumptech.glide.manager.RequestManagerRetriever;
import com.bumptech.glide.module.GlideModule;
import com.bumptech.glide.module.ManifestParser;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.ImageViewTargetFactory;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.util.Preconditions;
import com.bumptech.glide.util.Util;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Glide implements ComponentCallbacks2 {
   private static final String DEFAULT_DISK_CACHE_DIR = "image_manager_disk_cache";
   private static final String TAG = "Glide";
   private static volatile Glide glide;
   private static volatile boolean isInitializing;
   private final ArrayPool arrayPool;
   private final BitmapPool bitmapPool;
   private BitmapPreFiller bitmapPreFiller;
   private final ConnectivityMonitorFactory connectivityMonitorFactory;
   private final Glide.RequestOptionsFactory defaultRequestOptionsFactory;
   private final Engine engine;
   private final GlideContext glideContext;
   private final List managers = new ArrayList();
   private final MemoryCache memoryCache;
   private MemoryCategory memoryCategory;
   private final Registry registry;
   private final RequestManagerRetriever requestManagerRetriever;

   Glide(Context var1, Engine var2, MemoryCache var3, BitmapPool var4, ArrayPool var5, RequestManagerRetriever var6, ConnectivityMonitorFactory var7, int var8, Glide.RequestOptionsFactory var9, Map var10, List var11, boolean var12, boolean var13) {
      this.memoryCategory = MemoryCategory.NORMAL;
      this.engine = var2;
      this.bitmapPool = var4;
      this.arrayPool = var5;
      this.memoryCache = var3;
      this.requestManagerRetriever = var6;
      this.connectivityMonitorFactory = var7;
      this.defaultRequestOptionsFactory = var9;
      Resources var14 = var1.getResources();
      Registry var27 = new Registry();
      this.registry = var27;
      var27.register((ImageHeaderParser)(new DefaultImageHeaderParser()));
      if (VERSION.SDK_INT >= 27) {
         this.registry.register((ImageHeaderParser)(new ExifInterfaceImageHeaderParser()));
      }

      List var15 = this.registry.getImageHeaderParsers();
      ByteBufferGifDecoder var16 = new ByteBufferGifDecoder(var1, var15, var4, var5);
      ResourceDecoder var17 = VideoDecoder.parcel(var4);
      Downsampler var18 = new Downsampler(this.registry.getImageHeaderParsers(), var14.getDisplayMetrics(), var4, var5);
      Object var28;
      Object var31;
      if (var13 && VERSION.SDK_INT >= 28) {
         var28 = new InputStreamBitmapImageDecoderResourceDecoder();
         var31 = new ByteBufferBitmapImageDecoderResourceDecoder();
      } else {
         var31 = new ByteBufferBitmapDecoder(var18);
         var28 = new StreamBitmapDecoder(var18, var5);
      }

      ResourceDrawableDecoder var32 = new ResourceDrawableDecoder(var1);
      ResourceLoader.StreamFactory var19 = new ResourceLoader.StreamFactory(var14);
      ResourceLoader.UriFactory var20 = new ResourceLoader.UriFactory(var14);
      ResourceLoader.FileDescriptorFactory var21 = new ResourceLoader.FileDescriptorFactory(var14);
      ResourceLoader.AssetFileDescriptorFactory var22 = new ResourceLoader.AssetFileDescriptorFactory(var14);
      BitmapEncoder var23 = new BitmapEncoder(var5);
      BitmapBytesTranscoder var24 = new BitmapBytesTranscoder();
      GifDrawableBytesTranscoder var25 = new GifDrawableBytesTranscoder();
      ContentResolver var26 = var1.getContentResolver();
      this.registry.append(ByteBuffer.class, (Encoder)(new ByteBufferEncoder())).append(InputStream.class, (Encoder)(new StreamEncoder(var5))).append("Bitmap", ByteBuffer.class, Bitmap.class, (ResourceDecoder)var31).append("Bitmap", InputStream.class, Bitmap.class, (ResourceDecoder)var28);
      if (ParcelFileDescriptorRewinder.isSupported()) {
         this.registry.append("Bitmap", ParcelFileDescriptor.class, Bitmap.class, new ParcelFileDescriptorBitmapDecoder(var18));
      }

      this.registry.append("Bitmap", ParcelFileDescriptor.class, Bitmap.class, var17).append("Bitmap", AssetFileDescriptor.class, Bitmap.class, VideoDecoder.asset(var4)).append(Bitmap.class, Bitmap.class, (ModelLoaderFactory)UnitModelLoader.Factory.getInstance()).append("Bitmap", Bitmap.class, Bitmap.class, new UnitBitmapDecoder()).append(Bitmap.class, (ResourceEncoder)var23).append("BitmapDrawable", ByteBuffer.class, BitmapDrawable.class, new BitmapDrawableDecoder(var14, (ResourceDecoder)var31)).append("BitmapDrawable", InputStream.class, BitmapDrawable.class, new BitmapDrawableDecoder(var14, (ResourceDecoder)var28)).append("BitmapDrawable", ParcelFileDescriptor.class, BitmapDrawable.class, new BitmapDrawableDecoder(var14, var17)).append(BitmapDrawable.class, (ResourceEncoder)(new BitmapDrawableEncoder(var4, var23))).append("Gif", InputStream.class, GifDrawable.class, new StreamGifDecoder(var15, var16, var5)).append("Gif", ByteBuffer.class, GifDrawable.class, var16).append(GifDrawable.class, (ResourceEncoder)(new GifDrawableEncoder())).append(GifDecoder.class, GifDecoder.class, (ModelLoaderFactory)UnitModelLoader.Factory.getInstance()).append("Bitmap", GifDecoder.class, Bitmap.class, new GifFrameResourceDecoder(var4)).append(Uri.class, Drawable.class, (ResourceDecoder)var32).append(Uri.class, Bitmap.class, (ResourceDecoder)(new ResourceBitmapDecoder(var32, var4))).register((DataRewinder.Factory)(new ByteBufferRewinder.Factory())).append(File.class, ByteBuffer.class, (ModelLoaderFactory)(new ByteBufferFileLoader.Factory())).append(File.class, InputStream.class, (ModelLoaderFactory)(new FileLoader.StreamFactory())).append(File.class, File.class, (ResourceDecoder)(new FileDecoder())).append(File.class, ParcelFileDescriptor.class, (ModelLoaderFactory)(new FileLoader.FileDescriptorFactory())).append(File.class, File.class, (ModelLoaderFactory)UnitModelLoader.Factory.getInstance()).register((DataRewinder.Factory)(new InputStreamRewinder.Factory(var5)));
      if (ParcelFileDescriptorRewinder.isSupported()) {
         this.registry.register((DataRewinder.Factory)(new ParcelFileDescriptorRewinder.Factory()));
      }

      this.registry.append(Integer.TYPE, InputStream.class, (ModelLoaderFactory)var19).append(Integer.TYPE, ParcelFileDescriptor.class, (ModelLoaderFactory)var21).append(Integer.class, InputStream.class, (ModelLoaderFactory)var19).append(Integer.class, ParcelFileDescriptor.class, (ModelLoaderFactory)var21).append(Integer.class, Uri.class, (ModelLoaderFactory)var20).append(Integer.TYPE, AssetFileDescriptor.class, (ModelLoaderFactory)var22).append(Integer.class, AssetFileDescriptor.class, (ModelLoaderFactory)var22).append(Integer.TYPE, Uri.class, (ModelLoaderFactory)var20).append(String.class, InputStream.class, (ModelLoaderFactory)(new DataUrlLoader.StreamFactory())).append(Uri.class, InputStream.class, (ModelLoaderFactory)(new DataUrlLoader.StreamFactory())).append(String.class, InputStream.class, (ModelLoaderFactory)(new StringLoader.StreamFactory())).append(String.class, ParcelFileDescriptor.class, (ModelLoaderFactory)(new StringLoader.FileDescriptorFactory())).append(String.class, AssetFileDescriptor.class, (ModelLoaderFactory)(new StringLoader.AssetFileDescriptorFactory())).append(Uri.class, InputStream.class, (ModelLoaderFactory)(new HttpUriLoader.Factory())).append(Uri.class, InputStream.class, (ModelLoaderFactory)(new AssetUriLoader.StreamFactory(var1.getAssets()))).append(Uri.class, ParcelFileDescriptor.class, (ModelLoaderFactory)(new AssetUriLoader.FileDescriptorFactory(var1.getAssets()))).append(Uri.class, InputStream.class, (ModelLoaderFactory)(new MediaStoreImageThumbLoader.Factory(var1))).append(Uri.class, InputStream.class, (ModelLoaderFactory)(new MediaStoreVideoThumbLoader.Factory(var1)));
      if (VERSION.SDK_INT >= 29) {
         this.registry.append(Uri.class, InputStream.class, (ModelLoaderFactory)(new QMediaStoreUriLoader.InputStreamFactory(var1)));
         this.registry.append(Uri.class, ParcelFileDescriptor.class, (ModelLoaderFactory)(new QMediaStoreUriLoader.FileDescriptorFactory(var1)));
      }

      this.registry.append(Uri.class, InputStream.class, (ModelLoaderFactory)(new UriLoader.StreamFactory(var26))).append(Uri.class, ParcelFileDescriptor.class, (ModelLoaderFactory)(new UriLoader.FileDescriptorFactory(var26))).append(Uri.class, AssetFileDescriptor.class, (ModelLoaderFactory)(new UriLoader.AssetFileDescriptorFactory(var26))).append(Uri.class, InputStream.class, (ModelLoaderFactory)(new UrlUriLoader.StreamFactory())).append(URL.class, InputStream.class, (ModelLoaderFactory)(new UrlLoader.StreamFactory())).append(Uri.class, File.class, (ModelLoaderFactory)(new MediaStoreFileLoader.Factory(var1))).append(GlideUrl.class, InputStream.class, (ModelLoaderFactory)(new HttpGlideUrlLoader.Factory())).append(byte[].class, ByteBuffer.class, (ModelLoaderFactory)(new ByteArrayLoader.ByteBufferFactory())).append(byte[].class, InputStream.class, (ModelLoaderFactory)(new ByteArrayLoader.StreamFactory())).append(Uri.class, Uri.class, (ModelLoaderFactory)UnitModelLoader.Factory.getInstance()).append(Drawable.class, Drawable.class, (ModelLoaderFactory)UnitModelLoader.Factory.getInstance()).append(Drawable.class, Drawable.class, (ResourceDecoder)(new UnitDrawableDecoder())).register(Bitmap.class, BitmapDrawable.class, new BitmapDrawableTranscoder(var14)).register(Bitmap.class, byte[].class, var24).register(Drawable.class, byte[].class, new DrawableBytesTranscoder(var4, var24, var25)).register(GifDrawable.class, byte[].class, var25);
      if (VERSION.SDK_INT >= 23) {
         ResourceDecoder var29 = VideoDecoder.byteBuffer(var4);
         this.registry.append(ByteBuffer.class, Bitmap.class, var29);
         this.registry.append(ByteBuffer.class, BitmapDrawable.class, (ResourceDecoder)(new BitmapDrawableDecoder(var14, var29)));
      }

      ImageViewTargetFactory var30 = new ImageViewTargetFactory();
      this.glideContext = new GlideContext(var1, var5, this.registry, var30, var9, var10, var11, var2, var12, var8);
   }

   private static void checkAndInitializeGlide(Context var0, GeneratedAppGlideModule var1) {
      if (!isInitializing) {
         isInitializing = true;
         initializeGlide(var0, var1);
         isInitializing = false;
      } else {
         throw new IllegalStateException("You cannot call Glide.get() in registerComponents(), use the provided Glide instance instead");
      }
   }

   public static Glide get(Context var0) {
      if (glide == null) {
         GeneratedAppGlideModule var1 = getAnnotationGeneratedGlideModules(var0.getApplicationContext());
         synchronized(Glide.class){}

         Throwable var10000;
         boolean var10001;
         label144: {
            try {
               if (glide == null) {
                  checkAndInitializeGlide(var0, var1);
               }
            } catch (Throwable var13) {
               var10000 = var13;
               var10001 = false;
               break label144;
            }

            label141:
            try {
               return glide;
            } catch (Throwable var12) {
               var10000 = var12;
               var10001 = false;
               break label141;
            }
         }

         while(true) {
            Throwable var14 = var10000;

            try {
               throw var14;
            } catch (Throwable var11) {
               var10000 = var11;
               var10001 = false;
               continue;
            }
         }
      } else {
         return glide;
      }
   }

   private static GeneratedAppGlideModule getAnnotationGeneratedGlideModules(Context var0) {
      Object var1 = null;

      GeneratedAppGlideModule var7;
      try {
         var7 = (GeneratedAppGlideModule)Class.forName("com.bumptech.glide.GeneratedAppGlideModuleImpl").getDeclaredConstructor(Context.class).newInstance(var0.getApplicationContext());
      } catch (ClassNotFoundException var2) {
         var7 = (GeneratedAppGlideModule)var1;
         if (Log.isLoggable("Glide", 5)) {
            Log.w("Glide", "Failed to find GeneratedAppGlideModule. You should include an annotationProcessor compile dependency on com.github.bumptech.glide:compiler in your application and a @GlideModule annotated AppGlideModule implementation or LibraryGlideModules will be silently ignored");
            return null;
         }
      } catch (InstantiationException var3) {
         throwIncorrectGlideModule(var3);
         return null;
      } catch (IllegalAccessException var4) {
         throwIncorrectGlideModule(var4);
         return null;
      } catch (NoSuchMethodException var5) {
         throwIncorrectGlideModule(var5);
         return null;
      } catch (InvocationTargetException var6) {
         throwIncorrectGlideModule(var6);
         return null;
      }

      return var7;
   }

   public static File getPhotoCacheDir(Context var0) {
      return getPhotoCacheDir(var0, "image_manager_disk_cache");
   }

   public static File getPhotoCacheDir(Context var0, String var1) {
      File var2 = var0.getCacheDir();
      if (var2 == null) {
         if (Log.isLoggable("Glide", 6)) {
            Log.e("Glide", "default disk cache dir is null");
         }

         return null;
      } else {
         var2 = new File(var2, var1);
         return var2.mkdirs() || var2.exists() && var2.isDirectory() ? var2 : null;
      }
   }

   private static RequestManagerRetriever getRetriever(Context var0) {
      Preconditions.checkNotNull(var0, "You cannot start a load on a not yet attached View or a Fragment where getActivity() returns null (which usually occurs when getActivity() is called before the Fragment is attached or after the Fragment is destroyed).");
      return get(var0).getRequestManagerRetriever();
   }

   public static void init(Context var0, GlideBuilder var1) {
      GeneratedAppGlideModule var2 = getAnnotationGeneratedGlideModules(var0);
      synchronized(Glide.class){}

      Throwable var10000;
      boolean var10001;
      label122: {
         try {
            if (glide != null) {
               tearDown();
            }
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label122;
         }

         label119:
         try {
            initializeGlide(var0, var1, var2);
            return;
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label119;
         }
      }

      while(true) {
         Throwable var15 = var10000;

         try {
            throw var15;
         } catch (Throwable var12) {
            var10000 = var12;
            var10001 = false;
            continue;
         }
      }
   }

   @Deprecated
   public static void init(Glide var0) {
      synchronized(Glide.class){}

      try {
         if (glide != null) {
            tearDown();
         }

         glide = var0;
      } finally {
         ;
      }

   }

   private static void initializeGlide(Context var0, GeneratedAppGlideModule var1) {
      initializeGlide(var0, new GlideBuilder(), var1);
   }

   private static void initializeGlide(Context var0, GlideBuilder var1, GeneratedAppGlideModule var2) {
      Context var4 = var0.getApplicationContext();
      List var9 = Collections.emptyList();
      if (var2 == null || var2.isManifestParsingEnabled()) {
         var9 = (new ManifestParser(var4)).parse();
      }

      if (var2 != null && !var2.getExcludedModuleClasses().isEmpty()) {
         Set var3 = var2.getExcludedModuleClasses();
         Iterator var5 = var9.iterator();

         while(var5.hasNext()) {
            GlideModule var6 = (GlideModule)var5.next();
            if (var3.contains(var6.getClass())) {
               if (Log.isLoggable("Glide", 3)) {
                  StringBuilder var7 = new StringBuilder();
                  var7.append("AppGlideModule excludes manifest GlideModule: ");
                  var7.append(var6);
                  Log.d("Glide", var7.toString());
               }

               var5.remove();
            }
         }
      }

      Iterator var13;
      if (Log.isLoggable("Glide", 3)) {
         var13 = var9.iterator();

         while(var13.hasNext()) {
            GlideModule var15 = (GlideModule)var13.next();
            StringBuilder var16 = new StringBuilder();
            var16.append("Discovered GlideModule from manifest: ");
            var16.append(var15.getClass());
            Log.d("Glide", var16.toString());
         }
      }

      RequestManagerRetriever.RequestManagerFactory var14;
      if (var2 != null) {
         var14 = var2.getRequestManagerFactory();
      } else {
         var14 = null;
      }

      var1.setRequestManagerFactory(var14);
      var13 = var9.iterator();

      while(var13.hasNext()) {
         ((GlideModule)var13.next()).applyOptions(var4, var1);
      }

      if (var2 != null) {
         var2.applyOptions(var4, var1);
      }

      Glide var10 = var1.build(var4);
      var13 = var9.iterator();

      while(var13.hasNext()) {
         GlideModule var11 = (GlideModule)var13.next();

         try {
            var11.registerComponents(var4, var10, var10.registry);
         } catch (AbstractMethodError var8) {
            StringBuilder var12 = new StringBuilder();
            var12.append("Attempting to register a Glide v3 module. If you see this, you or one of your dependencies may be including Glide v3 even though you're using Glide v4. You'll need to find and remove (or update) the offending dependency. The v3 module name is: ");
            var12.append(var11.getClass().getName());
            throw new IllegalStateException(var12.toString(), var8);
         }
      }

      if (var2 != null) {
         var2.registerComponents(var4, var10, var10.registry);
      }

      var4.registerComponentCallbacks(var10);
      glide = var10;
   }

   public static void tearDown() {
      synchronized(Glide.class){}

      try {
         if (glide != null) {
            glide.getContext().getApplicationContext().unregisterComponentCallbacks(glide);
            glide.engine.shutdown();
         }

         glide = null;
      } finally {
         ;
      }

   }

   private static void throwIncorrectGlideModule(Exception var0) {
      throw new IllegalStateException("GeneratedAppGlideModuleImpl is implemented incorrectly. If you've manually implemented this class, remove your implementation. The Annotation processor will generate a correct implementation.", var0);
   }

   public static RequestManager with(Activity var0) {
      return getRetriever(var0).get(var0);
   }

   @Deprecated
   public static RequestManager with(Fragment var0) {
      return getRetriever(var0.getActivity()).get(var0);
   }

   public static RequestManager with(Context var0) {
      return getRetriever(var0).get(var0);
   }

   public static RequestManager with(View var0) {
      return getRetriever(var0.getContext()).get(var0);
   }

   public static RequestManager with(androidx.fragment.app.Fragment var0) {
      return getRetriever(var0.getContext()).get(var0);
   }

   public static RequestManager with(FragmentActivity var0) {
      return getRetriever(var0).get(var0);
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

   public void onConfigurationChanged(Configuration var1) {
   }

   public void onLowMemory() {
      this.clearMemory();
   }

   public void onTrimMemory(int var1) {
      this.trimMemory(var1);
   }

   public void preFillBitmapPool(PreFillType.Builder... var1) {
      synchronized(this){}

      try {
         if (this.bitmapPreFiller == null) {
            DecodeFormat var2 = (DecodeFormat)this.defaultRequestOptionsFactory.build().getOptions().get(Downsampler.DECODE_FORMAT);
            this.bitmapPreFiller = new BitmapPreFiller(this.memoryCache, this.bitmapPool, var2);
         }

         this.bitmapPreFiller.preFill(var1);
      } finally {
         ;
      }

   }

   void registerRequestManager(RequestManager var1) {
      List var2 = this.managers;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label122: {
         try {
            if (!this.managers.contains(var1)) {
               this.managers.add(var1);
               return;
            }
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label122;
         }

         label116:
         try {
            throw new IllegalStateException("Cannot register already registered manager");
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label116;
         }
      }

      while(true) {
         Throwable var15 = var10000;

         try {
            throw var15;
         } catch (Throwable var12) {
            var10000 = var12;
            var10001 = false;
            continue;
         }
      }
   }

   boolean removeFromManagers(Target var1) {
      List var2 = this.managers;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label226: {
         Iterator var3;
         try {
            var3 = this.managers.iterator();
         } catch (Throwable var22) {
            var10000 = var22;
            var10001 = false;
            break label226;
         }

         while(true) {
            try {
               if (!var3.hasNext()) {
                  break;
               }

               if (((RequestManager)var3.next()).untrack(var1)) {
                  return true;
               }
            } catch (Throwable var23) {
               var10000 = var23;
               var10001 = false;
               break label226;
            }
         }

         label208:
         try {
            return false;
         } catch (Throwable var21) {
            var10000 = var21;
            var10001 = false;
            break label208;
         }
      }

      while(true) {
         Throwable var24 = var10000;

         try {
            throw var24;
         } catch (Throwable var20) {
            var10000 = var20;
            var10001 = false;
            continue;
         }
      }
   }

   public MemoryCategory setMemoryCategory(MemoryCategory var1) {
      Util.assertMainThread();
      this.memoryCache.setSizeMultiplier(var1.getMultiplier());
      this.bitmapPool.setSizeMultiplier(var1.getMultiplier());
      MemoryCategory var2 = this.memoryCategory;
      this.memoryCategory = var1;
      return var2;
   }

   public void trimMemory(int var1) {
      Util.assertMainThread();
      Iterator var2 = this.managers.iterator();

      while(var2.hasNext()) {
         ((RequestManager)var2.next()).onTrimMemory(var1);
      }

      this.memoryCache.trimMemory(var1);
      this.bitmapPool.trimMemory(var1);
      this.arrayPool.trimMemory(var1);
   }

   void unregisterRequestManager(RequestManager var1) {
      List var2 = this.managers;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label122: {
         try {
            if (this.managers.contains(var1)) {
               this.managers.remove(var1);
               return;
            }
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label122;
         }

         label116:
         try {
            throw new IllegalStateException("Cannot unregister not yet registered manager");
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label116;
         }
      }

      while(true) {
         Throwable var15 = var10000;

         try {
            throw var15;
         } catch (Throwable var12) {
            var10000 = var12;
            var10001 = false;
            continue;
         }
      }
   }

   public interface RequestOptionsFactory {
      RequestOptions build();
   }
}
