/*
 * Decompiled with CFR 0_124.
 */
package androidx.room;

import androidx.annotation.RequiresApi;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value=RetentionPolicy.CLASS)
@Target(value={ElementType.FIELD, ElementType.METHOD})
public @interface ColumnInfo {
    public static final int BINARY = 2;
    public static final int BLOB = 5;
    public static final String INHERIT_FIELD_NAME = "[field-name]";
    public static final int INTEGER = 3;
    @RequiresApi(value=21)
    public static final int LOCALIZED = 5;
    public static final int NOCASE = 3;
    public static final int REAL = 4;
    public static final int RTRIM = 4;
    public static final int TEXT = 2;
    public static final int UNDEFINED = 1;
    @RequiresApi(value=21)
    public static final int UNICODE = 6;
    public static final int UNSPECIFIED = 1;

    @Collate
    public int collate() default 1;

    public boolean index() default false;

    public String name() default "[field-name]";

    @SQLiteTypeAffinity
    public int typeAffinity() default 1;

    public static @interface Collate {
    }

    public static @interface SQLiteTypeAffinity {
    }

}

