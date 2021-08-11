// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.content.pm;

import android.graphics.Bitmap;
import android.support.annotation.DrawableRes;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.content.pm.ShortcutInfo$Builder;
import android.content.pm.ShortcutInfo;
import java.util.Arrays;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Parcelable;
import android.content.Intent;
import android.support.v4.graphics.drawable.IconCompat;
import android.content.Context;
import android.content.ComponentName;

public class ShortcutInfoCompat
{
    private ComponentName mActivity;
    private Context mContext;
    private CharSequence mDisabledMessage;
    private IconCompat mIcon;
    private String mId;
    private Intent[] mIntents;
    private CharSequence mLabel;
    private CharSequence mLongLabel;
    
    private ShortcutInfoCompat() {
    }
    
    Intent addToIntent(final Intent intent) {
        final Intent[] mIntents = this.mIntents;
        intent.putExtra("android.intent.extra.shortcut.INTENT", (Parcelable)mIntents[mIntents.length - 1]).putExtra("android.intent.extra.shortcut.NAME", this.mLabel.toString());
        final IconCompat mIcon = this.mIcon;
        if (mIcon != null) {
            mIcon.addToShortcutIntent(intent);
            return intent;
        }
        return intent;
    }
    
    @Nullable
    public ComponentName getActivity() {
        return this.mActivity;
    }
    
    @Nullable
    public CharSequence getDisabledMessage() {
        return this.mDisabledMessage;
    }
    
    @NonNull
    public String getId() {
        return this.mId;
    }
    
    @NonNull
    public Intent getIntent() {
        final Intent[] mIntents = this.mIntents;
        return mIntents[mIntents.length - 1];
    }
    
    @NonNull
    public Intent[] getIntents() {
        final Intent[] mIntents = this.mIntents;
        return Arrays.copyOf(mIntents, mIntents.length);
    }
    
    @Nullable
    public CharSequence getLongLabel() {
        return this.mLongLabel;
    }
    
    @NonNull
    public CharSequence getShortLabel() {
        return this.mLabel;
    }
    
    @RequiresApi(26)
    ShortcutInfo toShortcutInfo() {
        final ShortcutInfo$Builder setIntents = new ShortcutInfo$Builder(this.mContext, this.mId).setShortLabel(this.mLabel).setIntents(this.mIntents);
        final IconCompat mIcon = this.mIcon;
        if (mIcon != null) {
            setIntents.setIcon(mIcon.toIcon());
        }
        if (!TextUtils.isEmpty(this.mLongLabel)) {
            setIntents.setLongLabel(this.mLongLabel);
        }
        if (!TextUtils.isEmpty(this.mDisabledMessage)) {
            setIntents.setDisabledMessage(this.mDisabledMessage);
        }
        final ComponentName mActivity = this.mActivity;
        if (mActivity != null) {
            setIntents.setActivity(mActivity);
        }
        return setIntents.build();
    }
    
    public static class Builder
    {
        private final ShortcutInfoCompat mInfo;
        
        public Builder(@NonNull final Context context, @NonNull final String s) {
            (this.mInfo = new ShortcutInfoCompat(null)).mContext = context;
            this.mInfo.mId = s;
        }
        
        @NonNull
        public ShortcutInfoCompat build() {
            if (TextUtils.isEmpty(this.mInfo.mLabel)) {
                throw new IllegalArgumentException("Shortcut much have a non-empty label");
            }
            if (this.mInfo.mIntents != null && this.mInfo.mIntents.length != 0) {
                return this.mInfo;
            }
            throw new IllegalArgumentException("Shortcut much have an intent");
        }
        
        @NonNull
        public Builder setActivity(@NonNull final ComponentName componentName) {
            this.mInfo.mActivity = componentName;
            return this;
        }
        
        @NonNull
        public Builder setDisabledMessage(@NonNull final CharSequence charSequence) {
            this.mInfo.mDisabledMessage = charSequence;
            return this;
        }
        
        @NonNull
        public Builder setIcon(@DrawableRes final int n) {
            return this.setIcon(IconCompat.createWithResource(this.mInfo.mContext, n));
        }
        
        @NonNull
        public Builder setIcon(@NonNull final Bitmap bitmap) {
            return this.setIcon(IconCompat.createWithBitmap(bitmap));
        }
        
        @NonNull
        public Builder setIcon(final IconCompat iconCompat) {
            this.mInfo.mIcon = iconCompat;
            return this;
        }
        
        @NonNull
        public Builder setIntent(@NonNull final Intent intent) {
            return this.setIntents(new Intent[] { intent });
        }
        
        @NonNull
        public Builder setIntents(@NonNull final Intent[] array) {
            this.mInfo.mIntents = array;
            return this;
        }
        
        @NonNull
        public Builder setLongLabel(@NonNull final CharSequence charSequence) {
            this.mInfo.mLongLabel = charSequence;
            return this;
        }
        
        @NonNull
        public Builder setShortLabel(@NonNull final CharSequence charSequence) {
            this.mInfo.mLabel = charSequence;
            return this;
        }
    }
}
