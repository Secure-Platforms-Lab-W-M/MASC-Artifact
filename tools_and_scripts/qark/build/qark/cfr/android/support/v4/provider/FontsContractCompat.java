/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.ContentResolver
 *  android.content.ContentUris
 *  android.content.Context
 *  android.content.pm.PackageInfo
 *  android.content.pm.PackageManager
 *  android.content.pm.PackageManager$NameNotFoundException
 *  android.content.pm.ProviderInfo
 *  android.content.pm.Signature
 *  android.content.res.Resources
 *  android.database.Cursor
 *  android.graphics.Typeface
 *  android.net.Uri
 *  android.net.Uri$Builder
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.CancellationSignal
 *  android.os.Handler
 *  android.provider.BaseColumns
 *  android.widget.TextView
 */
package android.support.v4.provider;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.pm.Signature;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.CancellationSignal;
import android.os.Handler;
import android.provider.BaseColumns;
import android.support.annotation.GuardedBy;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.annotation.VisibleForTesting;
import android.support.v4.content.res.FontResourcesParserCompat;
import android.support.v4.graphics.TypefaceCompat;
import android.support.v4.graphics.TypefaceCompatUtil;
import android.support.v4.provider.FontRequest;
import android.support.v4.provider.SelfDestructiveThread;
import android.support.v4.util.LruCache;
import android.support.v4.util.Preconditions;
import android.support.v4.util.SimpleArrayMap;
import android.widget.TextView;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class FontsContractCompat {
    private static final int BACKGROUND_THREAD_KEEP_ALIVE_DURATION_MS = 10000;
    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public static final String PARCEL_FONT_RESULTS = "font_results";
    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public static final int RESULT_CODE_PROVIDER_NOT_FOUND = -1;
    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public static final int RESULT_CODE_WRONG_CERTIFICATES = -2;
    private static final String TAG = "FontsContractCompat";
    private static final SelfDestructiveThread sBackgroundThread;
    private static final Comparator<byte[]> sByteArrayComparator;
    private static final Object sLock;
    @GuardedBy(value="sLock")
    private static final SimpleArrayMap<String, ArrayList<SelfDestructiveThread.ReplyCallback<Typeface>>> sPendingReplies;
    private static final LruCache<String, Typeface> sTypefaceCache;

    static {
        sTypefaceCache = new LruCache(16);
        sBackgroundThread = new SelfDestructiveThread("fonts", 10, 10000);
        sLock = new Object();
        sPendingReplies = new SimpleArrayMap();
        sByteArrayComparator = new Comparator<byte[]>(){

            @Override
            public int compare(byte[] arrby, byte[] arrby2) {
                if (arrby.length != arrby2.length) {
                    return arrby.length - arrby2.length;
                }
                for (int i = 0; i < arrby.length; ++i) {
                    if (arrby[i] == arrby2[i]) continue;
                    return arrby[i] - arrby2[i];
                }
                return 0;
            }
        };
    }

    private FontsContractCompat() {
    }

    public static Typeface buildTypeface(@NonNull Context context, @Nullable CancellationSignal cancellationSignal, @NonNull FontInfo[] arrfontInfo) {
        return TypefaceCompat.createFromFontInfo(context, cancellationSignal, arrfontInfo, 0);
    }

    private static List<byte[]> convertToByteArrayList(Signature[] arrsignature) {
        ArrayList<byte[]> arrayList = new ArrayList<byte[]>();
        for (int i = 0; i < arrsignature.length; ++i) {
            arrayList.add(arrsignature[i].toByteArray());
        }
        return arrayList;
    }

    private static boolean equalsByteArrayList(List<byte[]> list, List<byte[]> list2) {
        if (list.size() != list2.size()) {
            return false;
        }
        for (int i = 0; i < list.size(); ++i) {
            if (Arrays.equals(list.get(i), list2.get(i))) continue;
            return false;
        }
        return true;
    }

    @NonNull
    public static FontFamilyResult fetchFonts(@NonNull Context context, @Nullable CancellationSignal cancellationSignal, @NonNull FontRequest fontRequest) throws PackageManager.NameNotFoundException {
        ProviderInfo providerInfo = FontsContractCompat.getProvider(context.getPackageManager(), fontRequest, context.getResources());
        if (providerInfo == null) {
            return new FontFamilyResult(1, null);
        }
        return new FontFamilyResult(0, FontsContractCompat.getFontFromProvider(context, fontRequest, providerInfo.authority, cancellationSignal));
    }

    private static List<List<byte[]>> getCertificates(FontRequest fontRequest, Resources resources) {
        if (fontRequest.getCertificates() != null) {
            return fontRequest.getCertificates();
        }
        return FontResourcesParserCompat.readCerts(resources, fontRequest.getCertificatesArrayResId());
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    @NonNull
    @VisibleForTesting
    static FontInfo[] getFontFromProvider(Context var0, FontRequest var1_2, String var2_3, CancellationSignal var3_4) {
        block18 : {
            var14_5 = new ArrayList();
            var16_6 = new Uri.Builder().scheme("content").authority(var2_3).build();
            var17_7 = new Uri.Builder().scheme("content").authority(var2_3).appendPath("file").build();
            var15_8 = null;
            var2_3 = var15_8;
            try {
                if (Build.VERSION.SDK_INT > 16) {
                    var2_3 = var15_8;
                    var0 = var0.getContentResolver();
                    var2_3 = var15_8;
                    var1_2 = var1_2.getQuery();
                    var2_3 = var15_8;
                    var0 = var0.query(var16_6, new String[]{"_id", "file_id", "font_ttc_index", "font_variation_settings", "font_weight", "font_italic", "result_code"}, "query = ?", new String[]{var1_2}, null, (CancellationSignal)var3_4);
                } else {
                    var2_3 = var15_8;
                    var0 = var0.getContentResolver();
                    var2_3 = var15_8;
                    var1_2 = var1_2.getQuery();
                    var2_3 = var15_8;
                    var0 = var0.query(var16_6, new String[]{"_id", "file_id", "font_ttc_index", "font_variation_settings", "font_weight", "font_italic", "result_code"}, "query = ?", new String[]{var1_2}, null);
                }
                if (var0 != null) {
                    var2_3 = var0;
                    if (var0.getCount() > 0) {
                        var2_3 = var0;
                        var7_9 = var0.getColumnIndex("result_code");
                        var2_3 = var0;
                        var3_4 = new ArrayList<E>();
                        var2_3 = var0;
                        var8_10 = var0.getColumnIndex("_id");
                        var2_3 = var0;
                        var9_11 = var0.getColumnIndex("file_id");
                        var2_3 = var0;
                        var10_12 = var0.getColumnIndex("font_ttc_index");
                        var2_3 = var0;
                        var11_13 = var0.getColumnIndex("font_weight");
                        var2_3 = var0;
                        var12_14 = var0.getColumnIndex("font_italic");
                        break block18;
                    }
                }
                var1_2 = var14_5;
lbl40: // 2 sources:
                do {
                    if (var0 == null) return var1_2.toArray(new FontInfo[0]);
                    break;
                } while (true);
            }
            catch (Throwable var0_1) {
                if (var2_3 == null) throw var0_1;
                var2_3.close();
                throw var0_1;
            }
            {
                var0.close();
                return var1_2.toArray(new FontInfo[0]);
            }
        }
        do {
            var2_3 = var0;
            if (!var0.moveToNext()) break;
            if (var7_9 != -1) {
                var2_3 = var0;
                var4_15 = var0.getInt(var7_9);
            } else {
                var4_15 = 0;
            }
            if (var10_12 != -1) {
                var2_3 = var0;
                var5_16 = var0.getInt(var10_12);
            } else {
                var5_16 = 0;
            }
            if (var9_11 == -1) {
                var2_3 = var0;
                var1_2 = ContentUris.withAppendedId((Uri)var16_6, (long)var0.getLong(var8_10));
            } else {
                var2_3 = var0;
                var1_2 = ContentUris.withAppendedId((Uri)var17_7, (long)var0.getLong(var9_11));
            }
            if (var11_13 != -1) {
                var2_3 = var0;
                var6_17 = var0.getInt(var11_13);
            } else {
                var6_17 = 400;
            }
            if (var12_14 == -1) ** GOTO lbl-1000
            var2_3 = var0;
            if (var0.getInt(var12_14) == 1) {
                var13_18 = true;
            } else lbl-1000: // 2 sources:
            {
                var13_18 = false;
            }
            var2_3 = var0;
            var3_4.add(new FontInfo((Uri)var1_2, var5_16, var6_17, var13_18, var4_15));
        } while (true);
        var1_2 = var3_4;
        ** while (true)
    }

    private static Typeface getFontInternal(Context context, FontRequest object, int n) {
        try {
            object = FontsContractCompat.fetchFonts(context, null, (FontRequest)object);
        }
        catch (PackageManager.NameNotFoundException nameNotFoundException) {
            return null;
        }
        if (object.getStatusCode() == 0) {
            return TypefaceCompat.createFromFontInfo(context, null, object.getFonts(), n);
        }
        return null;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public static Typeface getFontSync(Context object, FontRequest object2, @Nullable TextView object3, int n, int n2, int n3) {
        CharSequence charSequence = new StringBuilder();
        charSequence.append(object2.getIdentifier());
        charSequence.append("-");
        charSequence.append(n3);
        charSequence = charSequence.toString();
        Object object4 = sTypefaceCache.get((String)charSequence);
        if (object4 != null) {
            return object4;
        }
        n = n == 0 ? 1 : 0;
        if (n != 0 && n2 == -1) {
            return FontsContractCompat.getFontInternal((Context)object, (FontRequest)object2, n3);
        }
        object = new Callable<Typeface>((Context)object, (FontRequest)object2, n3, (String)charSequence){
            final /* synthetic */ Context val$context;
            final /* synthetic */ String val$id;
            final /* synthetic */ FontRequest val$request;
            final /* synthetic */ int val$style;
            {
                this.val$context = context;
                this.val$request = fontRequest;
                this.val$style = n;
                this.val$id = string2;
            }

            @Override
            public Typeface call() throws Exception {
                Typeface typeface = FontsContractCompat.getFontInternal(this.val$context, this.val$request, this.val$style);
                if (typeface != null) {
                    sTypefaceCache.put(this.val$id, typeface);
                    return typeface;
                }
                return typeface;
            }
        };
        if (n != 0) {
            try {
                return (Typeface)sBackgroundThread.postAndWait(object, n2);
            }
            catch (InterruptedException interruptedException) {
                return null;
            }
        }
        object3 = new SelfDestructiveThread.ReplyCallback<Typeface>(new WeakReference<TextView>((TextView)object3), (TextView)object3, n3){
            final /* synthetic */ int val$style;
            final /* synthetic */ TextView val$targetView;
            final /* synthetic */ WeakReference val$textViewWeak;
            {
                this.val$textViewWeak = weakReference;
                this.val$targetView = textView;
                this.val$style = n;
            }

            @Override
            public void onReply(Typeface typeface) {
                if ((TextView)this.val$textViewWeak.get() != null) {
                    this.val$targetView.setTypeface(typeface, this.val$style);
                    return;
                }
            }
        };
        object2 = sLock;
        synchronized (object2) {
            if (sPendingReplies.containsKey(charSequence)) {
                sPendingReplies.get(charSequence).add((SelfDestructiveThread.ReplyCallback<Typeface>)object3);
                return null;
            }
            object4 = new ArrayList();
            object4.add(object3);
            sPendingReplies.put((String)charSequence, (ArrayList<SelfDestructiveThread.ReplyCallback<Typeface>>)object4);
        }
        sBackgroundThread.postAndReply(object, new SelfDestructiveThread.ReplyCallback<Typeface>((String)charSequence){
            final /* synthetic */ String val$id;
            {
                this.val$id = string2;
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public void onReply(Typeface typeface) {
                ArrayList arrayList;
                Object object = sLock;
                synchronized (object) {
                    arrayList = (ArrayList)sPendingReplies.get(this.val$id);
                    sPendingReplies.remove(this.val$id);
                }
                int n = 0;
                while (n < arrayList.size()) {
                    ((SelfDestructiveThread.ReplyCallback)arrayList.get(n)).onReply(typeface);
                    ++n;
                }
            }
        });
        return null;
    }

    @Nullable
    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    @VisibleForTesting
    public static ProviderInfo getProvider(@NonNull PackageManager list, @NonNull FontRequest object, @Nullable Resources object2) throws PackageManager.NameNotFoundException {
        String string2 = object.getProviderAuthority();
        ProviderInfo providerInfo = list.resolveContentProvider(string2, 0);
        if (providerInfo != null) {
            if (providerInfo.packageName.equals(object.getProviderPackage())) {
                list = FontsContractCompat.convertToByteArrayList(list.getPackageInfo((String)providerInfo.packageName, (int)64).signatures);
                Collections.sort(list, sByteArrayComparator);
                object = FontsContractCompat.getCertificates((FontRequest)object, (Resources)object2);
                for (int i = 0; i < object.size(); ++i) {
                    object2 = new ArrayList((Collection)object.get(i));
                    Collections.sort(object2, sByteArrayComparator);
                    if (!FontsContractCompat.equalsByteArrayList(list, object2)) continue;
                    return providerInfo;
                }
                return null;
            }
            list = new StringBuilder();
            list.append("Found content provider ");
            list.append(string2);
            list.append(", but package was not ");
            list.append(object.getProviderPackage());
            throw new PackageManager.NameNotFoundException(list.toString());
        }
        list = new StringBuilder();
        list.append("No package found for authority: ");
        list.append(string2);
        throw new PackageManager.NameNotFoundException(list.toString());
    }

    @RequiresApi(value=19)
    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public static Map<Uri, ByteBuffer> prepareFontData(Context context, FontInfo[] arrfontInfo, CancellationSignal cancellationSignal) {
        HashMap<FontInfo, ByteBuffer> hashMap = new HashMap<FontInfo, ByteBuffer>();
        for (FontInfo fontInfo : arrfontInfo) {
            if (fontInfo.getResultCode() != 0 || hashMap.containsKey(fontInfo = fontInfo.getUri())) continue;
            hashMap.put(fontInfo, TypefaceCompatUtil.mmap(context, cancellationSignal, (Uri)fontInfo));
        }
        return Collections.unmodifiableMap(hashMap);
    }

    public static void requestFont(final @NonNull Context context, final @NonNull FontRequest fontRequest, @NonNull FontRequestCallback fontRequestCallback, @NonNull Handler handler) {
        handler.post(new Runnable(new Handler(), fontRequestCallback){
            final /* synthetic */ FontRequestCallback val$callback;
            final /* synthetic */ Handler val$callerThreadHandler;
            {
                this.val$callerThreadHandler = handler;
                this.val$callback = fontRequestCallback;
            }

            @Override
            public void run() {
                Typeface typeface;
                try {
                    typeface = FontsContractCompat.fetchFonts(context, null, fontRequest);
                }
                catch (PackageManager.NameNotFoundException nameNotFoundException) {
                    this.val$callerThreadHandler.post(new Runnable(){

                        @Override
                        public void run() {
                            4.this.val$callback.onTypefaceRequestFailed(-1);
                        }
                    });
                    return;
                }
                if (typeface.getStatusCode() != 0) {
                    switch (typeface.getStatusCode()) {
                        default: {
                            this.val$callerThreadHandler.post(new Runnable(){

                                @Override
                                public void run() {
                                    4.this.val$callback.onTypefaceRequestFailed(-3);
                                }
                            });
                            return;
                        }
                        case 2: {
                            this.val$callerThreadHandler.post(new Runnable(){

                                @Override
                                public void run() {
                                    4.this.val$callback.onTypefaceRequestFailed(-3);
                                }
                            });
                            return;
                        }
                        case 1: 
                    }
                    this.val$callerThreadHandler.post(new Runnable(){

                        @Override
                        public void run() {
                            4.this.val$callback.onTypefaceRequestFailed(-2);
                        }
                    });
                    return;
                }
                if ((typeface = typeface.getFonts()) != null && typeface.length != 0) {
                    int n = typeface.length;
                    for (int i = 0; i < n; ++i) {
                        FontInfo fontInfo = typeface[i];
                        if (fontInfo.getResultCode() == 0) continue;
                        i = fontInfo.getResultCode();
                        if (i < 0) {
                            this.val$callerThreadHandler.post(new Runnable(){

                                @Override
                                public void run() {
                                    4.this.val$callback.onTypefaceRequestFailed(-3);
                                }
                            });
                            return;
                        }
                        this.val$callerThreadHandler.post(new Runnable(){

                            @Override
                            public void run() {
                                4.this.val$callback.onTypefaceRequestFailed(i);
                            }
                        });
                        return;
                    }
                    if ((typeface = FontsContractCompat.buildTypeface(context, null, (FontInfo[])typeface)) == null) {
                        this.val$callerThreadHandler.post(new Runnable(){

                            @Override
                            public void run() {
                                4.this.val$callback.onTypefaceRequestFailed(-3);
                            }
                        });
                        return;
                    }
                    this.val$callerThreadHandler.post(new Runnable(){

                        @Override
                        public void run() {
                            4.this.val$callback.onTypefaceRetrieved(typeface);
                        }
                    });
                    return;
                }
                this.val$callerThreadHandler.post(new Runnable(){

                    @Override
                    public void run() {
                        4.this.val$callback.onTypefaceRequestFailed(1);
                    }
                });
                return;
            }

        });
    }

    public static final class Columns
    implements BaseColumns {
        public static final String FILE_ID = "file_id";
        public static final String ITALIC = "font_italic";
        public static final String RESULT_CODE = "result_code";
        public static final int RESULT_CODE_FONT_NOT_FOUND = 1;
        public static final int RESULT_CODE_FONT_UNAVAILABLE = 2;
        public static final int RESULT_CODE_MALFORMED_QUERY = 3;
        public static final int RESULT_CODE_OK = 0;
        public static final String TTC_INDEX = "font_ttc_index";
        public static final String VARIATION_SETTINGS = "font_variation_settings";
        public static final String WEIGHT = "font_weight";
    }

    public static class FontFamilyResult {
        public static final int STATUS_OK = 0;
        public static final int STATUS_UNEXPECTED_DATA_PROVIDED = 2;
        public static final int STATUS_WRONG_CERTIFICATES = 1;
        private final FontInfo[] mFonts;
        private final int mStatusCode;

        @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
        public FontFamilyResult(int n, @Nullable FontInfo[] arrfontInfo) {
            this.mStatusCode = n;
            this.mFonts = arrfontInfo;
        }

        public FontInfo[] getFonts() {
            return this.mFonts;
        }

        public int getStatusCode() {
            return this.mStatusCode;
        }

        @Retention(value=RetentionPolicy.SOURCE)
        @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
        static @interface FontResultStatus {
        }

    }

    public static class FontInfo {
        private final boolean mItalic;
        private final int mResultCode;
        private final int mTtcIndex;
        private final Uri mUri;
        private final int mWeight;

        @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
        public FontInfo(@NonNull Uri uri, @IntRange(from=0L) int n, @IntRange(from=1L, to=1000L) int n2, boolean bl, int n3) {
            this.mUri = Preconditions.checkNotNull(uri);
            this.mTtcIndex = n;
            this.mWeight = n2;
            this.mItalic = bl;
            this.mResultCode = n3;
        }

        public int getResultCode() {
            return this.mResultCode;
        }

        @IntRange(from=0L)
        public int getTtcIndex() {
            return this.mTtcIndex;
        }

        @NonNull
        public Uri getUri() {
            return this.mUri;
        }

        @IntRange(from=1L, to=1000L)
        public int getWeight() {
            return this.mWeight;
        }

        public boolean isItalic() {
            return this.mItalic;
        }
    }

    public static class FontRequestCallback {
        public static final int FAIL_REASON_FONT_LOAD_ERROR = -3;
        public static final int FAIL_REASON_FONT_NOT_FOUND = 1;
        public static final int FAIL_REASON_FONT_UNAVAILABLE = 2;
        public static final int FAIL_REASON_MALFORMED_QUERY = 3;
        public static final int FAIL_REASON_PROVIDER_NOT_FOUND = -1;
        public static final int FAIL_REASON_WRONG_CERTIFICATES = -2;

        public void onTypefaceRequestFailed(int n) {
        }

        public void onTypefaceRetrieved(Typeface typeface) {
        }

        @Retention(value=RetentionPolicy.SOURCE)
        @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
        static @interface FontRequestFailReason {
        }

    }

}

