package javax.media.control;

import javax.media.Control;

public interface H263Control extends Control {
   boolean getAdvancedPrediction();

   boolean getArithmeticCoding();

   int getBppMaxKb();

   boolean getErrorCompensation();

   int getHRD_B();

   boolean getPBFrames();

   boolean getUnrestrictedVector();

   boolean isAdvancedPredictionSupported();

   boolean isArithmeticCodingSupported();

   boolean isErrorCompensationSupported();

   boolean isPBFramesSupported();

   boolean isUnrestrictedVectorSupported();

   boolean setAdvancedPrediction(boolean var1);

   boolean setArithmeticCoding(boolean var1);

   boolean setErrorCompensation(boolean var1);

   boolean setPBFrames(boolean var1);

   boolean setUnrestrictedVector(boolean var1);
}
