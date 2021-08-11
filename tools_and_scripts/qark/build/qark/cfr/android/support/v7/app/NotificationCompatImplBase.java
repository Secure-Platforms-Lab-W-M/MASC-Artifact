/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.app.Notification
 *  android.app.Notification$Builder
 *  android.app.PendingIntent
 *  android.content.Context
 *  android.content.res.Configuration
 *  android.content.res.Resources
 *  android.graphics.Bitmap
 *  android.graphics.Bitmap$Config
 *  android.graphics.Canvas
 *  android.graphics.ColorFilter
 *  android.graphics.PorterDuff
 *  android.graphics.PorterDuff$Mode
 *  android.graphics.PorterDuffColorFilter
 *  android.graphics.drawable.Drawable
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.SystemClock
 *  android.widget.RemoteViews
 */
package android.support.v7.app;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.SystemClock;
import android.support.v4.app.NotificationBuilderWithBuilderAccessor;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompatBase;
import android.support.v7.appcompat.R;
import android.widget.RemoteViews;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

class NotificationCompatImplBase {
    private static final int MAX_ACTION_BUTTONS = 3;
    static final int MAX_MEDIA_BUTTONS = 5;
    static final int MAX_MEDIA_BUTTONS_IN_COMPACT = 3;

    NotificationCompatImplBase() {
    }

    /*
     * Enabled aggressive block sorting
     */
    public static RemoteViews applyStandardTemplate(Context object, CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, int n, int n2, Bitmap bitmap, CharSequence charSequence4, boolean bl, long l, int n3, int n4, int n5, boolean bl2) {
        Resources resources = object.getResources();
        RemoteViews remoteViews = new RemoteViews(object.getPackageName(), n5);
        int n6 = 0;
        int n7 = 0;
        n3 = n3 < -1 ? 1 : 0;
        n5 = Build.VERSION.SDK_INT >= 16 ? 1 : 0;
        boolean bl3 = Build.VERSION.SDK_INT >= 21;
        if (n5 != 0 && !bl3) {
            if (n3 != 0) {
                remoteViews.setInt(R.id.notification_background, "setBackgroundResource", R.drawable.notification_bg_low);
                remoteViews.setInt(R.id.icon, "setBackgroundResource", R.drawable.notification_template_icon_low_bg);
            } else {
                remoteViews.setInt(R.id.notification_background, "setBackgroundResource", R.drawable.notification_bg);
                remoteViews.setInt(R.id.icon, "setBackgroundResource", R.drawable.notification_template_icon_bg);
            }
        }
        if (bitmap != null) {
            if (n5 != 0) {
                remoteViews.setViewVisibility(R.id.icon, 0);
                remoteViews.setImageViewBitmap(R.id.icon, bitmap);
            } else {
                remoteViews.setViewVisibility(R.id.icon, 8);
            }
            if (n2 != 0) {
                n3 = resources.getDimensionPixelSize(R.dimen.notification_right_icon_size);
                int n8 = resources.getDimensionPixelSize(R.dimen.notification_small_icon_background_padding);
                if (bl3) {
                    object = NotificationCompatImplBase.createIconWithBackground((Context)object, n2, n3, n3 - n8 * 2, n4);
                    remoteViews.setImageViewBitmap(R.id.right_icon, (Bitmap)object);
                } else {
                    remoteViews.setImageViewBitmap(R.id.right_icon, NotificationCompatImplBase.createColoredBitmap((Context)object, n2, -1));
                }
                remoteViews.setViewVisibility(R.id.right_icon, 0);
            }
        } else if (n2 != 0) {
            remoteViews.setViewVisibility(R.id.icon, 0);
            if (bl3) {
                object = NotificationCompatImplBase.createIconWithBackground((Context)object, n2, resources.getDimensionPixelSize(R.dimen.notification_large_icon_width) - resources.getDimensionPixelSize(R.dimen.notification_big_circle_margin), resources.getDimensionPixelSize(R.dimen.notification_small_icon_size_as_large), n4);
                remoteViews.setImageViewBitmap(R.id.icon, (Bitmap)object);
            } else {
                remoteViews.setImageViewBitmap(R.id.icon, NotificationCompatImplBase.createColoredBitmap((Context)object, n2, -1));
            }
        }
        if (charSequence != null) {
            remoteViews.setTextViewText(R.id.title, charSequence);
        }
        n2 = n6;
        if (charSequence2 != null) {
            remoteViews.setTextViewText(R.id.text, charSequence2);
            n2 = 1;
        }
        n3 = !bl3 && bitmap != null ? 1 : 0;
        if (charSequence3 != null) {
            remoteViews.setTextViewText(R.id.info, charSequence3);
            remoteViews.setViewVisibility(R.id.info, 0);
            n2 = 1;
            n = 1;
        } else if (n > 0) {
            if (n > resources.getInteger(R.integer.status_bar_notification_info_maxnum)) {
                remoteViews.setTextViewText(R.id.info, (CharSequence)resources.getString(R.string.status_bar_notification_info_overflow));
            } else {
                object = NumberFormat.getIntegerInstance();
                remoteViews.setTextViewText(R.id.info, (CharSequence)object.format(n));
            }
            remoteViews.setViewVisibility(R.id.info, 0);
            n2 = 1;
            n = 1;
        } else {
            remoteViews.setViewVisibility(R.id.info, 8);
            n = n3;
        }
        n3 = n7;
        if (charSequence4 != null) {
            n3 = n7;
            if (n5 != 0) {
                remoteViews.setTextViewText(R.id.text, charSequence4);
                if (charSequence2 != null) {
                    remoteViews.setTextViewText(R.id.text2, charSequence2);
                    remoteViews.setViewVisibility(R.id.text2, 0);
                    n3 = 1;
                } else {
                    remoteViews.setViewVisibility(R.id.text2, 8);
                    n3 = n7;
                }
            }
        }
        if (n3 != 0 && n5 != 0) {
            if (bl2) {
                float f = resources.getDimensionPixelSize(R.dimen.notification_subtext_size);
                remoteViews.setTextViewTextSize(R.id.text, 0, f);
            }
            remoteViews.setViewPadding(R.id.line1, 0, 0, 0, 0);
        }
        if (l != 0L) {
            if (bl && n5 != 0) {
                remoteViews.setViewVisibility(R.id.chronometer, 0);
                remoteViews.setLong(R.id.chronometer, "setBase", SystemClock.elapsedRealtime() - System.currentTimeMillis() + l);
                remoteViews.setBoolean(R.id.chronometer, "setStarted", true);
            } else {
                remoteViews.setViewVisibility(R.id.time, 0);
                remoteViews.setLong(R.id.time, "setTime", l);
            }
            n = 1;
        }
        n3 = R.id.right_side;
        n = n != 0 ? 0 : 8;
        remoteViews.setViewVisibility(n3, n);
        n3 = R.id.line3;
        n = n2 != 0 ? 0 : 8;
        remoteViews.setViewVisibility(n3, n);
        return remoteViews;
    }

