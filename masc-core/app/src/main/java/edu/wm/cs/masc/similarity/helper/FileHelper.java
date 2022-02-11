package edu.wm.cs.masc.similarity.helper;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class FileHelper {

	//reads file from a resource

	public static List<String> readLinesFromResource(String path){

		List<String> lines = new ArrayList<String>();
		try {
			//not doing this means files will not be readable inside a "fatJar"
			//therefore files must be read in this approach.
			BufferedReader br = new BufferedReader(new InputStreamReader(ClassLoader.getSystemResourceAsStream(path)));

			String line;
			while((line = br.readLine())!=null){
				lines.add(line);
			}
			br.close();
		} catch(IOException e){
			e.printStackTrace();
		}
		return lines;
	}

	public static List<String> readLines(String filePath){
		List<String> lines = new ArrayList<String>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(filePath));

			String line;
			while((line = br.readLine())!=null){
				lines.add(line);
			}

			br.close();
		} catch(FileNotFoundException e){
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return lines;
	}

	public static StringBuffer readSourceFile(String filePath)
			throws FileNotFoundException, IOException {
		StringBuffer source = new StringBuffer();
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		String line = null;

		while ((line = reader.readLine()) != null) {

			source.append(line).append("\n");
		}
		reader.close();
		return source;
	}


	public static boolean writeLines(String filePath, List<String> lines){
		
		try{
			BufferedWriter bw = new BufferedWriter(new FileWriter(filePath));
			
			for (String newLine : lines) {
				bw.write(newLine);
				bw.newLine();
				bw.flush();
			}
			
			bw.close(); 
		}  catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
}
