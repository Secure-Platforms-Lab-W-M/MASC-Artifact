/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  androidx.room.FtsOptions
 */
package androidx.room;

import androidx.annotation.RequiresApi;
import androidx.room.FtsOptions;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value=RetentionPolicy.CLASS)
@Target(value={ElementType.TYPE})
@RequiresApi(value=16)
public @interface Fts3 {
    public FtsOptions.Tokenizer tokenizer() default FtsOptions.Tokenizer.SIMPLE;

    public String[] tokenizerArgs() default {};
}

