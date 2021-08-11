/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.ContentResolver
 *  android.content.Context
 *  android.graphics.Bitmap
 *  android.graphics.Bitmap$Config
 *  android.graphics.BitmapFactory
 *  android.graphics.BitmapFactory$Options
 *  android.graphics.Canvas
 *  android.graphics.ColorFilter
 *  android.graphics.ColorMatrix
 *  android.graphics.ColorMatrixColorFilter
 *  android.graphics.Matrix
 *  android.graphics.Paint
 *  android.graphics.Rect
 *  android.graphics.RectF
 *  android.graphics.pdf.PdfDocument
 *  android.graphics.pdf.PdfDocument$Page
 *  android.graphics.pdf.PdfDocument$PageInfo
 *  android.net.Uri
 *  android.os.AsyncTask
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.os.CancellationSignal
 *  android.os.CancellationSignal$OnCancelListener
 *  android.os.ParcelFileDescriptor
 *  android.print.PageRange
 *  android.print.PrintAttributes
 *  android.print.PrintAttributes$Builder
 *  android.print.PrintAttributes$Margins
 *  android.print.PrintAttributes$MediaSize
 *  android.print.PrintAttributes$Resolution
 *  android.print.PrintDocumentAdapter
 *  android.print.PrintDocumentAdapter$LayoutResultCallback
 *  android.print.PrintDocumentAdapter$WriteResultCallback
 *  android.print.PrintDocumentInfo
 *  android.print.PrintDocumentInfo$Builder
 *  android.print.PrintJob
 *  android.print.PrintManager
 *  android.print.pdf.PrintedPdfDocument
 *  android.util.Log
 */
