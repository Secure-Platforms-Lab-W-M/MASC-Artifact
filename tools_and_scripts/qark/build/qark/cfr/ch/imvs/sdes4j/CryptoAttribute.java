/*
 * Decompiled with CFR 0_124.
 */
package ch.imvs.sdes4j;

import ch.imvs.sdes4j.CryptoSuite;
import ch.imvs.sdes4j.KeyParam;
import ch.imvs.sdes4j.SDesFactory;
import ch.imvs.sdes4j.SessionParam;
import java.util.LinkedList;
import java.util.List;

public class CryptoAttribute {
    protected CryptoSuite cryptoSuite;
    protected KeyParam[] keyParams;
    protected SessionParam[] sessionParams = null;
    protected int tag;

    protected CryptoAttribute() {
    }

    public CryptoAttribute(int n, CryptoSuite cryptoSuite, KeyParam[] arrkeyParam, SessionParam[] arrsessionParam) {
        if (n <= 99999999 && n >= 0) {
            if (cryptoSuite != null) {
                if (arrkeyParam != null && arrkeyParam.length != 0) {
                    this.tag = n;
                    this.cryptoSuite = cryptoSuite;
                    this.keyParams = arrkeyParam;
                    if (arrsessionParam == null) {
                        this.sessionParams = new SessionParam[0];
                        return;
                    }
                    this.sessionParams = arrsessionParam;
                    return;
                }
                throw new IllegalArgumentException("keyParams cannot be null or empty");
            }
            throw new IllegalArgumentException("cryptoSuite cannot be null");
        }
        throw new IllegalArgumentException("tag can have at most 10 digits and must be non-negative");
    }

    public static CryptoAttribute create(String arrstring, SDesFactory sDesFactory) {
        CryptoAttribute cryptoAttribute = sDesFactory.createCryptoAttribute();
        LinkedList<String> linkedList = new LinkedList<String>();
        for (String string2 : arrstring.split("\\s")) {
            if (string2.trim().length() <= 0) continue;
            linkedList.add(string2);
        }
        cryptoAttribute.setTag((String)linkedList.remove(0));
        cryptoAttribute.setCryptoSuite((String)linkedList.remove(0), sDesFactory);
        if (linkedList.size() >= 1) {
            cryptoAttribute.setKeyParams((String)linkedList.remove(0), sDesFactory);
            cryptoAttribute.setSessionParams(linkedList, sDesFactory);
            return cryptoAttribute;
        }
        throw new IllegalArgumentException("There must be at least one key parameter");
    }

    public static CryptoAttribute create(String object, String arrstring, String string22, String string3, SDesFactory sDesFactory) {
        CryptoAttribute cryptoAttribute = sDesFactory.createCryptoAttribute();
        cryptoAttribute.setTag((String)object);
        cryptoAttribute.setCryptoSuite((String)arrstring, sDesFactory);
        cryptoAttribute.setKeyParams(string22, sDesFactory);
        object = new LinkedList();
        if (string3 != null) {
            for (String string22 : string3.split("\\s")) {
                if (string22.trim().length() <= 0) continue;
                object.add(string22);
            }
        }
        cryptoAttribute.setSessionParams((List<String>)object, sDesFactory);
        return cryptoAttribute;
    }

    private void setCryptoSuite(String string2, SDesFactory sDesFactory) {
        this.cryptoSuite = sDesFactory.createCryptoSuite(string2);
    }

    private void setKeyParams(String arrstring, SDesFactory sDesFactory) {
        arrstring = arrstring.split(";");
        LinkedList<KeyParam> linkedList = new LinkedList<KeyParam>();
        int n = arrstring.length;
        for (int i = 0; i < n; ++i) {
            linkedList.add(sDesFactory.createKeyParam(arrstring[i]));
        }
        this.keyParams = linkedList.toArray(sDesFactory.createKeyParamArray(0));
    }

    private void setSessionParams(List<String> list, SDesFactory sDesFactory) {
        LinkedList<SessionParam> linkedList = new LinkedList<SessionParam>();
        while (list.size() > 0) {
            linkedList.add(sDesFactory.createSessionParam(list.remove(0)));
        }
        this.sessionParams = linkedList.toArray(sDesFactory.createSessionParamArray(0));
    }

    private void setTag(String string2) {
        int n = Integer.valueOf(string2);
        if (n <= 99999999 && n >= 0) {
            this.tag = n;
            return;
        }
        throw new IllegalArgumentException("tag can have at most 10 digits and must be non-negative");
    }

    public String encode() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.tag);
        stringBuilder.append(' ');
        stringBuilder.append(this.cryptoSuite.encode());
        stringBuilder.append(' ');
        stringBuilder.append(this.getKeyParamsString());
        SessionParam[] arrsessionParam = this.sessionParams;
        if (arrsessionParam != null && arrsessionParam.length > 0) {
            stringBuilder.append(' ');
            stringBuilder.append(this.getSessionParamsString());
        }
        return stringBuilder.toString();
    }

    public boolean equals(Object object) {
        if (object != null && object instanceof CryptoAttribute) {
            object = (CryptoAttribute)object;
            return this.encode().equals(object.encode());
        }
        return false;
    }

    public CryptoSuite getCryptoSuite() {
        return this.cryptoSuite;
    }

    public KeyParam[] getKeyParams() {
        return this.keyParams;
    }

    public String getKeyParamsString() {
        KeyParam[] arrkeyParam;
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < (arrkeyParam = this.keyParams).length; ++i) {
            stringBuilder.append(arrkeyParam[i].encode());
            if (i >= this.keyParams.length - 1) continue;
            stringBuilder.append(';');
        }
        return stringBuilder.toString();
    }

    public SessionParam[] getSessionParams() {
        return this.sessionParams;
    }

    public String getSessionParamsString() {
        SessionParam[] arrsessionParam = this.sessionParams;
        if (arrsessionParam != null && arrsessionParam.length > 0) {
            SessionParam[] arrsessionParam2;
            arrsessionParam = new StringBuilder();
            for (int i = 0; i < (arrsessionParam2 = this.sessionParams).length; ++i) {
                arrsessionParam.append(arrsessionParam2[i].encode());
                if (i >= this.sessionParams.length - 1) continue;
                arrsessionParam.append(' ');
            }
            return arrsessionParam.toString();
        }
        return null;
    }

    public int getTag() {
        return this.tag;
    }

    public int hashCode() {
        return this.encode().hashCode();
    }
}

