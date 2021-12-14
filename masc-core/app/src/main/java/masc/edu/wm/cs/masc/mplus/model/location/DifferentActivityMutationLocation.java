package edu.wm.cs.mplus.model.location;

public class DifferentActivityMutationLocation extends MutationLocation{

	private String otherActivity;
	private int importLine;
	
	
	public String getOtherActivity() {
		return otherActivity;
	}
	public void setOtherActivity(String otherActivity) {
		this.otherActivity = otherActivity;
	}
	public int getImportLine() {
		return importLine;
	}
	public void setImportLine(int importLine) {
		this.importLine = importLine;
	}

}
