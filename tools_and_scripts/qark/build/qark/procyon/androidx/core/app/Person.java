// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.core.app;

import android.graphics.drawable.Icon;
import android.app.Person$Builder;
import android.os.PersistableBundle;
import android.os.Bundle;
import androidx.core.graphics.drawable.IconCompat;

public class Person
{
    private static final String ICON_KEY = "icon";
    private static final String IS_BOT_KEY = "isBot";
    private static final String IS_IMPORTANT_KEY = "isImportant";
    private static final String KEY_KEY = "key";
    private static final String NAME_KEY = "name";
    private static final String URI_KEY = "uri";
    IconCompat mIcon;
    boolean mIsBot;
    boolean mIsImportant;
    String mKey;
    CharSequence mName;
    String mUri;
    
    Person(final Builder builder) {
        this.mName = builder.mName;
        this.mIcon = builder.mIcon;
        this.mUri = builder.mUri;
        this.mKey = builder.mKey;
        this.mIsBot = builder.mIsBot;
        this.mIsImportant = builder.mIsImportant;
    }
    
    public static Person fromAndroidPerson(final android.app.Person person) {
        final Builder setName = new Builder().setName(person.getName());
        IconCompat fromIcon;
        if (person.getIcon() != null) {
            fromIcon = IconCompat.createFromIcon(person.getIcon());
        }
        else {
            fromIcon = null;
        }
        return setName.setIcon(fromIcon).setUri(person.getUri()).setKey(person.getKey()).setBot(person.isBot()).setImportant(person.isImportant()).build();
    }
    
    public static Person fromBundle(final Bundle bundle) {
        final Bundle bundle2 = bundle.getBundle("icon");
        final Builder setName = new Builder().setName(bundle.getCharSequence("name"));
        IconCompat fromBundle;
        if (bundle2 != null) {
            fromBundle = IconCompat.createFromBundle(bundle2);
        }
        else {
            fromBundle = null;
        }
        return setName.setIcon(fromBundle).setUri(bundle.getString("uri")).setKey(bundle.getString("key")).setBot(bundle.getBoolean("isBot")).setImportant(bundle.getBoolean("isImportant")).build();
    }
    
    public static Person fromPersistableBundle(final PersistableBundle persistableBundle) {
        return new Builder().setName(persistableBundle.getString("name")).setUri(persistableBundle.getString("uri")).setKey(persistableBundle.getString("key")).setBot(persistableBundle.getBoolean("isBot")).setImportant(persistableBundle.getBoolean("isImportant")).build();
    }
    
    public IconCompat getIcon() {
        return this.mIcon;
    }
    
    public String getKey() {
        return this.mKey;
    }
    
    public CharSequence getName() {
        return this.mName;
    }
    
    public String getUri() {
        return this.mUri;
    }
    
    public boolean isBot() {
        return this.mIsBot;
    }
    
    public boolean isImportant() {
        return this.mIsImportant;
    }
    
    public android.app.Person toAndroidPerson() {
        final Person$Builder setName = new Person$Builder().setName(this.getName());
        Icon icon;
        if (this.getIcon() != null) {
            icon = this.getIcon().toIcon();
        }
        else {
            icon = null;
        }
        return setName.setIcon(icon).setUri(this.getUri()).setKey(this.getKey()).setBot(this.isBot()).setImportant(this.isImportant()).build();
    }
    
    public Builder toBuilder() {
        return new Builder(this);
    }
    
    public Bundle toBundle() {
        final Bundle bundle = new Bundle();
        bundle.putCharSequence("name", this.mName);
        final IconCompat mIcon = this.mIcon;
        Bundle bundle2;
        if (mIcon != null) {
            bundle2 = mIcon.toBundle();
        }
        else {
            bundle2 = null;
        }
        bundle.putBundle("icon", bundle2);
        bundle.putString("uri", this.mUri);
        bundle.putString("key", this.mKey);
        bundle.putBoolean("isBot", this.mIsBot);
        bundle.putBoolean("isImportant", this.mIsImportant);
        return bundle;
    }
    
    public PersistableBundle toPersistableBundle() {
        final PersistableBundle persistableBundle = new PersistableBundle();
        final CharSequence mName = this.mName;
        String string;
        if (mName != null) {
            string = mName.toString();
        }
        else {
            string = null;
        }
        persistableBundle.putString("name", string);
        persistableBundle.putString("uri", this.mUri);
        persistableBundle.putString("key", this.mKey);
        persistableBundle.putBoolean("isBot", this.mIsBot);
        persistableBundle.putBoolean("isImportant", this.mIsImportant);
        return persistableBundle;
    }
    
    public static class Builder
    {
        IconCompat mIcon;
        boolean mIsBot;
        boolean mIsImportant;
        String mKey;
        CharSequence mName;
        String mUri;
        
        public Builder() {
        }
        
        Builder(final Person person) {
            this.mName = person.mName;
            this.mIcon = person.mIcon;
            this.mUri = person.mUri;
            this.mKey = person.mKey;
            this.mIsBot = person.mIsBot;
            this.mIsImportant = person.mIsImportant;
        }
        
        public Person build() {
            return new Person(this);
        }
        
        public Builder setBot(final boolean mIsBot) {
            this.mIsBot = mIsBot;
            return this;
        }
        
        public Builder setIcon(final IconCompat mIcon) {
            this.mIcon = mIcon;
            return this;
        }
        
        public Builder setImportant(final boolean mIsImportant) {
            this.mIsImportant = mIsImportant;
            return this;
        }
        
        public Builder setKey(final String mKey) {
            this.mKey = mKey;
            return this;
        }
        
        public Builder setName(final CharSequence mName) {
            this.mName = mName;
            return this;
        }
        
        public Builder setUri(final String mUri) {
            this.mUri = mUri;
            return this;
        }
    }
}
