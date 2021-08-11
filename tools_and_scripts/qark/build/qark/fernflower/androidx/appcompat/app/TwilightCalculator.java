package androidx.appcompat.app;

class TwilightCalculator {
   private static final float ALTIDUTE_CORRECTION_CIVIL_TWILIGHT = -0.10471976F;
   // $FF: renamed from: C1 float
   private static final float field_92 = 0.0334196F;
   // $FF: renamed from: C2 float
   private static final float field_93 = 3.49066E-4F;
   // $FF: renamed from: C3 float
   private static final float field_94 = 5.236E-6F;
   public static final int DAY = 0;
   private static final float DEGREES_TO_RADIANS = 0.017453292F;
   // $FF: renamed from: J0 float
   private static final float field_95 = 9.0E-4F;
   public static final int NIGHT = 1;
   private static final float OBLIQUITY = 0.4092797F;
   private static final long UTC_2000 = 946728000000L;
   private static TwilightCalculator sInstance;
   public int state;
   public long sunrise;
   public long sunset;

   static TwilightCalculator getInstance() {
      if (sInstance == null) {
         sInstance = new TwilightCalculator();
      }

      return sInstance;
   }

   public void calculateTwilight(long var1, double var3, double var5) {
      float var9 = (float)(var1 - 946728000000L) / 8.64E7F;
      float var10 = 0.01720197F * var9 + 6.24006F;
      double var7 = 1.796593063D + (double)var10 + Math.sin((double)var10) * 0.03341960161924362D + Math.sin((double)(2.0F * var10)) * 3.4906598739326E-4D + Math.sin((double)(3.0F * var10)) * 5.236000106378924E-6D + 3.141592653589793D;
      var5 = -var5 / 360.0D;
      var5 = (double)(9.0E-4F + (float)Math.round((double)(var9 - 9.0E-4F) - var5)) + var5 + Math.sin((double)var10) * 0.0053D + Math.sin(2.0D * var7) * -0.0069D;
      var7 = Math.asin(Math.sin(var7) * Math.sin(0.4092797040939331D));
      var3 = 0.01745329238474369D * var3;
      var3 = (Math.sin(-0.10471975803375244D) - Math.sin(var3) * Math.sin(var7)) / (Math.cos(var3) * Math.cos(var7));
      if (var3 >= 1.0D) {
         this.state = 1;
         this.sunset = -1L;
         this.sunrise = -1L;
      } else if (var3 <= -1.0D) {
         this.state = 0;
         this.sunset = -1L;
         this.sunrise = -1L;
      } else {
         var9 = (float)(Math.acos(var3) / 6.283185307179586D);
         this.sunset = Math.round(((double)var9 + var5) * 8.64E7D) + 946728000000L;
         long var11 = Math.round((var5 - (double)var9) * 8.64E7D) + 946728000000L;
         this.sunrise = var11;
         if (var11 < var1 && this.sunset > var1) {
            this.state = 0;
         } else {
            this.state = 1;
         }
      }
   }
}
