package org.apache.commons.lang3.builder;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import org.apache.commons.lang3.reflect.FieldUtils;

public class ReflectionDiffBuilder implements Builder {
   private final DiffBuilder diffBuilder;
   private final Object left;
   private final Object right;

   public ReflectionDiffBuilder(Object var1, Object var2, ToStringStyle var3) {
      this.left = var1;
      this.right = var2;
      this.diffBuilder = new DiffBuilder(var1, var2, var3);
   }

   private boolean accept(Field var1) {
      if (var1.getName().indexOf(36) != -1) {
         return false;
      } else {
         return Modifier.isTransient(var1.getModifiers()) ? false : Modifier.isStatic(var1.getModifiers()) ^ true;
      }
   }

   private void appendFields(Class var1) {
      Field[] var6 = FieldUtils.getAllFields(var1);
      int var3 = var6.length;

      for(int var2 = 0; var2 < var3; ++var2) {
         Field var4 = var6[var2];
         if (this.accept(var4)) {
            try {
               this.diffBuilder.append(var4.getName(), FieldUtils.readField(var4, this.left, true), FieldUtils.readField(var4, this.right, true));
            } catch (IllegalAccessException var5) {
               StringBuilder var7 = new StringBuilder();
               var7.append("Unexpected IllegalAccessException: ");
               var7.append(var5.getMessage());
               throw new InternalError(var7.toString());
            }
         }
      }

   }

   public DiffResult build() {
      if (this.left.equals(this.right)) {
         return this.diffBuilder.build();
      } else {
         this.appendFields(this.left.getClass());
         return this.diffBuilder.build();
      }
   }
}
