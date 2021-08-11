package javax.media.control;

import javax.media.Control;
import javax.media.rtp.RTPManager;

public interface RtspControl extends Control {
   RTPManager[] getRTPManagers();
}
