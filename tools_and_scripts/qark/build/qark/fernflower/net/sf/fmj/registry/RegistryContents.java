package net.sf.fmj.registry;

import java.util.Vector;
import net.sf.fmj.media.MimeTable;

class RegistryContents {
   Vector captureDeviceInfoList = new Vector();
   Vector contentPrefixList = new Vector();
   final MimeTable mimeTable = new MimeTable();
   Vector[] plugins = new Vector[]{new Vector(), new Vector(), new Vector(), new Vector(), new Vector()};
   Vector protocolPrefixList = new Vector();
}
