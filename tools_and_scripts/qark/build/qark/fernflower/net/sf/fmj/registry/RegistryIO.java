package net.sf.fmj.registry;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

interface RegistryIO {
   void load(InputStream var1) throws IOException;

   void write(OutputStream var1) throws IOException;
}
