package edu.wm.cs.mplus.model.location;

public class ElementLocation {

	private int line;
	private int startLine;
	private int endLine;
	private int startColumn;
	private int endColumn;


	public MutationLocation convertToMutationLocation(){
		MutationLocation mLoc = new MutationLocation();

		mLoc.setLine(line);
		mLoc.setStartLine(startLine);
		mLoc.setEndLine(endLine);
		mLoc.setStartColumn(startColumn);
		mLoc.setEndColumn(endColumn);
		
		return mLoc;
	}


	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}

	public int getStartColumn() {
		return startColumn;
	}

	public void setStartColumn(int startCoumn) {
		this.startColumn = startCoumn;
	}

	public int getEndColumn() {
		return endColumn;
	}

	public void setEndColumn(int endColumn) {
		this.endColumn = endColumn;
	}

	public int getStartLine() {
		return startLine;
	}

	public void setStartLine(int startLine) {
		this.startLine = startLine;
	}

	public int getEndLine() {
		return endLine;
	}

	public void setEndLine(int endLine) {
		this.endLine = endLine;
	}


}
