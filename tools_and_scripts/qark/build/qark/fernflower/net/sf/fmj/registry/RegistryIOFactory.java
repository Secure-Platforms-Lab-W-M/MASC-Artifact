package net.sf.fmj.registry;

class RegistryIOFactory {
   public static final int PROPERTIES = 1;
   public static final int XML = 0;

   public static final RegistryIO createRegistryIO(int var0, RegistryContents var1) {
      if (var0 != 0) {
         if (var0 == 1) {
            return new PropertiesRegistryIO(var1);
         } else {
            throw new IllegalArgumentException();
         }
      } else {
         return new XMLRegistryIO(var1);
      }
   }
}
