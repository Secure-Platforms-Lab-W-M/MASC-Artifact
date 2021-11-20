package masc.edu.wm.cs.masc.muse;

import masc.edu.wm.cs.masc.properties.AOperatorProperties;
import masc.edu.wm.cs.masc.properties.IntOperatorProperties;
import org.apache.commons.configuration2.ex.ConfigurationException;

public class MuseIntOperatorProperties extends IntOperatorProperties {

    protected final String lib4ast;
    protected final String appSrc;
    protected final String scope;
    protected final String appName;

    public MuseIntOperatorProperties(String path)
            throws ConfigurationException {
        super(path);
        lib4ast = reader.getValueForAKey("lib4ast");
        appSrc = reader.getValueForAKey("appSrc");
        scope = reader.getValueForAKey("operatorType");
        appName = reader.getValueForAKey("appName");

    }

    public String getLib4ast() {return lib4ast;}

    public String getAppSrc() {return appSrc;}

    public String getScope() {return scope;}

    public String getAppName() {return appName;}

}
