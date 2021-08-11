// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.core.content.pm;

import java.util.Collection;
import java.util.HashSet;
import android.text.TextUtils;
import android.content.pm.ShortcutInfo$Builder;
import android.content.pm.ShortcutInfo;
import java.util.Arrays;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.content.pm.PackageManager$NameNotFoundException;
import android.os.Parcelable;
import android.os.PersistableBundle;
import androidx.core.app.Person;
import android.content.Intent;
import androidx.core.graphics.drawable.IconCompat;
import android.content.Context;
import java.util.Set;
import android.content.ComponentName;

public class ShortcutInfoCompat
{
    private static final String EXTRA_LONG_LIVED = "extraLongLived";
    private static final String EXTRA_PERSON_ = "extraPerson_";
    private static final String EXTRA_PERSON_COUNT = "extraPersonCount";
    ComponentName mActivity;
    Set<String> mCategories;
    Context mContext;
    CharSequence mDisabledMessage;
    IconCompat mIcon;
    String mId;
    Intent[] mIntents;
    boolean mIsAlwaysBadged;
    boolean mIsLongLived;
    CharSequence mLabel;
    CharSequence mLongLabel;
    Person[] mPersons;
    
    ShortcutInfoCompat() {
    }
    
    private PersistableBundle buildExtrasBundle() {
        final PersistableBundle persistableBundle = new PersistableBundle();
        final Person[] mPersons = this.mPersons;
        if (mPersons != null && mPersons.length > 0) {
            persistableBundle.putInt("extraPersonCount", mPersons.length);
            for (int i = 0; i < this.mPersons.length; ++i) {
                final StringBuilder sb = new StringBuilder();
                sb.append("extraPerson_");
                sb.append(i + 1);
                persistableBundle.putPersistableBundle(sb.toString(), this.mPersons[i].toPersistableBundle());
            }
        }
        persistableBundle.putBoolean("extraLongLived", this.mIsLongLived);
        return persistableBundle;
    }
    
    static boolean getLongLivedFromExtra(final PersistableBundle persistableBundle) {
        return persistableBundle != null && persistableBundle.containsKey("extraLongLived") && persistableBundle.getBoolean("extraLongLived");
    }
    
    static Person[] getPersonsFromExtra(final PersistableBundle persistableBundle) {
        if (persistableBundle != null && persistableBundle.containsKey("extraPersonCount")) {
            final int int1 = persistableBundle.getInt("extraPersonCount");
            final Person[] array = new Person[int1];
            for (int i = 0; i < int1; ++i) {
                final StringBuilder sb = new StringBuilder();
                sb.append("extraPerson_");
                sb.append(i + 1);
                array[i] = Person.fromPersistableBundle(persistableBundle.getPersistableBundle(sb.toString()));
            }
            return array;
        }
        return null;
    }
    
    Intent addToIntent(final Intent intent) {
        final Intent[] mIntents = this.mIntents;
        intent.putExtra("android.intent.extra.shortcut.INTENT", (Parcelable)mIntents[mIntents.length - 1]).putExtra("android.intent.extra.shortcut.NAME", this.mLabel.toString());
        if (this.mIcon != null) {
            Drawable loadIcon = null;
            final Drawable drawable = null;
            if (this.mIsAlwaysBadged) {
                final PackageManager packageManager = this.mContext.getPackageManager();
                final ComponentName mActivity = this.mActivity;
                Drawable activityIcon = drawable;
                if (mActivity != null) {
                    try {
                        activityIcon = packageManager.getActivityIcon(mActivity);
                    }
                    catch (PackageManager$NameNotFoundException ex) {
                        activityIcon = drawable;
                    }
                }
                if ((loadIcon = activityIcon) == null) {
                    loadIcon = this.mContext.getApplicationInfo().loadIcon(packageManager);
                }
            }
            this.mIcon.addToShortcutIntent(intent, loadIcon, this.mContext);
        }
        return intent;
    }
    
    public ComponentName getActivity() {
        return this.mActivity;
    }
    
    public Set<String> getCategories() {
        return this.mCategories;
    }
    
    public CharSequence getDisabledMessage() {
        return this.mDisabledMessage;
    }
    
    public IconCompat getIcon() {
        return this.mIcon;
    }
    
    public String getId() {
        return this.mId;
    }
    
    public Intent getIntent() {
        final Intent[] mIntents = this.mIntents;
        return mIntents[mIntents.length - 1];
    }
    
    public Intent[] getIntents() {
        final Intent[] mIntents = this.mIntents;
        return Arrays.copyOf(mIntents, mIntents.length);
    }
    
    public CharSequence getLongLabel() {
        return this.mLongLabel;
    }
    
    public CharSequence getShortLabel() {
        return this.mLabel;
    }
    
