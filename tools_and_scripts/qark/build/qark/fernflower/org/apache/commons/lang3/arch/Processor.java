package org.apache.commons.lang3.arch;

public class Processor {
   private final Processor.Arch arch;
   private final Processor.Type type;

   public Processor(Processor.Arch var1, Processor.Type var2) {
      this.arch = var1;
      this.type = var2;
   }

   public Processor.Arch getArch() {
      return this.arch;
   }

   public Processor.Type getType() {
      return this.type;
   }

   public boolean is32Bit() {
      return Processor.Arch.BIT_32.equals(this.arch);
   }

   public boolean is64Bit() {
      return Processor.Arch.BIT_64.equals(this.arch);
   }

   public boolean isIA64() {
      return Processor.Type.IA_64.equals(this.type);
   }

   public boolean isPPC() {
      return Processor.Type.PPC.equals(this.type);
   }

   public boolean isX86() {
      return Processor.Type.X86.equals(this.type);
   }

   public static enum Arch {
      BIT_32,
      BIT_64,
      UNKNOWN;

      static {
         Processor.Arch var0 = new Processor.Arch("UNKNOWN", 2);
         UNKNOWN = var0;
      }
   }

   public static enum Type {
      IA_64,
      PPC,
      UNKNOWN,
      X86;

      static {
         Processor.Type var0 = new Processor.Type("UNKNOWN", 3);
         UNKNOWN = var0;
      }
   }
}
