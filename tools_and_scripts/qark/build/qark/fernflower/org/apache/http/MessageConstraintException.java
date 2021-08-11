package org.apache.http;

import java.nio.charset.CharacterCodingException;

public class MessageConstraintException extends CharacterCodingException {
   private static final long serialVersionUID = 6077207720446368695L;
   private final String message;

   public MessageConstraintException(String var1) {
      this.message = var1;
   }

   public String getMessage() {
      return this.message;
   }
}