    public ShortcutInfo toShortcutInfo() {
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
        final Set<String> mCategories = this.mCategories;
        if (mCategories != null) {
            setIntents.setCategories((Set)mCategories);
        }
        setIntents.setExtras(this.buildExtrasBundle());
        return setIntents.build();
    }
    
    public static class Builder
    {
        private final ShortcutInfoCompat mInfo;
        
        public Builder(final Context mContext, final ShortcutInfo shortcutInfo) {
            final ShortcutInfoCompat mInfo = new ShortcutInfoCompat();
            this.mInfo = mInfo;
            mInfo.mContext = mContext;
            this.mInfo.mId = shortcutInfo.getId();
            final Intent[] intents = shortcutInfo.getIntents();
            this.mInfo.mIntents = Arrays.copyOf(intents, intents.length);
            this.mInfo.mActivity = shortcutInfo.getActivity();
            this.mInfo.mLabel = shortcutInfo.getShortLabel();
            this.mInfo.mLongLabel = shortcutInfo.getLongLabel();
            this.mInfo.mDisabledMessage = shortcutInfo.getDisabledMessage();
            this.mInfo.mCategories = (Set<String>)shortcutInfo.getCategories();
            this.mInfo.mPersons = ShortcutInfoCompat.getPersonsFromExtra(shortcutInfo.getExtras());
        }
        
        public Builder(final Context mContext, final String mId) {
            final ShortcutInfoCompat mInfo = new ShortcutInfoCompat();
            this.mInfo = mInfo;
            mInfo.mContext = mContext;
            this.mInfo.mId = mId;
        }
        
        public Builder(final ShortcutInfoCompat shortcutInfoCompat) {
            final ShortcutInfoCompat mInfo = new ShortcutInfoCompat();
            this.mInfo = mInfo;
            mInfo.mContext = shortcutInfoCompat.mContext;
            this.mInfo.mId = shortcutInfoCompat.mId;
            this.mInfo.mIntents = Arrays.copyOf(shortcutInfoCompat.mIntents, shortcutInfoCompat.mIntents.length);
            this.mInfo.mActivity = shortcutInfoCompat.mActivity;
            this.mInfo.mLabel = shortcutInfoCompat.mLabel;
            this.mInfo.mLongLabel = shortcutInfoCompat.mLongLabel;
            this.mInfo.mDisabledMessage = shortcutInfoCompat.mDisabledMessage;
            this.mInfo.mIcon = shortcutInfoCompat.mIcon;
            this.mInfo.mIsAlwaysBadged = shortcutInfoCompat.mIsAlwaysBadged;
            this.mInfo.mIsLongLived = shortcutInfoCompat.mIsLongLived;
            if (shortcutInfoCompat.mPersons != null) {
                this.mInfo.mPersons = Arrays.copyOf(shortcutInfoCompat.mPersons, shortcutInfoCompat.mPersons.length);
            }
            if (shortcutInfoCompat.mCategories != null) {
                this.mInfo.mCategories = new HashSet<String>(shortcutInfoCompat.mCategories);
            }
        }
        
        public ShortcutInfoCompat build() {
            if (TextUtils.isEmpty(this.mInfo.mLabel)) {
                throw new IllegalArgumentException("Shortcut must have a non-empty label");
            }
            if (this.mInfo.mIntents != null && this.mInfo.mIntents.length != 0) {
                return this.mInfo;
            }
            throw new IllegalArgumentException("Shortcut must have an intent");
        }
        
        public Builder setActivity(final ComponentName mActivity) {
            this.mInfo.mActivity = mActivity;
            return this;
        }
        
        public Builder setAlwaysBadged() {
            this.mInfo.mIsAlwaysBadged = true;
            return this;
        }
        
        public Builder setCategories(final Set<String> mCategories) {
            this.mInfo.mCategories = mCategories;
            return this;
        }
        
        public Builder setDisabledMessage(final CharSequence mDisabledMessage) {
            this.mInfo.mDisabledMessage = mDisabledMessage;
            return this;
        }
        
        public Builder setIcon(final IconCompat mIcon) {
            this.mInfo.mIcon = mIcon;
            return this;
        }
        
        public Builder setIntent(final Intent intent) {
            return this.setIntents(new Intent[] { intent });
        }
        
        public Builder setIntents(final Intent[] mIntents) {
            this.mInfo.mIntents = mIntents;
            return this;
        }
        
        public Builder setLongLabel(final CharSequence mLongLabel) {
            this.mInfo.mLongLabel = mLongLabel;
            return this;
        }
        
        public Builder setLongLived() {
            this.mInfo.mIsLongLived = true;
            return this;
        }
        
        public Builder setPerson(final Person person) {
            return this.setPersons(new Person[] { person });
        }
        
        public Builder setPersons(final Person[] mPersons) {
            this.mInfo.mPersons = mPersons;
            return this;
        }
        
        public Builder setShortLabel(final CharSequence mLabel) {
            this.mInfo.mLabel = mLabel;
            return this;
        }
    }
}
