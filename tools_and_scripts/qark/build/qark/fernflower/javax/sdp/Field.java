package javax.sdp;

import java.io.Serializable;

public interface Field extends Serializable, Cloneable {
   Object clone();

   char getTypeChar();
}
