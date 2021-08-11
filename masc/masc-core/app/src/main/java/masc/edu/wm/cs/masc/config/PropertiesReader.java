package masc.edu.wm.cs.masc.config;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.apache.commons.configuration2.ex.ConfigurationException;

public class PropertiesReader {
    Configuration config;

    public PropertiesReader(String filename) throws ConfigurationException {
        Parameters params = new Parameters();

        FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
                new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
                        .configure(params.properties()
                                .setFileName(filename)
                                .setListDelimiterHandler(new DefaultListDelimiterHandler(',')));
        config = builder.getConfiguration();
    }

    public java.util.Iterator<String> getKeys() {
        return config.getKeys();
    }

    public String getValueForAKey(String key) {
        return config.getString(key);
    }

    public String[] getArrayValuesForAKey(String key) {
        return config.getStringArray(key);
    }
}
