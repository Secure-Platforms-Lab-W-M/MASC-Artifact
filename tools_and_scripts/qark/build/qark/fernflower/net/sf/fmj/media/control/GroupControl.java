package net.sf.fmj.media.control;

import javax.media.Control;

public interface GroupControl extends AtomicControl {
   Control[] getControls();
}
