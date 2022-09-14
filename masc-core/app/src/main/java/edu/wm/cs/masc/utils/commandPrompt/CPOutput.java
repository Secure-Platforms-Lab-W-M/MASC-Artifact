package edu.wm.cs.masc.utils.commandPrompt;

public class CPOutput {
    public String errorStream, inputStream, command;
    public boolean error;

    public CPOutput(String errorStream, String inputStream, String command, boolean error) {
        this.errorStream = errorStream;
        this.inputStream = inputStream;
        this.command = command;
        this.error = error;
    }

    public String getCombinedOutput() {
        return errorStream + "\n" + inputStream;
    }


}
