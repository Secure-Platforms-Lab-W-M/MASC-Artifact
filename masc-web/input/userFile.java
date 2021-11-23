import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class PS1 {

	public static void main(String[] args) {
		//reads in file
		File file = new File("pa1_input.txt");
		
		try {
			//creates fil to write to
			FileWriter writer = new FileWriter("/Users/scottmarsden/Documents/Algorith_Analysis/PS1/output.txt");
			Scanner scnr = new Scanner(file);
			//reads file
			while(scnr.hasNextLine()) {
				//finds the int at the begining of line to figure out
				//length of the array
				int length = scnr.nextInt();
				int[] line = new int[length]; 
				//fills in values of the array based on the line
				for(int i = 0; i < length; i++) {
					line[i] = scnr.nextInt();
				}
				//writes the answer of unique elements when the array is used as input
				writer.write(""+uniqueElements(line) +"\n");
				scnr.nextLine();
			} 
			writer.close();
			System.out.print("It takes " + timeTest(10000) + " milliseconds to run.");
		} catch (FileNotFoundException e1) {
			
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	//checks if all elements in the array are unique
	public static boolean uniqueElements(int a[]) {
		
		for(int i = 0; i < a.length - 1; i++) {
			for(int j = i + 1; j < a.length; j++) {
				if (a[i] == a[j]) {
					
					return false;
				}
			}
		}
		
		return true;
	}
	
	//tests how much time it takes to run an array of n size
	public static long timeTest(int length) {
		int[] timeTest = new int[length];
		int count = 0;
		for (int i = 0; i < timeTest.length; i++) {
			timeTest[i] = count;
			count++;
		}
		
		long start = System.nanoTime();
		System.out.println(uniqueElements(timeTest));
		long end = System.nanoTime();
		long timeToRun =  (end - start)/1000000;
		return timeToRun;
	}

}
