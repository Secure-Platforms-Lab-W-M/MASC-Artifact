/*
 * Decompiled with CFR 0_124.
 */
package com.bumptech.glide.util;

import com.bumptech.glide.util.Util;

public class MultiClassKey {
    private Class<?> first;
    private Class<?> second;
    private Class<?> third;

    public MultiClassKey() {
    }

    public MultiClassKey(Class<?> class_, Class<?> class_2) {
        this.set(class_, class_2);
    }

    public MultiClassKey(Class<?> class_, Class<?> class_2, Class<?> class_3) {
        this.set(class_, class_2, class_3);
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object != null) {
            if (this.getClass() != object.getClass()) {
                return false;
            }
            object = (MultiClassKey)object;
            if (!this.first.equals(object.first)) {
                return false;
            }
            if (!this.second.equals(object.second)) {
                return false;
            }
            if (!Util.bothNullOrEqual(this.third, object.third)) {
                return false;
            }
            return true;
        }
        return false;
    }

    public int hashCode() {
        int n = this.first.hashCode();
        int n2 = this.second.hashCode();
        Class class_ = this.third;
        int n3 = class_ != null ? class_.hashCode() : 0;
        return (n * 31 + n2) * 31 + n3;
    }

    public void set(Class<?> class_, Class<?> class_2) {
        this.set(class_, class_2, null);
    }

    public void set(Class<?> class_, Class<?> class_2, Class<?> class_3) {
        this.first = class_;
        this.second = class_2;
        this.third = class_3;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("MultiClassKey{first=");
        stringBuilder.append(this.first);
        stringBuilder.append(", second=");
        stringBuilder.append(this.second);
        stringBuilder.append('}');
        return stringBuilder.toString();
    }
}

