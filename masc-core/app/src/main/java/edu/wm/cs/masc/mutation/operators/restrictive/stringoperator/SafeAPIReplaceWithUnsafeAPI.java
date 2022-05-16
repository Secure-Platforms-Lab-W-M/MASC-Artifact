//package edu.wm.cs.masc.mutation.operators.restrictive.stringoperator;
//
//import edu.wm.cs.masc.mutation.properties.StringOperatorProperties;
//
//public class SafeAPIReplaceWithUnsafeAPI extends AStringOperator {
//
//    public SafeAPIReplaceWithUnsafeAPI(StringOperatorProperties properties) {
//        super(properties);
//    }
//
//    @Override
//    public String mutation() {
//        StringBuilder s = new StringBuilder();
//        s.append(safe_api_name)
//                .append(".")
//                .append(invocation)
//                .append("(\"").append(parameter).append("\".")
//                .append("replace(\"")
//                .append(safe_api_name).append("\", \"")
//                .append(unsafe_api_name).append("\"));");
//        return s.toString();
//    }
//}

