package edu.wm.cs.masc.mutation.builders.flexible;

import edu.wm.cs.masc.mutation.operators.OperatorType;
import edu.wm.cs.masc.mutation.properties.AOperatorProperties;
import edu.wm.cs.masc.mutation.reflection.InheritanceType;
import edu.wm.cs.masc.utils.file.FilePack;

import java.util.ArrayList;

public class FlexibleFactory {
    private static final String ABSTRACT_CLASS = "abstract class ";
    private static final String INTERFACE = "interface";

    private static String getClassBody(AOperatorProperties p,
                                       String otherClassNature,
                                       InheritanceType relation) {
        return "public " +
                otherClassNature + " "
                + p.getOtherClassName() + " "
                + relation.toString() + " "
                + p.getApiName()
                + "{ }\n";
    }

    private static void abstractClassExtendsAbstractClass(
            OperatorType type,
            AOperatorProperties p,
            ArrayList<FilePack> filePacks) {
        filePacks.add(new FilePack(
                p.getOtherClassName(),
                p.getOutputDir(),
                getClassBody(
                        p,
                        ABSTRACT_CLASS,
                        InheritanceType.Relation_Extends)));
//        return filePacks;
    }

    private static void interfaceExtendsInterface(OperatorType type,
                                                  AOperatorProperties p,
                                                  ArrayList<FilePack> filePacks) {
        filePacks.add(new FilePack(p.getOtherClassName(), p.getOutputDir(),
                getClassBody(p, INTERFACE,
                        InheritanceType.Relation_Extends)));
    }

    private static void abstractClassImplementsInterface(OperatorType type,
                                                         AOperatorProperties p,
                                                         ArrayList<FilePack> filePacks) {
        filePacks.add(new FilePack(p.getOtherClassName(), p.getOutputDir(),
                getClassBody(p, INTERFACE,
                        InheritanceType.Relation_Implements)));
    }


    public static ArrayList<FilePack> getContent(OperatorType type,
                                                 AOperatorProperties p) {
        ArrayList<FilePack> filePacks;
        filePacks = new ArrayList<>();
        if (type == OperatorType.AIOEmptyFromAbstractType) {
            return null;
        } else if (type == OperatorType.AIOEmptyAbstractClassExtendsAbstractClass) {
            abstractClassExtendsAbstractClass(type, p, filePacks);
//            filePacks.add(new FilePack(
//                    p.getOtherClassName(),
//                    p.getOutputDir(),
//                    getClassBody(
//                            p,
//                            ABSTRACT_CLASS,
//                            InheritanceType.Relation_Extends))
//            );
        } else if (type == OperatorType.AIOEmptyInterfaceExtendsInterface) {
//            filePacks.add(new FilePack(p.getOtherClassName(), p
//            .getOutputDir(),
//                    getClassBody(p, INTERFACE,
//                            InheritanceType.Relation_Extends)));
            interfaceExtendsInterface(type, p, filePacks);
        } else if (type == OperatorType.AIOEmptyAbstractClassImplementsInterface) {
            abstractClassImplementsInterface(type, p, filePacks);
//            filePacks.add(new FilePack(p.getOtherClassName(), p
//            .getOutputDir(),
//                    getClassBody(p, INTERFACE,
//                            InheritanceType.Relation_Implements)));

        } else if (type == OperatorType.AIOGenericAbstractClassExtendsAbstractClass) {
            abstractClassExtendsAbstractClass(type, p, filePacks);
//            filePacks.add(new FilePack(
//                    p.getOtherClassName(),
//                    p.getOutputDir(),
//                    getClassBody(
//                            p,
//                            ABSTRACT_CLASS,
//                            InheritanceType.Relation_Extends)));
        } else if (type == OperatorType.AIOGenericAbstractClassImplementsInterface) {
            abstractClassImplementsInterface(type, p, filePacks);
//            filePacks.add(new FilePack(
//                    p.getOtherClassName(),
//                    p.getOutputDir(),
//                    getClassBody(
//                            p,
//                            ABSTRACT_CLASS,
//                            InheritanceType.Relation_Implements)));
        } else if (type == OperatorType.AIOGenericInterfaceExtendsInterface) {
            interfaceExtendsInterface(type, p, filePacks);
//            filePacks.add(new FilePack(
//                    p.getOtherClassName(),
//                    p.getOutputDir(),
//                    getClassBody(
//                            p,
//                            INTERFACE,
//                            InheritanceType.Relation_Extends)));
        } else if (type == OperatorType.AIOSpecificAbstractClassImplementsInterface) {
            abstractClassImplementsInterface(type, p, filePacks);
//            filePacks.add(new FilePack(p.getOtherClassName(), p
//            .getOutputDir(),
//                    getClassBody(p, ABSTRACT_CLASS,
//                            InheritanceType.Relation_Implements)));
        } else if (type == OperatorType.AIOSpecificAbstractClassExtendsAbstractClass) {
//            filePacks.add(new FilePack(
//                    p.getOtherClassName(),
//                    p.getOutputDir(),
//                    getClassBody(
//                            p,
//                            ABSTRACT_CLASS,
//                            InheritanceType.Relation_Extends)));
            abstractClassExtendsAbstractClass(type, p, filePacks);

        } else if (type == OperatorType.AIOSpecificInterfaceExtendsInterface) {
//            filePacks.add(new FilePack(p.getOtherClassName(), p
//            .getOutputDir(),
//                    getClassBody(p, INTERFACE,
//                            InheritanceType.Relation_Extends)));
            interfaceExtendsInterface(type, p, filePacks);
        }

        return filePacks;
    }
}
