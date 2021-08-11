package org.apache.commons.lang3;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.arch.Processor;

public class ArchUtils {
   private static final Map ARCH_TO_PROCESSOR = new HashMap();

   static {
      init();
   }

   private static void addProcessor(String var0, Processor var1) {
      if (!ARCH_TO_PROCESSOR.containsKey(var0)) {
         ARCH_TO_PROCESSOR.put(var0, var1);
      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append("Key ");
         var2.append(var0);
         var2.append(" already exists in processor map");
         throw new IllegalStateException(var2.toString());
      }
   }

   private static void addProcessors(Processor var0, String... var1) {
      int var3 = var1.length;

      for(int var2 = 0; var2 < var3; ++var2) {
         addProcessor(var1[var2], var0);
      }

   }

   public static Processor getProcessor() {
      return getProcessor(SystemUtils.OS_ARCH);
   }

   public static Processor getProcessor(String var0) {
      return (Processor)ARCH_TO_PROCESSOR.get(var0);
   }

   private static void init() {
      init_X86_32Bit();
      init_X86_64Bit();
      init_IA64_32Bit();
      init_IA64_64Bit();
      init_PPC_32Bit();
      init_PPC_64Bit();
   }

   private static void init_IA64_32Bit() {
      addProcessors(new Processor(Processor.Arch.BIT_32, Processor.Type.IA_64), "ia64_32", "ia64n");
   }

   private static void init_IA64_64Bit() {
      addProcessors(new Processor(Processor.Arch.BIT_64, Processor.Type.IA_64), "ia64", "ia64w");
   }

   private static void init_PPC_32Bit() {
      addProcessors(new Processor(Processor.Arch.BIT_32, Processor.Type.PPC), "ppc", "power", "powerpc", "power_pc", "power_rs");
   }

   private static void init_PPC_64Bit() {
      addProcessors(new Processor(Processor.Arch.BIT_64, Processor.Type.PPC), "ppc64", "power64", "powerpc64", "power_pc64", "power_rs64");
   }

   private static void init_X86_32Bit() {
      addProcessors(new Processor(Processor.Arch.BIT_32, Processor.Type.X86), "x86", "i386", "i486", "i586", "i686", "pentium");
   }

   private static void init_X86_64Bit() {
      addProcessors(new Processor(Processor.Arch.BIT_64, Processor.Type.X86), "x86_64", "amd64", "em64t", "universal");
   }
}
