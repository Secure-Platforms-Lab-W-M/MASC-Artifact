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
package androidx.print;

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
import android.util.Log;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class PrintHelper {
    public static final int COLOR_MODE_COLOR = 2;
    public static final int COLOR_MODE_MONOCHROME = 1;
    static final boolean IS_MIN_MARGINS_HANDLING_CORRECT;
    private static final String LOG_TAG = "PrintHelper";
    private static final int MAX_PRINT_SIZE = 3500;
    public static final int ORIENTATION_LANDSCAPE = 1;
    public static final int ORIENTATION_PORTRAIT = 2;
    static final boolean PRINT_ACTIVITY_RESPECTS_ORIENTATION;
    public static final int SCALE_MODE_FILL = 2;
    public static final int SCALE_MODE_FIT = 1;
    int mColorMode = 2;
    final Context mContext;
    BitmapFactory.Options mDecodeOptions = null;
    final Object mLock = new Object();
    int mOrientation = 1;
    int mScaleMode = 2;

    static {
        int n = Build.VERSION.SDK_INT;
        boolean bl = false;
        boolean bl2 = n < 20 || Build.VERSION.SDK_INT > 23;
        PRINT_ACTIVITY_RESPECTS_ORIENTATION = bl2;
        bl2 = bl;
        if (Build.VERSION.SDK_INT != 23) {
            bl2 = true;
        }
        IS_MIN_MARGINS_HANDLING_CORRECT = bl2;
    }

    public PrintHelper(Context context) {
        this.mContext = context;
    }

    static Bitmap convertBitmapForColorMode(Bitmap bitmap, int n) {
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

    private static PrintAttributes.Builder copyAttributes(PrintAttributes printAttributes) {
        PrintAttributes.Builder builder = new PrintAttributes.Builder().setMediaSize(printAttributes.getMediaSize()).setResolution(printAttributes.getResolution()).setMinMargins(printAttributes.getMinMargins());
        if (printAttributes.getColorMode() != 0) {
            builder.setColorMode(printAttributes.getColorMode());
        }
        if (Build.VERSION.SDK_INT >= 23 && printAttributes.getDuplexMode() != 0) {
            builder.setDuplexMode(printAttributes.getDuplexMode());
        }
        return builder;
    }

    static Matrix getMatrix(int n, int n2, RectF rectF, int n3) {
        Matrix matrix = new Matrix();
        float f = rectF.width() / (float)n;
        f = n3 == 2 ? Math.max(f, rectF.height() / (float)n2) : Math.min(f, rectF.height() / (float)n2);
        matrix.postScale(f, f);
        matrix.postTranslate((rectF.width() - (float)n * f) / 2.0f, (rectF.height() - (float)n2 * f) / 2.0f);
        return matrix;
    }

    static boolean isPortrait(Bitmap bitmap) {
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
                            Log.w((String)"PrintHelper", (String)"close fail ", (Throwable)iOException);
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
                    Log.w((String)"PrintHelper", (String)"close fail ", (Throwable)iOException);
                }
            }
            return options;
        }
        throw new IllegalArgumentException("bad argument to loadBitmap");
    }

    public static boolean systemSupportsPrint() {
        if (Build.VERSION.SDK_INT >= 19) {
            return true;
        }
        return false;
    }

    public int getColorMode() {
        return this.mColorMode;
    }

    public int getOrientation() {
        if (Build.VERSION.SDK_INT >= 19 && this.mOrientation == 0) {
            return 1;
        }
        return this.mOrientation;
    }

    public int getScaleMode() {
        return this.mScaleMode;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    Bitmap loadConstrainedBitmap(Uri object) throws FileNotFoundException {
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
            this.mDecodeOptions = options = new BitmapFactory.Options();
            options.inMutable = true;
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

    public void printBitmap(String string2, Bitmap bitmap) {
        this.printBitmap(string2, bitmap, null);
    }

    public void printBitmap(String string2, Bitmap bitmap, OnPrintFinishCallback onPrintFinishCallback) {
        if (Build.VERSION.SDK_INT >= 19) {
            if (bitmap == null) {
                return;
            }
            PrintManager printManager = (PrintManager)this.mContext.getSystemService("print");
            PrintAttributes.MediaSize mediaSize = PrintHelper.isPortrait(bitmap) ? PrintAttributes.MediaSize.UNKNOWN_PORTRAIT : PrintAttributes.MediaSize.UNKNOWN_LANDSCAPE;
            mediaSize = new PrintAttributes.Builder().setMediaSize(mediaSize).setColorMode(this.mColorMode).build();
            printManager.print(string2, (PrintDocumentAdapter)new PrintBitmapAdapter(string2, this.mScaleMode, bitmap, onPrintFinishCallback), (PrintAttributes)mediaSize);
            return;
        }
    }

    public void printBitmap(String string2, Uri uri) throws FileNotFoundException {
        this.printBitmap(string2, uri, null);
    }

    public void printBitmap(String string2, Uri object, OnPrintFinishCallback onPrintFinishCallback) throws FileNotFoundException {
        if (Build.VERSION.SDK_INT < 19) {
            return;
        }
        object = new PrintUriAdapter(string2, (Uri)object, onPrintFinishCallback, this.mScaleMode);
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

    public void setColorMode(int n) {
        this.mColorMode = n;
    }

    public void setOrientation(int n) {
        this.mOrientation = n;
    }

    public void setScaleMode(int n) {
        this.mScaleMode = n;
    }

    void writeBitmap(final PrintAttributes printAttributes, final int n, final Bitmap bitmap, final ParcelFileDescriptor parcelFileDescriptor, final CancellationSignal cancellationSignal, final PrintDocumentAdapter.WriteResultCallback writeResultCallback) {
        final PrintAttributes printAttributes2 = IS_MIN_MARGINS_HANDLING_CORRECT ? printAttributes : PrintHelper.copyAttributes(printAttributes).setMinMargins(new PrintAttributes.Margins(0, 0, 0, 0)).build();
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
                            printedPdfDocument = new PrintedPdfDocument(PrintHelper.this.mContext, printAttributes2);
                            bitmap2 = PrintHelper.convertBitmapForColorMode(bitmap, printAttributes2.getColorMode());
                            bl = cancellationSignal.isCanceled();
                            if (!bl) break block20;
                            return null;
                        }
                        catch (Throwable throwable) {
                            return throwable;
                        }
                    }
                    PdfDocument.Page page = printedPdfDocument.startPage(1);
                    if (PrintHelper.IS_MIN_MARGINS_HANDLING_CORRECT) {
                        rectF = new RectF(page.getInfo().getContentRect());
                    } else {
                        printedPdfDocument2 = new PrintedPdfDocument(PrintHelper.this.mContext, printAttributes);
                        PdfDocument.Page page2 = printedPdfDocument2.startPage(1);
                        rectF = new RectF(page2.getInfo().getContentRect());
                        printedPdfDocument2.finishPage(page2);
                        printedPdfDocument2.close();
                    }
                    printedPdfDocument2 = PrintHelper.getMatrix(bitmap2.getWidth(), bitmap2.getHeight(), rectF, n);
                    if (!PrintHelper.IS_MIN_MARGINS_HANDLING_CORRECT) {
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
                Log.e((String)"PrintHelper", (String)"Error writing printed content", (Throwable)throwable);
                writeResultCallback.onWriteFailed(null);
            }
        }.execute((Object[])new Void[0]);
    }

    public static interface OnPrintFinishCallback {
        public void onFinish();
    }

    private class PrintBitmapAdapter
    extends PrintDocumentAdapter {
        private PrintAttributes mAttributes;
        private final Bitmap mBitmap;
        private final OnPrintFinishCallback mCallback;
        private final int mFittingMode;
        private final String mJobName;

        PrintBitmapAdapter(String string2, int n, Bitmap bitmap, OnPrintFinishCallback onPrintFinishCallback) {
            this.mJobName = string2;
            this.mFittingMode = n;
            this.mBitmap = bitmap;
            this.mCallback = onPrintFinishCallback;
        }

        public void onFinish() {
            OnPrintFinishCallback onPrintFinishCallback = this.mCallback;
            if (onPrintFinishCallback != null) {
                onPrintFinishCallback.onFinish();
            }
        }

        public void onLayout(PrintAttributes printAttributes, PrintAttributes printAttributes2, CancellationSignal cancellationSignal, PrintDocumentAdapter.LayoutResultCallback layoutResultCallback, Bundle bundle) {
            this.mAttributes = printAttributes2;
            layoutResultCallback.onLayoutFinished(new PrintDocumentInfo.Builder(this.mJobName).setContentType(1).setPageCount(1).build(), true ^ printAttributes2.equals((Object)printAttributes));
        }

        public void onWrite(PageRange[] arrpageRange, ParcelFileDescriptor parcelFileDescriptor, CancellationSignal cancellationSignal, PrintDocumentAdapter.WriteResultCallback writeResultCallback) {
            PrintHelper.this.writeBitmap(this.mAttributes, this.mFittingMode, this.mBitmap, parcelFileDescriptor, cancellationSignal, writeResultCallback);
        }
    }

    private class PrintUriAdapter
    extends PrintDocumentAdapter {
        PrintAttributes mAttributes;
        Bitmap mBitmap;
        final OnPrintFinishCallback mCallback;
        final int mFittingMode;
        final Uri mImageFile;
        final String mJobName;
        AsyncTask<Uri, Boolean, Bitmap> mLoadBitmap;

        PrintUriAdapter(String string2, Uri uri, OnPrintFinishCallback onPrintFinishCallback, int n) {
            this.mJobName = string2;
            this.mImageFile = uri;
            this.mCallback = onPrintFinishCallback;
            this.mFittingMode = n;
            this.mBitmap = null;
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        void cancelLoad() {
            Object object = PrintHelper.this.mLock;
            synchronized (object) {
                if (PrintHelper.this.mDecodeOptions != null) {
                    if (Build.VERSION.SDK_INT < 24) {
                        PrintHelper.this.mDecodeOptions.requestCancelDecode();
                    }
                    PrintHelper.this.mDecodeOptions = null;
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
            if ((object = this.mCallback) != null) {
                object.onFinish();
            }
            if ((object = this.mBitmap) != null) {
                object.recycle();
                this.mBitmap = null;
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
                layoutResultCallback.onLayoutFinished(new PrintDocumentInfo.Builder(this.mJobName).setContentType(1).setPageCount(1).build(), true ^ printAttributes2.equals((Object)printAttributes));
                return;
            }
            this.mLoadBitmap = new AsyncTask<Uri, Boolean, Bitmap>(){

                protected /* varargs */ Bitmap doInBackground(Uri ... bitmap) {
                    try {
                        bitmap = PrintHelper.this.loadConstrainedBitmap(PrintUriAdapter.this.mImageFile);
                        return bitmap;
                    }
                    catch (FileNotFoundException fileNotFoundException) {
                        return null;
                    }
                }

                protected void onCancelled(Bitmap bitmap) {
                    layoutResultCallback.onLayoutCancelled();
                    PrintUriAdapter.this.mLoadBitmap = null;
                }

                /*
                 * Enabled aggressive block sorting
                 * Enabled unnecessary exception pruning
                 * Enabled aggressive exception aggregation
                 */
                protected void onPostExecute(Bitmap bitmap) {
                    Bitmap bitmap2;
                    block10 : {
                        PrintAttributes.MediaSize mediaSize;
                        block11 : {
                            super.onPostExecute((Object)bitmap);
                            bitmap2 = bitmap;
                            if (bitmap == null) break block10;
                            if (!PrintHelper.PRINT_ACTIVITY_RESPECTS_ORIENTATION) break block11;
                            bitmap2 = bitmap;
                            if (PrintHelper.this.mOrientation != 0) break block10;
                        }
                        synchronized (this) {
                            mediaSize = PrintUriAdapter.this.mAttributes.getMediaSize();
                        }
                        bitmap2 = bitmap;
                        if (mediaSize != null) {
                            bitmap2 = bitmap;
                            if (mediaSize.isPortrait() != PrintHelper.isPortrait(bitmap)) {
                                bitmap2 = new Matrix();
                                bitmap2.postRotate(90.0f);
                                bitmap2 = Bitmap.createBitmap((Bitmap)bitmap, (int)0, (int)0, (int)bitmap.getWidth(), (int)bitmap.getHeight(), (Matrix)bitmap2, (boolean)true);
                            }
                        }
                    }
                    PrintUriAdapter.this.mBitmap = bitmap2;
                    if (bitmap2 != null) {
                        bitmap = new PrintDocumentInfo.Builder(PrintUriAdapter.this.mJobName).setContentType(1).setPageCount(1).build();
                        boolean bl = printAttributes2.equals((Object)printAttributes);
                        layoutResultCallback.onLayoutFinished((PrintDocumentInfo)bitmap, true ^ bl);
                    } else {
                        layoutResultCallback.onLayoutFailed(null);
                    }
                    PrintUriAdapter.this.mLoadBitmap = null;
                }

                protected void onPreExecute() {
                    cancellationSignal.setOnCancelListener(new CancellationSignal.OnCancelListener(){

                        public void onCancel() {
                            PrintUriAdapter.this.cancelLoad();
                            1.this.cancel(false);
                        }
                    });
                }

            }.execute((Object[])new Uri[0]);
        }

        public void onWrite(PageRange[] arrpageRange, ParcelFileDescriptor parcelFileDescriptor, CancellationSignal cancellationSignal, PrintDocumentAdapter.WriteResultCallback writeResultCallback) {
            PrintHelper.this.writeBitmap(this.mAttributes, this.mFittingMode, this.mBitmap, parcelFileDescriptor, cancellationSignal, writeResultCallback);
        }

    }

}

