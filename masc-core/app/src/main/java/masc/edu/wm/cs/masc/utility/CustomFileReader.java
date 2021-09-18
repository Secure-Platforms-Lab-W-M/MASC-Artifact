package masc.edu.wm.cs.masc.utility;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class CustomFileReader {

    public static String readFileAsString(String fileName) throws IOException {
//        data = new String(String.valueOf(Files.read(Paths.get(fileName))));
        return FileUtils.readFileToString(new File(fileName), "utf-8");
//        Path path = Paths.get(fileName);
//        Stream<String> lines = Files.lines(path);
//        String data = lines.collect(Collectors.joining("\n"));
//        lines.close();
//        return data+"\n";
    }
}
