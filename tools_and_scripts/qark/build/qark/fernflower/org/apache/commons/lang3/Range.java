package org.apache.commons.lang3;

import java.io.Serializable;
import java.util.Comparator;

public final class Range implements Serializable {
   private static final long serialVersionUID = 1L;
   private final Comparator comparator;
   private transient int hashCode;
   private final Object maximum;
   private final Object minimum;
   private transient String toString;

   private Range(Object var1, Object var2, Comparator var3) {
      if (var1 != null && var2 != null) {
         if (var3 == null) {
            this.comparator = Range.ComparableComparator.INSTANCE;
         } else {
            this.comparator = var3;
         }

         if (this.comparator.compare(var1, var2) < 1) {
            this.minimum = var1;
            this.maximum = var2;
         } else {
            this.minimum = var2;
            this.maximum = var1;
         }
      } else {
         StringBuilder var4 = new StringBuilder();
         var4.append("Elements in a range must not be null: element1=");
         var4.append(var1);
         var4.append(", element2=");
         var4.append(var2);
         throw new IllegalArgumentException(var4.toString());
      }
   }

   public static Range between(Comparable var0, Comparable var1) {
      return between(var0, var1, (Comparator)null);
   }

   public static Range between(Object var0, Object var1, Comparator var2) {
      return new Range(var0, var1, var2);
   }

   // $FF: renamed from: is (java.lang.Comparable) org.apache.commons.lang3.Range
   public static Range method_40(Comparable var0) {
      return between(var0, var0, (Comparator)null);
   }

   // $FF: renamed from: is (java.lang.Object, java.util.Comparator) org.apache.commons.lang3.Range
   public static Range method_41(Object var0, Comparator var1) {
      return between(var0, var0, var1);
   }

   public boolean contains(Object var1) {
      boolean var3 = false;
      if (var1 == null) {
         return false;
      } else {
         boolean var2 = var3;
         if (this.comparator.compare(var1, this.minimum) > -1) {
            var2 = var3;
            if (this.comparator.compare(var1, this.maximum) < 1) {
               var2 = true;
            }
         }

         return var2;
      }
   }

   public boolean containsRange(Range var1) {
      if (var1 == null) {
         return false;
      } else {
         return this.contains(var1.minimum) && this.contains(var1.maximum);
      }
   }

   public int elementCompareTo(Object var1) {
      Validate.notNull(var1, "Element is null");
      if (this.isAfter(var1)) {
         return -1;
      } else {
         return this.isBefore(var1) ? 1 : 0;
      }
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (var1 != null) {
         if (var1.getClass() != this.getClass()) {
            return false;
         } else {
            Range var2 = (Range)var1;
            return this.minimum.equals(var2.minimum) && this.maximum.equals(var2.maximum);
         }
      } else {
         return false;
      }
   }

   public Comparator getComparator() {
      return this.comparator;
   }

   public Object getMaximum() {
      return this.maximum;
   }

   public Object getMinimum() {
      return this.minimum;
   }

   public int hashCode() {
      int var1 = this.hashCode;
      if (this.hashCode == 0) {
         var1 = ((17 * 37 + this.getClass().hashCode()) * 37 + this.minimum.hashCode()) * 37 + this.maximum.hashCode();
         this.hashCode = var1;
      }

      return var1;
   }

   public Range intersectionWith(Range var1) {
      if (this.isOverlappedBy(var1)) {
         if (this.equals(var1)) {
            return this;
         } else {
            Object var2;
            if (this.getComparator().compare(this.minimum, var1.minimum) < 0) {
               var2 = var1.minimum;
            } else {
               var2 = this.minimum;
            }

            Object var3;
            if (this.getComparator().compare(this.maximum, var1.maximum) < 0) {
               var3 = this.maximum;
            } else {
               var3 = var1.maximum;
            }

            return between(var2, var3, this.getComparator());
         }
      } else {
         throw new IllegalArgumentException(String.format("Cannot calculate intersection with non-overlapping range %s", var1));
      }
   }

   public boolean isAfter(Object var1) {
      boolean var2 = false;
      if (var1 == null) {
         return false;
      } else {
         if (this.comparator.compare(var1, this.minimum) < 0) {
            var2 = true;
         }

         return var2;
      }
   }

   public boolean isAfterRange(Range var1) {
      return var1 == null ? false : this.isAfter(var1.maximum);
   }

   public boolean isBefore(Object var1) {
      boolean var2 = false;
      if (var1 == null) {
         return false;
      } else {
         if (this.comparator.compare(var1, this.maximum) > 0) {
            var2 = true;
         }

         return var2;
      }
   }

   public boolean isBeforeRange(Range var1) {
      return var1 == null ? false : this.isBefore(var1.minimum);
   }

   public boolean isEndedBy(Object var1) {
      boolean var2 = false;
      if (var1 == null) {
         return false;
      } else {
         if (this.comparator.compare(var1, this.maximum) == 0) {
            var2 = true;
         }

         return var2;
      }
   }

   public boolean isNaturalOrdering() {
      return this.comparator == Range.ComparableComparator.INSTANCE;
   }

   public boolean isOverlappedBy(Range var1) {
      boolean var2 = false;
      if (var1 == null) {
         return false;
      } else {
         if (var1.contains(this.minimum) || var1.contains(this.maximum) || this.contains(var1.minimum)) {
            var2 = true;
         }

         return var2;
      }
   }

   public boolean isStartedBy(Object var1) {
      boolean var2 = false;
      if (var1 == null) {
         return false;
      } else {
         if (this.comparator.compare(var1, this.minimum) == 0) {
            var2 = true;
         }

         return var2;
      }
   }

   public String toString() {
      if (this.toString == null) {
         StringBuilder var1 = new StringBuilder();
         var1.append("[");
         var1.append(this.minimum);
         var1.append("..");
         var1.append(this.maximum);
         var1.append("]");
         this.toString = var1.toString();
      }

      return this.toString;
   }

   public String toString(String var1) {
      return String.format(var1, this.minimum, this.maximum, this.comparator);
   }

   private static enum ComparableComparator implements Comparator {
      INSTANCE;

      static {
         Range.ComparableComparator var0 = new Range.ComparableComparator("INSTANCE", 0);
         INSTANCE = var0;
      }

      public int compare(Object var1, Object var2) {
         return ((Comparable)var1).compareTo(var2);
      }
   }
}
