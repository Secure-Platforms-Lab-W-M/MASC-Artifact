package com.google.protobuf;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public abstract class AbstractMessage extends AbstractMessageLite implements Message {
   private int memoizedSize = -1;

   private static String delimitWithCommas(List var0) {
      StringBuilder var1 = new StringBuilder();

      String var2;
      for(Iterator var3 = var0.iterator(); var3.hasNext(); var1.append(var2)) {
         var2 = (String)var3.next();
         if (var1.length() > 0) {
            var1.append(", ");
         }
      }

      return var1.toString();
   }

   protected static int hashBoolean(boolean var0) {
      return var0 ? 1231 : 1237;
   }

   protected static int hashEnum(Internal.EnumLite var0) {
      return var0.getNumber();
   }

   protected static int hashEnumList(List var0) {
      int var1 = 1;

      for(Iterator var2 = var0.iterator(); var2.hasNext(); var1 = var1 * 31 + hashEnum((Internal.EnumLite)var2.next())) {
      }

      return var1;
   }

   protected static int hashLong(long var0) {
      return (int)(var0 >>> 32 ^ var0);
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof Message)) {
         return false;
      } else {
         Message var2 = (Message)var1;
         if (this.getDescriptorForType() != var2.getDescriptorForType()) {
            return false;
         } else {
            return this.getAllFields().equals(var2.getAllFields()) && this.getUnknownFields().equals(var2.getUnknownFields());
         }
      }
   }

   public List findInitializationErrors() {
      return AbstractMessage.Builder.findMissingFields(this);
   }

   public String getInitializationErrorString() {
      return delimitWithCommas(this.findInitializationErrors());
   }

   public int getSerializedSize() {
      int var1 = this.memoizedSize;
      if (var1 != -1) {
         return var1;
      } else {
         var1 = 0;
         boolean var2 = this.getDescriptorForType().getOptions().getMessageSetWireFormat();
         Iterator var3 = this.getAllFields().entrySet().iterator();

         while(true) {
            while(var3.hasNext()) {
               Entry var5 = (Entry)var3.next();
               Descriptors.FieldDescriptor var4 = (Descriptors.FieldDescriptor)var5.getKey();
               Object var7 = var5.getValue();
               if (var2 && var4.isExtension() && var4.getType() == Descriptors.FieldDescriptor.Type.MESSAGE && !var4.isRepeated()) {
                  var1 += CodedOutputStream.computeMessageSetExtensionSize(var4.getNumber(), (Message)var7);
               } else {
                  var1 += FieldSet.computeFieldSize(var4, var7);
               }
            }

            UnknownFieldSet var6 = this.getUnknownFields();
            if (var2) {
               var1 += var6.getSerializedSizeAsMessageSet();
            } else {
               var1 += var6.getSerializedSize();
            }

            this.memoizedSize = var1;
            return var1;
         }
      }
   }

   public int hashCode() {
      return this.hashFields(41 * 19 + this.getDescriptorForType().hashCode(), this.getAllFields()) * 29 + this.getUnknownFields().hashCode();
   }

   protected int hashFields(int var1, Map var2) {
      Iterator var5 = var2.entrySet().iterator();

      while(var5.hasNext()) {
         Entry var4 = (Entry)var5.next();
         Descriptors.FieldDescriptor var3 = (Descriptors.FieldDescriptor)var4.getKey();
         Object var6 = var4.getValue();
         var1 = var1 * 37 + var3.getNumber();
         if (var3.getType() != Descriptors.FieldDescriptor.Type.ENUM) {
            var1 = var1 * 53 + var6.hashCode();
         } else if (var3.isRepeated()) {
            var1 = var1 * 53 + hashEnumList((List)var6);
         } else {
            var1 = var1 * 53 + hashEnum((Internal.EnumLite)var6);
         }
      }

      return var1;
   }

   public boolean isInitialized() {
      Iterator var1 = this.getDescriptorForType().getFields().iterator();

      while(var1.hasNext()) {
         Descriptors.FieldDescriptor var2 = (Descriptors.FieldDescriptor)var1.next();
         if (var2.isRequired() && !this.hasField(var2)) {
            return false;
         }
      }

      var1 = this.getAllFields().entrySet().iterator();

      while(true) {
         while(true) {
            Descriptors.FieldDescriptor var3;
            Entry var4;
            do {
               if (!var1.hasNext()) {
                  return true;
               }

               var4 = (Entry)var1.next();
               var3 = (Descriptors.FieldDescriptor)var4.getKey();
            } while(var3.getJavaType() != Descriptors.FieldDescriptor.JavaType.MESSAGE);

            if (var3.isRepeated()) {
               Iterator var5 = ((List)var4.getValue()).iterator();

               while(var5.hasNext()) {
                  if (!((Message)var5.next()).isInitialized()) {
                     return false;
                  }
               }
            } else if (!((Message)var4.getValue()).isInitialized()) {
               return false;
            }
         }
      }
   }

   UninitializedMessageException newUninitializedMessageException() {
      return AbstractMessage.Builder.newUninitializedMessageException(this);
   }

   public final String toString() {
      return TextFormat.printToString((MessageOrBuilder)this);
   }

   public void writeTo(CodedOutputStream var1) throws IOException {
      boolean var2 = this.getDescriptorForType().getOptions().getMessageSetWireFormat();
      Iterator var3 = this.getAllFields().entrySet().iterator();

      while(true) {
         while(var3.hasNext()) {
            Entry var5 = (Entry)var3.next();
            Descriptors.FieldDescriptor var4 = (Descriptors.FieldDescriptor)var5.getKey();
            Object var7 = var5.getValue();
            if (var2 && var4.isExtension() && var4.getType() == Descriptors.FieldDescriptor.Type.MESSAGE && !var4.isRepeated()) {
               var1.writeMessageSetExtension(var4.getNumber(), (Message)var7);
            } else {
               FieldSet.writeField(var4, var7, var1);
            }
         }

         UnknownFieldSet var6 = this.getUnknownFields();
         if (var2) {
            var6.writeAsMessageSetTo(var1);
            return;
         }

         var6.writeTo(var1);
         return;
      }
   }

   public abstract static class Builder extends AbstractMessageLite.Builder implements Message.Builder {
      private static void addRepeatedField(Message.Builder var0, FieldSet var1, Descriptors.FieldDescriptor var2, Object var3) {
         if (var0 != null) {
            var0.addRepeatedField(var2, var3);
         } else {
            var1.addRepeatedField(var2, var3);
         }
      }

      private static void eagerlyMergeMessageSetExtension(CodedInputStream var0, ExtensionRegistry.ExtensionInfo var1, ExtensionRegistryLite var2, Message.Builder var3, FieldSet var4) throws IOException {
         Descriptors.FieldDescriptor var5 = var1.descriptor;
         Message var6;
         if (hasOriginalMessage(var3, var4, var5)) {
            Message.Builder var7 = getOriginalMessage(var3, var4, var5).toBuilder();
            var0.readMessage((MessageLite.Builder)var7, var2);
            var6 = var7.buildPartial();
         } else {
            var6 = (Message)var0.readMessage(var1.defaultInstance.getParserForType(), var2);
         }

         if (var3 != null) {
            var3.setField(var5, var6);
         } else {
            var4.setField(var5, var6);
         }
      }

      private static List findMissingFields(MessageOrBuilder var0) {
         ArrayList var1 = new ArrayList();
         findMissingFields(var0, "", var1);
         return var1;
      }

      private static void findMissingFields(MessageOrBuilder var0, String var1, List var2) {
         Iterator var4 = var0.getDescriptorForType().getFields().iterator();

         Descriptors.FieldDescriptor var5;
         while(var4.hasNext()) {
            var5 = (Descriptors.FieldDescriptor)var4.next();
            if (var5.isRequired() && !var0.hasField(var5)) {
               StringBuilder var6 = new StringBuilder();
               var6.append(var1);
               var6.append(var5.getName());
               var2.add(var6.toString());
            }
         }

         var4 = var0.getAllFields().entrySet().iterator();

         while(true) {
            while(true) {
               Object var8;
               do {
                  if (!var4.hasNext()) {
                     return;
                  }

                  Entry var7 = (Entry)var4.next();
                  var5 = (Descriptors.FieldDescriptor)var7.getKey();
                  var8 = var7.getValue();
               } while(var5.getJavaType() != Descriptors.FieldDescriptor.JavaType.MESSAGE);

               if (var5.isRepeated()) {
                  int var3 = 0;

                  for(Iterator var9 = ((List)var8).iterator(); var9.hasNext(); ++var3) {
                     findMissingFields((MessageOrBuilder)var9.next(), subMessagePrefix(var1, var5, var3), var2);
                  }
               } else if (var0.hasField(var5)) {
                  findMissingFields((MessageOrBuilder)var8, subMessagePrefix(var1, var5, -1), var2);
               }
            }
         }
      }

      private static Message getOriginalMessage(Message.Builder var0, FieldSet var1, Descriptors.FieldDescriptor var2) {
         return var0 != null ? (Message)var0.getField(var2) : (Message)var1.getField(var2);
      }

      private static boolean hasOriginalMessage(Message.Builder var0, FieldSet var1, Descriptors.FieldDescriptor var2) {
         return var0 != null ? var0.hasField(var2) : var1.hasField(var2);
      }

      static boolean mergeFieldFrom(CodedInputStream var0, UnknownFieldSet.Builder var1, ExtensionRegistryLite var2, Descriptors.Descriptor var3, Message.Builder var4, FieldSet var5, int var6) throws IOException {
         if (var3.getOptions().getMessageSetWireFormat() && var6 == WireFormat.MESSAGE_SET_ITEM_TAG) {
            mergeMessageSetExtensionFromCodedStream(var0, var1, var2, var3, var4, var5);
            return true;
         } else {
            int var10 = WireFormat.getTagWireType(var6);
            int var9 = WireFormat.getTagFieldNumber(var6);
            Message var12 = null;
            Descriptors.FieldDescriptor var11 = null;
            Descriptors.FieldDescriptor var20;
            if (var3.isExtensionNumber(var9)) {
               if (var2 instanceof ExtensionRegistry) {
                  ExtensionRegistry.ExtensionInfo var18 = ((ExtensionRegistry)var2).findExtensionByNumber(var3, var9);
                  Message var19;
                  if (var18 == null) {
                     var12 = null;
                     var19 = var11;
                     var11 = var12;
                  } else {
                     Descriptors.FieldDescriptor var22 = var18.descriptor;
                     Message var13 = var18.defaultInstance;
                     var19 = var13;
                     var11 = var22;
                     if (var13 == null) {
                        if (var22.getJavaType() == Descriptors.FieldDescriptor.JavaType.MESSAGE) {
                           StringBuilder var14 = new StringBuilder();
                           var14.append("Message-typed extension lacked default instance: ");
                           var14.append(var22.getFullName());
                           throw new IllegalStateException(var14.toString());
                        }

                        var19 = var13;
                        var11 = var22;
                     }
                  }

                  var12 = var19;
                  var20 = var11;
               } else {
                  var20 = null;
               }
            } else if (var4 != null) {
               var20 = var3.findFieldByNumber(var9);
            } else {
               var20 = null;
            }

            boolean var7 = false;
            boolean var8 = false;
            if (var20 == null) {
               var7 = true;
            } else if (var10 == FieldSet.getWireFormatForFieldType(var20.getLiteType(), false)) {
               var8 = false;
            } else if (var20.isPackable() && var10 == FieldSet.getWireFormatForFieldType(var20.getLiteType(), true)) {
               var8 = true;
            } else {
               var7 = true;
            }

            if (var7) {
               return var1.mergeFieldFrom(var6, var0);
            } else {
               if (var8) {
                  var6 = var0.pushLimit(var0.readRawVarint32());
                  if (var20.getLiteType() == WireFormat.FieldType.ENUM) {
                     while(var0.getBytesUntilLimit() > 0) {
                        int var21 = var0.readEnum();
                        Descriptors.EnumValueDescriptor var16 = var20.getEnumType().findValueByNumber(var21);
                        if (var16 == null) {
                           return true;
                        }

                        addRepeatedField(var4, var5, var20, var16);
                     }
                  } else {
                     while(var0.getBytesUntilLimit() > 0) {
                        addRepeatedField(var4, var5, var20, FieldSet.readPrimitiveField(var0, var20.getLiteType()));
                     }
                  }

                  var0.popLimit(var6);
               } else {
                  var6 = null.$SwitchMap$com$google$protobuf$Descriptors$FieldDescriptor$Type[var20.getType().ordinal()];
                  Object var15;
                  Message.Builder var17;
                  if (var6 != 1) {
                     if (var6 != 2) {
                        if (var6 != 3) {
                           var15 = FieldSet.readPrimitiveField(var0, var20.getLiteType());
                        } else {
                           var6 = var0.readEnum();
                           var15 = var20.getEnumType().findValueByNumber(var6);
                           if (var15 == null) {
                              var1.mergeVarintField(var9, var6);
                              return true;
                           }
                        }
                     } else {
                        if (var12 != null) {
                           var17 = var12.newBuilderForType();
                        } else {
                           var17 = var4.newBuilderForField(var20);
                        }

                        if (!var20.isRepeated()) {
                           mergeOriginalMessage(var4, var5, var20, var17);
                        }

                        var0.readMessage((MessageLite.Builder)var17, var2);
                        var15 = var17.buildPartial();
                     }
                  } else {
                     if (var12 != null) {
                        var17 = var12.newBuilderForType();
                     } else {
                        var17 = var4.newBuilderForField(var20);
                     }

                     if (!var20.isRepeated()) {
                        mergeOriginalMessage(var4, var5, var20, var17);
                     }

                     var0.readGroup(var20.getNumber(), (MessageLite.Builder)var17, var2);
                     var15 = var17.buildPartial();
                  }

                  if (var20.isRepeated()) {
                     addRepeatedField(var4, var5, var20, var15);
                  } else {
                     setField(var4, var5, var20, var15);
                  }
               }

               return true;
            }
         }
      }

      private static void mergeMessageSetExtensionFromBytes(ByteString var0, ExtensionRegistry.ExtensionInfo var1, ExtensionRegistryLite var2, Message.Builder var3, FieldSet var4) throws IOException {
         Descriptors.FieldDescriptor var6 = var1.descriptor;
         boolean var5 = hasOriginalMessage(var3, var4, var6);
         if (!var5 && !ExtensionRegistryLite.isEagerlyParseMessageSets()) {
            LazyField var8 = new LazyField(var1.defaultInstance, var2, var0);
            if (var3 != null) {
               if (var3 instanceof GeneratedMessage.ExtendableBuilder) {
                  var3.setField(var6, var8);
               } else {
                  var3.setField(var6, var8.getValue());
               }
            } else {
               var4.setField(var6, var8);
            }
         } else {
            Message var7;
            if (var5) {
               Message.Builder var9 = getOriginalMessage(var3, var4, var6).toBuilder();
               var9.mergeFrom(var0, var2);
               var7 = var9.buildPartial();
            } else {
               var7 = (Message)var1.defaultInstance.getParserForType().parsePartialFrom(var0, var2);
            }

            setField(var3, var4, var6, var7);
         }
      }

      private static void mergeMessageSetExtensionFromCodedStream(CodedInputStream var0, UnknownFieldSet.Builder var1, ExtensionRegistryLite var2, Descriptors.Descriptor var3, Message.Builder var4, FieldSet var5) throws IOException {
         int var7 = 0;
         ByteString var9 = null;
         ExtensionRegistry.ExtensionInfo var10 = null;

         while(true) {
            int var8 = var0.readTag();
            if (var8 == 0) {
               break;
            }

            int var6;
            ByteString var11;
            ExtensionRegistry.ExtensionInfo var12;
            if (var8 == WireFormat.MESSAGE_SET_TYPE_ID_TAG) {
               var7 = var0.readUInt32();
               var6 = var7;
               var11 = var9;
               var12 = var10;
               if (var7 != 0) {
                  var6 = var7;
                  var11 = var9;
                  var12 = var10;
                  if (var2 instanceof ExtensionRegistry) {
                     var12 = ((ExtensionRegistry)var2).findExtensionByNumber(var3, var7);
                     var6 = var7;
                     var11 = var9;
                  }
               }
            } else if (var8 == WireFormat.MESSAGE_SET_MESSAGE_TAG) {
               if (var7 != 0 && var10 != null && ExtensionRegistryLite.isEagerlyParseMessageSets()) {
                  eagerlyMergeMessageSetExtension(var0, var10, var2, var4, var5);
                  var9 = null;
                  continue;
               }

               var11 = var0.readBytes();
               var6 = var7;
               var12 = var10;
            } else {
               var6 = var7;
               var11 = var9;
               var12 = var10;
               if (!var0.skipField(var8)) {
                  break;
               }
            }

            var7 = var6;
            var9 = var11;
            var10 = var12;
         }

         var0.checkLastTagWas(WireFormat.MESSAGE_SET_ITEM_END_TAG);
         if (var9 != null && var7 != 0) {
            if (var10 != null) {
               mergeMessageSetExtensionFromBytes(var9, var10, var2, var4, var5);
               return;
            }

            if (var9 != null) {
               var1.mergeField(var7, UnknownFieldSet.Field.newBuilder().addLengthDelimited(var9).build());
            }
         }

      }

      private static void mergeOriginalMessage(Message.Builder var0, FieldSet var1, Descriptors.FieldDescriptor var2, Message.Builder var3) {
         Message var4 = getOriginalMessage(var0, var1, var2);
         if (var4 != null) {
            var3.mergeFrom(var4);
         }

      }

      protected static UninitializedMessageException newUninitializedMessageException(Message var0) {
         return new UninitializedMessageException(findMissingFields(var0));
      }

      private static void setField(Message.Builder var0, FieldSet var1, Descriptors.FieldDescriptor var2, Object var3) {
         if (var0 != null) {
            var0.setField(var2, var3);
         } else {
            var1.setField(var2, var3);
         }
      }

      private static String subMessagePrefix(String var0, Descriptors.FieldDescriptor var1, int var2) {
         StringBuilder var3 = new StringBuilder(var0);
         if (var1.isExtension()) {
            var3.append('(');
            var3.append(var1.getFullName());
            var3.append(')');
         } else {
            var3.append(var1.getName());
         }

         if (var2 != -1) {
            var3.append('[');
            var3.append(var2);
            var3.append(']');
         }

         var3.append('.');
         return var3.toString();
      }

      public AbstractMessage.Builder clear() {
         Iterator var1 = this.getAllFields().entrySet().iterator();

         while(var1.hasNext()) {
            this.clearField((Descriptors.FieldDescriptor)((Entry)var1.next()).getKey());
         }

         return this;
      }

      public abstract AbstractMessage.Builder clone();

      public List findInitializationErrors() {
         return findMissingFields(this);
      }

      public Message.Builder getFieldBuilder(Descriptors.FieldDescriptor var1) {
         throw new UnsupportedOperationException("getFieldBuilder() called on an unsupported message type.");
      }

      public String getInitializationErrorString() {
         return AbstractMessage.delimitWithCommas(this.findInitializationErrors());
      }

      public boolean mergeDelimitedFrom(InputStream var1) throws IOException {
         return super.mergeDelimitedFrom(var1);
      }

      public boolean mergeDelimitedFrom(InputStream var1, ExtensionRegistryLite var2) throws IOException {
         return super.mergeDelimitedFrom(var1, var2);
      }

      public AbstractMessage.Builder mergeFrom(ByteString var1) throws InvalidProtocolBufferException {
         return (AbstractMessage.Builder)super.mergeFrom(var1);
      }

      public AbstractMessage.Builder mergeFrom(ByteString var1, ExtensionRegistryLite var2) throws InvalidProtocolBufferException {
         return (AbstractMessage.Builder)super.mergeFrom(var1, var2);
      }

      public AbstractMessage.Builder mergeFrom(CodedInputStream var1) throws IOException {
         return this.mergeFrom((CodedInputStream)var1, ExtensionRegistry.getEmptyRegistry());
      }

      public AbstractMessage.Builder mergeFrom(CodedInputStream var1, ExtensionRegistryLite var2) throws IOException {
         UnknownFieldSet.Builder var4 = UnknownFieldSet.newBuilder(this.getUnknownFields());

         int var3;
         do {
            var3 = var1.readTag();
         } while(var3 != 0 && mergeFieldFrom(var1, var4, var2, this.getDescriptorForType(), this, (FieldSet)null, var3));

         this.setUnknownFields(var4.build());
         return this;
      }

      public AbstractMessage.Builder mergeFrom(Message var1) {
         if (var1.getDescriptorForType() != this.getDescriptorForType()) {
            throw new IllegalArgumentException("mergeFrom(Message) can only merge messages of the same type.");
         } else {
            Iterator var2 = var1.getAllFields().entrySet().iterator();

            while(true) {
               while(var2.hasNext()) {
                  Entry var4 = (Entry)var2.next();
                  Descriptors.FieldDescriptor var3 = (Descriptors.FieldDescriptor)var4.getKey();
                  if (var3.isRepeated()) {
                     Iterator var6 = ((List)var4.getValue()).iterator();

                     while(var6.hasNext()) {
                        this.addRepeatedField(var3, var6.next());
                     }
                  } else if (var3.getJavaType() == Descriptors.FieldDescriptor.JavaType.MESSAGE) {
                     Message var5 = (Message)this.getField(var3);
                     if (var5 == var5.getDefaultInstanceForType()) {
                        this.setField(var3, var4.getValue());
                     } else {
                        this.setField(var3, var5.newBuilderForType().mergeFrom(var5).mergeFrom((Message)var4.getValue()).build());
                     }
                  } else {
                     this.setField(var3, var4.getValue());
                  }
               }

               this.mergeUnknownFields(var1.getUnknownFields());
               return this;
            }
         }
      }

      public AbstractMessage.Builder mergeFrom(InputStream var1) throws IOException {
         return (AbstractMessage.Builder)super.mergeFrom(var1);
      }

      public AbstractMessage.Builder mergeFrom(InputStream var1, ExtensionRegistryLite var2) throws IOException {
         return (AbstractMessage.Builder)super.mergeFrom(var1, var2);
      }

      public AbstractMessage.Builder mergeFrom(byte[] var1) throws InvalidProtocolBufferException {
         return (AbstractMessage.Builder)super.mergeFrom(var1);
      }

      public AbstractMessage.Builder mergeFrom(byte[] var1, int var2, int var3) throws InvalidProtocolBufferException {
         return (AbstractMessage.Builder)super.mergeFrom(var1, var2, var3);
      }

      public AbstractMessage.Builder mergeFrom(byte[] var1, int var2, int var3, ExtensionRegistryLite var4) throws InvalidProtocolBufferException {
         return (AbstractMessage.Builder)super.mergeFrom(var1, var2, var3, var4);
      }

      public AbstractMessage.Builder mergeFrom(byte[] var1, ExtensionRegistryLite var2) throws InvalidProtocolBufferException {
         return (AbstractMessage.Builder)super.mergeFrom(var1, var2);
      }

      public AbstractMessage.Builder mergeUnknownFields(UnknownFieldSet var1) {
         this.setUnknownFields(UnknownFieldSet.newBuilder(this.getUnknownFields()).mergeFrom(var1).build());
         return this;
      }
   }
}
