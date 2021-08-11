package androidx.room;

import androidx.annotation.RequiresApi;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target({ElementType.TYPE})
@RequiresApi(16)
public @interface Fts4 {
   Class contentEntity() default Object.class;

   String languageId() default "";

   FtsOptions$MatchInfo matchInfo() default FtsOptions$MatchInfo.FTS4;

   String[] notIndexed() default {};

   FtsOptions$Order order() default FtsOptions$Order.ASC;

   int[] prefix() default {};

   FtsOptions$Tokenizer tokenizer() default FtsOptions$Tokenizer.SIMPLE;

   String[] tokenizerArgs() default {};
}
