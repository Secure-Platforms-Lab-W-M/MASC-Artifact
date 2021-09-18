package masc.edu.wm.cs.masc.barebone.mutationmakers;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import masc.edu.wm.cs.masc.builders.generic.BuilderMainClass;
import masc.edu.wm.cs.masc.builders.generic.BuilderMainMethod;
import masc.edu.wm.cs.masc.operator.OperatorType;
import masc.edu.wm.cs.masc.operator.flexible.*;
import masc.edu.wm.cs.masc.properties.FlexibleOperatorProperties;
import masc.edu.wm.cs.masc.reflection.ClassReflection;
import masc.edu.wm.cs.masc.reflection.EnumClassType;

public class FlexibleMutationMaker
        extends AMultiClassMutationMakerOptionalDependencies {
    FlexibleOperatorProperties p;

    public FlexibleMutationMaker(FlexibleOperatorProperties p) {
        this.p = p;

    }

    @Override
    public String getContent(OperatorType operatorType) {
        TypeSpec.Builder builder = BuilderMainClass
                .getClassBody(p.getClassName());
        System.out.println("Processing: " + operatorType.name());
        MethodSpec.Builder mainMethod = BuilderMainMethod
                .getMethodSpecWithException();
        mainMethod.addCode(operators.get(operatorType).mutation());
        builder.addMethod(mainMethod.build());
        return builder.build().toString();
    }

    @Override
    public void populateOperators() {
        /*
                AIOEmptyAbstractClassExtendsAbstractClass,
                AIOGenericAbstractClassExtendsAbstractClass,
                AIOSpecificAbstractClassExtendsAbstractClass

                AIOEmptyAbstractClassImplementsInterface,
                AIOEmptyInterfaceExtendsInterface,

            AIOGenericAbstractClassImplementsInterface,
            AIOGenericInterfaceExtendsInterface,

            AIOSpecificAbstractClassImplementsInterface,
            AIOSpecificInterfaceExtendsInterface,
         */
        EnumClassType apiType = ClassReflection.getType(p.getApiName());
        // basic
        this.operators.put(OperatorType.AIOEmptyFromAbstractType,
                new AIOEmptyFromAbstractType(this.p));

        // only if the parent type is abstract class, we can use extension
        if (apiType == EnumClassType.Abstract) {
            this.operators
                    .put(OperatorType
                                    .AIOEmptyAbstractClassExtendsAbstractClass,
                            new AIOEmptyAbstractClassExtendsAbstractClass(
                                    this.p));
            this.operators
                    .put(OperatorType.AIOGenericAbstractClassExtendsAbstractClass,
                            new AIOGeneric(
                                    this.p));
            this.operators
                    .put(OperatorType
                                    .AIOSpecificAbstractClassExtendsAbstractClass,
                            new AIOSpecific(
                                    this.p));
        }

        // if the type is of Interface, we can either extend / implement via
        // other abstract types

        // interface that extends interface

        this.operators.put(OperatorType
                        .AIOEmptyInterfaceExtendsInterface,
                new AIOEmptyInterfaceExtendsInterface(this.p));
//        abstract class that implements interface
        this.operators.put(OperatorType
                        .AIOEmptyAbstractClassImplementsInterface,
                new AIOEmptyAbstractClassImplementsInterface(this.p));
        this.operators
                .put(OperatorType.AIOGenericAbstractClassImplementsInterface,
                        new AIOGeneric(this.p));

        this.operators
                .put(OperatorType.AIOGenericInterfaceExtendsInterface,
                        new AIOGeneric(this.p));
        this.operators
                .put(OperatorType.AIOSpecificAbstractClassImplementsInterface,
                        new AIOSpecific(
                                this.p));
        this.operators.put(OperatorType.AIOSpecificInterfaceExtendsInterface,
                new AIOSpecific(this.p));

    }

    @Override
    protected boolean hasDependencies() {
        return true;
//        return p.hasDependencies();
    }
}
