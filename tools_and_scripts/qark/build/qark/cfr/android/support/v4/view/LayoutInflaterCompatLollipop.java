/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.view.LayoutInflater
 *  android.view.LayoutInflater$Factory2
 */
package android.support.v4.view;

import android.support.v4.view.LayoutInflaterCompatHC;
import android.support.v4.view.LayoutInflaterFactory;
import android.view.LayoutInflater;

class LayoutInflaterCompatLollipop {
    LayoutInflaterCompatLollipop() {
    }

    static void setFactory(LayoutInflater layoutInflater, LayoutInflaterFactory object) {
        object = object != null ? new LayoutInflaterCompatHC.FactoryWrapperHC((LayoutInflaterFactory)object) : null;
        layoutInflater.setFactory2((LayoutInflater.Factory2)object);
    }
}

