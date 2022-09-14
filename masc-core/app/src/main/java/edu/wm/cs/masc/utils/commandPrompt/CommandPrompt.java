package edu.wm.cs.masc.utils.commandPrompt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CommandPrompt {
    private String getAllFromStream(InputStream inputStream) throws IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        StringBuilder answer = new StringBuilder();
        while((line=input.readLine()) != null) {
            answer.append(line).append("\n");
        }

        return answer.toString();
    }

    private String processCommandString(String command) {
        String os = System.getProperty("os.name");
        if(os.startsWith("Windows")){
            command = command.replace("/", "\\");
        }
        return command;
    }

    public CPOutput run_command(String command) {
        try {
            command = processCommandString(command);
            Runtime rt = Runtime.getRuntime();
            Process pr = null;

            if(System.getProperty("os.name").startsWith("Windows")){
                pr = rt.exec("cmd /c " + command);
            }


            String err = getAllFromStream(pr.getErrorStream());
            String inp = getAllFromStream(pr.getInputStream());

            return new CPOutput(err, inp, command, pr.waitFor() != 0);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return new CPOutput("Command failed to run!", "", command, true);
    }
}
