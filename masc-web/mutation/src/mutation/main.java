package mutation;
import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.Random;

public class main {

	public static void main(String[] args) {
		try {
			System.out.println(new File("userFile.txt").exists());
		      File file = new File("userFile.txt");
		      Random rand = new Random();
		      //BufferedReader reader = new BufferedReader(new FileReader('userFile.txt'));
		      Scanner scnr = new Scanner(file);
		      FileWriter writer = new FileWriter("/Users/scottmarsden/Documents/output.txt");
		      while (scnr.hasNextLine()) {
		        String line = scnr.nextLine();
		        int index = rand.nextInt(line.length());
		        String newLine = line.substring(0, index) + index + line.substring(index + 1);
		        
		        writer.write(""+ newLine + "/n");
		        
		        
		      }
		      writer.close();
		      scnr.close();
		    } catch (FileNotFoundException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	}
	


}
