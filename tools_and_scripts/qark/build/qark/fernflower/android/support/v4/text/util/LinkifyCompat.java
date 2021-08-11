package android.support.v4.text.util;

import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.v4.util.PatternsCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.text.util.Linkify.MatchFilter;
import android.text.util.Linkify.TransformFilter;
import android.widget.TextView;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class LinkifyCompat {
   private static final Comparator COMPARATOR = new Comparator() {
      public final int compare(LinkifyCompat.LinkSpec var1, LinkifyCompat.LinkSpec var2) {
         if (var1.start < var2.start) {
            return -1;
         } else if (var1.start > var2.start) {
            return 1;
         } else if (var1.end < var2.end) {
            return 1;
         } else {
            return var1.end > var2.end ? -1 : 0;
         }
      }
   };
   private static final String[] EMPTY_STRING = new String[0];

   private LinkifyCompat() {
   }

   private static void addLinkMovementMethod(@NonNull TextView var0) {
      MovementMethod var1 = var0.getMovementMethod();
      if ((var1 == null || !(var1 instanceof LinkMovementMethod)) && var0.getLinksClickable()) {
         var0.setMovementMethod(LinkMovementMethod.getInstance());
      }

   }

   public static final void addLinks(@NonNull TextView var0, @NonNull Pattern var1, @Nullable String var2) {
      if (VERSION.SDK_INT >= 26) {
         Linkify.addLinks(var0, var1, var2);
      } else {
         addLinks((TextView)var0, var1, var2, (String[])null, (MatchFilter)null, (TransformFilter)null);
      }
   }

   public static final void addLinks(@NonNull TextView var0, @NonNull Pattern var1, @Nullable String var2, @Nullable MatchFilter var3, @Nullable TransformFilter var4) {
      if (VERSION.SDK_INT >= 26) {
         Linkify.addLinks(var0, var1, var2, var3, var4);
      } else {
         addLinks((TextView)var0, var1, var2, (String[])null, var3, var4);
      }
   }

   public static final void addLinks(@NonNull TextView var0, @NonNull Pattern var1, @Nullable String var2, @Nullable String[] var3, @Nullable MatchFilter var4, @Nullable TransformFilter var5) {
      if (VERSION.SDK_INT >= 26) {
         Linkify.addLinks(var0, var1, var2, var3, var4, var5);
      } else {
         SpannableString var6 = SpannableString.valueOf(var0.getText());
         if (addLinks((Spannable)var6, var1, var2, var3, var4, var5)) {
            var0.setText(var6);
            addLinkMovementMethod(var0);
         }

      }
   }

   public static final boolean addLinks(@NonNull Spannable var0, int var1) {
      if (VERSION.SDK_INT >= 26) {
         return Linkify.addLinks(var0, var1);
      } else if (var1 == 0) {
         return false;
      } else {
         URLSpan[] var3 = (URLSpan[])var0.getSpans(0, var0.length(), URLSpan.class);

         for(int var2 = var3.length - 1; var2 >= 0; --var2) {
            var0.removeSpan(var3[var2]);
         }

         if ((var1 & 4) != 0) {
            Linkify.addLinks(var0, 4);
         }

         ArrayList var6 = new ArrayList();
         if ((var1 & 1) != 0) {
            Pattern var4 = PatternsCompat.AUTOLINK_WEB_URL;
            MatchFilter var5 = Linkify.sUrlMatchFilter;
            gatherLinks(var6, var0, var4, new String[]{"http://", "https://", "rtsp://"}, var5, (TransformFilter)null);
         }

         if ((var1 & 2) != 0) {
            gatherLinks(var6, var0, PatternsCompat.AUTOLINK_EMAIL_ADDRESS, new String[]{"mailto:"}, (MatchFilter)null, (TransformFilter)null);
         }

         if ((var1 & 8) != 0) {
            gatherMapLinks(var6, var0);
         }

         pruneOverlaps(var6, var0);
         if (var6.size() == 0) {
            return false;
         } else {
            Iterator var7 = var6.iterator();

            while(var7.hasNext()) {
               LinkifyCompat.LinkSpec var8 = (LinkifyCompat.LinkSpec)var7.next();
               if (var8.frameworkAddedSpan == null) {
                  applyLink(var8.url, var8.start, var8.end, var0);
               }
            }

            return true;
         }
      }
   }

   public static final boolean addLinks(@NonNull Spannable var0, @NonNull Pattern var1, @Nullable String var2) {
      return VERSION.SDK_INT >= 26 ? Linkify.addLinks(var0, var1, var2) : addLinks((Spannable)var0, var1, var2, (String[])null, (MatchFilter)null, (TransformFilter)null);
   }

   public static final boolean addLinks(@NonNull Spannable var0, @NonNull Pattern var1, @Nullable String var2, @Nullable MatchFilter var3, @Nullable TransformFilter var4) {
      return VERSION.SDK_INT >= 26 ? Linkify.addLinks(var0, var1, var2, var3, var4) : addLinks((Spannable)var0, var1, var2, (String[])null, var3, var4);
   }

   public static final boolean addLinks(@NonNull Spannable var0, @NonNull Pattern var1, @Nullable String var2, @Nullable String[] var3, @Nullable MatchFilter var4, @Nullable TransformFilter var5) {
      if (VERSION.SDK_INT >= 26) {
         return Linkify.addLinks(var0, var1, var2, var3, var4, var5);
      } else {
         String var10 = var2;
         if (var2 == null) {
            var10 = "";
         }

         String[] var13;
         label42: {
            if (var3 != null) {
               var13 = var3;
               if (var3.length >= 1) {
                  break label42;
               }
            }

            var13 = EMPTY_STRING;
         }

         String[] var11 = new String[var13.length + 1];
         var11[0] = var10.toLowerCase(Locale.ROOT);

         int var6;
         for(var6 = 0; var6 < var13.length; ++var6) {
            String var14 = var13[var6];
            if (var14 == null) {
               var14 = "";
            } else {
               var14 = var14.toLowerCase(Locale.ROOT);
            }

            var11[var6 + 1] = var14;
         }

         boolean var8 = false;
         Matcher var12 = var1.matcher(var0);

         while(var12.find()) {
            var6 = var12.start();
            int var7 = var12.end();
            boolean var9 = true;
            if (var4 != null) {
               var9 = var4.acceptMatch(var0, var6, var7);
            }

            if (var9) {
               applyLink(makeUrl(var12.group(0), var11, var12, var5), var6, var7, var0);
               var8 = true;
            }
         }

         return var8;
      }
   }

   public static final boolean addLinks(@NonNull TextView var0, int var1) {
      if (VERSION.SDK_INT >= 26) {
         return Linkify.addLinks(var0, var1);
      } else if (var1 == 0) {
         return false;
      } else {
         CharSequence var2 = var0.getText();
         if (var2 instanceof Spannable) {
            if (addLinks((Spannable)var2, var1)) {
               addLinkMovementMethod(var0);
               return true;
            } else {
               return false;
            }
         } else {
            SpannableString var3 = SpannableString.valueOf(var2);
            if (addLinks((Spannable)var3, var1)) {
               addLinkMovementMethod(var0);
               var0.setText(var3);
               return true;
            } else {
               return false;
            }
         }
      }
   }

   private static void applyLink(String var0, int var1, int var2, Spannable var3) {
      var3.setSpan(new URLSpan(var0), var1, var2, 33);
   }

   private static void gatherLinks(ArrayList var0, Spannable var1, Pattern var2, String[] var3, MatchFilter var4, TransformFilter var5) {
      Matcher var9 = var2.matcher(var1);

      while(true) {
         int var6;
         int var7;
         do {
            if (!var9.find()) {
               return;
            }

            var6 = var9.start();
            var7 = var9.end();
         } while(var4 != null && !var4.acceptMatch(var1, var6, var7));

         LinkifyCompat.LinkSpec var8 = new LinkifyCompat.LinkSpec();
         var8.url = makeUrl(var9.group(0), var3, var9, var5);
         var8.start = var6;
         var8.end = var7;
         var0.add(var8);
      }
   }

   private static final void gatherMapLinks(ArrayList param0, Spannable param1) {
      // $FF: Couldn't be decompiled
   }

   private static String makeUrl(@NonNull String var0, @NonNull String[] var1, Matcher var2, @Nullable TransformFilter var3) {
      String var7 = var0;
      if (var3 != null) {
         var7 = var3.transformUrl(var2, var0);
      }

      boolean var6 = false;
      int var4 = 0;

      boolean var5;
      while(true) {
         var5 = var6;
         var0 = var7;
         if (var4 >= var1.length) {
            break;
         }

         if (var7.regionMatches(true, 0, var1[var4], 0, var1[var4].length())) {
            var6 = true;
            var5 = var6;
            var0 = var7;
            if (!var7.regionMatches(false, 0, var1[var4], 0, var1[var4].length())) {
               StringBuilder var8 = new StringBuilder();
               var8.append(var1[var4]);
               var8.append(var7.substring(var1[var4].length()));
               var0 = var8.toString();
               var5 = var6;
            }
            break;
         }

         ++var4;
      }

      String var9 = var0;
      if (!var5) {
         var9 = var0;
         if (var1.length > 0) {
            StringBuilder var10 = new StringBuilder();
            var10.append(var1[0]);
            var10.append(var0);
            var9 = var10.toString();
         }
      }

      return var9;
   }

   private static final void pruneOverlaps(ArrayList var0, Spannable var1) {
      URLSpan[] var5 = (URLSpan[])var1.getSpans(0, var1.length(), URLSpan.class);

      int var2;
      LinkifyCompat.LinkSpec var6;
      for(var2 = 0; var2 < var5.length; ++var2) {
         var6 = new LinkifyCompat.LinkSpec();
         var6.frameworkAddedSpan = var5[var2];
         var6.start = var1.getSpanStart(var5[var2]);
         var6.end = var1.getSpanEnd(var5[var2]);
         var0.add(var6);
      }

      Collections.sort(var0, COMPARATOR);
      int var4 = var0.size();
      int var3 = 0;

      while(true) {
         while(var3 < var4 - 1) {
            LinkifyCompat.LinkSpec var7 = (LinkifyCompat.LinkSpec)var0.get(var3);
            var6 = (LinkifyCompat.LinkSpec)var0.get(var3 + 1);
            var2 = -1;
            if (var7.start <= var6.start && var7.end > var6.start) {
               if (var6.end <= var7.end) {
                  var2 = var3 + 1;
               } else if (var7.end - var7.start > var6.end - var6.start) {
                  var2 = var3 + 1;
               } else if (var7.end - var7.start < var6.end - var6.start) {
                  var2 = var3;
               }

               if (var2 != -1) {
                  URLSpan var8 = ((LinkifyCompat.LinkSpec)var0.get(var2)).frameworkAddedSpan;
                  if (var8 != null) {
                     var1.removeSpan(var8);
                  }

                  var0.remove(var2);
                  --var4;
                  continue;
               }
            }

            ++var3;
         }

         return;
      }
   }

   private static class LinkSpec {
      int end;
      URLSpan frameworkAddedSpan;
      int start;
      String url;

      LinkSpec() {
      }
   }

   @Retention(RetentionPolicy.SOURCE)
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public @interface LinkifyMask {
   }
}
