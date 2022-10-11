package edu.wm.cs.masc.mutation.suppliers;

import java.util.HashMap;

import edu.wm.cs.masc.mutation.operators.IOperator;
import edu.wm.cs.masc.mutation.operators.OperatorType;
import edu.wm.cs.masc.mutation.operators.flexible.*;
import edu.wm.cs.masc.mutation.operators.restrictive.byteoperator.CurrentTime;
import edu.wm.cs.masc.mutation.operators.restrictive.interprocoperator.InterProcOperator;
import edu.wm.cs.masc.mutation.operators.restrictive.intoperator.*;
import edu.wm.cs.masc.mutation.operators.restrictive.stringoperator.*;
import edu.wm.cs.masc.mutation.operators.restrictive.stringoperator.ValueInVariable;
import edu.wm.cs.masc.mutation.properties.*;
import edu.wm.cs.masc.mutation.reflection.ClassReflection;
import edu.wm.cs.masc.mutation.reflection.EnumClassType;


public class MutationSupplier {
    HashMap<OperatorType, IOperator> operators = new HashMap<>();

    public MutationSupplier(StringOperatorProperties p) {
        operators.put(OperatorType.StringDifferentCase, new DifferentCase(p));
        operators.put(OperatorType.StringNoiseReplace, new NoiseReplace(p));
        operators.put(OperatorType.StringSafeReplaceWithUnsafe,
                new SafeReplaceWithUnsafe(p));
        operators.put(OperatorType.StringUnsafeReplaceWithUnsafe,
                new UnsafeReplaceWithUnsafe(p));
        operators.put(OperatorType.StringStringCaseTransform,
                new StringCaseTransform(p));
        operators.put(OperatorType.StringValueInVariable,
                new ValueInVariable(p));
    }

    public MutationSupplier(FlexibleOperatorProperties p) {
        EnumClassType apiType = ClassReflection.getType(p.getApiName());
        this.operators.put(OperatorType.AIOEmptyFromAbstractType,
                new AIOEmptyFromAbstractType(p));

        // only if the parent type is abstract class, we can use extension
        if (apiType == EnumClassType.Abstract) {
            this.operators
                    .put(OperatorType
                                    .AIOEmptyAbstractClassExtendsAbstractClass,
                            new AIOEmptyAbstractClassExtendsAbstractClass(
                                    p));
            this.operators
                    .put(OperatorType.AIOGenericAbstractClassExtendsAbstractClass,
                            new AIOGeneric(
                                    p));
            this.operators
                    .put(OperatorType
                                    .AIOSpecificAbstractClassExtendsAbstractClass,
                            new AIOSpecific(
                                    p));
        }

        // if the type is of Interface, we can either extend / implement via
        // other abstract types

        // interface that extends interface

        this.operators.put(OperatorType
                        .AIOEmptyInterfaceExtendsInterface,
                new AIOEmptyInterfaceExtendsInterface(p));
//        abstract class that implements interface
        this.operators.put(OperatorType
                        .AIOEmptyAbstractClassImplementsInterface,
                new AIOEmptyAbstractClassImplementsInterface(p));
        this.operators
                .put(OperatorType.AIOGenericAbstractClassImplementsInterface,
                        new AIOGeneric(p));

        this.operators
                .put(OperatorType.AIOGenericInterfaceExtendsInterface,
                        new AIOGeneric(p));
        this.operators
                .put(OperatorType.AIOSpecificAbstractClassImplementsInterface,
                        new AIOSpecific(
                                p));
        this.operators.put(OperatorType.AIOSpecificInterfaceExtendsInterface,
                new AIOSpecific(p));
    }

    public MutationSupplier(ByteOperatorProperties p) {
        operators.put(OperatorType.ByteCurrentTime, new CurrentTime(p));
        operators.put(OperatorType.ByteLoop, new CurrentTime(p));
    }

    public MutationSupplier(InterprocProperties p) {
        operators.put(OperatorType.Interproc, new InterProcOperator(p));
    }

    public MutationSupplier(IntOperatorProperties p) {
        operators.put(OperatorType.IntValueInVariable, new edu.wm.cs.masc.mutation.operators.restrictive.intoperator.ValueInVariable(p));
        operators.put(OperatorType.IntArithmetic, new Arithmetic(p));
        operators.put(OperatorType.IntValueInVariableArithmetic, new ValueInVariableArithmetic(p));
        operators.put(OperatorType.IntIterationMultipleCall, new IterationMultipleCall(p));
        operators.put(OperatorType.IntWhileLoopAccumulation, new WhileLoopAccumulation(p));
        operators.put(OperatorType.IntRoundValue, new RoundValue(p));
        operators.put(OperatorType.IntAbsoluteValue, new AbsoluteValue(p));
        operators.put(OperatorType.IntNestedClass, new NestedClass(p));
        operators.put(OperatorType.IntFromString, new FromString(p));
        operators.put(OperatorType.Overflow, new Overflow(p));
    }

    public HashMap<OperatorType, IOperator> getOperators(){
        return operators;
    }


}
