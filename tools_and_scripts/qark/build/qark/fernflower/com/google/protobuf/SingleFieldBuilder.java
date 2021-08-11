package com.google.protobuf;

public class SingleFieldBuilder implements GeneratedMessage.BuilderParent {
   private GeneratedMessage.Builder builder;
   private boolean isClean;
   private GeneratedMessage message;
   private GeneratedMessage.BuilderParent parent;

   public SingleFieldBuilder(GeneratedMessage var1, GeneratedMessage.BuilderParent var2, boolean var3) {
      if (var1 != null) {
         this.message = var1;
         this.parent = var2;
         this.isClean = var3;
      } else {
         throw null;
      }
   }

   private void onChanged() {
      if (this.builder != null) {
         this.message = null;
      }

      if (this.isClean) {
         GeneratedMessage.BuilderParent var1 = this.parent;
         if (var1 != null) {
            var1.markDirty();
            this.isClean = false;
         }
      }

   }

   public GeneratedMessage build() {
      this.isClean = true;
      return this.getMessage();
   }

   public SingleFieldBuilder clear() {
      GeneratedMessage var1 = this.message;
      Message var2;
      if (var1 != null) {
         var2 = var1.getDefaultInstanceForType();
      } else {
         var2 = this.builder.getDefaultInstanceForType();
      }

      this.message = (GeneratedMessage)((GeneratedMessage)var2);
      GeneratedMessage.Builder var3 = this.builder;
      if (var3 != null) {
         var3.dispose();
         this.builder = null;
      }

      this.onChanged();
      return this;
   }

   public void dispose() {
      this.parent = null;
   }

   public GeneratedMessage.Builder getBuilder() {
      if (this.builder == null) {
         GeneratedMessage.Builder var1 = (GeneratedMessage.Builder)this.message.newBuilderForType(this);
         this.builder = var1;
         var1.mergeFrom(this.message);
         this.builder.markClean();
      }

      return this.builder;
   }

   public GeneratedMessage getMessage() {
      if (this.message == null) {
         this.message = (GeneratedMessage)this.builder.buildPartial();
      }

      return this.message;
   }

   public MessageOrBuilder getMessageOrBuilder() {
      GeneratedMessage.Builder var1 = this.builder;
      return (MessageOrBuilder)(var1 != null ? var1 : this.message);
   }

   public void markDirty() {
      this.onChanged();
   }

   public SingleFieldBuilder mergeFrom(GeneratedMessage var1) {
      label12: {
         if (this.builder == null) {
            GeneratedMessage var2 = this.message;
            if (var2 == var2.getDefaultInstanceForType()) {
               this.message = var1;
               break label12;
            }
         }

         this.getBuilder().mergeFrom(var1);
      }

      this.onChanged();
      return this;
   }

   public SingleFieldBuilder setMessage(GeneratedMessage var1) {
      if (var1 != null) {
         this.message = var1;
         GeneratedMessage.Builder var2 = this.builder;
         if (var2 != null) {
            var2.dispose();
            this.builder = null;
         }

         this.onChanged();
         return this;
      } else {
         throw null;
      }
   }
}
