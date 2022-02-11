package edu.wm.cs.masc.mutation.properties;

import org.apache.commons.configuration2.ex.ConfigurationException;

public class FlexibleOperatorProperties extends AOperatorProperties {
    private final boolean booleanReturn;

    public String getSpecificCondition() {
        return specificCondition;
    }

    private final String specificCondition;
    public boolean getBooleanInCondition() {
        return booleanInCondition;
    }

    //    private final boolean hasDependencies;
    private final boolean booleanInCondition;

    public boolean getBooleanReturn() {
        return booleanReturn;
    }

//    public boolean hasDependencies() {
//        return hasDependencies;
//    }


    public FlexibleOperatorProperties(String path)
            throws ConfigurationException {
        super(path);
        booleanReturn = Boolean
                .parseBoolean(reader.getValueForAKey("booleanReturn"));
        booleanInCondition = Boolean
                .parseBoolean(reader.getValueForAKey("booleanInCondition"));
        specificCondition = reader.getValueForAKey("specificCondition");
//        hasDependencies = Boolean
//                .parseBoolean(reader.getValueForAKey("hasDependencies"));
    }

}
