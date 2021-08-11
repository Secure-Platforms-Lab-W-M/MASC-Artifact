package javax.media;

import java.io.IOException;
import javax.media.protocol.DataSource;

public interface MediaProxy extends MediaHandler {
   DataSource getDataSource() throws IOException, NoDataSourceException;
}
