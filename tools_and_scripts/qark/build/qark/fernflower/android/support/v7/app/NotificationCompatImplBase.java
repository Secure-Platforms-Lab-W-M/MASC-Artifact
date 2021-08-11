package android.support.v7.app;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.os.Build.VERSION;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationBuilderWithBuilderAccessor;
import android.support.v4.app.NotificationCompatBase;
import android.support.v7.appcompat.R$color;
import android.support.v7.appcompat.R$dimen;
import android.support.v7.appcompat.R$drawable;
import android.support.v7.appcompat.R$id;
import android.support.v7.appcompat.R$integer;
import android.support.v7.appcompat.R$layout;
import android.support.v7.appcompat.R$string;
import android.widget.RemoteViews;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

@TargetApi(9)
@RequiresApi(9)
class NotificationCompatImplBase {
   private static final int MAX_ACTION_BUTTONS = 3;
   static final int MAX_MEDIA_BUTTONS = 5;
   static final int MAX_MEDIA_BUTTONS_IN_COMPACT = 3;

   public static RemoteViews applyStandardTemplate(Context var0, CharSequence var1, CharSequence var2, CharSequence var3, int var4, int var5, Bitmap var6, CharSequence var7, boolean var8, long var9, int var11, int var12, int var13, boolean var14) {
      Resources var18 = var0.getResources();
      RemoteViews var19 = new RemoteViews(var0.getPackageName(), var13);
      boolean var16 = false;
      boolean var26 = false;
      boolean var25;
      if (var11 < -1) {
         var25 = true;
      } else {
         var25 = false;
      }

      if (VERSION.SDK_INT >= 16 && VERSION.SDK_INT < 21) {
         if (var25) {
            var19.setInt(R$id.notification_background, "setBackgroundResource", R$drawable.notification_bg_low);
            var19.setInt(R$id.icon, "setBackgroundResource", R$drawable.notification_template_icon_low_bg);
         } else {
            var19.setInt(R$id.notification_background, "setBackgroundResource", R$drawable.notification_bg);
            var19.setInt(R$id.icon, "setBackgroundResource", R$drawable.notification_template_icon_bg);
         }
      }

      Bitmap var20;
      if (var6 != null) {
         if (VERSION.SDK_INT >= 16) {
            var19.setViewVisibility(R$id.icon, 0);
            var19.setImageViewBitmap(R$id.icon, var6);
         } else {
            var19.setViewVisibility(R$id.icon, 8);
         }

         if (var5 != 0) {
            var11 = var18.getDimensionPixelSize(R$dimen.notification_right_icon_size);
            int var17 = var18.getDimensionPixelSize(R$dimen.notification_small_icon_background_padding);
            if (VERSION.SDK_INT >= 21) {
               var20 = createIconWithBackground(var0, var5, var11, var11 - var17 * 2, var12);
               var19.setImageViewBitmap(R$id.right_icon, var20);
            } else {
               var19.setImageViewBitmap(R$id.right_icon, createColoredBitmap(var0, var5, -1));
            }

            var19.setViewVisibility(R$id.right_icon, 0);
         }
      } else if (var5 != 0) {
         var19.setViewVisibility(R$id.icon, 0);
         if (VERSION.SDK_INT >= 21) {
            var20 = createIconWithBackground(var0, var5, var18.getDimensionPixelSize(R$dimen.notification_large_icon_width) - var18.getDimensionPixelSize(R$dimen.notification_big_circle_margin), var18.getDimensionPixelSize(R$dimen.notification_small_icon_size_as_large), var12);
            var19.setImageViewBitmap(R$id.icon, var20);
         } else {
            var19.setImageViewBitmap(R$id.icon, createColoredBitmap(var0, var5, -1));
         }
      }

      if (var1 != null) {
         var19.setTextViewText(R$id.title, var1);
      }

      boolean var23 = var16;
      if (var2 != null) {
         var19.setTextViewText(R$id.text, var2);
         var23 = true;
      }

      if (VERSION.SDK_INT < 21 && var6 != null) {
         var25 = true;
      } else {
         var25 = false;
      }

      boolean var22;
      if (var3 != null) {
         var19.setTextViewText(R$id.info, var3);
         var19.setViewVisibility(R$id.info, 0);
         var23 = true;
         var22 = true;
      } else if (var4 > 0) {
         if (var4 > var18.getInteger(R$integer.status_bar_notification_info_maxnum)) {
            var19.setTextViewText(R$id.info, var18.getString(R$string.status_bar_notification_info_overflow));
         } else {
            NumberFormat var21 = NumberFormat.getIntegerInstance();
            var19.setTextViewText(R$id.info, var21.format((long)var4));
         }

         var19.setViewVisibility(R$id.info, 0);
         var23 = true;
         var22 = true;
      } else {
         var19.setViewVisibility(R$id.info, 8);
         var22 = var25;
      }

      var25 = var26;
      if (var7 != null) {
         var25 = var26;
         if (VERSION.SDK_INT >= 16) {
            var19.setTextViewText(R$id.text, var7);
            if (var2 != null) {
               var19.setTextViewText(R$id.text2, var2);
               var19.setViewVisibility(R$id.text2, 0);
               var25 = true;
            } else {
               var19.setViewVisibility(R$id.text2, 8);
               var25 = var26;
            }
         }
      }

      if (var25 && VERSION.SDK_INT >= 16) {
         if (var14) {
            float var15 = (float)var18.getDimensionPixelSize(R$dimen.notification_subtext_size);
            var19.setTextViewTextSize(R$id.text, 0, var15);
         }

         var19.setViewPadding(R$id.line1, 0, 0, 0, 0);
      }

      if (var9 != 0L) {
         if (var8 && VERSION.SDK_INT >= 16) {
            var19.setViewVisibility(R$id.chronometer, 0);
            var19.setLong(R$id.chronometer, "setBase", SystemClock.elapsedRealtime() - System.currentTimeMillis() + var9);
            var19.setBoolean(R$id.chronometer, "setStarted", true);
         } else {
            var19.setViewVisibility(R$id.time, 0);
            var19.setLong(R$id.time, "setTime", var9);
         }

         var22 = true;
      }

      var11 = R$id.right_side;
      byte var24;
      if (var22) {
         var24 = 0;
      } else {
         var24 = 8;
      }

      var19.setViewVisibility(var11, var24);
      var11 = R$id.line3;
      if (var23) {
         var24 = 0;
      } else {
         var24 = 8;
      }

      var19.setViewVisibility(var11, var24);
      return var19;
   }

