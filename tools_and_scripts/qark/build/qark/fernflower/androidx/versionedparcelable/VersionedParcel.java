package androidx.versionedparcelable;

import android.os.BadParcelableException;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.NetworkOnMainThreadException;
import android.os.Parcelable;
import android.util.Size;
import android.util.SizeF;
import android.util.SparseBooleanArray;
import androidx.collection.ArrayMap;
import androidx.collection.ArraySet;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public abstract class VersionedParcel {
   private static final int EX_BAD_PARCELABLE = -2;
   private static final int EX_ILLEGAL_ARGUMENT = -3;
   private static final int EX_ILLEGAL_STATE = -5;
   private static final int EX_NETWORK_MAIN_THREAD = -6;
   private static final int EX_NULL_POINTER = -4;
   private static final int EX_PARCELABLE = -9;
   private static final int EX_SECURITY = -1;
   private static final int EX_UNSUPPORTED_OPERATION = -7;
   private static final String TAG = "VersionedParcel";
   private static final int TYPE_BINDER = 5;
   private static final int TYPE_FLOAT = 8;
   private static final int TYPE_INTEGER = 7;
   private static final int TYPE_PARCELABLE = 2;
   private static final int TYPE_SERIALIZABLE = 3;
   private static final int TYPE_STRING = 4;
   private static final int TYPE_VERSIONED_PARCELABLE = 1;
   protected final ArrayMap mParcelizerCache;
   protected final ArrayMap mReadCache;
   protected final ArrayMap mWriteCache;

   public VersionedParcel(ArrayMap var1, ArrayMap var2, ArrayMap var3) {
      this.mReadCache = var1;
      this.mWriteCache = var2;
      this.mParcelizerCache = var3;
   }

   private Exception createException(int var1, String var2) {
      switch(var1) {
      case -9:
         return (Exception)this.readParcelable();
      case -8:
      default:
         StringBuilder var3 = new StringBuilder();
         var3.append("Unknown exception code: ");
         var3.append(var1);
         var3.append(" msg ");
         var3.append(var2);
         return new RuntimeException(var3.toString());
      case -7:
         return new UnsupportedOperationException(var2);
      case -6:
         return new NetworkOnMainThreadException();
      case -5:
         return new IllegalStateException(var2);
      case -4:
         return new NullPointerException(var2);
      case -3:
         return new IllegalArgumentException(var2);
      case -2:
         return new BadParcelableException(var2);
      case -1:
         return new SecurityException(var2);
      }
   }

   private Class findParcelClass(Class var1) throws ClassNotFoundException {
      Class var3 = (Class)this.mParcelizerCache.get(var1.getName());
      Class var2 = var3;
      if (var3 == null) {
         var2 = Class.forName(String.format("%s.%sParcelizer", var1.getPackage().getName(), var1.getSimpleName()), false, var1.getClassLoader());
         this.mParcelizerCache.put(var1.getName(), var2);
      }

      return var2;
   }

   private Method getReadMethod(String var1) throws IllegalAccessException, NoSuchMethodException, ClassNotFoundException {
      Method var3 = (Method)this.mReadCache.get(var1);
      Method var2 = var3;
      if (var3 == null) {
         System.currentTimeMillis();
         var2 = Class.forName(var1, true, VersionedParcel.class.getClassLoader()).getDeclaredMethod("read", VersionedParcel.class);
         this.mReadCache.put(var1, var2);
      }

      return var2;
   }

   protected static Throwable getRootCause(Throwable var0) {
      while(var0.getCause() != null) {
         var0 = var0.getCause();
      }

      return var0;
   }

   private int getType(Object var1) {
      if (var1 instanceof String) {
         return 4;
      } else if (var1 instanceof Parcelable) {
         return 2;
      } else if (var1 instanceof VersionedParcelable) {
         return 1;
      } else if (var1 instanceof Serializable) {
         return 3;
      } else if (var1 instanceof IBinder) {
         return 5;
      } else if (var1 instanceof Integer) {
         return 7;
      } else if (var1 instanceof Float) {
         return 8;
      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append(var1.getClass().getName());
         var2.append(" cannot be VersionedParcelled");
         throw new IllegalArgumentException(var2.toString());
      }
   }

   private Method getWriteMethod(Class var1) throws IllegalAccessException, NoSuchMethodException, ClassNotFoundException {
      Method var3 = (Method)this.mWriteCache.get(var1.getName());
      Method var2 = var3;
      if (var3 == null) {
         Class var4 = this.findParcelClass(var1);
         System.currentTimeMillis();
         var2 = var4.getDeclaredMethod("write", var1, VersionedParcel.class);
         this.mWriteCache.put(var1.getName(), var2);
      }

      return var2;
   }

   private Collection readCollection(Collection var1) {
      int var2 = this.readInt();
      if (var2 < 0) {
         return null;
      } else {
         if (var2 != 0) {
            int var4 = this.readInt();
            if (var2 < 0) {
               return null;
            }

            int var3 = var2;
            if (var4 != 1) {
               var3 = var2;
               if (var4 == 2) {
                  while(var3 > 0) {
                     var1.add(this.readParcelable());
                     --var3;
                  }
               } else {
                  var3 = var2;
                  if (var4 == 3) {
                     while(var3 > 0) {
                        var1.add(this.readSerializable());
                        --var3;
                     }
                  } else {
                     var3 = var2;
                     if (var4 == 4) {
                        while(var3 > 0) {
                           var1.add(this.readString());
                           --var3;
                        }
                     } else {
                        if (var4 != 5) {
                           return var1;
                        }

                        while(var2 > 0) {
                           var1.add(this.readStrongBinder());
                           --var2;
                        }
                     }
                  }
               }
            } else {
               while(var3 > 0) {
                  var1.add(this.readVersionedParcelable());
                  --var3;
               }
            }
         }

         return var1;
      }
   }

   private Exception readException(int var1, String var2) {
      return this.createException(var1, var2);
   }

   private int readExceptionCode() {
      return this.readInt();
   }

   private void writeCollection(Collection var1) {
      if (var1 == null) {
         this.writeInt(-1);
      } else {
         int var2 = var1.size();
         this.writeInt(var2);
         if (var2 > 0) {
            var2 = this.getType(var1.iterator().next());
            this.writeInt(var2);
            Iterator var3;
            switch(var2) {
            case 1:
               var3 = var1.iterator();

               while(var3.hasNext()) {
                  this.writeVersionedParcelable((VersionedParcelable)var3.next());
               }

               return;
            case 2:
               var3 = var1.iterator();

               while(var3.hasNext()) {
                  this.writeParcelable((Parcelable)var3.next());
               }

               return;
            case 3:
               var3 = var1.iterator();

               while(var3.hasNext()) {
                  this.writeSerializable((Serializable)var3.next());
               }

               return;
            case 4:
               var3 = var1.iterator();

               while(var3.hasNext()) {
                  this.writeString((String)var3.next());
               }

               return;
            case 5:
               var3 = var1.iterator();

               while(var3.hasNext()) {
                  this.writeStrongBinder((IBinder)var3.next());
               }

               return;
            case 6:
            default:
               return;
            case 7:
               var3 = var1.iterator();

               while(var3.hasNext()) {
                  this.writeInt((Integer)var3.next());
               }

               return;
            case 8:
               var3 = var1.iterator();

               while(var3.hasNext()) {
                  this.writeFloat((Float)var3.next());
               }
            }
         }

      }
   }

   private void writeCollection(Collection var1, int var2) {
      this.setOutputField(var2);
      this.writeCollection(var1);
   }

   private void writeSerializable(Serializable var1) {
      if (var1 == null) {
         this.writeString((String)null);
      } else {
         String var2 = var1.getClass().getName();
         this.writeString(var2);
         ByteArrayOutputStream var3 = new ByteArrayOutputStream();

         try {
            ObjectOutputStream var4 = new ObjectOutputStream(var3);
            var4.writeObject(var1);
            var4.close();
            this.writeByteArray(var3.toByteArray());
         } catch (IOException var5) {
            StringBuilder var6 = new StringBuilder();
            var6.append("VersionedParcelable encountered IOException writing serializable object (name = ");
            var6.append(var2);
            var6.append(")");
            throw new RuntimeException(var6.toString(), var5);
         }
      }
   }

   private void writeVersionedParcelableCreator(VersionedParcelable var1) {
      Class var2;
      try {
         var2 = this.findParcelClass(var1.getClass());
      } catch (ClassNotFoundException var4) {
         StringBuilder var3 = new StringBuilder();
         var3.append(var1.getClass().getSimpleName());
         var3.append(" does not have a Parcelizer");
         throw new RuntimeException(var3.toString(), var4);
      }

      this.writeString(var2.getName());
   }

   protected abstract void closeField();

   protected abstract VersionedParcel createSubParcel();

   public boolean isStream() {
      return false;
   }

   protected Object[] readArray(Object[] var1) {
      int var2 = this.readInt();
      if (var2 < 0) {
         return null;
      } else {
         ArrayList var5 = new ArrayList(var2);
         if (var2 != 0) {
            int var4 = this.readInt();
            if (var2 < 0) {
               return null;
            }

            int var3 = var2;
            if (var4 != 1) {
               var3 = var2;
               if (var4 == 2) {
                  while(var3 > 0) {
                     var5.add(this.readParcelable());
                     --var3;
                  }
               } else {
                  var3 = var2;
                  if (var4 == 3) {
                     while(var3 > 0) {
                        var5.add(this.readSerializable());
                        --var3;
                     }
                  } else {
                     var3 = var2;
                     if (var4 == 4) {
                        while(var3 > 0) {
                           var5.add(this.readString());
                           --var3;
                        }
                     } else if (var4 == 5) {
                        while(var2 > 0) {
                           var5.add(this.readStrongBinder());
                           --var2;
                        }
                     }
                  }
               }
            } else {
               while(var3 > 0) {
                  var5.add(this.readVersionedParcelable());
                  --var3;
               }
            }
         }

         return var5.toArray(var1);
      }
   }

   public Object[] readArray(Object[] var1, int var2) {
      return !this.readField(var2) ? var1 : this.readArray(var1);
   }

   protected abstract boolean readBoolean();

   public boolean readBoolean(boolean var1, int var2) {
      return !this.readField(var2) ? var1 : this.readBoolean();
   }

   protected boolean[] readBooleanArray() {
      int var2 = this.readInt();
      if (var2 < 0) {
         return null;
      } else {
         boolean[] var4 = new boolean[var2];

         for(int var1 = 0; var1 < var2; ++var1) {
            boolean var3;
            if (this.readInt() != 0) {
               var3 = true;
            } else {
               var3 = false;
            }

            var4[var1] = var3;
         }

         return var4;
      }
   }

   public boolean[] readBooleanArray(boolean[] var1, int var2) {
      return !this.readField(var2) ? var1 : this.readBooleanArray();
   }

   protected abstract Bundle readBundle();

   public Bundle readBundle(Bundle var1, int var2) {
      return !this.readField(var2) ? var1 : this.readBundle();
   }

   public byte readByte(byte var1, int var2) {
      return !this.readField(var2) ? var1 : (byte)(this.readInt() & 255);
   }

   protected abstract byte[] readByteArray();

   public byte[] readByteArray(byte[] var1, int var2) {
      return !this.readField(var2) ? var1 : this.readByteArray();
   }

   public char[] readCharArray(char[] var1, int var2) {
      if (!this.readField(var2)) {
         return var1;
      } else {
         int var3 = this.readInt();
         if (var3 < 0) {
            return null;
         } else {
            var1 = new char[var3];

            for(var2 = 0; var2 < var3; ++var2) {
               var1[var2] = (char)this.readInt();
            }

            return var1;
         }
      }
   }

   protected abstract CharSequence readCharSequence();

   public CharSequence readCharSequence(CharSequence var1, int var2) {
      return !this.readField(var2) ? var1 : this.readCharSequence();
   }

   protected abstract double readDouble();

   public double readDouble(double var1, int var3) {
      return !this.readField(var3) ? var1 : this.readDouble();
   }

   protected double[] readDoubleArray() {
      int var2 = this.readInt();
      if (var2 < 0) {
         return null;
      } else {
         double[] var3 = new double[var2];

         for(int var1 = 0; var1 < var2; ++var1) {
            var3[var1] = this.readDouble();
         }

         return var3;
      }
   }

   public double[] readDoubleArray(double[] var1, int var2) {
      return !this.readField(var2) ? var1 : this.readDoubleArray();
   }

   public Exception readException(Exception var1, int var2) {
      if (!this.readField(var2)) {
         return var1;
      } else {
         var2 = this.readExceptionCode();
         return var2 != 0 ? this.readException(var2, this.readString()) : var1;
      }
   }

   protected abstract boolean readField(int var1);

   protected abstract float readFloat();

   public float readFloat(float var1, int var2) {
      return !this.readField(var2) ? var1 : this.readFloat();
   }

   protected float[] readFloatArray() {
      int var2 = this.readInt();
      if (var2 < 0) {
         return null;
      } else {
         float[] var3 = new float[var2];

         for(int var1 = 0; var1 < var2; ++var1) {
            var3[var1] = this.readFloat();
         }

         return var3;
      }
   }

   public float[] readFloatArray(float[] var1, int var2) {
      return !this.readField(var2) ? var1 : this.readFloatArray();
   }

   protected VersionedParcelable readFromParcel(String var1, VersionedParcel var2) {
      try {
         VersionedParcelable var7 = (VersionedParcelable)this.getReadMethod(var1).invoke((Object)null, var2);
         return var7;
      } catch (IllegalAccessException var3) {
         throw new RuntimeException("VersionedParcel encountered IllegalAccessException", var3);
      } catch (InvocationTargetException var4) {
         if (var4.getCause() instanceof RuntimeException) {
            throw (RuntimeException)var4.getCause();
         } else {
            throw new RuntimeException("VersionedParcel encountered InvocationTargetException", var4);
         }
      } catch (NoSuchMethodException var5) {
         throw new RuntimeException("VersionedParcel encountered NoSuchMethodException", var5);
      } catch (ClassNotFoundException var6) {
         throw new RuntimeException("VersionedParcel encountered ClassNotFoundException", var6);
      }
   }

   protected abstract int readInt();

   public int readInt(int var1, int var2) {
      return !this.readField(var2) ? var1 : this.readInt();
   }

   protected int[] readIntArray() {
      int var2 = this.readInt();
      if (var2 < 0) {
         return null;
      } else {
         int[] var3 = new int[var2];

         for(int var1 = 0; var1 < var2; ++var1) {
            var3[var1] = this.readInt();
         }

         return var3;
      }
   }

   public int[] readIntArray(int[] var1, int var2) {
      return !this.readField(var2) ? var1 : this.readIntArray();
   }

   public List readList(List var1, int var2) {
      return !this.readField(var2) ? var1 : (List)this.readCollection(new ArrayList());
   }

   protected abstract long readLong();

   public long readLong(long var1, int var3) {
      return !this.readField(var3) ? var1 : this.readLong();
   }

   protected long[] readLongArray() {
      int var2 = this.readInt();
      if (var2 < 0) {
         return null;
      } else {
         long[] var3 = new long[var2];

         for(int var1 = 0; var1 < var2; ++var1) {
            var3[var1] = this.readLong();
         }

         return var3;
      }
   }

   public long[] readLongArray(long[] var1, int var2) {
      return !this.readField(var2) ? var1 : this.readLongArray();
   }

   public Map readMap(Map var1, int var2) {
      if (!this.readField(var2)) {
         return var1;
      } else {
         int var3 = this.readInt();
         if (var3 < 0) {
            return null;
         } else {
            ArrayMap var6 = new ArrayMap();
            if (var3 == 0) {
               return var6;
            } else {
               ArrayList var4 = new ArrayList();
               ArrayList var5 = new ArrayList();
               this.readCollection(var4);
               this.readCollection(var5);

               for(var2 = 0; var2 < var3; ++var2) {
                  var6.put(var4.get(var2), var5.get(var2));
               }

               return var6;
            }
         }
      }
   }

   protected abstract Parcelable readParcelable();

   public Parcelable readParcelable(Parcelable var1, int var2) {
      return !this.readField(var2) ? var1 : this.readParcelable();
   }

   protected Serializable readSerializable() {
      String var1 = this.readString();
      if (var1 == null) {
         return null;
      } else {
         ByteArrayInputStream var2 = new ByteArrayInputStream(this.readByteArray());

         StringBuilder var3;
         try {
            Serializable var6 = (Serializable)(new ObjectInputStream(var2) {
               protected Class resolveClass(ObjectStreamClass var1) throws IOException, ClassNotFoundException {
                  Class var2 = Class.forName(var1.getName(), false, this.getClass().getClassLoader());
                  return var2 != null ? var2 : super.resolveClass(var1);
               }
            }).readObject();
            return var6;
         } catch (IOException var4) {
            var3 = new StringBuilder();
            var3.append("VersionedParcelable encountered IOException reading a Serializable object (name = ");
            var3.append(var1);
            var3.append(")");
            throw new RuntimeException(var3.toString(), var4);
         } catch (ClassNotFoundException var5) {
            var3 = new StringBuilder();
            var3.append("VersionedParcelable encountered ClassNotFoundException reading a Serializable object (name = ");
            var3.append(var1);
            var3.append(")");
            throw new RuntimeException(var3.toString(), var5);
         }
      }
   }

   public Set readSet(Set var1, int var2) {
      return !this.readField(var2) ? var1 : (Set)this.readCollection(new ArraySet());
   }

   public Size readSize(Size var1, int var2) {
      if (!this.readField(var2)) {
         return var1;
      } else {
         return this.readBoolean() ? new Size(this.readInt(), this.readInt()) : null;
      }
   }

   public SizeF readSizeF(SizeF var1, int var2) {
      if (!this.readField(var2)) {
         return var1;
      } else {
         return this.readBoolean() ? new SizeF(this.readFloat(), this.readFloat()) : null;
      }
   }

   public SparseBooleanArray readSparseBooleanArray(SparseBooleanArray var1, int var2) {
      if (!this.readField(var2)) {
         return var1;
      } else {
         int var3 = this.readInt();
         if (var3 < 0) {
            return null;
         } else {
            var1 = new SparseBooleanArray(var3);

            for(var2 = 0; var2 < var3; ++var2) {
               var1.put(this.readInt(), this.readBoolean());
            }

            return var1;
         }
      }
   }

   protected abstract String readString();

   public String readString(String var1, int var2) {
      return !this.readField(var2) ? var1 : this.readString();
   }

   protected abstract IBinder readStrongBinder();

   public IBinder readStrongBinder(IBinder var1, int var2) {
      return !this.readField(var2) ? var1 : this.readStrongBinder();
   }

   protected VersionedParcelable readVersionedParcelable() {
      String var1 = this.readString();
      return var1 == null ? null : this.readFromParcel(var1, this.createSubParcel());
   }

   public VersionedParcelable readVersionedParcelable(VersionedParcelable var1, int var2) {
      return !this.readField(var2) ? var1 : this.readVersionedParcelable();
   }

   protected abstract void setOutputField(int var1);

   public void setSerializationFlags(boolean var1, boolean var2) {
   }

   protected void writeArray(Object[] var1) {
      if (var1 == null) {
         this.writeInt(-1);
      } else {
         int var7 = var1.length;
         byte var4 = 0;
         byte var5 = 0;
         byte var6 = 0;
         int var2 = 0;
         byte var3 = 0;
         this.writeInt(var7);
         if (var7 > 0) {
            int var8 = this.getType(var1[0]);
            this.writeInt(var8);
            if (var8 != 1) {
               var2 = var6;
               if (var8 == 2) {
                  while(var2 < var7) {
                     this.writeParcelable((Parcelable)var1[var2]);
                     ++var2;
                  }
               } else {
                  var2 = var5;
                  if (var8 == 3) {
                     while(var2 < var7) {
                        this.writeSerializable((Serializable)var1[var2]);
                        ++var2;
                     }
                  } else {
                     var2 = var4;
                     if (var8 == 4) {
                        while(var2 < var7) {
                           this.writeString((String)var1[var2]);
                           ++var2;
                        }
                     } else {
                        var2 = var3;
                        if (var8 != 5) {
                           return;
                        }

                        while(var2 < var7) {
                           this.writeStrongBinder((IBinder)var1[var2]);
                           ++var2;
                        }
                     }
                  }
               }
            } else {
               while(var2 < var7) {
                  this.writeVersionedParcelable((VersionedParcelable)var1[var2]);
                  ++var2;
               }
            }
         }

      }
   }

   public void writeArray(Object[] var1, int var2) {
      this.setOutputField(var2);
      this.writeArray(var1);
   }

   protected abstract void writeBoolean(boolean var1);

   public void writeBoolean(boolean var1, int var2) {
      this.setOutputField(var2);
      this.writeBoolean(var1);
   }

   protected void writeBooleanArray(boolean[] var1) {
      throw new RuntimeException("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.provideAs(TypeTransformer.java:780)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.e2expr(TypeTransformer.java:553)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:716)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.enexpr(TypeTransformer.java:698)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:719)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.s1stmt(TypeTransformer.java:810)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.sxStmt(TypeTransformer.java:840)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:206)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\n");
   }

   public void writeBooleanArray(boolean[] var1, int var2) {
      this.setOutputField(var2);
      this.writeBooleanArray(var1);
   }

   protected abstract void writeBundle(Bundle var1);

   public void writeBundle(Bundle var1, int var2) {
      this.setOutputField(var2);
      this.writeBundle(var1);
   }

   public void writeByte(byte var1, int var2) {
      this.setOutputField(var2);
      this.writeInt(var1);
   }

   protected abstract void writeByteArray(byte[] var1);

   public void writeByteArray(byte[] var1, int var2) {
      this.setOutputField(var2);
      this.writeByteArray(var1);
   }

   protected abstract void writeByteArray(byte[] var1, int var2, int var3);

   public void writeByteArray(byte[] var1, int var2, int var3, int var4) {
      this.setOutputField(var4);
      this.writeByteArray(var1, var2, var3);
   }

   public void writeCharArray(char[] var1, int var2) {
      this.setOutputField(var2);
      if (var1 == null) {
         this.writeInt(-1);
      } else {
         int var3 = var1.length;
         this.writeInt(var3);

         for(var2 = 0; var2 < var3; ++var2) {
            this.writeInt(var1[var2]);
         }

      }
   }

   protected abstract void writeCharSequence(CharSequence var1);

   public void writeCharSequence(CharSequence var1, int var2) {
      this.setOutputField(var2);
      this.writeCharSequence(var1);
   }

   protected abstract void writeDouble(double var1);

   public void writeDouble(double var1, int var3) {
      this.setOutputField(var3);
      this.writeDouble(var1);
   }

   protected void writeDoubleArray(double[] var1) {
      if (var1 == null) {
         this.writeInt(-1);
      } else {
         int var3 = var1.length;
         this.writeInt(var3);

         for(int var2 = 0; var2 < var3; ++var2) {
            this.writeDouble(var1[var2]);
         }

      }
   }

   public void writeDoubleArray(double[] var1, int var2) {
      this.setOutputField(var2);
      this.writeDoubleArray(var1);
   }

   public void writeException(Exception var1, int var2) {
      this.setOutputField(var2);
      if (var1 == null) {
         this.writeNoException();
      } else {
         byte var3 = 0;
         if (var1 instanceof Parcelable && var1.getClass().getClassLoader() == Parcelable.class.getClassLoader()) {
            var3 = -9;
         } else if (var1 instanceof SecurityException) {
            var3 = -1;
         } else if (var1 instanceof BadParcelableException) {
            var3 = -2;
         } else if (var1 instanceof IllegalArgumentException) {
            var3 = -3;
         } else if (var1 instanceof NullPointerException) {
            var3 = -4;
         } else if (var1 instanceof IllegalStateException) {
            var3 = -5;
         } else if (var1 instanceof NetworkOnMainThreadException) {
            var3 = -6;
         } else if (var1 instanceof UnsupportedOperationException) {
            var3 = -7;
         }

         this.writeInt(var3);
         if (var3 == 0) {
            if (var1 instanceof RuntimeException) {
               throw (RuntimeException)var1;
            } else {
               throw new RuntimeException(var1);
            }
         } else {
            this.writeString(var1.getMessage());
            if (var3 == -9) {
               this.writeParcelable((Parcelable)var1);
            }
         }
      }
   }

   protected abstract void writeFloat(float var1);

   public void writeFloat(float var1, int var2) {
      this.setOutputField(var2);
      this.writeFloat(var1);
   }

   protected void writeFloatArray(float[] var1) {
      if (var1 == null) {
         this.writeInt(-1);
      } else {
         int var3 = var1.length;
         this.writeInt(var3);

         for(int var2 = 0; var2 < var3; ++var2) {
            this.writeFloat(var1[var2]);
         }

      }
   }

   public void writeFloatArray(float[] var1, int var2) {
      this.setOutputField(var2);
      this.writeFloatArray(var1);
   }

   protected abstract void writeInt(int var1);

   public void writeInt(int var1, int var2) {
      this.setOutputField(var2);
      this.writeInt(var1);
   }

   protected void writeIntArray(int[] var1) {
      if (var1 == null) {
         this.writeInt(-1);
      } else {
         int var3 = var1.length;
         this.writeInt(var3);

         for(int var2 = 0; var2 < var3; ++var2) {
            this.writeInt(var1[var2]);
         }

      }
   }

   public void writeIntArray(int[] var1, int var2) {
      this.setOutputField(var2);
      this.writeIntArray(var1);
   }

   public void writeList(List var1, int var2) {
      this.writeCollection(var1, var2);
   }

   protected abstract void writeLong(long var1);

   public void writeLong(long var1, int var3) {
      this.setOutputField(var3);
      this.writeLong(var1);
   }

   protected void writeLongArray(long[] var1) {
      if (var1 == null) {
         this.writeInt(-1);
      } else {
         int var3 = var1.length;
         this.writeInt(var3);

         for(int var2 = 0; var2 < var3; ++var2) {
            this.writeLong(var1[var2]);
         }

      }
   }

   public void writeLongArray(long[] var1, int var2) {
      this.setOutputField(var2);
      this.writeLongArray(var1);
   }

   public void writeMap(Map var1, int var2) {
      this.setOutputField(var2);
      if (var1 == null) {
         this.writeInt(-1);
      } else {
         var2 = var1.size();
         this.writeInt(var2);
         if (var2 != 0) {
            ArrayList var3 = new ArrayList();
            ArrayList var4 = new ArrayList();
            Iterator var6 = var1.entrySet().iterator();

            while(var6.hasNext()) {
               Entry var5 = (Entry)var6.next();
               var3.add(var5.getKey());
               var4.add(var5.getValue());
            }

            this.writeCollection(var3);
            this.writeCollection(var4);
         }
      }
   }

   protected void writeNoException() {
      this.writeInt(0);
   }

   protected abstract void writeParcelable(Parcelable var1);

   public void writeParcelable(Parcelable var1, int var2) {
      this.setOutputField(var2);
      this.writeParcelable(var1);
   }

   public void writeSerializable(Serializable var1, int var2) {
      this.setOutputField(var2);
      this.writeSerializable(var1);
   }

   public void writeSet(Set var1, int var2) {
      this.writeCollection(var1, var2);
   }

   public void writeSize(Size var1, int var2) {
      this.setOutputField(var2);
      boolean var3;
      if (var1 != null) {
         var3 = true;
      } else {
         var3 = false;
      }

      this.writeBoolean(var3);
      if (var1 != null) {
         this.writeInt(var1.getWidth());
         this.writeInt(var1.getHeight());
      }

   }

   public void writeSizeF(SizeF var1, int var2) {
      this.setOutputField(var2);
      boolean var3;
      if (var1 != null) {
         var3 = true;
      } else {
         var3 = false;
      }

      this.writeBoolean(var3);
      if (var1 != null) {
         this.writeFloat(var1.getWidth());
         this.writeFloat(var1.getHeight());
      }

   }

   public void writeSparseBooleanArray(SparseBooleanArray var1, int var2) {
      this.setOutputField(var2);
      if (var1 == null) {
         this.writeInt(-1);
      } else {
         int var3 = var1.size();
         this.writeInt(var3);

         for(var2 = 0; var2 < var3; ++var2) {
            this.writeInt(var1.keyAt(var2));
            this.writeBoolean(var1.valueAt(var2));
         }

      }
   }

   protected abstract void writeString(String var1);

   public void writeString(String var1, int var2) {
      this.setOutputField(var2);
      this.writeString(var1);
   }

   protected abstract void writeStrongBinder(IBinder var1);

   public void writeStrongBinder(IBinder var1, int var2) {
      this.setOutputField(var2);
      this.writeStrongBinder(var1);
   }

   protected abstract void writeStrongInterface(IInterface var1);

   public void writeStrongInterface(IInterface var1, int var2) {
      this.setOutputField(var2);
      this.writeStrongInterface(var1);
   }

   protected void writeToParcel(VersionedParcelable var1, VersionedParcel var2) {
      try {
         this.getWriteMethod(var1.getClass()).invoke((Object)null, var1, var2);
      } catch (IllegalAccessException var3) {
         throw new RuntimeException("VersionedParcel encountered IllegalAccessException", var3);
      } catch (InvocationTargetException var4) {
         if (var4.getCause() instanceof RuntimeException) {
            throw (RuntimeException)var4.getCause();
         } else {
            throw new RuntimeException("VersionedParcel encountered InvocationTargetException", var4);
         }
      } catch (NoSuchMethodException var5) {
         throw new RuntimeException("VersionedParcel encountered NoSuchMethodException", var5);
      } catch (ClassNotFoundException var6) {
         throw new RuntimeException("VersionedParcel encountered ClassNotFoundException", var6);
      }
   }

   protected void writeVersionedParcelable(VersionedParcelable var1) {
      if (var1 == null) {
         this.writeString((String)null);
      } else {
         this.writeVersionedParcelableCreator(var1);
         VersionedParcel var2 = this.createSubParcel();
         this.writeToParcel(var1, var2);
         var2.closeField();
      }
   }

   public void writeVersionedParcelable(VersionedParcelable var1, int var2) {
      this.setOutputField(var2);
      this.writeVersionedParcelable(var1);
   }

   public static class ParcelException extends RuntimeException {
      public ParcelException(Throwable var1) {
         super(var1);
      }
   }
}
