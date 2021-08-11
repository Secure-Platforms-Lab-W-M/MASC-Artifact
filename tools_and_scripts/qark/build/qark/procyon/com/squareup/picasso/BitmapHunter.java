// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package com.squareup.picasso;

import android.net.NetworkInfo;
import java.io.Writer;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import android.graphics.Matrix;
import android.graphics.BitmapFactory$Options;
import android.graphics.Rect;
import android.graphics.BitmapFactory;
import java.io.InputStream;
import java.util.Iterator;
import java.io.IOException;
import android.graphics.Bitmap;
import java.util.concurrent.Future;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

class BitmapHunter implements Runnable
{
    private static final Object DECODE_LOCK;
    private static final RequestHandler ERRORING_HANDLER;
    private static final ThreadLocal<StringBuilder> NAME_BUILDER;
    private static final AtomicInteger SEQUENCE_GENERATOR;
    Action action;
    List<Action> actions;
    final Cache cache;
    final Request data;
    final Dispatcher dispatcher;
    Exception exception;
    int exifRotation;
    Future<?> future;
    final String key;
    Picasso.LoadedFrom loadedFrom;
    final int memoryPolicy;
    int networkPolicy;
    final Picasso picasso;
    Picasso.Priority priority;
    final RequestHandler requestHandler;
    Bitmap result;
    int retryCount;
    final int sequence;
    final Stats stats;
    
    static {
        DECODE_LOCK = new Object();
        NAME_BUILDER = new ThreadLocal<StringBuilder>() {
            @Override
            protected StringBuilder initialValue() {
                return new StringBuilder("Picasso-");
            }
        };
        SEQUENCE_GENERATOR = new AtomicInteger();
        ERRORING_HANDLER = new RequestHandler() {
            @Override
            public boolean canHandleRequest(final Request request) {
                return true;
            }
            
            @Override
            public Result load(final Request request, final int n) throws IOException {
                throw new IllegalStateException("Unrecognized type of request: " + request);
            }
        };
    }
    
    BitmapHunter(final Picasso picasso, final Dispatcher dispatcher, final Cache cache, final Stats stats, final Action action, final RequestHandler requestHandler) {
        this.sequence = BitmapHunter.SEQUENCE_GENERATOR.incrementAndGet();
        this.picasso = picasso;
        this.dispatcher = dispatcher;
        this.cache = cache;
        this.stats = stats;
        this.action = action;
        this.key = action.getKey();
        this.data = action.getRequest();
        this.priority = action.getPriority();
        this.memoryPolicy = action.getMemoryPolicy();
        this.networkPolicy = action.getNetworkPolicy();
        this.requestHandler = requestHandler;
        this.retryCount = requestHandler.getRetryCount();
    }
    
