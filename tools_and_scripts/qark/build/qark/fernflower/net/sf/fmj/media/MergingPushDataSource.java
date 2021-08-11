package net.sf.fmj.media;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.media.Duration;
import javax.media.Time;
import javax.media.protocol.PushDataSource;
import javax.media.protocol.PushSourceStream;

public class MergingPushDataSource extends PushDataSource {
   protected final List sources;

   public MergingPushDataSource(List var1) {
      this.sources = var1;
   }

   public void connect() throws IOException {
      Iterator var1 = this.sources.iterator();

      while(var1.hasNext()) {
         ((PushDataSource)var1.next()).connect();
      }

   }

   public void disconnect() {
      Iterator var1 = this.sources.iterator();

      while(var1.hasNext()) {
         ((PushDataSource)var1.next()).disconnect();
      }

   }

   public String getContentType() {
      for(int var1 = 0; var1 < this.sources.size(); ++var1) {
         if (!((PushDataSource)this.sources.get(var1)).getContentType().equals(((PushDataSource)this.sources.get(0)).getContentType())) {
            return "application.mixed-data";
         }
      }

      return ((PushDataSource)this.sources.get(0)).getContentType();
   }

   public Object getControl(String var1) {
      Iterator var2 = this.sources.iterator();

      Object var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = ((PushDataSource)var2.next()).getControl(var1);
      } while(var3 == null);

      return var3;
   }

   public Object[] getControls() {
      ArrayList var4 = new ArrayList();
      Iterator var5 = this.sources.iterator();

      while(true) {
         boolean var3 = var5.hasNext();
         int var1 = 0;
         if (!var3) {
            return var4.toArray(new Object[0]);
         }

         Object[] var6 = ((PushDataSource)var5.next()).getControls();

         for(int var2 = var6.length; var1 < var2; ++var1) {
            var4.add(var6[var1]);
         }
      }
   }

   public Time getDuration() {
      ArrayList var5 = new ArrayList();
      Iterator var6 = this.sources.iterator();

      while(var6.hasNext()) {
         var5.add(((PushDataSource)var6.next()).getDuration());
      }

      var6 = var5.iterator();

      do {
         if (!var6.hasNext()) {
            var6 = var5.iterator();

            do {
               if (!var6.hasNext()) {
                  long var1 = -1L;

                  long var3;
                  for(Iterator var7 = var5.iterator(); var7.hasNext(); var1 = var3) {
                     Time var8 = (Time)var7.next();
                     var3 = var1;
                     if (var8.getNanoseconds() > var1) {
                        var3 = var8.getNanoseconds();
                     }
                  }

                  if (var1 < 0L) {
                     return Duration.DURATION_UNKNOWN;
                  }

                  return new Time(var1);
               }
            } while(((Time)var6.next()).getNanoseconds() != Duration.DURATION_UNBOUNDED.getNanoseconds());

            return Duration.DURATION_UNBOUNDED;
         }
      } while(((Time)var6.next()).getNanoseconds() != Duration.DURATION_UNKNOWN.getNanoseconds());

      return Duration.DURATION_UNKNOWN;
   }

   public PushSourceStream[] getStreams() {
      ArrayList var4 = new ArrayList();
      Iterator var5 = this.sources.iterator();

      while(true) {
         boolean var3 = var5.hasNext();
         int var1 = 0;
         if (!var3) {
            return (PushSourceStream[])var4.toArray(new PushSourceStream[0]);
         }

         PushSourceStream[] var6 = ((PushDataSource)var5.next()).getStreams();

         for(int var2 = var6.length; var1 < var2; ++var1) {
            var4.add(var6[var1]);
         }
      }
   }

   public void start() throws IOException {
      Iterator var1 = this.sources.iterator();

      while(var1.hasNext()) {
         ((PushDataSource)var1.next()).start();
      }

   }

   public void stop() throws IOException {
      Iterator var1 = this.sources.iterator();

      while(var1.hasNext()) {
         ((PushDataSource)var1.next()).stop();
      }

   }
}
