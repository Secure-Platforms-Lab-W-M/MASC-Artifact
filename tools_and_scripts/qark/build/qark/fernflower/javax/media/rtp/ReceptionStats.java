package javax.media.rtp;

public interface ReceptionStats {
   int getPDUDrop();

   int getPDUDuplicate();

   int getPDUInvalid();

   int getPDUMisOrd();

   int getPDUProcessed();

   int getPDUlost();
}
