package edu.wm.cs.masc.mutation.operators.restrictive.stringoperator;

import edu.wm.cs.masc.mutation.properties.StringOperatorProperties;

    public class injectInsecureObject extends AStringOperator {

        public injectInsecureObject(StringOperatorProperties properties) {
            super(properties);
        }

        @Override
        public String mutation() {
            StringBuilder s = new StringBuilder();
            s.append("insecureObjectName")
                    .append(" ")
                    .append("foo")
                    .append(" ")
                    .append("=")
                    .append(" ")
                    .append("insecureObjectName")
                    .append("()");


//        Note: quick question: why do we not just build the insecure?
//        Is there a point to building safe and then replacing with unsafe

//                .append("(\"").append(secureParam).append("\".")
//                .append("replace(\"")
//                .append(secureParam).append("\", \"")
//                .append(insecureParam).append("\"));");
            return s.toString();
        }
    }


