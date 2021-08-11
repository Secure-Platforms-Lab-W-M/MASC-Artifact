// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.jacoco.agent.rt.internal_8ff85ea.asm;

public final class Handle
{
    final String desc;
    final boolean itf;
    final String name;
    final String owner;
    final int tag;
    
    @Deprecated
    public Handle(final int n, final String s, final String s2, final String s3) {
        this(n, s, s2, s3, n == 9);
    }
    
    public Handle(final int tag, final String owner, final String name, final String desc, final boolean itf) {
        this.tag = tag;
        this.owner = owner;
        this.name = name;
        this.desc = desc;
        this.itf = itf;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Handle)) {
            return false;
        }
        final Handle handle = (Handle)o;
        return this.tag == handle.tag && this.itf == handle.itf && this.owner.equals(handle.owner) && this.name.equals(handle.name) && this.desc.equals(handle.desc);
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
    
    @Override
    public int hashCode() {
        final int tag = this.tag;
        int n;
        if (this.itf) {
            n = 64;
        }
        else {
            n = 0;
        }
        return tag + n + this.owner.hashCode() * this.name.hashCode() * this.desc.hashCode();
    }
    
    public boolean isInterface() {
        return this.itf;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.owner);
        sb.append('.');
        sb.append(this.name);
        sb.append(this.desc);
        sb.append(" (");
        sb.append(this.tag);
        String s;
        if (this.itf) {
            s = " itf";
        }
        else {
            s = "";
        }
        sb.append(s);
        sb.append(')');
        return sb.toString();
    }
}