    /*
     * Enabled aggressive block sorting
     */
    public static RemoteViews applyStandardTemplateWithActions(Context context, CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, int n, int n2, Bitmap bitmap, CharSequence charSequence4, boolean bl, long l, int n3, int n4, int n5, boolean bl2, ArrayList<NotificationCompat.Action> arrayList) {
        charSequence = NotificationCompatImplBase.applyStandardTemplate(context, charSequence, charSequence2, charSequence3, n, n2, bitmap, charSequence4, bl, l, n3, n4, n5, bl2);
        charSequence.removeAllViews(R.id.actions);
        n2 = n = 0;
        if (arrayList != null) {
            n3 = arrayList.size();
            n2 = n;
            if (n3 > 0) {
                n4 = 1;
                n = n3;
                if (n3 > 3) {
                    n = 3;
                }
                n3 = 0;
                do {
                    n2 = n4;
                    if (n3 >= n) break;
                    charSequence2 = NotificationCompatImplBase.generateActionButton(context, arrayList.get(n3));
                    charSequence.addView(R.id.actions, (RemoteViews)charSequence2);
                    ++n3;
                } while (true);
            }
        }
        n = n2 != 0 ? 0 : 8;
        charSequence.setViewVisibility(R.id.actions, n);
        charSequence.setViewVisibility(R.id.action_divider, n);
        return charSequence;
    }

    public static void buildIntoRemoteViews(Context context, RemoteViews remoteViews, RemoteViews remoteViews2) {
        NotificationCompatImplBase.hideNormalContent(remoteViews);
        remoteViews.removeAllViews(R.id.notification_main_column);
        remoteViews.addView(R.id.notification_main_column, remoteViews2.clone());
        remoteViews.setViewVisibility(R.id.notification_main_column, 0);
        if (Build.VERSION.SDK_INT >= 21) {
            remoteViews.setViewPadding(R.id.notification_main_column_container, 0, NotificationCompatImplBase.calculateTopPadding(context), 0, 0);
        }
    }

