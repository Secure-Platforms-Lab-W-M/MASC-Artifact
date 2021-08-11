package javax.sdp;

import java.net.URL;
import java.util.Date;
import java.util.Vector;

public interface SdpFactory {
   Attribute createAttribute(String var1, String var2);

   BandWidth createBandwidth(String var1, int var2);

   Connection createConnection(String var1) throws SdpException;

   Connection createConnection(String var1, int var2, int var3) throws SdpException;

   Connection createConnection(String var1, String var2, String var3) throws SdpException;

   Connection createConnection(String var1, String var2, String var3, int var4, int var5) throws SdpException;

   EMail createEMail(String var1);

   Info createInfo(String var1);

   Key createKey(String var1, String var2);

   Media createMedia(String var1, int var2, int var3, String var4, Vector var5) throws SdpException;

   MediaDescription createMediaDescription(String var1, int var2, int var3, String var4, int[] var5) throws IllegalArgumentException, SdpException;

   MediaDescription createMediaDescription(String var1, int var2, int var3, String var4, String[] var5);

   Origin createOrigin(String var1, long var2, long var4, String var6, String var7, String var8) throws SdpException;

   Origin createOrigin(String var1, String var2) throws SdpException;

   Phone createPhone(String var1);

   RepeatTime createRepeatTime(int var1, int var2, int[] var3);

   SessionDescription createSessionDescription() throws SdpException;

   SessionDescription createSessionDescription(String var1) throws SdpParseException;

   SessionName createSessionName(String var1);

   Time createTime() throws SdpException;

   Time createTime(Date var1, Date var2) throws SdpException;

   TimeDescription createTimeDescription() throws SdpException;

   TimeDescription createTimeDescription(Date var1, Date var2) throws SdpException;

   TimeDescription createTimeDescription(Time var1) throws SdpException;

   TimeZoneAdjustment createTimeZoneAdjustment(Date var1, int var2);

   URI createURI(URL var1) throws SdpException;

   Version createVersion(int var1);

   String formatMulticastAddress(String var1, int var2, int var3);

   Date getDateFromNtp(long var1);

   long getNtpTime(Date var1) throws SdpParseException;
}
