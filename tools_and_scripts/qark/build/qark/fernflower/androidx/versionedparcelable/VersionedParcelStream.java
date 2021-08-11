package androidx.versionedparcelable;

import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcelable;
import androidx.collection.ArrayMap;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

class VersionedParcelStream extends VersionedParcel {
   private static final int TYPE_BOOLEAN = 5;
   private static final int TYPE_BOOLEAN_ARRAY = 6;
   private static final int TYPE_DOUBLE = 7;
   private static final int TYPE_DOUBLE_ARRAY = 8;
   private static final int TYPE_FLOAT = 13;
   private static final int TYPE_FLOAT_ARRAY = 14;
   private static final int TYPE_INT = 9;
   private static final int TYPE_INT_ARRAY = 10;
   private static final int TYPE_LONG = 11;
   private static final int TYPE_LONG_ARRAY = 12;
   private static final int TYPE_NULL = 0;
   private static final int TYPE_STRING = 3;
   private static final int TYPE_STRING_ARRAY = 4;
   private static final int TYPE_SUB_BUNDLE = 1;
   private static final int TYPE_SUB_PERSISTABLE_BUNDLE = 2;
   private static final Charset UTF_16 = Charset.forName("UTF-16");
   int mCount;
   private DataInputStream mCurrentInput;
   private DataOutputStream mCurrentOutput;
   private VersionedParcelStream.FieldBuffer mFieldBuffer;
   private int mFieldId;
   int mFieldSize;
   private boolean mIgnoreParcelables;
   private final DataInputStream mMasterInput;
   private final DataOutputStream mMasterOutput;

   public VersionedParcelStream(InputStream var1, OutputStream var2) {
      this(var1, var2, new ArrayMap(), new ArrayMap(), new ArrayMap());
   }

   private VersionedParcelStream(InputStream var1, OutputStream var2, ArrayMap var3, ArrayMap var4, ArrayMap var5) {
      super(var3, var4, var5);
      this.mCount = 0;
      this.mFieldId = -1;
      this.mFieldSize = -1;
      var3 = null;
      DataInputStream var6;
      if (var1 != null) {
         var6 = new DataInputStream(new FilterInputStream(var1) {
            public int read() throws IOException {
               if (VersionedParcelStream.this.mFieldSize != -1 && VersionedParcelStream.this.mCount >= VersionedParcelStream.this.mFieldSize) {
                  throw new IOException();
               } else {
                  int var1 = super.read();
                  VersionedParcelStream var2 = VersionedParcelStream.this;
                  ++var2.mCount;
                  return var1;
               }
            }

            public int read(byte[] var1, int var2, int var3) throws IOException {
               if (VersionedParcelStream.this.mFieldSize != -1 && VersionedParcelStream.this.mCount >= VersionedParcelStream.this.mFieldSize) {
                  throw new IOException();
               } else {
                  var2 = super.read(var1, var2, var3);
                  if (var2 > 0) {
                     VersionedParcelStream var4 = VersionedParcelStream.this;
                     var4.mCount += var2;
                  }

                  return var2;
               }
            }

            public long skip(long var1) throws IOException {
               if (VersionedParcelStream.this.mFieldSize != -1 && VersionedParcelStream.this.mCount >= VersionedParcelStream.this.mFieldSize) {
                  throw new IOException();
               } else {
                  var1 = super.skip(var1);
                  if (var1 > 0L) {
                     VersionedParcelStream var3 = VersionedParcelStream.this;
                     var3.mCount += (int)var1;
                  }

                  return var1;
               }
            }
         });
      } else {
         var6 = null;
      }

      this.mMasterInput = var6;
      DataOutputStream var7 = var3;
      if (var2 != null) {
         var7 = new DataOutputStream(var2);
      }

      this.mMasterOutput = var7;
      this.mCurrentInput = this.mMasterInput;
      this.mCurrentOutput = var7;
   }

