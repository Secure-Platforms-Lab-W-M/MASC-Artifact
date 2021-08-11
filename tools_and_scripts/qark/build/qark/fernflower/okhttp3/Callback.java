package okhttp3;

import java.io.IOException;

public interface Callback {
   void onFailure(Call var1, IOException var2);

   void onResponse(Call var1, Response var2) throws IOException;
}
