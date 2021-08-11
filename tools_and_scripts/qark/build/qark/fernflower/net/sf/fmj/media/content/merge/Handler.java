package net.sf.fmj.media.content.merge;

import java.io.IOException;
import java.util.logging.Logger;
import javax.media.IncompatibleSourceException;
import javax.media.MediaProxy;
import javax.media.NoDataSourceException;
import net.sf.fmj.media.protocol.merge.DataSource;
import net.sf.fmj.utility.LoggerSingleton;

public class Handler implements MediaProxy {
   private static final Logger logger;
   private DataSource source;

   static {
      logger = LoggerSingleton.logger;
   }

   public javax.media.protocol.DataSource getDataSource() throws IOException, NoDataSourceException {
      // $FF: Couldn't be decompiled
   }

   public void setSource(javax.media.protocol.DataSource var1) throws IOException, IncompatibleSourceException {
      if (var1 instanceof DataSource) {
         this.source = (DataSource)var1;
      } else {
         throw new IncompatibleSourceException();
      }
   }
}
