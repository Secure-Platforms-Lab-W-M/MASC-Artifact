package javax.media.bean.playerbean;

import org.atalk.android.util.java.awt.Image;
import org.atalk.android.util.java.beans.BeanDescriptor;
import org.atalk.android.util.java.beans.EventSetDescriptor;
import org.atalk.android.util.java.beans.IntrospectionException;
import org.atalk.android.util.java.beans.PropertyDescriptor;
import org.atalk.android.util.java.beans.SimpleBeanInfo;

public class MediaPlayerBeanInfo extends SimpleBeanInfo {
   private final PropertyDescriptor[] propertyDescriptors;

   public MediaPlayerBeanInfo() {
      try {
         this.propertyDescriptors = new PropertyDescriptor[]{buildPropertyDescriptor(MediaPlayer.class, "mediaLocation", "media location", MediaPlayerMediaLocationEditor.class, true), buildPropertyDescriptor(MediaPlayer.class, "controlPanelVisible", "show control panel", (Class)null, true), buildPropertyDescriptor(MediaPlayer.class, "cachingControlVisible", "show caching control", (Class)null, true), buildPropertyDescriptor(MediaPlayer.class, "fixedAspectRatio", "fixedAspectRatio", (Class)null, true), buildPropertyDescriptor(MediaPlayer.class, "playbackLoop", "loop", (Class)null, true), buildPropertyDescriptor(MediaPlayer.class, "volumeLevel", "volume", MediaPlayerVolumePropertyEditor.class, true), buildPropertyDescriptor(MediaPlayer.class, "background", "background", (Class)null, false), buildPropertyDescriptor(MediaPlayer.class, "foreground", "foreground", (Class)null, false), buildPropertyDescriptor(MediaPlayer.class, "font", "font", (Class)null, false)};
      } catch (IntrospectionException var2) {
         throw new RuntimeException(var2);
      }
   }

   private static PropertyDescriptor buildPropertyDescriptor(Class var0, String var1, String var2, Class var3, boolean var4) throws IntrospectionException {
      PropertyDescriptor var5 = new PropertyDescriptor(var1, var0);
      var5.setDisplayName(var2);
      var5.setPropertyEditorClass(var3);
      var5.setBound(var4);
      return var5;
   }

   public BeanDescriptor getBeanDescriptor() {
      BeanDescriptor var1 = new BeanDescriptor(MediaPlayer.class);
      var1.setName("MediaPlayer");
      var1.setDisplayName("MediaPlayer Bean");
      var1.setShortDescription("MediaPlayer Bean");
      return var1;
   }

   public int getDefaultPropertyIndex() {
      return 1;
   }

   public EventSetDescriptor[] getEventSetDescriptors() {
      throw new Error(new IntrospectionException("Method \"controllerUpdate\" should have argument \"ControllerUpdateEvent\""));
   }

   public Image getIcon(int var1) {
      return null;
   }

   public PropertyDescriptor[] getPropertyDescriptors() {
      return this.propertyDescriptors;
   }
}
