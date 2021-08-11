package okhttp3.internal.http2;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import okio.BufferedSink;
import okio.ByteString;

class Huffman {
   private static final int[] CODES = new int[]{8184, 8388568, 268435426, 268435427, 268435428, 268435429, 268435430, 268435431, 268435432, 16777194, 1073741820, 268435433, 268435434, 1073741821, 268435435, 268435436, 268435437, 268435438, 268435439, 268435440, 268435441, 268435442, 1073741822, 268435443, 268435444, 268435445, 268435446, 268435447, 268435448, 268435449, 268435450, 268435451, 20, 1016, 1017, 4090, 8185, 21, 248, 2042, 1018, 1019, 249, 2043, 250, 22, 23, 24, 0, 1, 2, 25, 26, 27, 28, 29, 30, 31, 92, 251, 32764, 32, 4091, 1020, 8186, 33, 93, 94, 95, 96, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 252, 115, 253, 8187, 524272, 8188, 16380, 34, 32765, 3, 35, 4, 36, 5, 37, 38, 39, 6, 116, 117, 40, 41, 42, 7, 43, 118, 44, 8, 9, 45, 119, 120, 121, 122, 123, 32766, 2044, 16381, 8189, 268435452, 1048550, 4194258, 1048551, 1048552, 4194259, 4194260, 4194261, 8388569, 4194262, 8388570, 8388571, 8388572, 8388573, 8388574, 16777195, 8388575, 16777196, 16777197, 4194263, 8388576, 16777198, 8388577, 8388578, 8388579, 8388580, 2097116, 4194264, 8388581, 4194265, 8388582, 8388583, 16777199, 4194266, 2097117, 1048553, 4194267, 4194268, 8388584, 8388585, 2097118, 8388586, 4194269, 4194270, 16777200, 2097119, 4194271, 8388587, 8388588, 2097120, 2097121, 4194272, 2097122, 8388589, 4194273, 8388590, 8388591, 1048554, 4194274, 4194275, 4194276, 8388592, 4194277, 4194278, 8388593, 67108832, 67108833, 1048555, 524273, 4194279, 8388594, 4194280, 33554412, 67108834, 67108835, 67108836, 134217694, 134217695, 67108837, 16777201, 33554413, 524274, 2097123, 67108838, 134217696, 134217697, 67108839, 134217698, 16777202, 2097124, 2097125, 67108840, 67108841, 268435453, 134217699, 134217700, 134217701, 1048556, 16777203, 1048557, 2097126, 4194281, 2097127, 2097128, 8388595, 4194282, 4194283, 33554414, 33554415, 16777204, 16777205, 67108842, 8388596, 67108843, 134217702, 67108844, 67108845, 134217703, 134217704, 134217705, 134217706, 134217707, 268435454, 134217708, 134217709, 134217710, 134217711, 134217712, 67108846};
   private static final byte[] CODE_LENGTHS = new byte[]{13, 23, 28, 28, 28, 28, 28, 28, 28, 24, 30, 28, 28, 30, 28, 28, 28, 28, 28, 28, 28, 28, 30, 28, 28, 28, 28, 28, 28, 28, 28, 28, 6, 10, 10, 12, 13, 6, 8, 11, 10, 10, 8, 11, 8, 6, 6, 6, 5, 5, 5, 6, 6, 6, 6, 6, 6, 6, 7, 8, 15, 6, 12, 10, 13, 6, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 8, 7, 8, 13, 19, 13, 14, 6, 15, 5, 6, 5, 6, 5, 6, 6, 6, 5, 7, 7, 6, 6, 6, 5, 6, 7, 6, 5, 5, 6, 7, 7, 7, 7, 7, 15, 11, 14, 13, 28, 20, 22, 20, 20, 22, 22, 22, 23, 22, 23, 23, 23, 23, 23, 24, 23, 24, 24, 22, 23, 24, 23, 23, 23, 23, 21, 22, 23, 22, 23, 23, 24, 22, 21, 20, 22, 22, 23, 23, 21, 23, 22, 22, 24, 21, 22, 23, 23, 21, 21, 22, 21, 23, 22, 23, 23, 20, 22, 22, 22, 23, 22, 22, 23, 26, 26, 20, 19, 22, 23, 22, 25, 26, 26, 26, 27, 27, 26, 24, 25, 19, 21, 26, 27, 27, 26, 27, 24, 21, 21, 26, 26, 28, 27, 27, 27, 20, 24, 20, 21, 22, 21, 21, 23, 22, 22, 25, 25, 24, 24, 26, 23, 26, 27, 26, 26, 27, 27, 27, 27, 27, 28, 27, 27, 27, 27, 27, 26};
   private static final Huffman INSTANCE = new Huffman();
   private final Huffman.Node root = new Huffman.Node();

