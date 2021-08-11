package masc.edu.wm.cs.masc.reflection;

public class MemberContainer {

    private String memberName;
    private String memberFullName;
    private String container;

    public String getContainer() {
        return container;
    }

    public void setContainer(String container) {
        this.container = container;
    }

    public String getMemberFullName() {
        return memberFullName;
    }

    public void setMemberFullName(String memberFullName) {
        this.memberFullName = memberFullName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberName() {
        return memberName;
    }
    public MemberContainer(String memberName,String memberFullName, String container){
        this.memberFullName = memberFullName;
        this.memberName = memberName;
        this.container = container;
    }

    @Override
    public String toString() {
        return "{" +
                memberName + '\'' +
                memberFullName + '\'' +
                '}';
    }
}
