package org.apache.commons.lang3.builder;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang3.Validate;

public class DiffResult implements Iterable {
   private static final String DIFFERS_STRING = "differs from";
   public static final String OBJECTS_SAME_STRING = "";
   private final List diffs;
   private final Object lhs;
   private final Object rhs;
   private final ToStringStyle style;

   DiffResult(Object var1, Object var2, List var3, ToStringStyle var4) {
      boolean var6 = true;
      boolean var5;
      if (var1 != null) {
         var5 = true;
      } else {
         var5 = false;
      }

      Validate.isTrue(var5, "Left hand object cannot be null");
      if (var2 != null) {
         var5 = true;
      } else {
         var5 = false;
      }

      Validate.isTrue(var5, "Right hand object cannot be null");
      if (var3 != null) {
         var5 = var6;
      } else {
         var5 = false;
      }

      Validate.isTrue(var5, "List of differences cannot be null");
      this.diffs = var3;
      this.lhs = var1;
      this.rhs = var2;
      if (var4 == null) {
         this.style = ToStringStyle.DEFAULT_STYLE;
      } else {
         this.style = var4;
      }
   }

   public List getDiffs() {
      return Collections.unmodifiableList(this.diffs);
   }

   public int getNumberOfDiffs() {
      return this.diffs.size();
   }

   public ToStringStyle getToStringStyle() {
      return this.style;
   }

   public Iterator iterator() {
      return this.diffs.iterator();
   }

   public String toString() {
      return this.toString(this.style);
   }

   public String toString(ToStringStyle var1) {
      if (this.diffs.isEmpty()) {
         return "";
      } else {
         ToStringBuilder var2 = new ToStringBuilder(this.lhs, var1);
         ToStringBuilder var5 = new ToStringBuilder(this.rhs, var1);
         Iterator var3 = this.diffs.iterator();

         while(var3.hasNext()) {
            Diff var4 = (Diff)var3.next();
            var2.append(var4.getFieldName(), var4.getLeft());
            var5.append(var4.getFieldName(), var4.getRight());
         }

         return String.format("%s %s %s", var2.build(), "differs from", var5.build());
      }
   }
}
