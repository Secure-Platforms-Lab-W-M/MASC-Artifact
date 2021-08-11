package edu.wm.cs.semeru.runonuithread;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
   protected void onCreate(Bundle var1) {
      super.onCreate(var1);
      this.setContentView(2131296283);
      this.runOnUiThread(new Runnable() {
         public void run() {
            (new Random()).nextInt();
         }
      });
   }
}
