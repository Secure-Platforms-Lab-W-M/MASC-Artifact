package net.sf.fmj.utility;

import com.lti.utils.StringUtils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.media.Format;

public class SerializationUtils {
   public static Format deserialize(String var0) throws IOException, ClassNotFoundException {
      ByteArrayInputStream var3 = new ByteArrayInputStream(StringUtils.hexStringToByteArray(var0));
      ObjectInputStream var1 = new ObjectInputStream(var3);
      Object var2 = var1.readObject();
      var1.close();
      var3.close();
      return (Format)var2;
   }

   public static String serialize(Format var0) throws IOException {
      ByteArrayOutputStream var1 = new ByteArrayOutputStream();
      ObjectOutputStream var2 = new ObjectOutputStream(var1);
      var2.writeObject(var0);
      var2.close();
      var1.close();
      return StringUtils.byteArrayToHexString(var1.toByteArray());
   }
}
