package org.apache.http.impl.auth;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.http.Consts;
import org.apache.http.HeaderElement;
import org.apache.http.HttpRequest;
import org.apache.http.auth.ChallengeState;
import org.apache.http.auth.MalformedChallengeException;
import org.apache.http.message.BasicHeaderValueParser;
import org.apache.http.message.ParserCursor;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.CharsetUtils;

public abstract class RFC2617Scheme extends AuthSchemeBase implements Serializable {
   private static final long serialVersionUID = -2845454858205884623L;
   private transient Charset credentialsCharset;
   private final Map params;

   public RFC2617Scheme() {
      this(Consts.ASCII);
   }

   public RFC2617Scheme(Charset var1) {
      this.params = new HashMap();
      if (var1 == null) {
         var1 = Consts.ASCII;
      }

      this.credentialsCharset = var1;
   }

   @Deprecated
   public RFC2617Scheme(ChallengeState var1) {
      super(var1);
      this.params = new HashMap();
      this.credentialsCharset = Consts.ASCII;
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.defaultReadObject();
      Charset var2 = CharsetUtils.get(var1.readUTF());
      this.credentialsCharset = var2;
      if (var2 == null) {
         this.credentialsCharset = Consts.ASCII;
      }

      this.challengeState = (ChallengeState)var1.readObject();
   }

   private void readObjectNoData() throws ObjectStreamException {
   }

   private void writeObject(ObjectOutputStream var1) throws IOException {
      var1.defaultWriteObject();
      var1.writeUTF(this.credentialsCharset.name());
      var1.writeObject(this.challengeState);
   }

   String getCredentialsCharset(HttpRequest var1) {
      String var2 = (String)var1.getParams().getParameter("http.auth.credential-charset");
      String var3 = var2;
      if (var2 == null) {
         var3 = this.getCredentialsCharset().name();
      }

      return var3;
   }

   public Charset getCredentialsCharset() {
      Charset var1 = this.credentialsCharset;
      return var1 != null ? var1 : Consts.ASCII;
   }

   public String getParameter(String var1) {
      return var1 == null ? null : (String)this.params.get(var1.toLowerCase(Locale.ROOT));
   }

   protected Map getParameters() {
      return this.params;
   }

   public String getRealm() {
      return this.getParameter("realm");
   }

   protected void parseChallenge(CharArrayBuffer var1, int var2, int var3) throws MalformedChallengeException {
      HeaderElement[] var5 = BasicHeaderValueParser.INSTANCE.parseElements(var1, new ParserCursor(var2, var1.length()));
      this.params.clear();
      var3 = var5.length;

      for(var2 = 0; var2 < var3; ++var2) {
         HeaderElement var4 = var5[var2];
         this.params.put(var4.getName().toLowerCase(Locale.ROOT), var4.getValue());
      }

   }
}
