package com.karumi.dexter;

import android.content.Context;
import android.content.Intent;

class IntentProvider {
   public Intent get(Context var1, Class var2) {
      return new Intent(var1, var2);
   }
}
