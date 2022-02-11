package edu.wm.cs.masc.exhaustive.mdroid;

public class MethodDeclarationVO {

	private String classQualifiedName;
	private String methodName;
	private int line;
	private int start;
	private int length;
	
	
	
	public MethodDeclarationVO(String classQualifiedName, String methodName) {
		this.classQualifiedName = classQualifiedName;
		this.methodName = methodName;
	}
	
	
	
	public MethodDeclarationVO(String classQualifiedName, String methodName, int start, int length) {
		super();
		this.classQualifiedName = classQualifiedName;
		this.methodName = methodName;
		this.start = start;
		this.length = length;
	}
	
	
	



	public MethodDeclarationVO(String classQualifiedName, String methodName, int line, int start, int length) {
		super();
		this.classQualifiedName = classQualifiedName;
		this.methodName = methodName;
		this.line = line;
		this.start = start;
		this.length = length;
	}



	public String getClassQualifiedName() {
		return classQualifiedName;
	}
	public void setClassQualifiedName(String classQualifiedName) {
		this.classQualifiedName = classQualifiedName;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}



	public int getLine() {
		return line;
	}



	public void setLine(int line) {
		this.line = line;
	}



	public int getStart() {
		return start;
	}



	public void setStart(int start) {
		this.start = start;
	}



	public int getLength() {
		return length;
	}



	public void setLength(int length) {
		this.length = length;
	}
	
	public String getFullName(){
		return this.classQualifiedName+"."+this.getMethodName();
	}
	
	
	
}
