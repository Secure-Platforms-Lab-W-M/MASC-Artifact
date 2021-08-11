package com.sun.media.controls;

import javax.media.Codec;
import javax.media.control.H263Control;
import org.atalk.android.util.java.awt.Component;

public class H263Adapter implements H263Control {
   private boolean advancedPrediction;
   private boolean arithmeticCoding;
   private int bppMaxKb;
   private boolean errorCompensation;
   private int hrd_B;
   private boolean pbFrames;
   private boolean unrestrictedVector;

   public H263Adapter(Codec var1, boolean var2, boolean var3, boolean var4, boolean var5, boolean var6, int var7, int var8, boolean var9) {
      this.advancedPrediction = var2;
      this.arithmeticCoding = var3;
      this.errorCompensation = var4;
      this.pbFrames = var5;
      this.unrestrictedVector = var6;
      this.hrd_B = var7;
      this.bppMaxKb = var8;
   }

   public boolean getAdvancedPrediction() {
      return this.advancedPrediction;
   }

   public boolean getArithmeticCoding() {
      return this.arithmeticCoding;
   }

   public int getBppMaxKb() {
      return this.bppMaxKb;
   }

   public Component getControlComponent() {
      throw new UnsupportedOperationException();
   }

   public boolean getErrorCompensation() {
      return this.errorCompensation;
   }

   public int getHRD_B() {
      return this.hrd_B;
   }

   public boolean getPBFrames() {
      return this.pbFrames;
   }

   public boolean getUnrestrictedVector() {
      return this.unrestrictedVector;
   }

   public boolean isAdvancedPredictionSupported() {
      throw new UnsupportedOperationException();
   }

   public boolean isArithmeticCodingSupported() {
      throw new UnsupportedOperationException();
   }

   public boolean isErrorCompensationSupported() {
      throw new UnsupportedOperationException();
   }

   public boolean isPBFramesSupported() {
      throw new UnsupportedOperationException();
   }

   public boolean isUnrestrictedVectorSupported() {
      throw new UnsupportedOperationException();
   }

   public boolean setAdvancedPrediction(boolean var1) {
      throw new UnsupportedOperationException();
   }

   public boolean setArithmeticCoding(boolean var1) {
      throw new UnsupportedOperationException();
   }

   public boolean setErrorCompensation(boolean var1) {
      throw new UnsupportedOperationException();
   }

   public boolean setPBFrames(boolean var1) {
      throw new UnsupportedOperationException();
   }

   public boolean setUnrestrictedVector(boolean var1) {
      throw new UnsupportedOperationException();
   }
}