   public static RemoteViews applyStandardTemplateWithActions(Context var0, CharSequence var1, CharSequence var2, CharSequence var3, int var4, int var5, Bitmap var6, CharSequence var7, boolean var8, long var9, int var11, int var12, int var13, boolean var14, ArrayList var15) {
      RemoteViews var16 = applyStandardTemplate(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, var11, var12, var13, var14);
      var16.removeAllViews(R$id.actions);
      boolean var18 = false;
      boolean var19 = var18;
      if (var15 != null) {
         var11 = var15.size();
         var19 = var18;
         if (var11 > 0) {
            boolean var21 = true;
            var4 = var11;
            if (var11 > 3) {
               var4 = 3;
            }

            var11 = 0;

            while(true) {
               var19 = var21;
               if (var11 >= var4) {
                  break;
               }

               RemoteViews var17 = generateActionButton(var0, (android.support.v4.app.NotificationCompat.Action)var15.get(var11));
               var16.addView(R$id.actions, var17);
               ++var11;
            }
         }
      }

      byte var20;
      if (var19) {
         var20 = 0;
      } else {
         var20 = 8;
      }

      var16.setViewVisibility(R$id.actions, var20);
      var16.setViewVisibility(R$id.action_divider, var20);
      return var16;
   }

   public static void buildIntoRemoteViews(Context var0, RemoteViews var1, RemoteViews var2) {
      hideNormalContent(var1);
      var1.removeAllViews(R$id.notification_main_column);
      var1.addView(R$id.notification_main_column, var2.clone());
      var1.setViewVisibility(R$id.notification_main_column, 0);
      if (VERSION.SDK_INT >= 21) {
         var1.setViewPadding(R$id.notification_main_column_container, 0, calculateTopPadding(var0), 0, 0);
      }

   }

   public static int calculateTopPadding(Context var0) {
      int var2 = var0.getResources().getDimensionPixelSize(R$dimen.notification_top_pad);
      int var3 = var0.getResources().getDimensionPixelSize(R$dimen.notification_top_pad_large_text);
      float var1 = (constrain(var0.getResources().getConfiguration().fontScale, 1.0F, 1.3F) - 1.0F) / 0.29999995F;
      return Math.round((1.0F - var1) * (float)var2 + (float)var3 * var1);
   }