    public static int calculateTopPadding(Context context) {
        int n = context.getResources().getDimensionPixelSize(R.dimen.notification_top_pad);
        int n2 = context.getResources().getDimensionPixelSize(R.dimen.notification_top_pad_large_text);
        float f = (NotificationCompatImplBase.constrain(context.getResources().getConfiguration().fontScale, 1.0f, 1.3f) - 1.0f) / 0.29999995f;
        return Math.round((1.0f - f) * (float)n + (float)n2 * f);
    }

    public static float constrain(float f, float f2, float f3) {
        if (f < f2) {
            return f2;
        }
        if (f > f3) {
            return f3;
        }
        return f;
    }

    private static Bitmap createColoredBitmap(Context context, int n, int n2) {
        return NotificationCompatImplBase.createColoredBitmap(context, n, n2, 0);
    }

    /*
     * Enabled aggressive block sorting
     */
    private static Bitmap createColoredBitmap(Context context, int n, int n2, int n3) {
        context = context.getResources().getDrawable(n);
        n = n3 == 0 ? context.getIntrinsicWidth() : n3;
        if (n3 == 0) {
            n3 = context.getIntrinsicHeight();
        }
        Bitmap bitmap = Bitmap.createBitmap((int)n, (int)n3, (Bitmap.Config)Bitmap.Config.ARGB_8888);
        context.setBounds(0, 0, n, n3);
        if (n2 != 0) {
            context.mutate().setColorFilter((ColorFilter)new PorterDuffColorFilter(n2, PorterDuff.Mode.SRC_IN));
        }
        context.draw(new Canvas(bitmap));
        return bitmap;
    }

    public static Bitmap createIconWithBackground(Context context, int n, int n2, int n3, int n4) {
        int n5 = R.drawable.notification_icon_background;
        int n6 = n4;
        if (n4 == 0) {
            n6 = 0;
        }
        Bitmap bitmap = NotificationCompatImplBase.createColoredBitmap(context, n5, n6, n2);
        Canvas canvas = new Canvas(bitmap);
        context = context.getResources().getDrawable(n).mutate();
        context.setFilterBitmap(true);
        n = (n2 - n3) / 2;
        context.setBounds(n, n, n3 + n, n3 + n);
        context.setColorFilter((ColorFilter)new PorterDuffColorFilter(-1, PorterDuff.Mode.SRC_ATOP));
        context.draw(canvas);
        return bitmap;
    }

    /*
     * Enabled aggressive block sorting
     */
    private static RemoteViews generateActionButton(Context context, NotificationCompat.Action action) {
        boolean bl = action.actionIntent == null;
        String string2 = context.getPackageName();
        int n = bl ? NotificationCompatImplBase.getActionTombstoneLayoutResource() : NotificationCompatImplBase.getActionLayoutResource();
        string2 = new RemoteViews(string2, n);
        string2.setImageViewBitmap(R.id.action_image, NotificationCompatImplBase.createColoredBitmap(context, action.getIcon(), context.getResources().getColor(R.color.notification_action_color_filter)));
        string2.setTextViewText(R.id.action_text, action.title);
        if (!bl) {
            string2.setOnClickPendingIntent(R.id.action_container, action.actionIntent);
        }
        string2.setContentDescription(R.id.action_container, action.title);
        return string2;
    }

    /*
     * Enabled aggressive block sorting
     */
    private static <T extends NotificationCompatBase.Action> RemoteViews generateContentViewMedia(Context context, CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, int n, Bitmap bitmap, CharSequence charSequence4, boolean bl, long l, int n2, List<T> list, int[] arrn, boolean bl2, PendingIntent pendingIntent, boolean bl3) {
        int n3 = bl3 ? R.layout.notification_template_media_custom : R.layout.notification_template_media;
        charSequence = NotificationCompatImplBase.applyStandardTemplate(context, charSequence, charSequence2, charSequence3, n, 0, bitmap, charSequence4, bl, l, n2, 0, n3, true);
        n3 = list.size();
        n = arrn == null ? 0 : Math.min(arrn.length, 3);
        charSequence.removeAllViews(R.id.media_actions);
        if (n > 0) {
            for (n2 = 0; n2 < n; ++n2) {
                if (n2 >= n3) {
                    throw new IllegalArgumentException(String.format("setShowActionsInCompactView: action %d out of bounds (max %d)", n2, n3 - 1));
                }
                charSequence2 = NotificationCompatImplBase.generateMediaActionButton(context, (NotificationCompatBase.Action)list.get(arrn[n2]));
                charSequence.addView(R.id.media_actions, (RemoteViews)charSequence2);
            }
        }
        if (bl2) {
            charSequence.setViewVisibility(R.id.end_padder, 8);
            charSequence.setViewVisibility(R.id.cancel_action, 0);
            charSequence.setOnClickPendingIntent(R.id.cancel_action, pendingIntent);
            charSequence.setInt(R.id.cancel_action, "setAlpha", context.getResources().getInteger(R.integer.cancel_button_image_alpha));
            return charSequence;
        }
        charSequence.setViewVisibility(R.id.end_padder, 0);
        charSequence.setViewVisibility(R.id.cancel_action, 8);
        return charSequence;
    }

