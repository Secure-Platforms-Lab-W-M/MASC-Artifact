/*
 * Decompiled with CFR 0_124.
 */
package android.support.v7.app;

class TwilightCalculator {
    private static final float ALTIDUTE_CORRECTION_CIVIL_TWILIGHT = -0.10471976f;
    private static final float C1 = 0.0334196f;
    private static final float C2 = 3.49066E-4f;
    private static final float C3 = 5.236E-6f;
    public static final int DAY = 0;
    private static final float DEGREES_TO_RADIANS = 0.017453292f;
    private static final float J0 = 9.0E-4f;
    public static final int NIGHT = 1;
    private static final float OBLIQUITY = 0.4092797f;
    private static final long UTC_2000 = 946728000000L;
    private static TwilightCalculator sInstance;
    public int state;
    public long sunrise;
    public long sunset;

    TwilightCalculator() {
    }

    static TwilightCalculator getInstance() {
        if (sInstance == null) {
            sInstance = new TwilightCalculator();
        }
        return sInstance;
    }

    public void calculateTwilight(long l, double d, double d2) {
        float f = (float)(l - 946728000000L) / 8.64E7f;
        float f2 = 0.01720197f * f + 6.24006f;
        double d3 = f2;
        double d4 = Math.sin(f2);
        Double.isNaN(d3);
        d3 = 1.796593063 + (d3 + d4 * 0.03341960161924362 + Math.sin(2.0f * f2) * 3.4906598739326E-4 + Math.sin(3.0f * f2) * 5.236000106378924E-6) + 3.141592653589793;
        d2 = (- d2) / 360.0;
        d4 = f - 9.0E-4f;
        Double.isNaN(d4);
        d4 = 9.0E-4f + (float)Math.round(d4 - d2);
        Double.isNaN(d4);
        d2 = d4 + d2 + Math.sin(f2) * 0.0053 + Math.sin(2.0 * d3) * -0.0069;
        d3 = Math.asin(Math.sin(d3) * Math.sin(0.4092797040939331));
        d = 0.01745329238474369 * d;
        d = (Math.sin(-0.10471975803375244) - Math.sin(d) * Math.sin(d3)) / (Math.cos(d) * Math.cos(d3));
        if (d >= 1.0) {
            this.state = 1;
            this.sunset = -1L;
            this.sunrise = -1L;
            return;
        }
        if (d <= -1.0) {
            this.state = 0;
            this.sunset = -1L;
            this.sunrise = -1L;
            return;
        }
        f = (float)(Math.acos(d) / 6.283185307179586);
        d = f;
        Double.isNaN(d);
        this.sunset = Math.round((d + d2) * 8.64E7) + 946728000000L;
        d = f;
        Double.isNaN(d);
        this.sunrise = Math.round((d2 - d) * 8.64E7) + 946728000000L;
        if (this.sunrise < l && this.sunset > l) {
            this.state = 0;
            return;
        }
        this.state = 1;
    }
}

