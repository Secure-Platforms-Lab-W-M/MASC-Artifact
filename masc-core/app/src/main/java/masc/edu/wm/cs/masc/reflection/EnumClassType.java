package masc.edu.wm.cs.masc.reflection;

public enum EnumClassType {
    Interface("Interface"),
    Class("Class"),
    Abstract("Abstract")
    ;
    public final String label;
    EnumClassType(String label) {
        this.label = label;
    }
}
