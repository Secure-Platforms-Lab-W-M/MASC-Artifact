/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 */
package android.support.v4.print;

import android.content.Context;
import android.support.v4.print.PrintHelperApi23;

class PrintHelperApi24
extends PrintHelperApi23 {
    PrintHelperApi24(Context context) {
        super(context);
        this.mIsMinMarginsHandlingCorrect = true;
        this.mPrintActivityRespectsOrientation = true;
    }
}