   private void readObject(int var1, String var2, Bundle var3) {
      switch(var1) {
      case 0:
         var3.putParcelable(var2, (Parcelable)null);
         return;
      case 1:
         var3.putBundle(var2, this.readBundle());
         return;
      case 2:
         var3.putBundle(var2, this.readBundle());
         return;
      case 3:
         var3.putString(var2, this.readString());
         return;
      case 4:
         var3.putStringArray(var2, (String[])this.readArray(new String[0]));
         return;
      case 5:
         var3.putBoolean(var2, this.readBoolean());
         return;
      case 6:
         var3.putBooleanArray(var2, this.readBooleanArray());
         return;
      case 7:
         var3.putDouble(var2, this.readDouble());
         return;
      case 8:
         var3.putDoubleArray(var2, this.readDoubleArray());
         return;
      case 9:
         var3.putInt(var2, this.readInt());
         return;
      case 10:
         var3.putIntArray(var2, this.readIntArray());
         return;
      case 11:
         var3.putLong(var2, this.readLong());
         return;
      case 12:
         var3.putLongArray(var2, this.readLongArray());
         return;
      case 13:
         var3.putFloat(var2, this.readFloat());
         return;
      case 14:
         var3.putFloatArray(var2, this.readFloatArray());
         return;
      default:
         StringBuilder var4 = new StringBuilder();
         var4.append("Unknown type ");
         var4.append(var1);
         throw new RuntimeException(var4.toString());
      }
   }

   private void writeObject(Object var1) {
      if (var1 == null) {
         this.writeInt(0);
      } else if (var1 instanceof Bundle) {
         this.writeInt(1);
         this.writeBundle((Bundle)var1);
      } else if (var1 instanceof String) {
         this.writeInt(3);
         this.writeString((String)var1);
      } else if (var1 instanceof String[]) {
         this.writeInt(4);
         this.writeArray((String[])((String[])var1));
      } else if (var1 instanceof Boolean) {
         this.writeInt(5);
         this.writeBoolean((Boolean)var1);
      } else if (var1 instanceof boolean[]) {
         this.writeInt(6);
         this.writeBooleanArray((boolean[])((boolean[])var1));
      } else if (var1 instanceof Double) {
         this.writeInt(7);
         this.writeDouble((Double)var1);
      } else if (var1 instanceof double[]) {
         this.writeInt(8);
         this.writeDoubleArray((double[])((double[])var1));
      } else if (var1 instanceof Integer) {
         this.writeInt(9);
         this.writeInt((Integer)var1);
      } else if (var1 instanceof int[]) {
         this.writeInt(10);
         this.writeIntArray((int[])((int[])var1));
      } else if (var1 instanceof Long) {
         this.writeInt(11);
         this.writeLong((Long)var1);
      } else if (var1 instanceof long[]) {
         this.writeInt(12);
         this.writeLongArray((long[])((long[])var1));
      } else if (var1 instanceof Float) {
         this.writeInt(13);
         this.writeFloat((Float)var1);
      } else if (var1 instanceof float[]) {
         this.writeInt(14);
         this.writeFloatArray((float[])((float[])var1));
      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append("Unsupported type ");
         var2.append(var1.getClass());
         throw new IllegalArgumentException(var2.toString());
      }
   }

   public void closeField() {
      VersionedParcelStream.FieldBuffer var1 = this.mFieldBuffer;
      if (var1 != null) {
         try {
            if (var1.mOutput.size() != 0) {
               this.mFieldBuffer.flushField();
            }
         } catch (IOException var2) {
            throw new VersionedParcel.ParcelException(var2);
         }

         this.mFieldBuffer = null;
      }
   }

   protected VersionedParcel createSubParcel() {
      return new VersionedParcelStream(this.mCurrentInput, this.mCurrentOutput, this.mReadCache, this.mWriteCache, this.mParcelizerCache);
   }

   public boolean isStream() {
      return true;
   }

   public boolean readBoolean() {
      try {
         boolean var1 = this.mCurrentInput.readBoolean();
         return var1;
      } catch (IOException var3) {
         throw new VersionedParcel.ParcelException(var3);
      }
   }

   public Bundle readBundle() {
      int var2 = this.readInt();
      if (var2 < 0) {
         return null;
      } else {
         Bundle var3 = new Bundle();

         for(int var1 = 0; var1 < var2; ++var1) {
            String var4 = this.readString();
            this.readObject(this.readInt(), var4, var3);
         }

         return var3;
      }
   }

   public byte[] readByteArray() {
      // $FF: Couldn't be decompiled
   }

