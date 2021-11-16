package edu.wm.cs.muse.dataleak.support;

public class TypeMismatchException extends Exception {
	
  /**
  * Exception thrown after a type mismatch for ASTNodes
  * 
  * Author: Ian Wolff
  */
  private static final long serialVersionUID = 1L;
  
  public TypeMismatchException() {
    super();
  }

  public TypeMismatchException(String message) {
    super(message);
  }
}
