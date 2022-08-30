package edu.wm.cs.masc.mainScope.mutationmakers;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import edu.wm.cs.masc.mutation.operators.flexible.*;
import edu.wm.cs.masc.mutation.builders.generic.BuilderMainClass;
import edu.wm.cs.masc.mutation.builders.generic.BuilderMainMethod;
import edu.wm.cs.masc.mutation.operators.OperatorType;
//import masc.edu.wm.cs.mascDeprecated.operator.flexible.*;
import edu.wm.cs.masc.mutation.properties.FlexibleOperatorProperties;
import edu.wm.cs.masc.mutation.reflection.ClassReflection;
import edu.wm.cs.masc.mutation.reflection.EnumClassType;

public class FlexibleMutationMaker
        extends AMultiClassMutationMakerOptionalDependencies {
    FlexibleOperatorProperties p;

    public FlexibleMutationMaker(FlexibleOperatorProperties p) {
        this.p = p;

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