   protected CharSequence readCharSequence() {
      return null;
   }

   public double readDouble() {
      try {
         double var1 = this.mCurrentInput.readDouble();
         return var1;
      } catch (IOException var4) {
         throw new VersionedParcel.ParcelException(var4);
      }
   }

   public boolean readField(int var1) {
      while(true) {
         boolean var10001;
         try {
            if (this.mFieldId == var1) {
               return true;
            }
         } catch (IOException var11) {
            var10001 = false;
            break;
         }

         try {
            if (String.valueOf(this.mFieldId).compareTo(String.valueOf(var1)) > 0) {
               return false;
            }
         } catch (IOException var10) {
            var10001 = false;
            break;
         }

         try {
            if (this.mCount < this.mFieldSize) {
               this.mMasterInput.skip((long)(this.mFieldSize - this.mCount));
            }
         } catch (IOException var9) {
            var10001 = false;
            break;
         }

         int var4;
         try {
            this.mFieldSize = -1;
            var4 = this.mMasterInput.readInt();
            this.mCount = 0;
         } catch (IOException var8) {
            var10001 = false;
            break;
         }

         int var3 = var4 & '\uffff';
         int var2 = var3;
         if (var3 == 65535) {
            try {
               var2 = this.mMasterInput.readInt();
            } catch (IOException var7) {
               var10001 = false;
               break;
            }
         }

         try {
            this.mFieldId = '\uffff' & var4 >> 16;
            this.mFieldSize = var2;
         } catch (IOException var6) {
            var10001 = false;
            break;
         }
      }

      return false;
   }

   public float readFloat() {
      try {
         float var1 = this.mCurrentInput.readFloat();
         return var1;
      } catch (IOException var3) {
         throw new VersionedParcel.ParcelException(var3);
      }
   }

   public int readInt() {
      try {
         int var1 = this.mCurrentInput.readInt();
         return var1;
      } catch (IOException var3) {
         throw new VersionedParcel.ParcelException(var3);
      }
   }

   public long readLong() {
      try {
         long var1 = this.mCurrentInput.readLong();
         return var1;
      } catch (IOException var4) {
         throw new VersionedParcel.ParcelException(var4);
      }
   }

   public Parcelable readParcelable() {
      return null;
   }

   public String readString() {
      // $FF: Couldn't be decompiled
   }

   public IBinder readStrongBinder() {
      return null;
   }

   public void setOutputField(int var1) {
      this.closeField();
      VersionedParcelStream.FieldBuffer var2 = new VersionedParcelStream.FieldBuffer(var1, this.mMasterOutput);
      this.mFieldBuffer = var2;
      this.mCurrentOutput = var2.mDataStream;
   }

   public void setSerializationFlags(boolean var1, boolean var2) {
      if (var1) {
         this.mIgnoreParcelables = var2;
      } else {
         throw new RuntimeException("Serialization of this object is not allowed");
      }
   }

   public void writeBoolean(boolean var1) {
      try {
         this.mCurrentOutput.writeBoolean(var1);
      } catch (IOException var3) {
         throw new VersionedParcel.ParcelException(var3);
      }
   }

   public void writeBundle(Bundle var1) {
      IOException var10000;
      boolean var10001;
      if (var1 != null) {
         label30: {
            Iterator var8;
            try {
               Set var2 = var1.keySet();
               this.mCurrentOutput.writeInt(var2.size());
               var8 = var2.iterator();
            } catch (IOException var5) {
               var10000 = var5;
               var10001 = false;
               break label30;
            }

            while(true) {
               try {
                  if (!var8.hasNext()) {
                     return;
                  }

                  String var3 = (String)var8.next();
                  this.writeString(var3);
                  this.writeObject(var1.get(var3));
               } catch (IOException var4) {
                  var10000 = var4;
                  var10001 = false;
                  break;
               }
            }
         }
      } else {
         try {
            this.mCurrentOutput.writeInt(-1);
            return;
         } catch (IOException var6) {
            var10000 = var6;
            var10001 = false;
         }
      }

      IOException var7 = var10000;
      throw new VersionedParcel.ParcelException(var7);
   }

