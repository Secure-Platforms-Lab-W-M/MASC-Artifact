package edu.wm.cs.masc.mutation.reflection;

public enum InheritanceType {
    Relation_Extends("extends"),
    Relation_Implements("implements");

    private final String s;

    InheritanceType(String s) {
        this.s = s;
    }

    @Override
    public String toString() {
        return s;
    }
}