    static Bitmap applyCustomTransformations(final List<Transformation> list, Bitmap bitmap) {
        int n = 0;
        final int size = list.size();
        Transformation transformation = null;
        Block_6: {
            Block_4: {
                StringBuilder append = null;
                Label_0148: {
                    Bitmap bitmap2;
                    while (true) {
                        bitmap2 = bitmap;
                        if (n >= size) {
                            break;
                        }
                        transformation = list.get(n);
                        Bitmap transform = null;
                        Label_0165: {
                            try {
                                transform = transformation.transform(bitmap);
                                if (transform == null) {
                                    append = new StringBuilder().append("Transformation ").append(transformation.key()).append(" returned null after ").append(n).append(" previous transformation(s).\n\nTransformation list:\n");
                                    final Iterator<Transformation> iterator = list.iterator();
                                    while (iterator.hasNext()) {
                                        append.append(iterator.next().key()).append('\n');
                                    }
                                    break Label_0148;
                                }
                                break Label_0165;
                            }
                            catch (RuntimeException ex) {
                                Picasso.HANDLER.post((Runnable)new Runnable() {
                                    @Override
                                    public void run() {
                                        throw new RuntimeException("Transformation " + transformation.key() + " crashed with exception.", ex);
                                    }
                                });
                                bitmap2 = null;
                            }
                            break;
                        }
                        if (transform == bitmap && bitmap.isRecycled()) {
                            break Block_4;
                        }
                        if (transform != bitmap && !bitmap.isRecycled()) {
                            break Block_6;
                        }
                        bitmap = transform;
                        ++n;
                    }
                    return bitmap2;
                }
                Picasso.HANDLER.post((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        throw new NullPointerException(append.toString());
                    }
                });
                return null;
            }
            Picasso.HANDLER.post((Runnable)new Runnable() {
                @Override
                public void run() {
                    throw new IllegalStateException("Transformation " + transformation.key() + " returned input Bitmap but recycled it.");
                }
            });
            return null;
        }
        Picasso.HANDLER.post((Runnable)new Runnable() {
            @Override
            public void run() {
                throw new IllegalStateException("Transformation " + transformation.key() + " mutated input Bitmap but failed to recycle the original.");
            }
        });
        return null;
    }
    
    private Picasso.Priority computeNewPriority() {
        Enum<Picasso.Priority> enum1 = Picasso.Priority.LOW;
        boolean b;
        if (this.actions != null && !this.actions.isEmpty()) {
            b = true;
        }
        else {
            b = false;
        }
        int n;
        if (this.action != null || b) {
            n = 1;
        }
        else {
            n = 0;
        }
        if (n == 0) {
            return (Picasso.Priority)enum1;
        }
        if (this.action != null) {
            enum1 = this.action.getPriority();
        }
        Enum<Picasso.Priority> enum2 = enum1;
        if (b) {
            int n2 = 0;
            final int size = this.actions.size();
            while (true) {
                enum2 = enum1;
                if (n2 >= size) {
                    break;
                }
                final Picasso.Priority priority = this.actions.get(n2).getPriority();
                Enum<Picasso.Priority> enum3 = enum1;
                if (priority.ordinal() > enum1.ordinal()) {
                    enum3 = priority;
                }
                ++n2;
                enum1 = enum3;
            }
        }
        return (Picasso.Priority)enum2;
    }
    
    static Bitmap decodeStream(final InputStream inputStream, final Request request) throws IOException {
        final MarkableInputStream markableInputStream = new MarkableInputStream(inputStream);
        final long savePosition = markableInputStream.savePosition(65536);
        final BitmapFactory$Options bitmapOptions = RequestHandler.createBitmapOptions(request);
        final boolean requiresInSampleSize = RequestHandler.requiresInSampleSize(bitmapOptions);
        final boolean webPFile = Utils.isWebPFile(markableInputStream);
        markableInputStream.reset(savePosition);
        Bitmap bitmap;
        if (webPFile) {
            final byte[] byteArray = Utils.toByteArray(markableInputStream);
            if (requiresInSampleSize) {
                BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, bitmapOptions);
                RequestHandler.calculateInSampleSize(request.targetWidth, request.targetHeight, bitmapOptions, request);
            }
            bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, bitmapOptions);
        }
        else {
            if (requiresInSampleSize) {
                BitmapFactory.decodeStream((InputStream)markableInputStream, (Rect)null, bitmapOptions);
                RequestHandler.calculateInSampleSize(request.targetWidth, request.targetHeight, bitmapOptions, request);
                markableInputStream.reset(savePosition);
            }
            if ((bitmap = BitmapFactory.decodeStream((InputStream)markableInputStream, (Rect)null, bitmapOptions)) == null) {
                throw new IOException("Failed to decode stream.");
            }
        }
        return bitmap;
    }
    
    static BitmapHunter forRequest(final Picasso picasso, final Dispatcher dispatcher, final Cache cache, final Stats stats, final Action action) {
        final Request request = action.getRequest();
        final List<RequestHandler> requestHandlers = picasso.getRequestHandlers();
        for (int i = 0; i < requestHandlers.size(); ++i) {
            final RequestHandler requestHandler = requestHandlers.get(i);
            if (requestHandler.canHandleRequest(request)) {
                return new BitmapHunter(picasso, dispatcher, cache, stats, action, requestHandler);
            }
        }
        return new BitmapHunter(picasso, dispatcher, cache, stats, action, BitmapHunter.ERRORING_HANDLER);
    }
    
    private static boolean shouldResize(final boolean b, final int n, final int n2, final int n3, final int n4) {
        return !b || n > n3 || n2 > n4;
    }
    
    static Bitmap transformResult(final Request request, final Bitmap bitmap, final int n) {
        final int width = bitmap.getWidth();
        final int height = bitmap.getHeight();
        final boolean onlyScaleDown = request.onlyScaleDown;
        final int n2 = 0;
        final int n3 = 0;
        final int n4 = 0;
        int n5 = 0;
        final int n6 = width;
        int n7 = height;
        final Matrix matrix = new Matrix();
        int n8 = n2;
        int n9 = n4;
        int n10 = n6;
        int n11 = n7;
        Label_0243: {
            if (request.needsMatrixTransform()) {
                final int targetWidth = request.targetWidth;
                final int targetHeight = request.targetHeight;
                final float rotationDegrees = request.rotationDegrees;
                if (rotationDegrees != 0.0f) {
                    if (request.hasRotationPivot) {
                        matrix.setRotate(rotationDegrees, request.rotationPivotX, request.rotationPivotY);
                    }
                    else {
                        matrix.setRotate(rotationDegrees);
                    }
                }
                if (request.centerCrop) {
                    float n12 = targetWidth / (float)width;
                    float n13 = targetHeight / (float)height;
                    int n14;
                    int n15;
                    if (n12 > n13) {
                        n7 = (int)Math.ceil(height * (n13 / n12));
                        n5 = (height - n7) / 2;
                        n13 = targetHeight / (float)n7;
                        n14 = n6;
                        n15 = n3;
                    }
                    else {
                        n14 = (int)Math.ceil(width * (n12 / n13));
                        n15 = (width - n14) / 2;
                        n12 = targetWidth / (float)n14;
                    }
                    n8 = n15;
                    n9 = n5;
                    n10 = n14;
                    n11 = n7;
                    if (shouldResize(onlyScaleDown, width, height, targetWidth, targetHeight)) {
                        matrix.preScale(n12, n13);
                        n11 = n7;
                        n10 = n14;
                        n9 = n5;
                        n8 = n15;
                    }
                }
                else if (request.centerInside) {
                    float n16 = targetWidth / (float)width;
                    final float n17 = targetHeight / (float)height;
                    if (n16 >= n17) {
                        n16 = n17;
                    }
                    n8 = n2;
                    n9 = n4;
                    n10 = n6;
                    n11 = n7;
                    if (shouldResize(onlyScaleDown, width, height, targetWidth, targetHeight)) {
                        matrix.preScale(n16, n16);
                        n8 = n2;
                        n9 = n4;
                        n10 = n6;
                        n11 = n7;
                    }
                }
                else {
                    if (targetWidth == 0) {
                        n8 = n2;
                        n9 = n4;
                        n10 = n6;
                        n11 = n7;
                        if (targetHeight == 0) {
                            break Label_0243;
                        }
                    }
                    if (targetWidth == width) {
                        n8 = n2;
                        n9 = n4;
                        n10 = n6;
                        n11 = n7;
                        if (targetHeight == height) {
                            break Label_0243;
                        }
                    }
                    float n18;
                    if (targetWidth != 0) {
                        n18 = targetWidth / (float)width;
                    }
                    else {
                        n18 = targetHeight / (float)height;
                    }
                    float n19;
                    if (targetHeight != 0) {
                        n19 = targetHeight / (float)height;
                    }
                    else {
                        n19 = targetWidth / (float)width;
                    }
                    n8 = n2;
                    n9 = n4;
                    n10 = n6;
                    n11 = n7;
                    if (shouldResize(onlyScaleDown, width, height, targetWidth, targetHeight)) {
                        matrix.preScale(n18, n19);
                        n8 = n2;
                        n9 = n4;
                        n10 = n6;
                        n11 = n7;
                    }
                }
            }
        }
        if (n != 0) {
            matrix.preRotate((float)n);
        }
        final Bitmap bitmap2 = Bitmap.createBitmap(bitmap, n8, n9, n10, n11, matrix, true);
        Bitmap bitmap3;
        if (bitmap2 != (bitmap3 = bitmap)) {
            bitmap.recycle();
            bitmap3 = bitmap2;
        }
        return bitmap3;
    }
    
    static void updateThreadName(final Request request) {
        final String name = request.getName();
        final StringBuilder sb = BitmapHunter.NAME_BUILDER.get();
        sb.ensureCapacity("Picasso-".length() + name.length());
        sb.replace("Picasso-".length(), sb.length(), name);
        Thread.currentThread().setName(sb.toString());
    }
    
    void attach(final Action action) {
        final boolean loggingEnabled = this.picasso.loggingEnabled;
        final Request request = action.request;
        if (this.action == null) {
            this.action = action;
            if (loggingEnabled) {
                if (this.actions != null && !this.actions.isEmpty()) {
                    Utils.log("Hunter", "joined", request.logId(), Utils.getLogIdsForHunter(this, "to "));
                    return;
                }
                Utils.log("Hunter", "joined", request.logId(), "to empty hunter");
            }
        }
        else {
            if (this.actions == null) {
                this.actions = new ArrayList<Action>(3);
            }
            this.actions.add(action);
            if (loggingEnabled) {
                Utils.log("Hunter", "joined", request.logId(), Utils.getLogIdsForHunter(this, "to "));
            }
            final Picasso.Priority priority = action.getPriority();
            if (priority.ordinal() > this.priority.ordinal()) {
                this.priority = priority;
            }
        }
    }
    
    boolean cancel() {
        boolean b2;
        final boolean b = b2 = false;
        if (this.action == null) {
            if (this.actions != null) {
                b2 = b;
                if (!this.actions.isEmpty()) {
                    return b2;
                }
            }
            b2 = b;
            if (this.future != null) {
                b2 = b;
                if (this.future.cancel(false)) {
                    b2 = true;
                }
            }
        }
        return b2;
    }
    
    void detach(final Action action) {
        boolean remove = false;
        if (this.action == action) {
            this.action = null;
            remove = true;
        }
        else if (this.actions != null) {
            remove = this.actions.remove(action);
        }
        if (remove && action.getPriority() == this.priority) {
            this.priority = this.computeNewPriority();
        }
        if (this.picasso.loggingEnabled) {
            Utils.log("Hunter", "removed", action.request.logId(), Utils.getLogIdsForHunter(this, "from "));
        }
    }
    
    Action getAction() {
        return this.action;
    }
    
    List<Action> getActions() {
        return this.actions;
    }
    
    Request getData() {
        return this.data;
    }
    
    Exception getException() {
        return this.exception;
    }
    
    String getKey() {
        return this.key;
    }
    
    Picasso.LoadedFrom getLoadedFrom() {
        return this.loadedFrom;
    }
    
    int getMemoryPolicy() {
        return this.memoryPolicy;
    }
    
    Picasso getPicasso() {
        return this.picasso;
    }
    
    Picasso.Priority getPriority() {
        return this.priority;
    }
    
    Bitmap getResult() {
        return this.result;
    }
    
    Bitmap hunt() throws IOException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     2: aload_0        
        //     3: getfield        com/squareup/picasso/BitmapHunter.memoryPolicy:I
        //     6: invokestatic    com/squareup/picasso/MemoryPolicy.shouldReadFromMemoryCache:(I)Z
        //     9: ifeq            77
        //    12: aload_0        
        //    13: getfield        com/squareup/picasso/BitmapHunter.cache:Lcom/squareup/picasso/Cache;
        //    16: aload_0        
        //    17: getfield        com/squareup/picasso/BitmapHunter.key:Ljava/lang/String;
        //    20: invokeinterface com/squareup/picasso/Cache.get:(Ljava/lang/String;)Landroid/graphics/Bitmap;
        //    25: astore_3       
        //    26: aload_3        
        //    27: astore_2       
        //    28: aload_3        
        //    29: ifnull          77
        //    32: aload_0        
        //    33: getfield        com/squareup/picasso/BitmapHunter.stats:Lcom/squareup/picasso/Stats;
        //    36: invokevirtual   com/squareup/picasso/Stats.dispatchCacheHit:()V
        //    39: aload_0        
        //    40: getstatic       com/squareup/picasso/Picasso$LoadedFrom.MEMORY:Lcom/squareup/picasso/Picasso$LoadedFrom;
        //    43: putfield        com/squareup/picasso/BitmapHunter.loadedFrom:Lcom/squareup/picasso/Picasso$LoadedFrom;
        //    46: aload_0        
        //    47: getfield        com/squareup/picasso/BitmapHunter.picasso:Lcom/squareup/picasso/Picasso;
        //    50: getfield        com/squareup/picasso/Picasso.loggingEnabled:Z
        //    53: ifeq            75
        //    56: ldc_w           "Hunter"
        //    59: ldc_w           "decoded"
        //    62: aload_0        
        //    63: getfield        com/squareup/picasso/BitmapHunter.data:Lcom/squareup/picasso/Request;
        //    66: invokevirtual   com/squareup/picasso/Request.logId:()Ljava/lang/String;
        //    69: ldc_w           "from cache"
        //    72: invokestatic    com/squareup/picasso/Utils.log:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V
        //    75: aload_3        
        //    76: areturn        
        //    77: aload_0        
        //    78: getfield        com/squareup/picasso/BitmapHunter.data:Lcom/squareup/picasso/Request;
        //    81: astore_3       
        //    82: aload_0        
        //    83: getfield        com/squareup/picasso/BitmapHunter.retryCount:I
        //    86: ifne            379
        //    89: getstatic       com/squareup/picasso/NetworkPolicy.OFFLINE:Lcom/squareup/picasso/NetworkPolicy;
        //    92: getfield        com/squareup/picasso/NetworkPolicy.index:I
        //    95: istore_1       
        //    96: aload_3        
        //    97: iload_1        
        //    98: putfield        com/squareup/picasso/Request.networkPolicy:I
        //   101: aload_0        
        //   102: getfield        com/squareup/picasso/BitmapHunter.requestHandler:Lcom/squareup/picasso/RequestHandler;
        //   105: aload_0        
        //   106: getfield        com/squareup/picasso/BitmapHunter.data:Lcom/squareup/picasso/Request;
        //   109: aload_0        
        //   110: getfield        com/squareup/picasso/BitmapHunter.networkPolicy:I
        //   113: invokevirtual   com/squareup/picasso/RequestHandler.load:(Lcom/squareup/picasso/Request;I)Lcom/squareup/picasso/RequestHandler$Result;
        //   116: astore          4
        //   118: aload           4
        //   120: ifnull          172
        //   123: aload_0        
        //   124: aload           4
        //   126: invokevirtual   com/squareup/picasso/RequestHandler$Result.getLoadedFrom:()Lcom/squareup/picasso/Picasso$LoadedFrom;
        //   129: putfield        com/squareup/picasso/BitmapHunter.loadedFrom:Lcom/squareup/picasso/Picasso$LoadedFrom;
        //   132: aload_0        
        //   133: aload           4
        //   135: invokevirtual   com/squareup/picasso/RequestHandler$Result.getExifOrientation:()I
        //   138: putfield        com/squareup/picasso/BitmapHunter.exifRotation:I
        //   141: aload           4
        //   143: invokevirtual   com/squareup/picasso/RequestHandler$Result.getBitmap:()Landroid/graphics/Bitmap;
        //   146: astore_3       
        //   147: aload_3        
        //   148: astore_2       
        //   149: aload_3        
        //   150: ifnonnull       172
        //   153: aload           4
        //   155: invokevirtual   com/squareup/picasso/RequestHandler$Result.getStream:()Ljava/io/InputStream;
        //   158: astore_3       
        //   159: aload_3        
        //   160: aload_0        
        //   161: getfield        com/squareup/picasso/BitmapHunter.data:Lcom/squareup/picasso/Request;
        //   164: invokestatic    com/squareup/picasso/BitmapHunter.decodeStream:(Ljava/io/InputStream;Lcom/squareup/picasso/Request;)Landroid/graphics/Bitmap;
        //   167: astore_2       
        //   168: aload_3        
        //   169: invokestatic    com/squareup/picasso/Utils.closeQuietly:(Ljava/io/InputStream;)V
        //   172: aload_2        
        //   173: astore_3       
        //   174: aload_2        
        //   175: ifnull          377
        //   178: aload_0        
        //   179: getfield        com/squareup/picasso/BitmapHunter.picasso:Lcom/squareup/picasso/Picasso;
        //   182: getfield        com/squareup/picasso/Picasso.loggingEnabled:Z
        //   185: ifeq            204
        //   188: ldc_w           "Hunter"
        //   191: ldc_w           "decoded"
        //   194: aload_0        
        //   195: getfield        com/squareup/picasso/BitmapHunter.data:Lcom/squareup/picasso/Request;
        //   198: invokevirtual   com/squareup/picasso/Request.logId:()Ljava/lang/String;
        //   201: invokestatic    com/squareup/picasso/Utils.log:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V
        //   204: aload_0        
        //   205: getfield        com/squareup/picasso/BitmapHunter.stats:Lcom/squareup/picasso/Stats;
        //   208: aload_2        
        //   209: invokevirtual   com/squareup/picasso/Stats.dispatchBitmapDecoded:(Landroid/graphics/Bitmap;)V
        //   212: aload_0        
        //   213: getfield        com/squareup/picasso/BitmapHunter.data:Lcom/squareup/picasso/Request;
        //   216: invokevirtual   com/squareup/picasso/Request.needsTransformation:()Z
        //   219: ifne            231
        //   222: aload_2        
        //   223: astore_3       
        //   224: aload_0        
        //   225: getfield        com/squareup/picasso/BitmapHunter.exifRotation:I
        //   228: ifeq            377
        //   231: getstatic       com/squareup/picasso/BitmapHunter.DECODE_LOCK:Ljava/lang/Object;
        //   234: astore          4
        //   236: aload           4
        //   238: monitorenter   
        //   239: aload_0        
        //   240: getfield        com/squareup/picasso/BitmapHunter.data:Lcom/squareup/picasso/Request;
        //   243: invokevirtual   com/squareup/picasso/Request.needsMatrixTransform:()Z
        //   246: ifne            258
        //   249: aload_2        
        //   250: astore_3       
        //   251: aload_0        
        //   252: getfield        com/squareup/picasso/BitmapHunter.exifRotation:I
        //   255: ifeq            301
        //   258: aload_0        
        //   259: getfield        com/squareup/picasso/BitmapHunter.data:Lcom/squareup/picasso/Request;
        //   262: aload_2        
        //   263: aload_0        
        //   264: getfield        com/squareup/picasso/BitmapHunter.exifRotation:I
        //   267: invokestatic    com/squareup/picasso/BitmapHunter.transformResult:(Lcom/squareup/picasso/Request;Landroid/graphics/Bitmap;I)Landroid/graphics/Bitmap;
        //   270: astore_2       
        //   271: aload_2        
        //   272: astore_3       
        //   273: aload_0        
        //   274: getfield        com/squareup/picasso/BitmapHunter.picasso:Lcom/squareup/picasso/Picasso;
        //   277: getfield        com/squareup/picasso/Picasso.loggingEnabled:Z
        //   280: ifeq            301
        //   283: ldc_w           "Hunter"
        //   286: ldc_w           "transformed"
        //   289: aload_0        
        //   290: getfield        com/squareup/picasso/BitmapHunter.data:Lcom/squareup/picasso/Request;
        //   293: invokevirtual   com/squareup/picasso/Request.logId:()Ljava/lang/String;
        //   296: invokestatic    com/squareup/picasso/Utils.log:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V
        //   299: aload_2        
        //   300: astore_3       
        //   301: aload_3        
        //   302: astore_2       
        //   303: aload_0        
        //   304: getfield        com/squareup/picasso/BitmapHunter.data:Lcom/squareup/picasso/Request;
        //   307: invokevirtual   com/squareup/picasso/Request.hasCustomTransformations:()Z
        //   310: ifeq            358
        //   313: aload_0        
        //   314: getfield        com/squareup/picasso/BitmapHunter.data:Lcom/squareup/picasso/Request;
        //   317: getfield        com/squareup/picasso/Request.transformations:Ljava/util/List;
        //   320: aload_3        
        //   321: invokestatic    com/squareup/picasso/BitmapHunter.applyCustomTransformations:(Ljava/util/List;Landroid/graphics/Bitmap;)Landroid/graphics/Bitmap;
        //   324: astore_3       
        //   325: aload_3        
        //   326: astore_2       
        //   327: aload_0        
        //   328: getfield        com/squareup/picasso/BitmapHunter.picasso:Lcom/squareup/picasso/Picasso;
        //   331: getfield        com/squareup/picasso/Picasso.loggingEnabled:Z
        //   334: ifeq            358
        //   337: ldc_w           "Hunter"
        //   340: ldc_w           "transformed"
        //   343: aload_0        
        //   344: getfield        com/squareup/picasso/BitmapHunter.data:Lcom/squareup/picasso/Request;
        //   347: invokevirtual   com/squareup/picasso/Request.logId:()Ljava/lang/String;
        //   350: ldc_w           "from custom transformations"
        //   353: invokestatic    com/squareup/picasso/Utils.log:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V
        //   356: aload_3        
        //   357: astore_2       
        //   358: aload           4
        //   360: monitorexit    
        //   361: aload_2        
        //   362: astore_3       
        //   363: aload_2        
        //   364: ifnull          377
        //   367: aload_0        
        //   368: getfield        com/squareup/picasso/BitmapHunter.stats:Lcom/squareup/picasso/Stats;
        //   371: aload_2        
        //   372: invokevirtual   com/squareup/picasso/Stats.dispatchBitmapTransformed:(Landroid/graphics/Bitmap;)V
        //   375: aload_2        
        //   376: astore_3       
        //   377: aload_3        
        //   378: areturn        
        //   379: aload_0        
        //   380: getfield        com/squareup/picasso/BitmapHunter.networkPolicy:I
        //   383: istore_1       
        //   384: goto            96
        //   387: astore_2       
        //   388: aload_3        
        //   389: invokestatic    com/squareup/picasso/Utils.closeQuietly:(Ljava/io/InputStream;)V
        //   392: aload_2        
        //   393: athrow         
        //   394: astore_2       
        //   395: aload           4
        //   397: monitorexit    
        //   398: aload_2        
        //   399: athrow         
        //    Exceptions:
        //  throws java.io.IOException
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type
        //  -----  -----  -----  -----  ----
        //  159    168    387    394    Any
        //  239    249    394    400    Any
        //  251    258    394    400    Any
        //  258    271    394    400    Any
        //  273    299    394    400    Any
        //  303    325    394    400    Any
        //  327    356    394    400    Any
        //  358    361    394    400    Any
        //  395    398    394    400    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0258:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:141)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    boolean isCancelled() {
        return this.future != null && this.future.isCancelled();
    }
    
    @Override
    public void run() {
        try {
            updateThreadName(this.data);
            if (this.picasso.loggingEnabled) {
                Utils.log("Hunter", "executing", Utils.getLogIdsForHunter(this));
            }
            this.result = this.hunt();
            if (this.result == null) {
                this.dispatcher.dispatchFailed(this);
            }
            else {
                this.dispatcher.dispatchComplete(this);
            }
        }
        catch (Downloader.ResponseException exception) {
            if (!exception.localCacheOnly || exception.responseCode != 504) {
                this.exception = exception;
            }
            this.dispatcher.dispatchFailed(this);
        }
        catch (NetworkRequestHandler.ContentLengthException exception2) {
            this.exception = exception2;
            this.dispatcher.dispatchRetry(this);
        }
        catch (IOException exception3) {
            this.exception = exception3;
            this.dispatcher.dispatchRetry(this);
        }
        catch (OutOfMemoryError outOfMemoryError) {
            final StringWriter stringWriter = new StringWriter();
            this.stats.createSnapshot().dump(new PrintWriter(stringWriter));
            this.exception = new RuntimeException(stringWriter.toString(), outOfMemoryError);
            this.dispatcher.dispatchFailed(this);
        }
        catch (Exception exception4) {
            this.exception = exception4;
            this.dispatcher.dispatchFailed(this);
        }
        finally {
            Thread.currentThread().setName("Picasso-Idle");
        }
    }
    
    boolean shouldRetry(final boolean b, final NetworkInfo networkInfo) {
        int n;
        if (this.retryCount > 0) {
            n = 1;
        }
        else {
            n = 0;
        }
        if (n == 0) {
            return false;
        }
        --this.retryCount;
        return this.requestHandler.shouldRetry(b, networkInfo);
    }
    
    boolean supportsReplay() {
        return this.requestHandler.supportsReplay();
    }
}
