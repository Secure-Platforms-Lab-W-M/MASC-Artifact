// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.room;

import java.lang.annotation.Annotation;

public @interface Update {
    int onConflict() default 3;
}
