package edu.wm.cs.masc.utils.commandPrompt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CommandPrompt {
    private static final String OS = System.getProperty("os.name").toLowerCase();
    private static final boolean IS_WINDOWS = (OS.contains("win"));
    private static final boolean IS_MAC = (OS.contains("mac"));
    private static final boolean IS_UNIX = (OS.contains("nix") || OS.contains("nux") || OS.indexOf("aix") > 0);
    private static final boolean IS_SOLARIS = (OS.contains("sunos"));

    String commandPrefix1, commandPrefix2;
    int successCode;

    public CommandPrompt(){
        if(IS_WINDOWS) {
            commandPrefix1 = "cmd";
            commandPrefix2 = "/c";
        }
        else {
            commandPrefix1 = "sh";
            commandPrefix2 = "-c";
        }
        successCode = 0;
    }
    private String getAllFromStream(InputStream inputStream) throws IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        StringBuilder answer = new StringBuilder();
        while((line=input.readLine()) != null) {
            answer.append(line).append("\n");
        }

        return answer.toString();
    }

    public CPOutput run_command(String command) {
        try {
            if(IS_WINDOWS)
            {
                command = command.replace("/", "\\");
                command = command.replaceFirst("^cd ", "pushd ");
            }

            ProcessBuilder builder = new ProcessBuilder(
                    commandPrefix1, commandPrefix2, command);
            builder.redirectErrorStream(true);
            Process p = builder.start();

            String err = getAllFromStream(p.getErrorStream());
            String inp = getAllFromStream(p.getInputStream());
            return new CPOutput(err, inp, command, p.waitFor() != successCode);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return new CPOutput("Command failed to run!", "", command, true);
    }
}
