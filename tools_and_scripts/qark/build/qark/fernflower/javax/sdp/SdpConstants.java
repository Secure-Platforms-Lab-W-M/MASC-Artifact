package javax.sdp;

public interface SdpConstants {
   int AVP_DEFINED_STATIC_MAX = 35;
   int AVP_DYNAMIC_MIN = -35;
   int CELB = 25;
   // $FF: renamed from: CN int
   int field_6 = 13;
   int CN_DEPRECATED = 19;
   int DVI4_11025 = 16;
   int DVI4_16000 = 6;
   int DVI4_22050 = 17;
   int DVI4_8000 = 5;
   String DYNAMIC = "-35";
   String FMTP = "FMTP";
   int G722 = 9;
   int G723 = 4;
   int G726_32 = 2;
   int G728 = 15;
   int G729 = 18;
   int GSM = 3;
   int H261 = 31;
   int H263 = 34;
   int JPEG = 26;
   int L16_1CH = 11;
   int L16_2CH = 10;
   int LPC = 7;
   int MP2T = 33;
   int MPA = 14;
   int MPV = 32;
   long NTP_CONST = 2208988800L;
   // $FF: renamed from: NV int
   int field_7 = 28;
   int PCMA = 8;
   int PCMU = 0;
   int QCELP = 12;
   String RESERVED = "0";
   String RTPMAP = "rtpmap";
   String RTP_AVP = "RTP/AVP";
   int TENSIXTEEN = 1;
   String UNASSIGNED = "35";
   int[] avpChannels = new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, -1, -1, 1, 1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
   int[] avpClockRates = new int[]{8000, 8000, 8000, 8000, 8000, 8000, 16000, 8000, 8000, 8000, 44100, 44100, -1, -1, 90000, 8000, -1, -1, -1, -1, -1, -1, -1, -1, -1, 90000, 90000, -1, 90000, -1, -1, 90000, 90000, 90000, -1};
   String[] avpTypeNames = new String[]{"PCMU", "1016", "G721", "GSM", "G723", "DVI4_8000", "DVI4_16000", "LPC", "PCMA", "G722", "L16_2CH", "L16_1CH", "QCELP", "CN", "MPA", "G728", "DVI4_11025", "DVI4_22050", "G729", "CN_DEPRECATED", "UNASSIGNED", "UNASSIGNED", "UNASSIGNED", "UNASSIGNED", "UNASSIGNED", "CelB", "JPEG", "UNASSIGNED", "nv", "UNASSIGNED", "UNASSIGNED", "H261", "MPV", "MP2T", "H263"};
}
