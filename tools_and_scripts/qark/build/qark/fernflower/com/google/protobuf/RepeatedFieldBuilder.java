package com.google.protobuf;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class RepeatedFieldBuilder implements GeneratedMessage.BuilderParent {
   private List builders;
   private RepeatedFieldBuilder.BuilderExternalList externalBuilderList;
   private RepeatedFieldBuilder.MessageExternalList externalMessageList;
   private RepeatedFieldBuilder.MessageOrBuilderExternalList externalMessageOrBuilderList;
   private boolean isClean;
   private boolean isMessagesListMutable;
   private List messages;
   private GeneratedMessage.BuilderParent parent;

   public RepeatedFieldBuilder(List var1, boolean var2, GeneratedMessage.BuilderParent var3, boolean var4) {
      this.messages = var1;
      this.isMessagesListMutable = var2;
      this.parent = var3;
      this.isClean = var4;
   }

   private void ensureBuilders() {
      if (this.builders == null) {
         this.builders = new ArrayList(this.messages.size());

         for(int var1 = 0; var1 < this.messages.size(); ++var1) {
            this.builders.add((Object)null);
         }
      }

   }

   private void ensureMutableMessageList() {
      if (!this.isMessagesListMutable) {
         this.messages = new ArrayList(this.messages);
         this.isMessagesListMutable = true;
      }

   }

   private GeneratedMessage getMessage(int var1, boolean var2) {
      List var3 = this.builders;
      if (var3 == null) {
         return (GeneratedMessage)this.messages.get(var1);
      } else {
         SingleFieldBuilder var4 = (SingleFieldBuilder)var3.get(var1);
         if (var4 == null) {
            return (GeneratedMessage)this.messages.get(var1);
         } else {
            return var2 ? var4.build() : var4.getMessage();
         }
      }
   }

   private void incrementModCounts() {
      RepeatedFieldBuilder.MessageExternalList var1 = this.externalMessageList;
      if (var1 != null) {
         var1.incrementModCount();
      }

      RepeatedFieldBuilder.BuilderExternalList var2 = this.externalBuilderList;
      if (var2 != null) {
         var2.incrementModCount();
      }

      RepeatedFieldBuilder.MessageOrBuilderExternalList var3 = this.externalMessageOrBuilderList;
      if (var3 != null) {
         var3.incrementModCount();
      }

   }

   private void onChanged() {
      if (this.isClean) {
         GeneratedMessage.BuilderParent var1 = this.parent;
         if (var1 != null) {
            var1.markDirty();
            this.isClean = false;
         }
      }

   }

   public RepeatedFieldBuilder addAllMessages(Iterable var1) {
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         if ((GeneratedMessage)var2.next() == null) {
            throw null;
         }
      }

      Iterator var3;
      if (var1 instanceof Collection) {
         if (((Collection)var1).size() == 0) {
            return this;
         }

         this.ensureMutableMessageList();
         var3 = var1.iterator();

         while(var3.hasNext()) {
            this.addMessage((GeneratedMessage)var3.next());
         }
      } else {
         this.ensureMutableMessageList();
         var3 = var1.iterator();

         while(var3.hasNext()) {
            this.addMessage((GeneratedMessage)var3.next());
         }
      }

      this.onChanged();
      this.incrementModCounts();
      return this;
   }

   public GeneratedMessage.Builder addBuilder(int var1, GeneratedMessage var2) {
      this.ensureMutableMessageList();
      this.ensureBuilders();
      SingleFieldBuilder var3 = new SingleFieldBuilder(var2, this, this.isClean);
      this.messages.add(var1, (Object)null);
      this.builders.add(var1, var3);
      this.onChanged();
      this.incrementModCounts();
      return var3.getBuilder();
   }

   public GeneratedMessage.Builder addBuilder(GeneratedMessage var1) {
      this.ensureMutableMessageList();
      this.ensureBuilders();
      SingleFieldBuilder var2 = new SingleFieldBuilder(var1, this, this.isClean);
      this.messages.add((Object)null);
      this.builders.add(var2);
      this.onChanged();
      this.incrementModCounts();
      return var2.getBuilder();
   }

   public RepeatedFieldBuilder addMessage(int var1, GeneratedMessage var2) {
      if (var2 != null) {
         this.ensureMutableMessageList();
         this.messages.add(var1, var2);
         List var3 = this.builders;
         if (var3 != null) {
            var3.add(var1, (Object)null);
         }

         this.onChanged();
         this.incrementModCounts();
         return this;
      } else {
         throw null;
      }
   }

   public RepeatedFieldBuilder addMessage(GeneratedMessage var1) {
      if (var1 != null) {
         this.ensureMutableMessageList();
         this.messages.add(var1);
         List var2 = this.builders;
         if (var2 != null) {
            var2.add((Object)null);
         }

         this.onChanged();
         this.incrementModCounts();
         return this;
      } else {
         throw null;
      }
   }

   public List build() {
      this.isClean = true;
      if (!this.isMessagesListMutable && this.builders == null) {
         return this.messages;
      } else {
         boolean var3 = true;
         int var1;
         if (!this.isMessagesListMutable) {
            var1 = 0;

            boolean var2;
            while(true) {
               var2 = var3;
               if (var1 >= this.messages.size()) {
                  break;
               }

               Message var4 = (Message)this.messages.get(var1);
               SingleFieldBuilder var5 = (SingleFieldBuilder)this.builders.get(var1);
               if (var5 != null && var5.build() != var4) {
                  var2 = false;
                  break;
               }

               ++var1;
            }

            if (var2) {
               return this.messages;
            }
         }

         this.ensureMutableMessageList();

         for(var1 = 0; var1 < this.messages.size(); ++var1) {
            this.messages.set(var1, this.getMessage(var1, true));
         }

         List var6 = Collections.unmodifiableList(this.messages);
         this.messages = var6;
         this.isMessagesListMutable = false;
         return var6;
      }
   }

   public void clear() {
      this.messages = Collections.emptyList();
      this.isMessagesListMutable = false;
      List var1 = this.builders;
      if (var1 != null) {
         Iterator var3 = var1.iterator();

         while(var3.hasNext()) {
            SingleFieldBuilder var2 = (SingleFieldBuilder)var3.next();
            if (var2 != null) {
               var2.dispose();
            }
         }

         this.builders = null;
      }

      this.onChanged();
      this.incrementModCounts();
   }

   public void dispose() {
      this.parent = null;
   }

   public GeneratedMessage.Builder getBuilder(int var1) {
      this.ensureBuilders();
      SingleFieldBuilder var3 = (SingleFieldBuilder)this.builders.get(var1);
      SingleFieldBuilder var2 = var3;
      if (var3 == null) {
         var2 = new SingleFieldBuilder((GeneratedMessage)this.messages.get(var1), this, this.isClean);
         this.builders.set(var1, var2);
      }

      return var2.getBuilder();
   }

   public List getBuilderList() {
      if (this.externalBuilderList == null) {
         this.externalBuilderList = new RepeatedFieldBuilder.BuilderExternalList(this);
      }

      return this.externalBuilderList;
   }

   public int getCount() {
      return this.messages.size();
   }

   public GeneratedMessage getMessage(int var1) {
      return this.getMessage(var1, false);
   }

   public List getMessageList() {
      if (this.externalMessageList == null) {
         this.externalMessageList = new RepeatedFieldBuilder.MessageExternalList(this);
      }

      return this.externalMessageList;
   }

   public MessageOrBuilder getMessageOrBuilder(int var1) {
      List var2 = this.builders;
      if (var2 == null) {
         return (MessageOrBuilder)this.messages.get(var1);
      } else {
         SingleFieldBuilder var3 = (SingleFieldBuilder)var2.get(var1);
         return var3 == null ? (MessageOrBuilder)this.messages.get(var1) : var3.getMessageOrBuilder();
      }
   }

   public List getMessageOrBuilderList() {
      if (this.externalMessageOrBuilderList == null) {
         this.externalMessageOrBuilderList = new RepeatedFieldBuilder.MessageOrBuilderExternalList(this);
      }

      return this.externalMessageOrBuilderList;
   }

   public boolean isEmpty() {
      return this.messages.isEmpty();
   }

   public void markDirty() {
      this.onChanged();
   }

   public void remove(int var1) {
      this.ensureMutableMessageList();
      this.messages.remove(var1);
      List var2 = this.builders;
      if (var2 != null) {
         SingleFieldBuilder var3 = (SingleFieldBuilder)var2.remove(var1);
         if (var3 != null) {
            var3.dispose();
         }
      }

      this.onChanged();
      this.incrementModCounts();
   }

   public RepeatedFieldBuilder setMessage(int var1, GeneratedMessage var2) {
      if (var2 != null) {
         this.ensureMutableMessageList();
         this.messages.set(var1, var2);
         List var3 = this.builders;
         if (var3 != null) {
            SingleFieldBuilder var4 = (SingleFieldBuilder)var3.set(var1, (Object)null);
            if (var4 != null) {
               var4.dispose();
            }
         }

         this.onChanged();
         this.incrementModCounts();
         return this;
      } else {
         throw null;
      }
   }

   private static class BuilderExternalList extends AbstractList implements List {
      RepeatedFieldBuilder builder;

      BuilderExternalList(RepeatedFieldBuilder var1) {
         this.builder = var1;
      }

      public GeneratedMessage.Builder get(int var1) {
         return this.builder.getBuilder(var1);
      }

      void incrementModCount() {
         ++this.modCount;
      }

      public int size() {
         return this.builder.getCount();
      }
   }

   private static class MessageExternalList extends AbstractList implements List {
      RepeatedFieldBuilder builder;

      MessageExternalList(RepeatedFieldBuilder var1) {
         this.builder = var1;
      }

      public GeneratedMessage get(int var1) {
         return this.builder.getMessage(var1);
      }

      void incrementModCount() {
         ++this.modCount;
      }

      public int size() {
         return this.builder.getCount();
      }
   }

   private static class MessageOrBuilderExternalList extends AbstractList implements List {
      RepeatedFieldBuilder builder;

      MessageOrBuilderExternalList(RepeatedFieldBuilder var1) {
         this.builder = var1;
      }

      public MessageOrBuilder get(int var1) {
         return this.builder.getMessageOrBuilder(var1);
      }

      void incrementModCount() {
         ++this.modCount;
      }

      public int size() {
         return this.builder.getCount();
      }
   }
}
