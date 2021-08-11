/*
 * Decompiled with CFR 0_124.
 */
package org.jacoco.agent.rt.internal_8ff85ea.asm;

public final class Handle {
    final String desc;
    final boolean itf;
    final String name;
    final String owner;
    final int tag;

    @Deprecated
    public Handle(int n, String string2, String string3, String string4) {
        boolean bl = n == 9;
        this(n, string2, string3, string4, bl);
    }

    public Handle(int n, String string2, String string3, String string4, boolean bl) {
        this.tag = n;
        this.owner = string2;
        this.name = string3;
        this.desc = string4;
        this.itf = bl;
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof Handle)) {
            return false;
        }
        object = (Handle)object;
        if (this.tag == object.tag && this.itf == object.itf && this.owner.equals(object.owner) && this.name.equals(object.name) && this.desc.equals(object.desc)) {
            return true;
        }
        return false;
    }

    public String getDesc() {
        return this.desc;
    }

    public String getName() {
        return this.name;
    }

    public String getOwner() {
        return this.owner;
    }

    public int getTag() {
        return this.tag;
    }

    public int hashCode() {
        int n = this.tag;
        int n2 = this.itf ? 64 : 0;
        return n + n2 + this.owner.hashCode() * this.name.hashCode() * this.desc.hashCode();
    }

    public boolean isInterface() {
        return this.itf;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.owner);
        stringBuilder.append('.');
        stringBuilder.append(this.name);
        stringBuilder.append(this.desc);
        stringBuilder.append(" (");
        stringBuilder.append(this.tag);
        String string2 = this.itf ? " itf" : "";
        stringBuilder.append(string2);
        stringBuilder.append(')');
        return stringBuilder.toString();
    }
}

