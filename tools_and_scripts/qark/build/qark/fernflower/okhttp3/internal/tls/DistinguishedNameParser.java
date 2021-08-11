package okhttp3.internal.tls;

import javax.security.auth.x500.X500Principal;

final class DistinguishedNameParser {
   private int beg;
   private char[] chars;
   private int cur;
   // $FF: renamed from: dn java.lang.String
   private final String field_60;
   private int end;
   private final int length;
   private int pos;

   DistinguishedNameParser(X500Principal var1) {
      String var2 = var1.getName("RFC2253");
      this.field_60 = var2;
      this.length = var2.length();
   }

   private String escapedAV() {
      int var1 = this.pos;
      this.beg = var1;
      this.end = var1;

      while(true) {
         var1 = this.pos;
         char[] var3;
         if (var1 >= this.length) {
            var3 = this.chars;
            var1 = this.beg;
            return new String(var3, var1, this.end - var1);
         }

         var3 = this.chars;
         char var2 = var3[var1];
         int var4;
         if (var2 != ' ') {
            if (var2 != ';') {
               if (var2 == '\\') {
                  var1 = this.end++;
                  var3[var1] = this.getEscaped();
                  ++this.pos;
                  continue;
               }

               if (var2 != '+' && var2 != ',') {
                  var4 = this.end++;
                  var3[var4] = var3[var1];
                  this.pos = var1 + 1;
                  continue;
               }
            }

            var3 = this.chars;
            var1 = this.beg;
            return new String(var3, var1, this.end - var1);
         } else {
            var4 = this.end;
            this.cur = var4;
            this.pos = var1 + 1;
            this.end = var4 + 1;
            var3[var4] = ' ';

            while(true) {
               var1 = this.pos;
               if (var1 >= this.length) {
                  break;
               }

               var3 = this.chars;
               if (var3[var1] != ' ') {
                  break;
               }

               var4 = this.end++;
               var3[var4] = ' ';
               this.pos = var1 + 1;
            }

            var1 = this.pos;
            if (var1 != this.length) {
               var3 = this.chars;
               if (var3[var1] != ',' && var3[var1] != '+' && var3[var1] != ';') {
                  continue;
               }
            }

            var3 = this.chars;
            var1 = this.beg;
            return new String(var3, var1, this.cur - var1);
         }
      }
   }

   private int getByte(int var1) {
      StringBuilder var3;
      if (var1 + 1 >= this.length) {
         var3 = new StringBuilder();
         var3.append("Malformed DN: ");
         var3.append(this.field_60);
         throw new IllegalStateException(var3.toString());
      } else {
         char var2 = this.chars[var1];
         int var5;
         if (var2 >= '0' && var2 <= '9') {
            var5 = var2 - 48;
         } else if (var2 >= 'a' && var2 <= 'f') {
            var5 = var2 - 87;
         } else {
            if (var2 < 'A' || var2 > 'F') {
               var3 = new StringBuilder();
               var3.append("Malformed DN: ");
               var3.append(this.field_60);
               throw new IllegalStateException(var3.toString());
            }

            var5 = var2 - 55;
         }

         char var4 = this.chars[var1 + 1];
         if (var4 >= '0' && var4 <= '9') {
            var1 = var4 - 48;
         } else if (var4 >= 'a' && var4 <= 'f') {
            var1 = var4 - 87;
         } else {
            if (var4 < 'A' || var4 > 'F') {
               var3 = new StringBuilder();
               var3.append("Malformed DN: ");
               var3.append(this.field_60);
               throw new IllegalStateException(var3.toString());
            }

            var1 = var4 - 55;
         }

         return (var5 << 4) + var1;
      }
   }

   private char getEscaped() {
      int var1 = this.pos + 1;
      this.pos = var1;
      if (var1 != this.length) {
         char var3 = this.chars[var1];
         if (var3 != ' ' && var3 != '%' && var3 != '\\' && var3 != '_' && var3 != '"' && var3 != '#') {
            switch(var3) {
            case '*':
            case '+':
            case ',':
               break;
            default:
               switch(var3) {
               case ';':
               case '<':
               case '=':
               case '>':
                  break;
               default:
                  return this.getUTF8();
               }
            }
         }

         return this.chars[this.pos];
      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append("Unexpected end of DN: ");
         var2.append(this.field_60);
         throw new IllegalStateException(var2.toString());
      }
   }

