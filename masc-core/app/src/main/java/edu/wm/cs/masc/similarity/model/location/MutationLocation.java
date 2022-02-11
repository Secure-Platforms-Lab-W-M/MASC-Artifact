package edu.wm.cs.masc.similarity.model.location;

import edu.wm.cs.masc.similarity.model.MutationType;

public class MutationLocation {
	
	private String filePath;
	private int line;
	private int startLine;
	private int endLine;
	private int startColumn;
	private int endColumn;
	private int length;
	
	private MutationType type;
	
	public String getFilePath() {
		return filePath;
	}
	
	public void setFilePath(String filePath) {
		this.filePath = filePath;
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

	

	public MutationType getType() {
		return type;
	}

	public void setType(MutationType type) {
		this.type = type;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	
	
	
}
