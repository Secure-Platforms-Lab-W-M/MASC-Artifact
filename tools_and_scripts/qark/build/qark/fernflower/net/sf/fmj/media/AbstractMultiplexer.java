package net.sf.fmj.media;

import java.util.logging.Logger;
import javax.media.Format;
import javax.media.Multiplexer;
import javax.media.protocol.ContentDescriptor;
import net.sf.fmj.utility.LoggerSingleton;

public abstract class AbstractMultiplexer extends AbstractPlugIn implements Multiplexer {
   private static final Logger logger;
   protected Format[] inputFormats;
   protected int numTracks;
   protected ContentDescriptor outputContentDescriptor;

   static {
      logger = LoggerSingleton.logger;
   }

   public ContentDescriptor setContentDescriptor(ContentDescriptor var1) {
      this.outputContentDescriptor = var1;
      return var1;
   }

   public Format setInputFormat(Format var1, int var2) {
      StringBuilder var7;
      Logger var8;
      if (var2 >= this.numTracks) {
         var8 = logger;
         var7 = new StringBuilder();
         var7.append("Rejecting input format for track number out of range: ");
         var7.append(var2);
         var7.append(": ");
         var7.append(var1);
         var8.warning(var7.toString());
         return null;
      } else {
         boolean var4 = false;
         Format[] var6 = this.getSupportedInputFormats();
         int var5 = var6.length;

         for(int var3 = 0; var3 < var5; ++var3) {
            if (var6[var3].matches(var1)) {
               var4 = true;
            }
         }

         if (!var4) {
            var8 = logger;
            var7 = new StringBuilder();
            var7.append("Rejecting unsupported input format for track ");
            var7.append(var2);
            var7.append(": ");
            var7.append(var1);
            var8.fine(var7.toString());
            return null;
         } else {
            var8 = logger;
            var7 = new StringBuilder();
            var7.append("setInputFormat ");
            var7.append(var1);
            var7.append(" ");
            var7.append(var2);
            var8.finer(var7.toString());
            var6 = this.inputFormats;
            if (var6 != null) {
               var6[var2] = var1;
            }

            return var1;
         }
      }
   }

   public int setNumTracks(int var1) {
      Logger var2 = logger;
      StringBuilder var3 = new StringBuilder();
      var3.append("setNumTracks ");
      var3.append(var1);
      var2.finer(var3.toString());
      this.inputFormats = new Format[var1];
      this.numTracks = var1;
      return var1;
   }
}
