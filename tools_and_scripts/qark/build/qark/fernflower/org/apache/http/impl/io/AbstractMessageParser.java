package org.apache.http.impl.io;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.HttpMessage;
import org.apache.http.MessageConstraintException;
import org.apache.http.ParseException;
import org.apache.http.ProtocolException;
import org.apache.http.config.MessageConstraints;
import org.apache.http.io.HttpMessageParser;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.message.BasicLineParser;
import org.apache.http.message.LineParser;
import org.apache.http.params.HttpParamConfig;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

public abstract class AbstractMessageParser implements HttpMessageParser {
   private static final int HEADERS = 1;
   private static final int HEAD_LINE = 0;
   private final List headerLines;
   protected final LineParser lineParser;
   private HttpMessage message;
   private final MessageConstraints messageConstraints;
   private final SessionInputBuffer sessionBuffer;
   private int state;

   public AbstractMessageParser(SessionInputBuffer var1, LineParser var2, MessageConstraints var3) {
      this.sessionBuffer = (SessionInputBuffer)Args.notNull(var1, "Session input buffer");
      if (var2 == null) {
         var2 = BasicLineParser.INSTANCE;
      }

      this.lineParser = (LineParser)var2;
      if (var3 == null) {
         var3 = MessageConstraints.DEFAULT;
      }

      this.messageConstraints = var3;
      this.headerLines = new ArrayList();
      this.state = 0;
   }

   @Deprecated
   public AbstractMessageParser(SessionInputBuffer var1, LineParser var2, HttpParams var3) {
      Args.notNull(var1, "Session input buffer");
      Args.notNull(var3, "HTTP parameters");
      this.sessionBuffer = var1;
      this.messageConstraints = HttpParamConfig.getMessageConstraints(var3);
      if (var2 == null) {
         var2 = BasicLineParser.INSTANCE;
      }

      this.lineParser = (LineParser)var2;
      this.headerLines = new ArrayList();
      this.state = 0;
   }

   public static Header[] parseHeaders(SessionInputBuffer var0, int var1, int var2, LineParser var3) throws HttpException, IOException {
      ArrayList var4 = new ArrayList();
      if (var3 == null) {
         var3 = BasicLineParser.INSTANCE;
      }

      return parseHeaders(var0, var1, var2, (LineParser)var3, var4);
   }

   public static Header[] parseHeaders(SessionInputBuffer var0, int var1, int var2, LineParser var3, List var4) throws HttpException, IOException {
      Args.notNull(var0, "Session input buffer");
      Args.notNull(var3, "Line parser");
      Args.notNull(var4, "Header line list");
      CharArrayBuffer var7 = null;
      CharArrayBuffer var8 = null;

      do {
         if (var7 == null) {
            var7 = new CharArrayBuffer(64);
         } else {
            var7.clear();
         }

         if (var0.readLine(var7) == -1 || var7.length() < 1) {
            Header[] var11 = new Header[var4.size()];

            for(var1 = 0; var1 < var4.size(); ++var1) {
               var7 = (CharArrayBuffer)var4.get(var1);

               try {
                  var11[var1] = var3.parseHeader(var7);
               } catch (ParseException var10) {
                  throw new ProtocolException(var10.getMessage());
               }
            }

            return var11;
         }

         if ((var7.charAt(0) == ' ' || var7.charAt(0) == '\t') && var8 != null) {
            int var5;
            for(var5 = 0; var5 < var7.length(); ++var5) {
               char var6 = var7.charAt(var5);
               if (var6 != ' ' && var6 != '\t') {
                  break;
               }
            }

            if (var2 > 0 && var8.length() + 1 + var7.length() - var5 > var2) {
               throw new MessageConstraintException("Maximum line length limit exceeded");
            }

            var8.append(' ');
            var8.append(var7, var5, var7.length() - var5);
         } else {
            var4.add(var7);
            Object var9 = null;
            var8 = var7;
            var7 = (CharArrayBuffer)var9;
         }
      } while(var1 <= 0 || var4.size() < var1);

      throw new MessageConstraintException("Maximum header count exceeded");
   }

   public HttpMessage parse() throws IOException, HttpException {
      int var1 = this.state;
      if (var1 != 0) {
         if (var1 != 1) {
            throw new IllegalStateException("Inconsistent parser state");
         }
      } else {
         try {
            this.message = this.parseHead(this.sessionBuffer);
         } catch (ParseException var3) {
            throw new ProtocolException(var3.getMessage(), var3);
         }

         this.state = 1;
      }

      Header[] var2 = parseHeaders(this.sessionBuffer, this.messageConstraints.getMaxHeaderCount(), this.messageConstraints.getMaxLineLength(), this.lineParser, this.headerLines);
      this.message.setHeaders(var2);
      HttpMessage var4 = this.message;
      this.message = null;
      this.headerLines.clear();
      this.state = 0;
      return var4;
   }

   protected abstract HttpMessage parseHead(SessionInputBuffer var1) throws IOException, HttpException, ParseException;
}
