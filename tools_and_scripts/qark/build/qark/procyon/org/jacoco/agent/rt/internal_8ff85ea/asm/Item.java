// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.jacoco.agent.rt.internal_8ff85ea.asm;

final class Item
{
    int hashCode;
    int index;
    int intVal;
    long longVal;
    Item next;
    String strVal1;
    String strVal2;
    String strVal3;
    int type;
    
    Item() {
    }
    
    Item(final int index) {
        this.index = index;
    }
    
    Item(final int index, final Item item) {
        this.index = index;
        this.type = item.type;
        this.intVal = item.intVal;
        this.longVal = item.longVal;
        this.strVal1 = item.strVal1;
        this.strVal2 = item.strVal2;
        this.strVal3 = item.strVal3;
        this.hashCode = item.hashCode;
    }
    
    boolean isEqualTo(final Item item) {
        final int type = this.type;
        Label_0278: {
            if (type != 1) {
                if (type == 12) {
                    return item.strVal1.equals(this.strVal1) && item.strVal2.equals(this.strVal2);
                }
                if (type != 16) {
                    if (type == 18) {
                        return item.longVal == this.longVal && item.strVal1.equals(this.strVal1) && item.strVal2.equals(this.strVal2);
                    }
                    switch (type) {
                        default: {
                            switch (type) {
                                default: {
                                    return item.strVal1.equals(this.strVal1) && item.strVal2.equals(this.strVal2) && item.strVal3.equals(this.strVal3);
                                }
                                case 31: {
                                    return item.intVal == this.intVal && item.strVal1.equals(this.strVal1);
                                }
                                case 32: {
                                    return item.longVal == this.longVal;
                                }
                                case 30: {
                                    break Label_0278;
                                }
                            }
                            break;
                        }
                        case 5:
                        case 6: {
                            return item.longVal == this.longVal;
                        }
                        case 3:
                        case 4: {
                            return item.intVal == this.intVal;
                        }
                        case 7:
                        case 8: {
                            break;
                        }
                    }
                }
            }
        }
        return item.strVal1.equals(this.strVal1);
    }
    
    void set(final double n) {
        this.type = 6;
        this.longVal = Double.doubleToRawLongBits(n);
        this.hashCode = (this.type + (int)n & Integer.MAX_VALUE);
    }
    
    void set(final float n) {
        this.type = 4;
        this.intVal = Float.floatToRawIntBits(n);
        this.hashCode = (this.type + (int)n & Integer.MAX_VALUE);
    }
    
    void set(final int intVal) {
        this.type = 3;
        this.intVal = intVal;
        this.hashCode = (this.type + intVal & Integer.MAX_VALUE);
    }
    
    void set(final int intVal, final int hashCode) {
        this.type = 33;
        this.intVal = intVal;
        this.hashCode = hashCode;
    }
    
    void set(final int type, final String strVal1, final String strVal2, final String strVal3) {
        this.type = type;
        this.strVal1 = strVal1;
        this.strVal2 = strVal2;
        this.strVal3 = strVal3;
        if (type != 1) {
            if (type == 12) {
                this.hashCode = (strVal1.hashCode() * strVal2.hashCode() + type & Integer.MAX_VALUE);
                return;
            }
            if (type != 16 && type != 30) {
                switch (type) {
                    default: {
                        this.hashCode = (strVal1.hashCode() * strVal2.hashCode() * strVal3.hashCode() + type & Integer.MAX_VALUE);
                        return;
                    }
                    case 7: {
                        this.intVal = 0;
                    }
                    case 8: {
                        break;
                    }
                }
            }
        }
        this.hashCode = (strVal1.hashCode() + type & Integer.MAX_VALUE);
    }
    
    void set(final long longVal) {
        this.type = 5;
        this.longVal = longVal;
        this.hashCode = (this.type + (int)longVal & Integer.MAX_VALUE);
    }
    
    void set(final String strVal1, final String strVal2, final int n) {
        this.type = 18;
        this.longVal = n;
        this.strVal1 = strVal1;
        this.strVal2 = strVal2;
        this.hashCode = (Integer.MAX_VALUE & this.strVal1.hashCode() * n * this.strVal2.hashCode() + 18);
    }
}
