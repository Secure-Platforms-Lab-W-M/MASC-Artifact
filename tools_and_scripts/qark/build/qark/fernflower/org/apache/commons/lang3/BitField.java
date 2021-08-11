package org.apache.commons.lang3;

public class BitField {
   private final int _mask;
   private final int _shift_count;

   public BitField(int var1) {
      this._mask = var1;
      if (var1 == 0) {
         var1 = 0;
      } else {
         var1 = Integer.numberOfTrailingZeros(var1);
      }

      this._shift_count = var1;
   }

   public int clear(int var1) {
      return this._mask & var1;
   }

   public byte clearByte(byte var1) {
      return (byte)this.clear(var1);
   }

   public short clearShort(short var1) {
      return (short)this.clear(var1);
   }

   public int getRawValue(int var1) {
      return this._mask & var1;
   }

   public short getShortRawValue(short var1) {
      return (short)this.getRawValue(var1);
   }

   public short getShortValue(short var1) {
      return (short)this.getValue(var1);
   }

   public int getValue(int var1) {
      return this.getRawValue(var1) >> this._shift_count;
   }

   public boolean isAllSet(int var1) {
      int var2 = this._mask;
      return (var1 & var2) == var2;
   }

   public boolean isSet(int var1) {
      return (this._mask & var1) != 0;
   }

   public int set(int var1) {
      return this._mask | var1;
   }

   public int setBoolean(int var1, boolean var2) {
      return var2 ? this.set(var1) : this.clear(var1);
   }

   public byte setByte(byte var1) {
      return (byte)this.set(var1);
   }

   public byte setByteBoolean(byte var1, boolean var2) {
      return var2 ? this.setByte(var1) : this.clearByte(var1);
   }

   public short setShort(short var1) {
      return (short)this.set(var1);
   }

   public short setShortBoolean(short var1, boolean var2) {
      return var2 ? this.setShort(var1) : this.clearShort(var1);
   }

   public short setShortValue(short var1, short var2) {
      return (short)this.setValue(var1, var2);
   }

   public int setValue(int var1, int var2) {
      int var3 = this._mask;
      return var3 & var2 << this._shift_count | var3 & var1;
   }
}