    /*
     * Enabled aggressive block sorting
     */
    private static RemoteViews generateMediaActionButton(Context context, NotificationCompatBase.Action action) {
        boolean bl = action.getActionIntent() == null;
        context = new RemoteViews(context.getPackageName(), R.layout.notification_media_action);
        context.setImageViewResource(R.id.action0, action.getIcon());
        if (!bl) {
            context.setOnClickPendingIntent(R.id.action0, action.getActionIntent());
        }
        if (Build.VERSION.SDK_INT >= 15) {
            context.setContentDescription(R.id.action0, action.getTitle());
        }
        return context;
    }

    public static <T extends NotificationCompatBase.Action> RemoteViews generateMediaBigView(Context context, CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, int n, Bitmap bitmap, CharSequence charSequence4, boolean bl, long l, int n2, int n3, List<T> list, boolean bl2, PendingIntent pendingIntent, boolean bl3) {
        int n4 = Math.min(list.size(), 5);
        charSequence = NotificationCompatImplBase.applyStandardTemplate(context, charSequence, charSequence2, charSequence3, n, 0, bitmap, charSequence4, bl, l, n2, n3, NotificationCompatImplBase.getBigMediaLayoutResource(bl3, n4), false);
        charSequence.removeAllViews(R.id.media_actions);
        if (n4 > 0) {
            for (n = 0; n < n4; ++n) {
                charSequence2 = NotificationCompatImplBase.generateMediaActionButton(context, (NotificationCompatBase.Action)list.get(n));
                charSequence.addView(R.id.media_actions, (RemoteViews)charSequence2);
            }
        }
        if (bl2) {
            charSequence.setViewVisibility(R.id.cancel_action, 0);
            charSequence.setInt(R.id.cancel_action, "setAlpha", context.getResources().getInteger(R.integer.cancel_button_image_alpha));
            charSequence.setOnClickPendingIntent(R.id.cancel_action, pendingIntent);
            return charSequence;
        }
        charSequence.setViewVisibility(R.id.cancel_action, 8);
        return charSequence;
    }

    private static int getActionLayoutResource() {
        return R.layout.notification_action;
    }

    private static int getActionTombstoneLayoutResource() {
        return R.layout.notification_action_tombstone;
    }

    private static int getBigMediaLayoutResource(boolean bl, int n) {
        if (n <= 3) {
            if (bl) {
                return R.layout.notification_template_big_media_narrow_custom;
            }
            return R.layout.notification_template_big_media_narrow;
        }
        if (bl) {
            return R.layout.notification_template_big_media_custom;
        }
        return R.layout.notification_template_big_media;
    }

    private static void hideNormalContent(RemoteViews remoteViews) {
        remoteViews.setViewVisibility(R.id.title, 8);
        remoteViews.setViewVisibility(R.id.text2, 8);
        remoteViews.setViewVisibility(R.id.text, 8);
    }

    public static <T extends NotificationCompatBase.Action> RemoteViews overrideContentViewMedia(NotificationBuilderWithBuilderAccessor notificationBuilderWithBuilderAccessor, Context context, CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, int n, Bitmap bitmap, CharSequence charSequence4, boolean bl, long l, int n2, List<T> list, int[] arrn, boolean bl2, PendingIntent pendingIntent, boolean bl3) {
        context = NotificationCompatImplBase.generateContentViewMedia(context, charSequence, charSequence2, charSequence3, n, bitmap, charSequence4, bl, l, n2, list, arrn, bl2, pendingIntent, bl3);
        notificationBuilderWithBuilderAccessor.getBuilder().setContent((RemoteViews)context);
        if (bl2) {
            notificationBuilderWithBuilderAccessor.getBuilder().setOngoing(true);
        }
        return context;
    }

    public static <T extends NotificationCompatBase.Action> void overrideMediaBigContentView(Notification notification, Context context, CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, int n, Bitmap bitmap, CharSequence charSequence4, boolean bl, long l, int n2, int n3, List<T> list, boolean bl2, PendingIntent pendingIntent, boolean bl3) {
        notification.bigContentView = NotificationCompatImplBase.generateMediaBigView(context, charSequence, charSequence2, charSequence3, n, bitmap, charSequence4, bl, l, n2, n3, list, bl2, pendingIntent, bl3);
        if (bl2) {
            notification.flags |= 2;
        }
    }
}

