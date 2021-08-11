package net.sf.fmj.utility;

import com.lti.utils.StringUtils;
import java.util.ArrayList;
import javax.media.Buffer;
import javax.media.Format;

public final class LoggingStringUtils {
   private LoggingStringUtils() {
   }

   public static String bufferFlagsToStr(int var0) {
      ArrayList var1 = new ArrayList();
      if ((var0 & 1) != 0) {
         var1.add("FLAG_EOM");
      }

      if ((var0 & 2) != 0) {
         var1.add("FLAG_DISCARD");
      }

      if ((var0 & 4) != 0) {
         var1.add("FLAG_SILENCE");
      }

      if ((var0 & 8) != 0) {
         var1.add("FLAG_SID");
      }

      if ((var0 & 16) != 0) {
         var1.add("FLAG_KEY_FRAME");
      }

      if ((var0 & 64) != 0) {
         var1.add("FLAG_NO_WAIT");
      }

      if ((var0 & 96) != 0) {
         var1.add("FLAG_NO_SYNC");
      }

      if ((var0 & 128) != 0) {
         var1.add("FLAG_SYSTEM_TIME");
      }

      if ((var0 & 256) != 0) {
         var1.add("FLAG_RELATIVE_TIME");
      }

      if ((var0 & 512) != 0) {
         var1.add("FLAG_FLUSH");
      }

      if ((var0 & 1024) != 0) {
         var1.add("FLAG_SYSTEM_MARKER");
      }

      if ((var0 & 2048) != 0) {
         var1.add("FLAG_RTP_MARKER");
      }

      if ((var0 & 4096) != 0) {
         var1.add("FLAG_RTP_TIME");
      }

      if ((var0 & 8192) != 0) {
         var1.add("FLAG_BUF_OVERFLOWN");
      }

      if ((var0 & 16384) != 0) {
         var1.add("FLAG_BUF_UNDERFLOWN");
      }

      if (('耀' & var0) != 0) {
         var1.add("FLAG_LIVE_DATA");
      }

      StringBuffer var2 = new StringBuffer();

      for(var0 = 0; var0 < var1.size(); ++var0) {
         if (var2.length() != 0) {
            var2.append(" | ");
         }

         var2.append((String)var1.get(var0));
      }

      return var2.toString();
   }

   public static String bufferToStr(Buffer var0) {
      if (var0 == null) {
         return "null";
      } else {
         StringBuffer var1 = new StringBuffer();
         var1.append(var0);
         StringBuilder var2 = new StringBuilder();
         var2.append(" seq=");
         var2.append(var0.getSequenceNumber());
         var1.append(var2.toString());
         var2 = new StringBuilder();
         var2.append(" off=");
         var2.append(var0.getOffset());
         var1.append(var2.toString());
         var2 = new StringBuilder();
         var2.append(" len=");
         var2.append(var0.getLength());
         var1.append(var2.toString());
         var2 = new StringBuilder();
         var2.append(" flags=[");
         var2.append(bufferFlagsToStr(var0.getFlags()));
         var2.append("]");
         var1.append(var2.toString());
         var2 = new StringBuilder();
         var2.append(" fmt=[");
         var2.append(var0.getFormat());
         var2.append("]");
         var1.append(var2.toString());
         if (var0.getData() != null && var0.getData() instanceof byte[]) {
            var2 = new StringBuilder();
            var2.append(" data=[");
            var2.append(var0.getData());
            var2.append(" ");
            var2.append(StringUtils.byteArrayToHexString((byte[])((byte[])var0.getData()), var0.getLength(), var0.getOffset()));
            var2.append("]");
            var1.append(var2.toString());
         } else if (var0.getData() != null) {
            var2 = new StringBuilder();
            var2.append(" data=[");
            var2.append(var0.getData());
            var2.append("]");
            var1.append(var2.toString());
         } else {
            var1.append(" data=[null]");
         }

         return var1.toString();
      }
   }

   public static String formatToStr(Format var0) {
      StringBuilder var1 = new StringBuilder();
      var1.append("");
      var1.append(var0);
      return var1.toString();
   }

   public static String plugInResultToStr(int var0) {
      if (var0 != 0) {
         if (var0 != 1) {
            if (var0 != 2) {
               if (var0 != 4) {
                  if (var0 != 8) {
                     StringBuilder var1 = new StringBuilder();
                     var1.append("");
                     var1.append(var0);
                     return var1.toString();
                  } else {
                     return "PLUGIN_TERMINATED";
                  }
               } else {
                  return "OUTPUT_BUFFER_NOT_FILLED";
               }
            } else {
               return "INPUT_BUFFER_NOT_CONSUMED";
            }
         } else {
            return "BUFFER_PROCESSED_FAILED";
         }
      } else {
         return "BUFFER_PROCESSED_OK";
      }
   }
}
