package net.sf.fmj.media.datasink;

public interface RandomAccess {
   void setEnabled(boolean var1);

   boolean write(long var1, int var3);
}
