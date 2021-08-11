/*
 * Decompiled with CFR 0_124.
 */
package com.google.protobuf;

import com.google.protobuf.GeneratedMessageLite;
import com.google.protobuf.MessageLite;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ExtensionRegistryLite {
    private static final ExtensionRegistryLite EMPTY;
    private static volatile boolean eagerlyParseMessageSets;
    private final Map<ObjectIntPair, GeneratedMessageLite.GeneratedExtension<?, ?>> extensionsByNumber;

    static {
        eagerlyParseMessageSets = false;
        EMPTY = new ExtensionRegistryLite(true);
    }

    ExtensionRegistryLite() {
        this.extensionsByNumber = new HashMap();
    }

    ExtensionRegistryLite(ExtensionRegistryLite extensionRegistryLite) {
        if (extensionRegistryLite == EMPTY) {
            this.extensionsByNumber = Collections.emptyMap();
            return;
        }
        this.extensionsByNumber = Collections.unmodifiableMap(extensionRegistryLite.extensionsByNumber);
    }

    private ExtensionRegistryLite(boolean bl) {
        this.extensionsByNumber = Collections.emptyMap();
    }

    public static ExtensionRegistryLite getEmptyRegistry() {
        return EMPTY;
    }

    public static boolean isEagerlyParseMessageSets() {
        return eagerlyParseMessageSets;
    }

    public static ExtensionRegistryLite newInstance() {
        return new ExtensionRegistryLite();
    }

    public static void setEagerlyParseMessageSets(boolean bl) {
        eagerlyParseMessageSets = bl;
    }

    public final void add(GeneratedMessageLite.GeneratedExtension<?, ?> generatedExtension) {
        this.extensionsByNumber.put(new ObjectIntPair(generatedExtension.getContainingTypeDefaultInstance(), generatedExtension.getNumber()), generatedExtension);
    }

    public <ContainingType extends MessageLite> GeneratedMessageLite.GeneratedExtension<ContainingType, ?> findLiteExtensionByNumber(ContainingType ContainingType, int n) {
        return this.extensionsByNumber.get(new ObjectIntPair(ContainingType, n));
    }

    public ExtensionRegistryLite getUnmodifiable() {
        return new ExtensionRegistryLite(this);
    }

    private static final class ObjectIntPair {
        private final int number;
        private final Object object;

        ObjectIntPair(Object object, int n) {
            this.object = object;
            this.number = n;
        }

        public boolean equals(Object object) {
            boolean bl = object instanceof ObjectIntPair;
            boolean bl2 = false;
            if (!bl) {
                return false;
            }
            object = (ObjectIntPair)object;
            bl = bl2;
            if (this.object == object.object) {
                bl = bl2;
                if (this.number == object.number) {
                    bl = true;
                }
            }
            return bl;
        }

        public int hashCode() {
            return System.identityHashCode(this.object) * 65535 + this.number;
        }
    }

}

