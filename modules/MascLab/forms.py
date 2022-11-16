from django import forms


class StringOperatorForm(forms.Form):
    OPTIONS = (
        ("StringDifferentCase", "StringDifferentCase"),
        ("StringNoiseReplace", "StringNoiseReplace"),
        ("StringSafeReplaceWithUnsafe", "StringSafeReplaceWithUnsafe"),
        ("StringStringCaseTransform", "StringStringCaseTransform"),
        ("StringValueInVariable", "StringValueInVariable"),
        ("StringUnsafeReplaceWithUnsafe", "StringUnsafeReplaceWithUnsafe"),
    )
    Operators_List = forms.MultipleChoiceField(widget=forms.CheckboxSelectMultiple,
                                               choices=OPTIONS)


class FlexibleOperatorForm(forms.Form):
    OPTIONS = (
        ("AIOEmptyFromAbstractType", "AIOEmptyFromAbstractType"),
        ("AIOEmptyAbstractClassExtendsAbstractClass", "AIOEmptyAbstractClassExtendsAbstractClass"),
        ("AIOGenericAbstractClassExtendsAbstractClass", "AIOGenericAbstractClassExtendsAbstractClass"),
        ("AIOSpecificAbstractClassExtendsAbstractClass", "AIOSpecificAbstractClassExtendsAbstractClass"),
        ("AIOEmptyInterfaceExtendsInterface", "AIOEmptyInterfaceExtendsInterface"),
        ("AIOEmptyAbstractClassImplementsInterface", "AIOEmptyAbstractClassImplementsInterface"),
        ("AIOGenericAbstractClassImplementsInterface", "AIOGenericAbstractClassImplementsInterface"),
        ("AIOGenericInterfaceExtendsInterface", "AIOGenericInterfaceExtendsInterface"),
        ("AIOSpecificAbstractClassImplementsInterface", "AIOSpecificAbstractClassImplementsInterface"),
        ("AIOSpecificInterfaceExtendsInterface", "AIOSpecificInterfaceExtendsInterface"),
    )
    Operators_List = forms.MultipleChoiceField(widget=forms.CheckboxSelectMultiple,
                                               choices=OPTIONS)

class ByteOperatorForm(forms.Form):
    OPTIONS = (
        ("ByteCurrentTime", "ByteCurrentTime"),
        ("ByteLoop", "ByteLoop"),
    )
    Operators_List = forms.MultipleChoiceField(widget=forms.CheckboxSelectMultiple,
                                               choices=OPTIONS)

class InterProcOperatorForm(forms.Form):
    OPTIONS = (
        ("Interproc", "Interproc"),
    )
    Operators_List = forms.MultipleChoiceField(widget=forms.CheckboxSelectMultiple,
                                               choices=OPTIONS)

class IntOperatorForm(forms.Form):
    OPTIONS = (
        ("IntValueInVariable", "IntValueInVariable"),
        ("IntArithmetic", "IntArithmetic"),
        ("IntValueInVariableArithmetic", "IntValueInVariableArithmetic"),
        ("IntIterationMultipleCall", "IntIterationMultipleCall"),
        ("IntWhileLoopAccumulation", "IntWhileLoopAccumulation"),
        ("IntRoundValue", "IntRoundValue"),
        ("IntAbsoluteValue", "IntAbsoluteValue"),
        ("IntNestedClass", "IntNestedClass"),
        ("IntFromString", "IntFromString"),
        ("IntFromString", "IntFromString"),
    )
    Operators_List = forms.MultipleChoiceField(widget=forms.CheckboxSelectMultiple,
                                               choices=OPTIONS)

class NullOperatorForm(forms.Form):
    OPTIONS = (
        ("no_such_operator", "no_such_operator"),
    )
    Operators_List = forms.MultipleChoiceField(widget=forms.CheckboxSelectMultiple,
                                               choices=OPTIONS)

class CheckSave(forms.Form):
    OPTIONS = (
        ("true", "Yes"),
    )
    Save_Changes = forms.MultipleChoiceField(widget=forms.CheckboxSelectMultiple,
                                               choices=OPTIONS)