   private char getUTF8() {
      int var1 = this.getByte(this.pos);
      ++this.pos;
      if (var1 < 128) {
         return (char)var1;
      } else if (var1 >= 192 && var1 <= 247) {
         byte var2;
         if (var1 <= 223) {
            var2 = 1;
            var1 &= 31;
         } else if (var1 <= 239) {
            var2 = 2;
            var1 &= 15;
         } else {
            var2 = 3;
            var1 &= 7;
         }

         for(int var3 = 0; var3 < var2; ++var3) {
            int var4 = this.pos + 1;
            this.pos = var4;
            if (var4 == this.length) {
               return '?';
            }

            if (this.chars[var4] != '\\') {
               return '?';
            }

            ++var4;
            this.pos = var4;
            var4 = this.getByte(var4);
            ++this.pos;
            if ((var4 & 192) != 128) {
               return '?';
            }

            var1 = (var1 << 6) + (var4 & 63);
         }

         return (char)var1;
      } else {
         return '?';
      }
   }

   private String hexAV() {
      int var1 = this.pos;
      StringBuilder var5;
      if (var1 + 4 >= this.length) {
         var5 = new StringBuilder();
         var5.append("Unexpected end of DN: ");
         var5.append(this.field_60);
         throw new IllegalStateException(var5.toString());
      } else {
         this.beg = var1;
         this.pos = var1 + 1;

         label58:
         while(true) {
            var1 = this.pos;
            if (var1 != this.length) {
               char[] var4 = this.chars;
               if (var4[var1] != '+' && var4[var1] != ',' && var4[var1] != ';') {
                  if (var4[var1] == ' ') {
                     this.end = var1;
                     this.pos = var1 + 1;

                     while(true) {
                        var1 = this.pos;
                        if (var1 >= this.length || this.chars[var1] != ' ') {
                           break label58;
                        }

                        this.pos = var1 + 1;
                     }
                  }

                  if (var4[var1] >= 'A' && var4[var1] <= 'F') {
                     var4[var1] = (char)(var4[var1] + 32);
                  }

                  ++this.pos;
                  continue;
               }
            }

            this.end = this.pos;
            break;
         }

         var1 = this.end;
         int var2 = this.beg;
         int var3 = var1 - var2;
         if (var3 >= 5 && (var3 & 1) != 0) {
            byte[] var6 = new byte[var3 / 2];
            var1 = 0;
            ++var2;

            while(var1 < var6.length) {
               var6[var1] = (byte)this.getByte(var2);
               var2 += 2;
               ++var1;
            }

            return new String(this.chars, this.beg, var3);
         } else {
            var5 = new StringBuilder();
            var5.append("Unexpected end of DN: ");
            var5.append(this.field_60);
            throw new IllegalStateException(var5.toString());
         }
      }
   }

