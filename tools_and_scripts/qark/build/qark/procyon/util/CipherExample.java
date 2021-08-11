// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package util;

public class CipherExample
{
    private String cipherName;
    
    public CipherExample() {
        this.cipherName = "AES/CBC/PKCS5Padding";
    }
    
    public String getCipherName() {
        return this.cipherName;
    }
    
    public CipherExample methodA() {
        this.cipherName = "AES/CBC/PKCS5Padding";
        return this;
    }
    
    public CipherExample methodB() {
        this.cipherName = "DES";
        return this;
    }
}
