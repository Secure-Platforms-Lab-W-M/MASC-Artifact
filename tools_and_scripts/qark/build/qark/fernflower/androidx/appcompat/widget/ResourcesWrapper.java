package androidx.appcompat.widget;

import android.content.res.AssetFileDescriptor;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.content.res.Resources.NotFoundException;
import android.content.res.Resources.Theme;
import android.graphics.Movie;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import java.io.IOException;
import java.io.InputStream;
import org.xmlpull.v1.XmlPullParserException;

class ResourcesWrapper extends Resources {
   private final Resources mResources;

   public ResourcesWrapper(Resources var1) {
      super(var1.getAssets(), var1.getDisplayMetrics(), var1.getConfiguration());
      this.mResources = var1;
   }

   public XmlResourceParser getAnimation(int var1) throws NotFoundException {
      return this.mResources.getAnimation(var1);
   }

   public boolean getBoolean(int var1) throws NotFoundException {
      return this.mResources.getBoolean(var1);
   }

   public int getColor(int var1) throws NotFoundException {
      return this.mResources.getColor(var1);
   }

   public ColorStateList getColorStateList(int var1) throws NotFoundException {
      return this.mResources.getColorStateList(var1);
   }

   public Configuration getConfiguration() {
      return this.mResources.getConfiguration();
   }

   public float getDimension(int var1) throws NotFoundException {
      return this.mResources.getDimension(var1);
   }

   public int getDimensionPixelOffset(int var1) throws NotFoundException {
      return this.mResources.getDimensionPixelOffset(var1);
   }

   public int getDimensionPixelSize(int var1) throws NotFoundException {
      return this.mResources.getDimensionPixelSize(var1);
   }

   public DisplayMetrics getDisplayMetrics() {
      return this.mResources.getDisplayMetrics();
   }

   public Drawable getDrawable(int var1) throws NotFoundException {
      return this.mResources.getDrawable(var1);
   }

   public Drawable getDrawable(int var1, Theme var2) throws NotFoundException {
      return this.mResources.getDrawable(var1, var2);
   }

   public Drawable getDrawableForDensity(int var1, int var2) throws NotFoundException {
      return this.mResources.getDrawableForDensity(var1, var2);
   }

   public Drawable getDrawableForDensity(int var1, int var2, Theme var3) {
      return this.mResources.getDrawableForDensity(var1, var2, var3);
   }

   public float getFraction(int var1, int var2, int var3) {
      return this.mResources.getFraction(var1, var2, var3);
   }

   public int getIdentifier(String var1, String var2, String var3) {
      return this.mResources.getIdentifier(var1, var2, var3);
   }

   public int[] getIntArray(int var1) throws NotFoundException {
      return this.mResources.getIntArray(var1);
   }

   public int getInteger(int var1) throws NotFoundException {
      return this.mResources.getInteger(var1);
   }

   public XmlResourceParser getLayout(int var1) throws NotFoundException {
      return this.mResources.getLayout(var1);
   }

   public Movie getMovie(int var1) throws NotFoundException {
      return this.mResources.getMovie(var1);
   }

   public String getQuantityString(int var1, int var2) throws NotFoundException {
      return this.mResources.getQuantityString(var1, var2);
   }

   public String getQuantityString(int var1, int var2, Object... var3) throws NotFoundException {
      return this.mResources.getQuantityString(var1, var2, var3);
   }

   public CharSequence getQuantityText(int var1, int var2) throws NotFoundException {
      return this.mResources.getQuantityText(var1, var2);
   }

   public String getResourceEntryName(int var1) throws NotFoundException {
      return this.mResources.getResourceEntryName(var1);
   }

   public String getResourceName(int var1) throws NotFoundException {
      return this.mResources.getResourceName(var1);
   }

   public String getResourcePackageName(int var1) throws NotFoundException {
      return this.mResources.getResourcePackageName(var1);
   }

   public String getResourceTypeName(int var1) throws NotFoundException {
      return this.mResources.getResourceTypeName(var1);
   }

   public String getString(int var1) throws NotFoundException {
      return this.mResources.getString(var1);
   }

   public String getString(int var1, Object... var2) throws NotFoundException {
      return this.mResources.getString(var1, var2);
   }

   public String[] getStringArray(int var1) throws NotFoundException {
      return this.mResources.getStringArray(var1);
   }

   public CharSequence getText(int var1) throws NotFoundException {
      return this.mResources.getText(var1);
   }

   public CharSequence getText(int var1, CharSequence var2) {
      return this.mResources.getText(var1, var2);
   }

   public CharSequence[] getTextArray(int var1) throws NotFoundException {
      return this.mResources.getTextArray(var1);
   }

   public void getValue(int var1, TypedValue var2, boolean var3) throws NotFoundException {
      this.mResources.getValue(var1, var2, var3);
   }

   public void getValue(String var1, TypedValue var2, boolean var3) throws NotFoundException {
      this.mResources.getValue(var1, var2, var3);
   }

   public void getValueForDensity(int var1, int var2, TypedValue var3, boolean var4) throws NotFoundException {
      this.mResources.getValueForDensity(var1, var2, var3, var4);
   }

   public XmlResourceParser getXml(int var1) throws NotFoundException {
      return this.mResources.getXml(var1);
   }

   public TypedArray obtainAttributes(AttributeSet var1, int[] var2) {
      return this.mResources.obtainAttributes(var1, var2);
   }

   public TypedArray obtainTypedArray(int var1) throws NotFoundException {
      return this.mResources.obtainTypedArray(var1);
   }

   public InputStream openRawResource(int var1) throws NotFoundException {
      return this.mResources.openRawResource(var1);
   }

   public InputStream openRawResource(int var1, TypedValue var2) throws NotFoundException {
      return this.mResources.openRawResource(var1, var2);
   }

   public AssetFileDescriptor openRawResourceFd(int var1) throws NotFoundException {
      return this.mResources.openRawResourceFd(var1);
   }

   public void parseBundleExtra(String var1, AttributeSet var2, Bundle var3) throws XmlPullParserException {
      this.mResources.parseBundleExtra(var1, var2, var3);
   }

   public void parseBundleExtras(XmlResourceParser var1, Bundle var2) throws XmlPullParserException, IOException {
      this.mResources.parseBundleExtras(var1, var2);
   }

   public void updateConfiguration(Configuration var1, DisplayMetrics var2) {
      super.updateConfiguration(var1, var2);
      Resources var3 = this.mResources;
      if (var3 != null) {
         var3.updateConfiguration(var1, var2);
      }

   }
}
