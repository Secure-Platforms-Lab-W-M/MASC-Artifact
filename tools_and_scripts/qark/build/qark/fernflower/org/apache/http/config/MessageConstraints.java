package org.apache.http.config;

import org.apache.http.util.Args;

public class MessageConstraints implements Cloneable {
   public static final MessageConstraints DEFAULT = (new MessageConstraints.Builder()).build();
   private final int maxHeaderCount;
   private final int maxLineLength;

   MessageConstraints(int var1, int var2) {
      this.maxLineLength = var1;
      this.maxHeaderCount = var2;
   }

   public static MessageConstraints.Builder copy(MessageConstraints var0) {
      Args.notNull(var0, "Message constraints");
      return (new MessageConstraints.Builder()).setMaxHeaderCount(var0.getMaxHeaderCount()).setMaxLineLength(var0.getMaxLineLength());
   }

   public static MessageConstraints.Builder custom() {
      return new MessageConstraints.Builder();
   }

   public static MessageConstraints lineLen(int var0) {
      return new MessageConstraints(Args.notNegative(var0, "Max line length"), -1);
   }

   protected MessageConstraints clone() throws CloneNotSupportedException {
      return (MessageConstraints)super.clone();
   }

   public int getMaxHeaderCount() {
      return this.maxHeaderCount;
   }

   public int getMaxLineLength() {
      return this.maxLineLength;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("[maxLineLength=");
      var1.append(this.maxLineLength);
      var1.append(", maxHeaderCount=");
      var1.append(this.maxHeaderCount);
      var1.append("]");
      return var1.toString();
   }

   public static class Builder {
      private int maxHeaderCount = -1;
      private int maxLineLength = -1;

      Builder() {
      }

      public MessageConstraints build() {
         return new MessageConstraints(this.maxLineLength, this.maxHeaderCount);
      }

      public MessageConstraints.Builder setMaxHeaderCount(int var1) {
         this.maxHeaderCount = var1;
         return this;
      }

      public MessageConstraints.Builder setMaxLineLength(int var1) {
         this.maxLineLength = var1;
         return this;
      }
   }
}
