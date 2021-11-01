package masc.edu.wm.cs.masc.operator;

/**
 * Describes the different types of mutations related to restrictive and
 * flexible crypto APIs
 */
public enum OperatorType {
    StringDifferentCase,
    StringNoiseReplace,
    StringSafeReplaceWithUnsafe,
    StringUnsafeReplaceWithUnsafe,
    StringStringCaseTransform,
    StringValueInVariable,
    ByteLoop,
    ByteCurrentTime,
    Interproc,
    AIOEmptyFromAbstractType,
    AIOEmptyAbstractClassExtendsAbstractClass,
    AIOEmptyAbstractClassImplementsInterface,
    AIOEmptyInterfaceExtendsInterface,
    AIOGenericAbstractClassExtendsAbstractClass,
    AIOGenericAbstractClassImplementsInterface,
    AIOGenericInterfaceExtendsInterface,
    AIOSpecificAbstractClassExtendsAbstractClass,
    AIOSpecificAbstractClassImplementsInterface,
    AIOSpecificInterfaceExtendsInterface,
    IntValueInVariable,
    IntArithmetic,
    IntValueInVariableArithmetic,
    IntIterationMultipleCall,
    IntWhileLoopAccumulation,
    IntRoundValue,
    IntAbsoluteValue,
    IntNestedClass,
    IntSquareThenRoot,
}
