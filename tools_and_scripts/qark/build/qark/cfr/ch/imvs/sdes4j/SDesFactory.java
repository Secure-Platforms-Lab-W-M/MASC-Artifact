/*
 * Decompiled with CFR 0_124.
 */
package ch.imvs.sdes4j;

import ch.imvs.sdes4j.CryptoAttribute;
import ch.imvs.sdes4j.CryptoSuite;
import ch.imvs.sdes4j.KeyParam;
import ch.imvs.sdes4j.SessionParam;
import java.util.Random;

public interface SDesFactory {
    public CryptoAttribute createCryptoAttribute();

    public CryptoSuite createCryptoSuite(String var1);

    public KeyParam createKeyParam(String var1);

    public KeyParam[] createKeyParamArray(int var1);

    public SessionParam createSessionParam(String var1);

    public SessionParam[] createSessionParamArray(int var1);

    public void setRandomGenerator(Random var1);
}