   private String nextAT() {
      while(true) {
         int var1 = this.pos;
         if (var1 >= this.length || this.chars[var1] != ' ') {
            var1 = this.pos;
            if (var1 == this.length) {
               return null;
            }

            this.beg = var1;
            this.pos = var1 + 1;

            char[] var3;
            while(true) {
               var1 = this.pos;
               if (var1 >= this.length) {
                  break;
               }

               var3 = this.chars;
               if (var3[var1] == '=' || var3[var1] == ' ') {
                  break;
               }

               this.pos = var1 + 1;
            }

            var1 = this.pos;
            StringBuilder var4;
            if (var1 >= this.length) {
               var4 = new StringBuilder();
               var4.append("Unexpected end of DN: ");
               var4.append(this.field_60);
               throw new IllegalStateException(var4.toString());
            }

            this.end = var1;
            if (this.chars[var1] == ' ') {
               while(true) {
                  var1 = this.pos;
                  if (var1 >= this.length) {
                     break;
                  }

                  var3 = this.chars;
                  if (var3[var1] == '=' || var3[var1] != ' ') {
                     break;
                  }

                  this.pos = var1 + 1;
               }

               var3 = this.chars;
               var1 = this.pos;
               if (var3[var1] != '=' || var1 == this.length) {
                  var4 = new StringBuilder();
                  var4.append("Unexpected end of DN: ");
                  var4.append(this.field_60);
                  throw new IllegalStateException(var4.toString());
               }
            }

            ++this.pos;

            while(true) {
               var1 = this.pos;
               if (var1 >= this.length || this.chars[var1] != ' ') {
                  var1 = this.end;
                  int var2 = this.beg;
                  if (var1 - var2 > 4) {
                     var3 = this.chars;
                     if (var3[var2 + 3] == '.' && (var3[var2] == 'O' || var3[var2] == 'o')) {
                        var3 = this.chars;
                        var1 = this.beg;
                        if (var3[var1 + 1] == 'I' || var3[var1 + 1] == 'i') {
                           var3 = this.chars;
                           var1 = this.beg;
                           if (var3[var1 + 2] == 'D' || var3[var1 + 2] == 'd') {
                              this.beg += 4;
                           }
                        }
                     }
                  }

                  var3 = this.chars;
                  var1 = this.beg;
                  return new String(var3, var1, this.end - var1);
               }

               this.pos = var1 + 1;
            }
         }

         this.pos = var1 + 1;
      }
   }

   private String quotedAV() {
      int var1 = this.pos + 1;
      this.pos = var1;
      this.beg = var1;
      this.end = var1;

      while(true) {
         var1 = this.pos;
         if (var1 == this.length) {
            StringBuilder var3 = new StringBuilder();
            var3.append("Unexpected end of DN: ");
            var3.append(this.field_60);
            throw new IllegalStateException(var3.toString());
         }

         char[] var2 = this.chars;
         if (var2[var1] == '"') {
            this.pos = var1 + 1;

            while(true) {
               var1 = this.pos;
               if (var1 >= this.length || this.chars[var1] != ' ') {
                  var2 = this.chars;
                  var1 = this.beg;
                  return new String(var2, var1, this.end - var1);
               }

               this.pos = var1 + 1;
            }
         }

         if (var2[var1] == '\\') {
            var2[this.end] = this.getEscaped();
         } else {
            var2[this.end] = var2[var1];
         }

         ++this.pos;
         ++this.end;
      }
   }

   public String findMostSpecific(String var1) {
      this.pos = 0;
      this.beg = 0;
      this.end = 0;
      this.cur = 0;
      this.chars = this.field_60.toCharArray();
      String var3 = this.nextAT();
      String var4 = var3;
      if (var3 == null) {
         return null;
      } else {
         StringBuilder var5;
         do {
            var3 = "";
            int var2 = this.pos;
            if (var2 == this.length) {
               return null;
            }

            char var6 = this.chars[var2];
            if (var6 != '"') {
               if (var6 != '#') {
                  if (var6 != '+' && var6 != ',' && var6 != ';') {
                     var3 = this.escapedAV();
                  }
               } else {
                  var3 = this.hexAV();
               }
            } else {
               var3 = this.quotedAV();
            }

            if (var1.equalsIgnoreCase(var4)) {
               return var3;
            }

            var2 = this.pos;
            if (var2 >= this.length) {
               return null;
            }

            char[] var7 = this.chars;
            if (var7[var2] != ',' && var7[var2] != ';' && var7[var2] != '+') {
               var5 = new StringBuilder();
               var5.append("Malformed DN: ");
               var5.append(this.field_60);
               throw new IllegalStateException(var5.toString());
            }

            ++this.pos;
            var4 = this.nextAT();
         } while(var4 != null);

         var5 = new StringBuilder();
         var5.append("Malformed DN: ");
         var5.append(this.field_60);
         throw new IllegalStateException(var5.toString());
      }
   }
}