   private Huffman() {
      this.buildTree();
   }

   private void addCode(int var1, int var2, byte var3) {
      Huffman.Node var5 = new Huffman.Node(var1, var3);

      Huffman.Node var4;
      for(var4 = this.root; var3 > 8; var4 = var4.children[var1]) {
         var3 = (byte)(var3 - 8);
         var1 = var2 >>> var3 & 255;
         if (var4.children == null) {
            throw new IllegalStateException("invalid dictionary: prefix not unique");
         }

         if (var4.children[var1] == null) {
            var4.children[var1] = new Huffman.Node();
         }
      }

      int var6 = 8 - var3;
      var2 = var2 << var6 & 255;

      for(var1 = var2; var1 < var2 + (1 << var6); ++var1) {
         var4.children[var1] = var5;
      }

   }

   private void buildTree() {
      int var1 = 0;

      while(true) {
         byte[] var2 = CODE_LENGTHS;
         if (var1 >= var2.length) {
            return;
         }

         this.addCode(var1, CODES[var1], var2[var1]);
         ++var1;
      }
   }

   public static Huffman get() {
      return INSTANCE;
   }

   byte[] decode(byte[] var1) {
      ByteArrayOutputStream var8 = new ByteArrayOutputStream();
      Huffman.Node var6 = this.root;
      int var4 = 0;
      int var2 = 0;
      int var3 = 0;

      while(true) {
         Huffman.Node var7 = var6;
         int var5 = var2;
         if (var3 >= var1.length) {
            while(var5 > 0) {
               Huffman.Node var9 = var7.children[var4 << 8 - var5 & 255];
               if (var9.children != null || var9.terminalBits > var5) {
                  break;
               }

               var8.write(var9.symbol);
               var5 -= var9.terminalBits;
               var7 = this.root;
            }

            return var8.toByteArray();
         }

         var4 = var4 << 8 | var1[var3] & 255;
         var2 += 8;

         while(var2 >= 8) {
            var6 = var6.children[var4 >>> var2 - 8 & 255];
            if (var6.children == null) {
               var8.write(var6.symbol);
               var2 -= var6.terminalBits;
               var6 = this.root;
            } else {
               var2 -= 8;
            }
         }

         ++var3;
      }
   }

   void encode(ByteString var1, BufferedSink var2) throws IOException {
      long var7 = 0L;
      int var3 = 0;

      for(int var4 = 0; var4 < var1.size(); ++var4) {
         int var6 = var1.getByte(var4) & 255;
         int var5 = CODES[var6];
         byte var9 = CODE_LENGTHS[var6];
         var7 = var7 << var9 | (long)var5;
         var3 += var9;

         while(var3 >= 8) {
            var3 -= 8;
            var2.writeByte((int)(var7 >> var3));
         }
      }

      if (var3 > 0) {
         var2.writeByte((int)(var7 << 8 - var3 | (long)(255 >>> var3)));
      }

   }

   int encodedLength(ByteString var1) {
      long var4 = 0L;

      for(int var2 = 0; var2 < var1.size(); ++var2) {
         byte var3 = var1.getByte(var2);
         var4 += (long)CODE_LENGTHS[var3 & 255];
      }

      return (int)(7L + var4 >> 3);
   }

   private static final class Node {
      final Huffman.Node[] children;
      final int symbol;
      final int terminalBits;

      Node() {
         this.children = new Huffman.Node[256];
         this.symbol = 0;
         this.terminalBits = 0;
      }

      Node(int var1, int var2) {
         this.children = null;
         this.symbol = var1;
         var1 = var2 & 7;
         if (var1 == 0) {
            var1 = 8;
         }

         this.terminalBits = var1;
      }
   }
}