   public static float constrain(float var0, float var1, float var2) {
      if (var0 < var1) {
         return var1;
      } else {
         return var0 > var2 ? var2 : var0;
      }
   }

   private static Bitmap createColoredBitmap(Context var0, int var1, int var2) {
      return createColoredBitmap(var0, var1, var2, 0);
   }

   private static Bitmap createColoredBitmap(Context var0, int var1, int var2, int var3) {
      Drawable var5 = var0.getResources().getDrawable(var1);
      if (var3 == 0) {
         var1 = var5.getIntrinsicWidth();
      } else {
         var1 = var3;
      }

      if (var3 == 0) {
         var3 = var5.getIntrinsicHeight();
      }

      Bitmap var4 = Bitmap.createBitmap(var1, var3, Config.ARGB_8888);
      var5.setBounds(0, 0, var1, var3);
      if (var2 != 0) {
         var5.mutate().setColorFilter(new PorterDuffColorFilter(var2, Mode.SRC_IN));
      }

      var5.draw(new Canvas(var4));
      return var4;
   }

   public static Bitmap createIconWithBackground(Context var0, int var1, int var2, int var3, int var4) {
      int var6 = R$drawable.notification_icon_background;
      int var5 = var4;
      if (var4 == 0) {
         var5 = 0;
      }

      Bitmap var7 = createColoredBitmap(var0, var6, var5, var2);
      Canvas var8 = new Canvas(var7);
      Drawable var9 = var0.getResources().getDrawable(var1).mutate();
      var9.setFilterBitmap(true);
      var1 = (var2 - var3) / 2;
      var9.setBounds(var1, var1, var3 + var1, var3 + var1);
      var9.setColorFilter(new PorterDuffColorFilter(-1, Mode.SRC_ATOP));
      var9.draw(var8);
      return var7;
   }

   private static RemoteViews generateActionButton(Context var0, android.support.v4.app.NotificationCompat.Action var1) {
      boolean var2;
      if (var1.actionIntent == null) {
         var2 = true;
      } else {
         var2 = false;
      }

      String var4 = var0.getPackageName();
      int var3;
      if (var2) {
         var3 = getActionTombstoneLayoutResource();
      } else {
         var3 = getActionLayoutResource();
      }

      RemoteViews var5 = new RemoteViews(var4, var3);
      var5.setImageViewBitmap(R$id.action_image, createColoredBitmap(var0, var1.getIcon(), var0.getResources().getColor(R$color.notification_action_color_filter)));
      var5.setTextViewText(R$id.action_text, var1.title);
      if (!var2) {
         var5.setOnClickPendingIntent(R$id.action_container, var1.actionIntent);
      }

      if (VERSION.SDK_INT >= 15) {
         var5.setContentDescription(R$id.action_container, var1.title);
      }

      return var5;
   }

   @TargetApi(11)
   @RequiresApi(11)
   private static RemoteViews generateContentViewMedia(Context var0, CharSequence var1, CharSequence var2, CharSequence var3, int var4, Bitmap var5, CharSequence var6, boolean var7, long var8, int var10, List var11, int[] var12, boolean var13, PendingIntent var14, boolean var15) {
      int var16;
      if (var15) {
         var16 = R$layout.notification_template_media_custom;
      } else {
         var16 = R$layout.notification_template_media;
      }

      RemoteViews var17 = applyStandardTemplate(var0, var1, var2, var3, var4, 0, var5, var6, var7, var8, var10, 0, var16, true);
      var16 = var11.size();
      if (var12 == null) {
         var4 = 0;
      } else {
         var4 = Math.min(var12.length, 3);
      }

      var17.removeAllViews(R$id.media_actions);
      if (var4 > 0) {
         for(var10 = 0; var10 < var4; ++var10) {
            if (var10 >= var16) {
               throw new IllegalArgumentException(String.format("setShowActionsInCompactView: action %d out of bounds (max %d)", var10, var16 - 1));
            }

            RemoteViews var18 = generateMediaActionButton(var0, (NotificationCompatBase.Action)var11.get(var12[var10]));
            var17.addView(R$id.media_actions, var18);
         }
      }

      if (var13) {
         var17.setViewVisibility(R$id.end_padder, 8);
         var17.setViewVisibility(R$id.cancel_action, 0);
         var17.setOnClickPendingIntent(R$id.cancel_action, var14);
         var17.setInt(R$id.cancel_action, "setAlpha", var0.getResources().getInteger(R$integer.cancel_button_image_alpha));
         return var17;
      } else {
         var17.setViewVisibility(R$id.end_padder, 0);
         var17.setViewVisibility(R$id.cancel_action, 8);
         return var17;
      }
   }

