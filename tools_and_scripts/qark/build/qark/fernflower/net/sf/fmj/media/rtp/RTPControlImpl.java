package net.sf.fmj.media.rtp;

import java.util.Enumeration;
import java.util.Hashtable;
import javax.media.Format;
import javax.media.rtp.GlobalReceptionStats;
import javax.media.rtp.RTPControl;
import javax.media.rtp.ReceptionStats;
import net.sf.fmj.media.util.RTPInfo;
import org.atalk.android.util.java.awt.Component;

public abstract class RTPControlImpl implements RTPControl, RTPInfo {
   String cname = null;
   String codec = "";
   Hashtable codeclist = new Hashtable(5);
   Format currentformat = null;
   int payload = -1;
   int rtptime = 0;
   int seqno = 0;
   SSRCInfo stream = null;

   public void addFormat(Format var1, int var2) {
      this.codeclist.put(var2, var1);
   }

   public abstract String getCNAME();

   public Component getControlComponent() {
      return null;
   }

   public Format getFormat() {
      return this.currentformat;
   }

   public Format getFormat(int var1) {
      return (Format)this.codeclist.get(var1);
   }

   public Format[] getFormatList() {
      Format[] var2 = new Format[this.codeclist.size()];
      int var1 = 0;

      for(Enumeration var3 = this.codeclist.elements(); var3.hasMoreElements(); ++var1) {
         var2[var1] = (Format)((Format)var3.nextElement()).clone();
      }

      return var2;
   }

   public GlobalReceptionStats getGlobalStats() {
      return null;
   }

   public ReceptionStats getReceptionStats() {
      SSRCInfo var1 = this.stream;
      return var1 == null ? null : ((RecvSSRCInfo)var1).getSourceReceptionStats();
   }

   public abstract int getSSRC();

   public void setRTPInfo(int var1, int var2) {
      this.rtptime = var1;
      this.seqno = var2;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("\n\tRTPTime is ");
      var1.append(this.rtptime);
      var1.append("\n\tSeqno is ");
      var1.append(this.seqno);
      String var3 = var1.toString();
      StringBuilder var2;
      if (this.codeclist != null) {
         var2 = new StringBuilder();
         var2.append(var3);
         var2.append("\n\tCodecInfo is ");
         var2.append(this.codeclist.toString());
         return var2.toString();
      } else {
         var2 = new StringBuilder();
         var2.append(var3);
         var2.append("\n\tcodeclist is null");
         return var2.toString();
      }
   }
}
