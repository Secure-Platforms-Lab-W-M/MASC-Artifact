package org.apache.http;

import java.nio.charset.Charset;

public final class Consts {
   public static final Charset ASCII = Charset.forName("US-ASCII");
   // $FF: renamed from: CR int
   public static final int field_177 = 13;
   // $FF: renamed from: HT int
   public static final int field_178 = 9;
   public static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1");
   // $FF: renamed from: LF int
   public static final int field_179 = 10;
   // $FF: renamed from: SP int
   public static final int field_180 = 32;
   public static final Charset UTF_8 = Charset.forName("UTF-8");

   private Consts() {
   }
}