   @TargetApi(11)
   @RequiresApi(11)
   private static RemoteViews generateMediaActionButton(Context var0, NotificationCompatBase.Action var1) {
      boolean var2;
      if (var1.getActionIntent() == null) {
         var2 = true;
      } else {
         var2 = false;
      }

      RemoteViews var3 = new RemoteViews(var0.getPackageName(), R$layout.notification_media_action);
      var3.setImageViewResource(R$id.action0, var1.getIcon());
      if (!var2) {
         var3.setOnClickPendingIntent(R$id.action0, var1.getActionIntent());
      }

      if (VERSION.SDK_INT >= 15) {
         var3.setContentDescription(R$id.action0, var1.getTitle());
      }

      return var3;
   }

   @TargetApi(11)
   @RequiresApi(11)
   public static RemoteViews generateMediaBigView(Context var0, CharSequence var1, CharSequence var2, CharSequence var3, int var4, Bitmap var5, CharSequence var6, boolean var7, long var8, int var10, int var11, List var12, boolean var13, PendingIntent var14, boolean var15) {
      int var16 = Math.min(var12.size(), 5);
      RemoteViews var17 = applyStandardTemplate(var0, var1, var2, var3, var4, 0, var5, var6, var7, var8, var10, var11, getBigMediaLayoutResource(var15, var16), false);
      var17.removeAllViews(R$id.media_actions);
      if (var16 > 0) {
         for(var4 = 0; var4 < var16; ++var4) {
            RemoteViews var18 = generateMediaActionButton(var0, (NotificationCompatBase.Action)var12.get(var4));
            var17.addView(R$id.media_actions, var18);
         }
      }

      if (var13) {
         var17.setViewVisibility(R$id.cancel_action, 0);
         var17.setInt(R$id.cancel_action, "setAlpha", var0.getResources().getInteger(R$integer.cancel_button_image_alpha));
         var17.setOnClickPendingIntent(R$id.cancel_action, var14);
         return var17;
      } else {
         var17.setViewVisibility(R$id.cancel_action, 8);
         return var17;
      }
   }

   private static int getActionLayoutResource() {
      return R$layout.notification_action;
   }

   private static int getActionTombstoneLayoutResource() {
      return R$layout.notification_action_tombstone;
   }

   @TargetApi(11)
   @RequiresApi(11)
   private static int getBigMediaLayoutResource(boolean var0, int var1) {
      if (var1 <= 3) {
         return var0 ? R$layout.notification_template_big_media_narrow_custom : R$layout.notification_template_big_media_narrow;
      } else {
         return var0 ? R$layout.notification_template_big_media_custom : R$layout.notification_template_big_media;
      }
   }

   private static void hideNormalContent(RemoteViews var0) {
      var0.setViewVisibility(R$id.title, 8);
      var0.setViewVisibility(R$id.text2, 8);
      var0.setViewVisibility(R$id.text, 8);
   }

   @TargetApi(11)
   @RequiresApi(11)
   public static RemoteViews overrideContentViewMedia(NotificationBuilderWithBuilderAccessor var0, Context var1, CharSequence var2, CharSequence var3, CharSequence var4, int var5, Bitmap var6, CharSequence var7, boolean var8, long var9, int var11, List var12, int[] var13, boolean var14, PendingIntent var15, boolean var16) {
      RemoteViews var17 = generateContentViewMedia(var1, var2, var3, var4, var5, var6, var7, var8, var9, var11, var12, var13, var14, var15, var16);
      var0.getBuilder().setContent(var17);
      if (var14) {
         var0.getBuilder().setOngoing(true);
      }

      return var17;
   }

   @TargetApi(16)
   @RequiresApi(16)
   public static void overrideMediaBigContentView(Notification var0, Context var1, CharSequence var2, CharSequence var3, CharSequence var4, int var5, Bitmap var6, CharSequence var7, boolean var8, long var9, int var11, int var12, List var13, boolean var14, PendingIntent var15, boolean var16) {
      var0.bigContentView = generateMediaBigView(var1, var2, var3, var4, var5, var6, var7, var8, var9, var11, var12, var13, var14, var15, var16);
      if (var14) {
         var0.flags |= 2;
      }

   }
}
