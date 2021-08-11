package gnu.java.zrtp;

public interface ZrtpCodes {
   public static enum InfoCodes {
      InfoCommitDHGenerated,
      InfoDH1DHGenerated,
      InfoHelloReceived,
      InfoInitConf1Received,
      InfoInitDH1Received,
      InfoRSMatchFound,
      InfoRespCommitReceived,
      InfoRespConf2Received,
      InfoRespDH2Received,
      InfoSecureStateOff,
      InfoSecureStateOn;

      static {
         ZrtpCodes.InfoCodes var0 = new ZrtpCodes.InfoCodes("InfoSecureStateOff", 10);
         InfoSecureStateOff = var0;
      }
   }

   public static enum InfoEnrollment {
      EnrollmentCanceled,
      EnrollmentFailed,
      EnrollmentOk,
      EnrollmentRequest;

      static {
         ZrtpCodes.InfoEnrollment var0 = new ZrtpCodes.InfoEnrollment("EnrollmentOk", 3);
         EnrollmentOk = var0;
      }
   }

   public static enum MessageSeverity {
      Info,
      Severe,
      Warning,
      ZrtpError;

      static {
         ZrtpCodes.MessageSeverity var0 = new ZrtpCodes.MessageSeverity("ZrtpError", 3);
         ZrtpError = var0;
      }
   }

   public static enum SevereCodes {
      SevereCannotSend,
      SevereCommitHMACFailed,
      SevereDH1HMACFailed,
      SevereDH2HMACFailed,
      SevereHelloHMACFailed,
      SevereNoTimer,
      SevereProtocolError,
      SevereSecurityException,
      SevereTooMuchRetries;

      static {
         ZrtpCodes.SevereCodes var0 = new ZrtpCodes.SevereCodes("SevereSecurityException", 8);
         SevereSecurityException = var0;
      }
   }

   public static enum WarningCodes {
      WarningCRCmismatch,
      WarningDHAESmismatch,
      WarningDHShort,
      WarningGoClearReceived,
      WarningNoExpectedRSMatch,
      WarningNoRSMatch,
      WarningSRTPauthError,
      WarningSRTPreplayError;

      static {
         ZrtpCodes.WarningCodes var0 = new ZrtpCodes.WarningCodes("WarningNoExpectedRSMatch", 7);
         WarningNoExpectedRSMatch = var0;
      }
   }

   public static enum ZrtpErrorCodes {
      ConfirmHMACWrong(112),
      CriticalSWError(32),
      DHErrorWrongHVI(98),
      DHErrorWrongPV(97),
      EqualZIDHello(144),
      GoClearNotAllowed(256),
      HelloCompMismatch(64),
      IgnorePacket,
      MalformedPacket(16),
      NoSharedSecret(86),
      NonceReused(128),
      SASuntrustedMiTM(99),
      UnsuppCiphertype(82),
      UnsuppHashType(81),
      UnsuppPKExchange(83),
      UnsuppSASScheme(85),
      UnsuppSRTPAuthTag(84),
      UnsuppZRTPVersion(48);

      public int value;

      static {
         ZrtpCodes.ZrtpErrorCodes var0 = new ZrtpCodes.ZrtpErrorCodes("IgnorePacket", 17, Integer.MAX_VALUE);
         IgnorePacket = var0;
      }

      private ZrtpErrorCodes(int var3) {
         this.value = var3;
      }
   }
}
