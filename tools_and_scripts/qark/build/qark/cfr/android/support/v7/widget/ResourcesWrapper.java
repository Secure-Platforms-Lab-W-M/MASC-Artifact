/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.res.AssetFileDescriptor
 *  android.content.res.AssetManager
 *  android.content.res.ColorStateList
 *  android.content.res.Configuration
 *  android.content.res.Resources
 *  android.content.res.Resources$NotFoundException
 *  android.content.res.Resources$Theme
 *  android.content.res.TypedArray
 *  android.content.res.XmlResourceParser
 *  android.graphics.Movie
 *  android.graphics.drawable.Drawable
 *  android.os.Bundle
 *  android.util.AttributeSet
 *  android.util.DisplayMetrics
 *  android.util.TypedValue
 *  org.xmlpull.v1.XmlPullParserException
 */
package android.support.v7.widget;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.Movie;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import java.io.IOException;
import java.io.InputStream;
import org.xmlpull.v1.XmlPullParserException;

class ResourcesWrapper
extends Resources {
    private final Resources mResources;

    public ResourcesWrapper(Resources resources) {
        super(resources.getAssets(), resources.getDisplayMetrics(), resources.getConfiguration());
        this.mResources = resources;
    }

    public XmlResourceParser getAnimation(int n) throws Resources.NotFoundException {
        return this.mResources.getAnimation(n);
    }

    public boolean getBoolean(int n) throws Resources.NotFoundException {
        return this.mResources.getBoolean(n);
    }

    public int getColor(int n) throws Resources.NotFoundException {
        return this.mResources.getColor(n);
    }

    public ColorStateList getColorStateList(int n) throws Resources.NotFoundException {
        return this.mResources.getColorStateList(n);
    }

    public Configuration getConfiguration() {
        return this.mResources.getConfiguration();
    }

    public float getDimension(int n) throws Resources.NotFoundException {
        return this.mResources.getDimension(n);
    }

    public int getDimensionPixelOffset(int n) throws Resources.NotFoundException {
        return this.mResources.getDimensionPixelOffset(n);
    }

    public int getDimensionPixelSize(int n) throws Resources.NotFoundException {
        return this.mResources.getDimensionPixelSize(n);
    }

    public DisplayMetrics getDisplayMetrics() {
        return this.mResources.getDisplayMetrics();
    }

    public Drawable getDrawable(int n) throws Resources.NotFoundException {
        return this.mResources.getDrawable(n);
    }

    @RequiresApi(value=21)
    public Drawable getDrawable(int n, Resources.Theme theme) throws Resources.NotFoundException {
        return this.mResources.getDrawable(n, theme);
    }

    @RequiresApi(value=15)
    public Drawable getDrawableForDensity(int n, int n2) throws Resources.NotFoundException {
        return this.mResources.getDrawableForDensity(n, n2);
    }

    @RequiresApi(value=21)
    public Drawable getDrawableForDensity(int n, int n2, Resources.Theme theme) {
        return this.mResources.getDrawableForDensity(n, n2, theme);
    }

    public float getFraction(int n, int n2, int n3) {
        return this.mResources.getFraction(n, n2, n3);
    }

    public int getIdentifier(String string2, String string3, String string4) {
        return this.mResources.getIdentifier(string2, string3, string4);
    }

    public int[] getIntArray(int n) throws Resources.NotFoundException {
        return this.mResources.getIntArray(n);
    }

    public int getInteger(int n) throws Resources.NotFoundException {
        return this.mResources.getInteger(n);
    }

    public XmlResourceParser getLayout(int n) throws Resources.NotFoundException {
        return this.mResources.getLayout(n);
    }

    public Movie getMovie(int n) throws Resources.NotFoundException {
        return this.mResources.getMovie(n);
    }

    public String getQuantityString(int n, int n2) throws Resources.NotFoundException {
        return this.mResources.getQuantityString(n, n2);
    }

    public /* varargs */ String getQuantityString(int n, int n2, Object ... arrobject) throws Resources.NotFoundException {
        return this.mResources.getQuantityString(n, n2, arrobject);
    }

    public CharSequence getQuantityText(int n, int n2) throws Resources.NotFoundException {
        return this.mResources.getQuantityText(n, n2);
    }

    public String getResourceEntryName(int n) throws Resources.NotFoundException {
        return this.mResources.getResourceEntryName(n);
    }

    public String getResourceName(int n) throws Resources.NotFoundException {
        return this.mResources.getResourceName(n);
    }

    public String getResourcePackageName(int n) throws Resources.NotFoundException {
        return this.mResources.getResourcePackageName(n);
    }

    public String getResourceTypeName(int n) throws Resources.NotFoundException {
        return this.mResources.getResourceTypeName(n);
    }

    public String getString(int n) throws Resources.NotFoundException {
        return this.mResources.getString(n);
    }

    public /* varargs */ String getString(int n, Object ... arrobject) throws Resources.NotFoundException {
        return this.mResources.getString(n, arrobject);
    }

    public String[] getStringArray(int n) throws Resources.NotFoundException {
        return this.mResources.getStringArray(n);
    }

    public CharSequence getText(int n) throws Resources.NotFoundException {
        return this.mResources.getText(n);
    }

    public CharSequence getText(int n, CharSequence charSequence) {
        return this.mResources.getText(n, charSequence);
    }

    public CharSequence[] getTextArray(int n) throws Resources.NotFoundException {
        return this.mResources.getTextArray(n);
    }

    public void getValue(int n, TypedValue typedValue, boolean bl) throws Resources.NotFoundException {
        this.mResources.getValue(n, typedValue, bl);
    }

    public void getValue(String string2, TypedValue typedValue, boolean bl) throws Resources.NotFoundException {
        this.mResources.getValue(string2, typedValue, bl);
    }

    @RequiresApi(value=15)
    public void getValueForDensity(int n, int n2, TypedValue typedValue, boolean bl) throws Resources.NotFoundException {
        this.mResources.getValueForDensity(n, n2, typedValue, bl);
    }

    public XmlResourceParser getXml(int n) throws Resources.NotFoundException {
        return this.mResources.getXml(n);
    }

    public TypedArray obtainAttributes(AttributeSet attributeSet, int[] arrn) {
        return this.mResources.obtainAttributes(attributeSet, arrn);
    }

    public TypedArray obtainTypedArray(int n) throws Resources.NotFoundException {
        return this.mResources.obtainTypedArray(n);
    }

    public InputStream openRawResource(int n) throws Resources.NotFoundException {
        return this.mResources.openRawResource(n);
    }

    public InputStream openRawResource(int n, TypedValue typedValue) throws Resources.NotFoundException {
        return this.mResources.openRawResource(n, typedValue);
    }

    public AssetFileDescriptor openRawResourceFd(int n) throws Resources.NotFoundException {
        return this.mResources.openRawResourceFd(n);
    }

    public void parseBundleExtra(String string2, AttributeSet attributeSet, Bundle bundle) throws XmlPullParserException {
        this.mResources.parseBundleExtra(string2, attributeSet, bundle);
    }

    public void parseBundleExtras(XmlResourceParser xmlResourceParser, Bundle bundle) throws XmlPullParserException, IOException {
        this.mResources.parseBundleExtras(xmlResourceParser, bundle);
    }

    public void updateConfiguration(Configuration configuration, DisplayMetrics displayMetrics) {
        super.updateConfiguration(configuration, displayMetrics);
        Resources resources = this.mResources;
        if (resources != null) {
            resources.updateConfiguration(configuration, displayMetrics);
            return;
        }
    }
}

