/*
 * Decompiled with CFR 0_124.
 */
package androidx.databinding;

public interface Observable {
    public void addOnPropertyChangedCallback(OnPropertyChangedCallback var1);

    public void removeOnPropertyChangedCallback(OnPropertyChangedCallback var1);

    public static abstract class OnPropertyChangedCallback {
        public abstract void onPropertyChanged(Observable var1, int var2);
    }

}

