// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package com.google.gson.internal;

import java.io.ObjectStreamException;
import java.math.BigDecimal;

public final class LazilyParsedNumber extends Number
{
    private final String value;
    
    public LazilyParsedNumber(final String value) {
        this.value = value;
    }
    
    private Object writeReplace() throws ObjectStreamException {
        return new BigDecimal(this.value);
    }
    
    @Override
    public double doubleValue() {
        return Double.parseDouble(this.value);
    }
    
    @Override
    public boolean equals(final Object o) {
        final boolean b = false;
        boolean b2;
        if (this == o) {
            b2 = true;
        }
        else {
            b2 = b;
            if (o instanceof LazilyParsedNumber) {
                final LazilyParsedNumber lazilyParsedNumber = (LazilyParsedNumber)o;
                if (this.value != lazilyParsedNumber.value) {
                    b2 = b;
                    if (!this.value.equals(lazilyParsedNumber.value)) {
                        return b2;
                    }
                }
                return true;
            }
        }
        return b2;
    }
    
    @Override
    public float floatValue() {
        return Float.parseFloat(this.value);
    }
    
    @Override
    public int hashCode() {
        return this.value.hashCode();
    }
    
    @Override
    public int intValue() {
        try {
            return Integer.parseInt(this.value);
        }
        catch (NumberFormatException ex) {
            try {
                return (int)Long.parseLong(this.value);
            }
            catch (NumberFormatException ex2) {
                return new BigDecimal(this.value).intValue();
            }
        }
    }
    
    @Override
    public long longValue() {
        try {
            return Long.parseLong(this.value);
        }
        catch (NumberFormatException ex) {
            return new BigDecimal(this.value).longValue();
        }
    }
    
    @Override
    public String toString() {
        return this.value;
    }
}
