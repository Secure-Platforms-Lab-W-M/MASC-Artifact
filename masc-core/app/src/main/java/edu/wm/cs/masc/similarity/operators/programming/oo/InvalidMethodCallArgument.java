package edu.wm.cs.masc.similarity.operators.programming.oo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import edu.wm.cs.masc.similarity.model.location.ArgumentMutationLocation;
import edu.wm.cs.masc.similarity.model.location.MutationLocation;
import edu.wm.cs.masc.similarity.operators.MutationOperator;
import edu.wm.cs.masc.similarity.helper.FileHelper;
import edu.wm.cs.masc.similarity.helper.IntegerGenerator;
import edu.wm.cs.masc.similarity.helper.StringGenerator;

public class InvalidMethodCallArgument  implements MutationOperator {

	@Override
	public boolean performMutation(MutationLocation location) {

		ArgumentMutationLocation mLocation = (ArgumentMutationLocation) location;

		List<String> newLines = new ArrayList<String>();
		List<String> lines = FileHelper.readLines(location.getFilePath());

		for(int i=0; i < lines.size(); i++){

			String currLine = lines.get(i);

			//Mutate argument
			if(i == location.getLine()){
				String sub1 = currLine.substring(0, location.getStartColumn());
				String sub2 = currLine.substring(location.getEndColumn());

				int type = mLocation.getAmlType();
				String argVal = "";
				Random rnd = new Random();

				switch(type){
				case ArgumentMutationLocation.INT:	argVal = ""+ IntegerGenerator
						.generateRandomInt();
				break;
				case ArgumentMutationLocation.DOUBLE:	argVal = ""+rnd.nextDouble();
				break;
				case ArgumentMutationLocation.LONG:	argVal = ""+rnd.nextLong();
				break;
				case ArgumentMutationLocation.CHAR:	argVal = ""+(char)(rnd.nextInt(26) + 'a');;
				break;
				case ArgumentMutationLocation.BOOLEAN:	argVal = ""+rnd.nextBoolean();
				break;
				case ArgumentMutationLocation.STRING:	argVal = ""+ StringGenerator
						.generateRandomString();
				break;
				}


				currLine = sub1 + argVal + sub2;
			}

			newLines.add(currLine);

		}

		FileHelper.writeLines(location.getFilePath(), newLines);

		return true;
	}

}
