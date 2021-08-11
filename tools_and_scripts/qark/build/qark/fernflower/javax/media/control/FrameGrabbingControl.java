package javax.media.control;

import javax.media.Buffer;
import javax.media.Control;

public interface FrameGrabbingControl extends Control {
   Buffer grabFrame();
}
