package edu.wm.cs.mplus.model.location;

public class ArgumentMutationLocation extends MutationLocation{

	public static final int INT = 0;
	public static final int DOUBLE = 1;
	public static final int LONG = 2;
	public static final int CHAR = 3;
	public static final int BOOLEAN = 4;
	public static final int STRING = 5;

	
	private int amlType;


	public int getAmlType() {
		return amlType;
	}


	public void setAmlType(int amlType) {
		this.amlType = amlType;
	}

	
	
}
