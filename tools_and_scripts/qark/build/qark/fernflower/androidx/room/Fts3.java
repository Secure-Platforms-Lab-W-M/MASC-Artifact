package androidx.room;

import androidx.annotation.RequiresApi;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target({ElementType.TYPE})
@RequiresApi(16)
public @interface Fts3 {
   FtsOptions$Tokenizer tokenizer() default FtsOptions$Tokenizer.SIMPLE;

   String[] tokenizerArgs() default {};
}