package android.support.v4.print;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.PrintJob;
import android.print.PrintManager;
import android.print.pdf.PrintedPdfDocument;
import android.support.annotation.RequiresApi;
import android.util.Log;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class PrintHelper {
    public static final int COLOR_MODE_COLOR = 2;
    public static final int COLOR_MODE_MONOCHROME = 1;
    public static final int ORIENTATION_LANDSCAPE = 1;
    public static final int ORIENTATION_PORTRAIT = 2;
    public static final int SCALE_MODE_FILL = 2;
    public static final int SCALE_MODE_FIT = 1;
    private final PrintHelperVersionImpl mImpl;

    public PrintHelper(Context context) {
        if (Build.VERSION.SDK_INT >= 24) {
            this.mImpl = new PrintHelperApi24(context);
            return;
        }
        if (Build.VERSION.SDK_INT >= 23) {
            this.mImpl = new PrintHelperApi23(context);
            return;
        }
        if (Build.VERSION.SDK_INT >= 20) {
            this.mImpl = new PrintHelperApi20(context);
            return;
        }
        if (Build.VERSION.SDK_INT >= 19) {
            this.mImpl = new PrintHelperApi19(context);
            return;
        }
        this.mImpl = new PrintHelperStub();
    }

    public static boolean systemSupportsPrint() {
        if (Build.VERSION.SDK_INT >= 19) {
            return true;
        }
        return false;
    }

    public int getColorMode() {
        return this.mImpl.getColorMode();
    }

    public int getOrientation() {
        return this.mImpl.getOrientation();
    }

    public int getScaleMode() {
        return this.mImpl.getScaleMode();
    }

    public void printBitmap(String string2, Bitmap bitmap) {
        this.mImpl.printBitmap(string2, bitmap, null);
    }

    public void printBitmap(String string2, Bitmap bitmap, OnPrintFinishCallback onPrintFinishCallback) {
        this.mImpl.printBitmap(string2, bitmap, onPrintFinishCallback);
    }

    public void printBitmap(String string2, Uri uri) throws FileNotFoundException {
        this.mImpl.printBitmap(string2, uri, null);
    }

    public void printBitmap(String string2, Uri uri, OnPrintFinishCallback onPrintFinishCallback) throws FileNotFoundException {
        this.mImpl.printBitmap(string2, uri, onPrintFinishCallback);
    }

    public void setColorMode(int n) {
        this.mImpl.setColorMode(n);
    }

    public void setOrientation(int n) {
        this.mImpl.setOrientation(n);
    }

    public void setScaleMode(int n) {
        this.mImpl.setScaleMode(n);
    }

    @Retention(value=RetentionPolicy.SOURCE)
    private static @interface ColorMode {
    }

    public static interface OnPrintFinishCallback {
        public void onFinish();
    }

    @Retention(value=RetentionPolicy.SOURCE)
    private static @interface Orientation {
    }

    @RequiresApi(value=19)
    private static class PrintHelperApi19
    implements PrintHelperVersionImpl {
        private static final String LOG_TAG = "PrintHelperApi19";
        private static final int MAX_PRINT_SIZE = 3500;
        int mColorMode = 2;
        final Context mContext;
        BitmapFactory.Options mDecodeOptions = null;
        protected boolean mIsMinMarginsHandlingCorrect = true;
        private final Object mLock = new Object();
        int mOrientation;
        protected boolean mPrintActivityRespectsOrientation = true;
        int mScaleMode = 2;

        PrintHelperApi19(Context context) {
            this.mContext = context;
        }

        private Bitmap convertBitmapForColorMode(Bitmap bitmap, int n) {
            if (n != 1) {
                return bitmap;
            }
            Bitmap bitmap2 = Bitmap.createBitmap((int)bitmap.getWidth(), (int)bitmap.getHeight(), (Bitmap.Config)Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap2);
            Paint paint = new Paint();
            ColorMatrix colorMatrix = new ColorMatrix();
            colorMatrix.setSaturation(0.0f);
            paint.setColorFilter((ColorFilter)new ColorMatrixColorFilter(colorMatrix));
            canvas.drawBitmap(bitmap, 0.0f, 0.0f, paint);
            canvas.setBitmap(null);
            return bitmap2;
        }

        private Matrix getMatrix(int n, int n2, RectF rectF, int n3) {
            Matrix matrix = new Matrix();
            float f = rectF.width() / (float)n;
            f = n3 == 2 ? Math.max(f, rectF.height() / (float)n2) : Math.min(f, rectF.height() / (float)n2);
            matrix.postScale(f, f);
            matrix.postTranslate((rectF.width() - (float)n * f) / 2.0f, (rectF.height() - (float)n2 * f) / 2.0f);
            return matrix;
        }

        private static boolean isPortrait(Bitmap bitmap) {
            if (bitmap.getWidth() <= bitmap.getHeight()) {
                return true;
            }
            return false;
        }

        private Bitmap loadBitmap(Uri object, BitmapFactory.Options options) throws FileNotFoundException {
            Context context;
            if (object != null && (context = this.mContext) != null) {
                block9 : {
                    Object object2 = null;
                    try {
                        object2 = object = context.getContentResolver().openInputStream((Uri)object);
                    }
                    catch (Throwable throwable) {
                        if (object2 != null) {
                            try {
                                object2.close();
                            }
                            catch (IOException iOException) {
                                Log.w((String)"PrintHelperApi19", (String)"close fail ", (Throwable)iOException);
                            }
                        }
                        throw throwable;
                    }
                    options = BitmapFactory.decodeStream((InputStream)object, (Rect)null, (BitmapFactory.Options)options);
                    if (object == null) break block9;
                    try {
                        object.close();
                        return options;
                    }
                    catch (IOException iOException) {
                        Log.w((String)"PrintHelperApi19", (String)"close fail ", (Throwable)iOException);
                        return options;
                    }
                }
                return options;
            }
            throw new IllegalArgumentException("bad argument to loadBitmap");
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        private Bitmap loadConstrainedBitmap(Uri object) throws FileNotFoundException {
            BitmapFactory.Options options;
            if (object == null) throw new IllegalArgumentException("bad argument to getScaledBitmap");
            if (this.mContext == null) throw new IllegalArgumentException("bad argument to getScaledBitmap");
            Object object2 = new BitmapFactory.Options();
            object2.inJustDecodeBounds = true;
            this.loadBitmap((Uri)object, (BitmapFactory.Options)object2);
            int n = object2.outWidth;
            int n2 = object2.outHeight;
            if (n <= 0) return null;
            if (n2 <= 0) {
                return null;
            }
            int n3 = 1;
            for (int i = Math.max((int)n, (int)n2); i > 3500; i >>>= 1, n3 <<= 1) {
            }
            if (n3 <= 0) return null;
            if (Math.min(n, n2) / n3 <= 0) {
                return null;
            }
            object2 = this.mLock;
            synchronized (object2) {
                this.mDecodeOptions = new BitmapFactory.Options();
                this.mDecodeOptions.inMutable = true;
                this.mDecodeOptions.inSampleSize = n3;
                options = this.mDecodeOptions;
            }
            try {
                object2 = this.loadBitmap((Uri)object, options);
                return object2;
            }
            finally {
                object = this.mLock;
                synchronized (object) {
                    this.mDecodeOptions = null;
                }
            }
        }

        private void writeBitmap(final PrintAttributes printAttributes, final int n, final Bitmap bitmap, final ParcelFileDescriptor parcelFileDescriptor, final CancellationSignal cancellationSignal, final PrintDocumentAdapter.WriteResultCallback writeResultCallback) {
            final PrintAttributes printAttributes2 = this.mIsMinMarginsHandlingCorrect ? printAttributes : this.copyAttributes(printAttributes).setMinMargins(new PrintAttributes.Margins(0, 0, 0, 0)).build();
            new AsyncTask<Void, Void, Throwable>(){

                /*
                 * Loose catch block
                 * Enabled aggressive block sorting
                 * Enabled unnecessary exception pruning
                 * Enabled aggressive exception aggregation
                 * Lifted jumps to return sites
                 */
                protected /* varargs */ Throwable doInBackground(Void ... rectF) {
                    PrintedPdfDocument printedPdfDocument;
                    Bitmap bitmap2;
                    block21 : {
                        PrintedPdfDocument printedPdfDocument2;
                        boolean bl;
                        block20 : {
                            try {
                                if (cancellationSignal.isCanceled()) {
                                    return null;
                                }
                                printedPdfDocument = new PrintedPdfDocument(PrintHelperApi19.this.mContext, printAttributes2);
                                bitmap2 = PrintHelperApi19.this.convertBitmapForColorMode(bitmap, printAttributes2.getColorMode());
                                bl = cancellationSignal.isCanceled();
                                if (!bl) break block20;
                                return null;
                            }
                            catch (Throwable throwable) {
                                return throwable;
                            }
                        }
                        PdfDocument.Page page = printedPdfDocument.startPage(1);
                        if (PrintHelperApi19.this.mIsMinMarginsHandlingCorrect) {
                            rectF = new RectF(page.getInfo().getContentRect());
                        } else {
                            printedPdfDocument2 = new PrintedPdfDocument(PrintHelperApi19.this.mContext, printAttributes);
                            PdfDocument.Page page2 = printedPdfDocument2.startPage(1);
                            rectF = new RectF(page2.getInfo().getContentRect());
                            printedPdfDocument2.finishPage(page2);
                            printedPdfDocument2.close();
                        }
                        printedPdfDocument2 = PrintHelperApi19.this.getMatrix(bitmap2.getWidth(), bitmap2.getHeight(), rectF, n);
                        if (!PrintHelperApi19.this.mIsMinMarginsHandlingCorrect) {
                            printedPdfDocument2.postTranslate(rectF.left, rectF.top);
                            page.getCanvas().clipRect(rectF);
                        }
                        page.getCanvas().drawBitmap(bitmap2, (Matrix)printedPdfDocument2, null);
                        printedPdfDocument.finishPage(page);
                        bl = cancellationSignal.isCanceled();
                        if (!bl) break block21;
                        printedPdfDocument.close();
                        rectF = parcelFileDescriptor;
                        if (rectF != null) {
                            try {
                                parcelFileDescriptor.close();
                            }
                            catch (IOException iOException) {
                                // empty catch block
                            }
                        }
                        if (bitmap2 == bitmap) return null;
                        bitmap2.recycle();
                        return null;
                    }
                    printedPdfDocument.writeTo((OutputStream)new FileOutputStream(parcelFileDescriptor.getFileDescriptor()));
                    {
                        catch (Throwable throwable) {
                            printedPdfDocument.close();
                            printedPdfDocument = parcelFileDescriptor;
                            if (printedPdfDocument != null) {
                                try {
                                    parcelFileDescriptor.close();
                                }
                                catch (IOException iOException) {
                                    // empty catch block
                                }
                            }
                            if (bitmap2 == bitmap) throw throwable;
                            bitmap2.recycle();
                            throw throwable;
                        }
                    }
                    printedPdfDocument.close();
                    rectF = parcelFileDescriptor;
                    if (rectF != null) {
                        try {
                            parcelFileDescriptor.close();
                        }
                        catch (IOException iOException) {
                            // empty catch block
                        }
                    }
                    if (bitmap2 == bitmap) return null;
                    bitmap2.recycle();
                    return null;
                }

                protected void onPostExecute(Throwable throwable) {
                    if (cancellationSignal.isCanceled()) {
                        writeResultCallback.onWriteCancelled();
                        return;
                    }
                    if (throwable == null) {
                        writeResultCallback.onWriteFinished(new PageRange[]{PageRange.ALL_PAGES});
                        return;
                    }
                    Log.e((String)"PrintHelperApi19", (String)"Error writing printed content", (Throwable)throwable);
                    writeResultCallback.onWriteFailed(null);
                }
            }.execute((Object[])new Void[0]);
        }

        protected PrintAttributes.Builder copyAttributes(PrintAttributes printAttributes) {
            PrintAttributes.Builder builder = new PrintAttributes.Builder().setMediaSize(printAttributes.getMediaSize()).setResolution(printAttributes.getResolution()).setMinMargins(printAttributes.getMinMargins());
            if (printAttributes.getColorMode() != 0) {
                builder.setColorMode(printAttributes.getColorMode());
                return builder;
            }
            return builder;
        }

        @Override
        public int getColorMode() {
            return this.mColorMode;
        }

        @Override
        public int getOrientation() {
            int n = this.mOrientation;
            if (n == 0) {
                return 1;
            }
            return n;
        }

        @Override
        public int getScaleMode() {
            return this.mScaleMode;
        }

        @Override
        public void printBitmap(final String string2, final Bitmap bitmap, final OnPrintFinishCallback onPrintFinishCallback) {
            if (bitmap == null) {
                return;
            }
            final int n = this.mScaleMode;
            PrintManager printManager = (PrintManager)this.mContext.getSystemService("print");
            PrintAttributes.MediaSize mediaSize = PrintHelperApi19.isPortrait(bitmap) ? PrintAttributes.MediaSize.UNKNOWN_PORTRAIT : PrintAttributes.MediaSize.UNKNOWN_LANDSCAPE;
            mediaSize = new PrintAttributes.Builder().setMediaSize(mediaSize).setColorMode(this.mColorMode).build();
            printManager.print(string2, new PrintDocumentAdapter(){
                private PrintAttributes mAttributes;

                public void onFinish() {
                    OnPrintFinishCallback onPrintFinishCallback2 = onPrintFinishCallback;
                    if (onPrintFinishCallback2 != null) {
                        onPrintFinishCallback2.onFinish();
                        return;
                    }
                }

                public void onLayout(PrintAttributes printAttributes, PrintAttributes printAttributes2, CancellationSignal cancellationSignal, PrintDocumentAdapter.LayoutResultCallback layoutResultCallback, Bundle bundle) {
                    this.mAttributes = printAttributes2;
                    layoutResultCallback.onLayoutFinished(new PrintDocumentInfo.Builder(string2).setContentType(1).setPageCount(1).build(), true ^ printAttributes2.equals((Object)printAttributes));
                }

                public void onWrite(PageRange[] arrpageRange, ParcelFileDescriptor parcelFileDescriptor, CancellationSignal cancellationSignal, PrintDocumentAdapter.WriteResultCallback writeResultCallback) {
                    PrintHelperApi19.this.writeBitmap(this.mAttributes, n, bitmap, parcelFileDescriptor, cancellationSignal, writeResultCallback);
                }
            }, (PrintAttributes)mediaSize);
        }

        @Override
        public void printBitmap(final String string2, Uri object, OnPrintFinishCallback onPrintFinishCallback) throws FileNotFoundException {
            object = new PrintDocumentAdapter((Uri)object, onPrintFinishCallback, this.mScaleMode){
                private PrintAttributes mAttributes;
                Bitmap mBitmap;
                AsyncTask<Uri, Boolean, Bitmap> mLoadBitmap;
                final /* synthetic */ OnPrintFinishCallback val$callback;
                final /* synthetic */ int val$fittingMode;
                final /* synthetic */ Uri val$imageFile;
                {
                    this.val$imageFile = uri;
                    this.val$callback = onPrintFinishCallback;
                    this.val$fittingMode = n;
                    this.mBitmap = null;
                }

                /*
                 * Enabled aggressive block sorting
                 * Enabled unnecessary exception pruning
                 * Enabled aggressive exception aggregation
                 */
                private void cancelLoad() {
                    Object object = PrintHelperApi19.this.mLock;
                    synchronized (object) {
                        if (PrintHelperApi19.this.mDecodeOptions != null) {
                            PrintHelperApi19.this.mDecodeOptions.requestCancelDecode();
                            PrintHelperApi19.this.mDecodeOptions = null;
                        }
                        return;
                    }
                }

                public void onFinish() {
                    super.onFinish();
                    this.cancelLoad();
                    Object object = this.mLoadBitmap;
                    if (object != null) {
                        object.cancel(true);
                    }
                    if ((object = this.val$callback) != null) {
                        object.onFinish();
                    }
                    if ((object = this.mBitmap) != null) {
                        object.recycle();
                        this.mBitmap = null;
                        return;
                    }
                }

                /*
                 * Enabled aggressive block sorting
                 * Enabled unnecessary exception pruning
                 * Enabled aggressive exception aggregation
                 * Converted monitor instructions to comments
                 * Lifted jumps to return sites
                 */
                public void onLayout(final PrintAttributes printAttributes, final PrintAttributes printAttributes2, final CancellationSignal cancellationSignal, final PrintDocumentAdapter.LayoutResultCallback layoutResultCallback, Bundle bundle) {
                    // MONITORENTER : this
                    this.mAttributes = printAttributes2;
                    // MONITOREXIT : this
                    if (cancellationSignal.isCanceled()) {
                        layoutResultCallback.onLayoutCancelled();
                        return;
                    }
                    if (this.mBitmap != null) {
                        layoutResultCallback.onLayoutFinished(new PrintDocumentInfo.Builder(string2).setContentType(1).setPageCount(1).build(), true ^ printAttributes2.equals((Object)printAttributes));
                        return;
                    }
                    this.mLoadBitmap = new AsyncTask<Uri, Boolean, Bitmap>(){

                        protected /* varargs */ Bitmap doInBackground(Uri ... bitmap) {
                            try {
                                bitmap = PrintHelperApi19.this.loadConstrainedBitmap(3.this.val$imageFile);
                                return bitmap;
                            }
                            catch (FileNotFoundException fileNotFoundException) {
                                return null;
                            }
                        }

                        protected void onCancelled(Bitmap bitmap) {
                            layoutResultCallback.onLayoutCancelled();
                            3.this.mLoadBitmap = null;
                        }

                        /*
                         * Enabled aggressive block sorting
                         * Enabled unnecessary exception pruning
                         * Enabled aggressive exception aggregation
                         * Converted monitor instructions to comments
                         * Lifted jumps to return sites
                         */
                        protected void onPostExecute(Bitmap bitmap) {
                            PrintHelperApi19 var3_2;
                            super.onPostExecute((Object)bitmap);
                            if (!(bitmap == null || PrintHelperApi19.this.mPrintActivityRespectsOrientation && PrintHelperApi19.this.mOrientation != 0)) {
                                // MONITORENTER : this
                                var3_2 = 3.this.mAttributes.getMediaSize();
                                // MONITOREXIT : this
                                if (var3_2 != null && var3_2.isPortrait() != PrintHelperApi19.isPortrait(bitmap)) {
                                    var3_2 = new Matrix();
                                    var3_2.postRotate(90.0f);
                                    bitmap = Bitmap.createBitmap((Bitmap)bitmap, (int)0, (int)0, (int)bitmap.getWidth(), (int)bitmap.getHeight(), (Matrix)var3_2, (boolean)true);
                                }
                            }
                            var3_2 = 3.this;
                            var3_2.mBitmap = bitmap;
                            if (bitmap != null) {
                                bitmap = new PrintDocumentInfo.Builder(var3_2.string2).setContentType(1).setPageCount(1).build();
                                boolean bl = printAttributes2.equals((Object)printAttributes);
                                layoutResultCallback.onLayoutFinished((PrintDocumentInfo)bitmap, true ^ bl);
                            } else {
                                layoutResultCallback.onLayoutFailed(null);
                            }
                            3.this.mLoadBitmap = null;
                        }

                        protected void onPreExecute() {
                            cancellationSignal.setOnCancelListener(new CancellationSignal.OnCancelListener(){

                                public void onCancel() {
                                    3.this.cancelLoad();
                                    1.this.cancel(false);
                                }
                            });
                        }

                    }.execute((Object[])new Uri[0]);
                }

                public void onWrite(PageRange[] arrpageRange, ParcelFileDescriptor parcelFileDescriptor, CancellationSignal cancellationSignal, PrintDocumentAdapter.WriteResultCallback writeResultCallback) {
                    PrintHelperApi19.this.writeBitmap(this.mAttributes, this.val$fittingMode, this.mBitmap, parcelFileDescriptor, cancellationSignal, writeResultCallback);
                }

            };
            onPrintFinishCallback = (PrintManager)this.mContext.getSystemService("print");
            PrintAttributes.Builder builder = new PrintAttributes.Builder();
            builder.setColorMode(this.mColorMode);
            int n = this.mOrientation;
            if (n != 1 && n != 0) {
                if (n == 2) {
                    builder.setMediaSize(PrintAttributes.MediaSize.UNKNOWN_PORTRAIT);
                }
            } else {
                builder.setMediaSize(PrintAttributes.MediaSize.UNKNOWN_LANDSCAPE);
            }
            onPrintFinishCallback.print(string2, (PrintDocumentAdapter)object, builder.build());
        }

        @Override
        public void setColorMode(int n) {
            this.mColorMode = n;
        }

        @Override
        public void setOrientation(int n) {
            this.mOrientation = n;
        }

        @Override
        public void setScaleMode(int n) {
            this.mScaleMode = n;
        }

    }

    @RequiresApi(value=20)
    private static class PrintHelperApi20
    extends PrintHelperApi19 {
        PrintHelperApi20(Context context) {
            super(context);
            this.mPrintActivityRespectsOrientation = false;
        }
    }

    @RequiresApi(value=23)
    private static class PrintHelperApi23
    extends PrintHelperApi20 {
        PrintHelperApi23(Context context) {
            super(context);
            this.mIsMinMarginsHandlingCorrect = false;
        }

        @Override
        protected PrintAttributes.Builder copyAttributes(PrintAttributes printAttributes) {
            PrintAttributes.Builder builder = super.copyAttributes(printAttributes);
            if (printAttributes.getDuplexMode() != 0) {
                builder.setDuplexMode(printAttributes.getDuplexMode());
                return builder;
            }
            return builder;
        }
    }

    @RequiresApi(value=24)
    private static class PrintHelperApi24
    extends PrintHelperApi23 {
        PrintHelperApi24(Context context) {
            super(context);
            this.mIsMinMarginsHandlingCorrect = true;
            this.mPrintActivityRespectsOrientation = true;
        }
    }

    private static final class PrintHelperStub
    implements PrintHelperVersionImpl {
        int mColorMode = 2;
        int mOrientation = 1;
        int mScaleMode = 2;

        private PrintHelperStub() {
        }

        @Override
        public int getColorMode() {
            return this.mColorMode;
        }

        @Override
        public int getOrientation() {
            return this.mOrientation;
        }

        @Override
        public int getScaleMode() {
            return this.mScaleMode;
        }

        @Override
        public void printBitmap(String string2, Bitmap bitmap, OnPrintFinishCallback onPrintFinishCallback) {
        }

        @Override
        public void printBitmap(String string2, Uri uri, OnPrintFinishCallback onPrintFinishCallback) {
        }

        @Override
        public void setColorMode(int n) {
            this.mColorMode = n;
        }

        @Override
        public void setOrientation(int n) {
            this.mOrientation = n;
        }

        @Override
        public void setScaleMode(int n) {
            this.mScaleMode = n;
        }
    }

    static interface PrintHelperVersionImpl {
        public int getColorMode();

        public int getOrientation();

        public int getScaleMode();

        public void printBitmap(String var1, Bitmap var2, OnPrintFinishCallback var3);

        public void printBitmap(String var1, Uri var2, OnPrintFinishCallback var3) throws FileNotFoundException;

        public void setColorMode(int var1);

        public void setOrientation(int var1);

        public void setScaleMode(int var1);
    }

    @Retention(value=RetentionPolicy.SOURCE)
    private static @interface ScaleMode {
    }

}