   public void writeByteArray(byte[] var1) {
      IOException var10000;
      boolean var10001;
      if (var1 != null) {
         try {
            this.mCurrentOutput.writeInt(var1.length);
            this.mCurrentOutput.write(var1);
            return;
         } catch (IOException var2) {
            var10000 = var2;
            var10001 = false;
         }
      } else {
         try {
            this.mCurrentOutput.writeInt(-1);
            return;
         } catch (IOException var3) {
            var10000 = var3;
            var10001 = false;
         }
      }

      IOException var4 = var10000;
      throw new VersionedParcel.ParcelException(var4);
   }

   public void writeByteArray(byte[] var1, int var2, int var3) {
      IOException var10000;
      boolean var10001;
      if (var1 != null) {
         try {
            this.mCurrentOutput.writeInt(var3);
            this.mCurrentOutput.write(var1, var2, var3);
            return;
         } catch (IOException var4) {
            var10000 = var4;
            var10001 = false;
         }
      } else {
         try {
            this.mCurrentOutput.writeInt(-1);
            return;
         } catch (IOException var5) {
            var10000 = var5;
            var10001 = false;
         }
      }

      IOException var6 = var10000;
      throw new VersionedParcel.ParcelException(var6);
   }

   protected void writeCharSequence(CharSequence var1) {
      if (!this.mIgnoreParcelables) {
         throw new RuntimeException("CharSequence cannot be written to an OutputStream");
      }
   }

   public void writeDouble(double var1) {
      try {
         this.mCurrentOutput.writeDouble(var1);
      } catch (IOException var4) {
         throw new VersionedParcel.ParcelException(var4);
      }
   }

   public void writeFloat(float var1) {
      try {
         this.mCurrentOutput.writeFloat(var1);
      } catch (IOException var3) {
         throw new VersionedParcel.ParcelException(var3);
      }
   }

   public void writeInt(int var1) {
      try {
         this.mCurrentOutput.writeInt(var1);
      } catch (IOException var3) {
         throw new VersionedParcel.ParcelException(var3);
      }
   }

   public void writeLong(long var1) {
      try {
         this.mCurrentOutput.writeLong(var1);
      } catch (IOException var4) {
         throw new VersionedParcel.ParcelException(var4);
      }
   }

   public void writeParcelable(Parcelable var1) {
      if (!this.mIgnoreParcelables) {
         throw new RuntimeException("Parcelables cannot be written to an OutputStream");
      }
   }

   public void writeString(String var1) {
      IOException var10000;
      boolean var10001;
      if (var1 != null) {
         try {
            byte[] var4 = var1.getBytes(UTF_16);
            this.mCurrentOutput.writeInt(var4.length);
            this.mCurrentOutput.write(var4);
            return;
         } catch (IOException var2) {
            var10000 = var2;
            var10001 = false;
         }
      } else {
         try {
            this.mCurrentOutput.writeInt(-1);
            return;
         } catch (IOException var3) {
            var10000 = var3;
            var10001 = false;
         }
      }

      IOException var5 = var10000;
      throw new VersionedParcel.ParcelException(var5);
   }

   public void writeStrongBinder(IBinder var1) {
      if (!this.mIgnoreParcelables) {
         throw new RuntimeException("Binders cannot be written to an OutputStream");
      }
   }

   public void writeStrongInterface(IInterface var1) {
      if (!this.mIgnoreParcelables) {
         throw new RuntimeException("Binders cannot be written to an OutputStream");
      }
   }

   private static class FieldBuffer {
      final DataOutputStream mDataStream;
      private final int mFieldId;
      final ByteArrayOutputStream mOutput = new ByteArrayOutputStream();
      private final DataOutputStream mTarget;

      FieldBuffer(int var1, DataOutputStream var2) {
         this.mDataStream = new DataOutputStream(this.mOutput);
         this.mFieldId = var1;
         this.mTarget = var2;
      }

      void flushField() throws IOException {
         this.mDataStream.flush();
         int var2 = this.mOutput.size();
         int var3 = this.mFieldId;
         int var1;
         if (var2 >= 65535) {
            var1 = 65535;
         } else {
            var1 = var2;
         }

         this.mTarget.writeInt(var3 << 16 | var1);
         if (var2 >= 65535) {
            this.mTarget.writeInt(var2);
         }

         this.mOutput.writeTo(this.mTarget);
      }
   }
}
