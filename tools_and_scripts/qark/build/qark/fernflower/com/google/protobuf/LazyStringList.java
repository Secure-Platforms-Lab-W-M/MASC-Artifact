package com.google.protobuf;

import java.util.List;

public interface LazyStringList extends List {
   void add(ByteString var1);

   ByteString getByteString(int var1);

   List getUnderlyingElements();
}
