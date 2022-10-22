# How Does sarifPar work?

sarifPar is an evaluation component of the MASC tool. This tool is designed to compliment MASC by automating a portion of the evaluation of some Crypto-Detectors. This tool leverages the SARIF output file which is a JSON based output used by security tools. Not all Crypto-Detectors use utilize SARIF output but many of them do so the idea behind this component is to leverage this static output from the tools that produce it. This helps to automate a large portion of the evaluation.

The way the tool works is by taking in four command line arguments: .sarif file before mutation, .sarif file after mutation, properties file used for mutation, and file path to MASC (Once the tool is integrated further into the MASC project this most likely will not be necessary). 

The two SARIF files are needed so that we can get a baseline for what security misuses may already be present before mutation. The two files are parsed and compared to see if there are any similarities and those similarties are removed from the ouput since these were already present prior to MASC mutating. 

Once the results are found the properties file is then analyzed to find the relevant information for this part of the tool: operator type, output directory, class name, and api name. Using this information the tool will look at each of the output files produced by MASC to find the relevant mutation. Using this mutation the results found in the previous step can be analyzed to determine if the Crypto-Detector in question was able to find this misuse case. If it is able to find the misuse located within the first output produced then the next misuse will be compared until all misuses are caught or the tool failed to find a misuse. Each subsequent misuse is more complex than the previous. If the tool fails to find a misuse the tool will stop since a Crypto-Detecotr is not expected to find a more complex version of a misuse than the one it fails on.

Currently this is only designed to work within the MAIN scope. It can be expanded to work for other scopes. Most of the changes would likely be in the flowAnalysis functions.

## Functions

dataFlowAnalysis(): Looks at properties file to find class name, operator type, output directory, and api name. Based on the operator type it passes to either stringFlowAnalysis(), intFlowAnalysis(), byteFlowAnalysis(), or interprocFlowAnalysis() 

stringFlowAnalysis(), intFlowAnalysis(), byteFlowAnalysis(), interprocFlowAnalysis() : Locates all the output files produced by the operator. Using the api name found in the properties folder it calls getJavaMutant() to obtain the line that has the mutation for each file. Then it checks the SARIF results to see if the misuse was caught. The program stops if it fails to catch any misuse.

getJavaMutant(): Takes the api name in the properties file and one of the output files as input. Then it outputs the line that contains that api

findMutation(): Takes an input of the String containing the mutation, the number contained within the file (in MAIN scope this is 1), and an ArrayList with the SARIF results. Then returns if the mutation is found within the ArrayList or not.

getResult(): Takes a SARIF file as input and extracts the results contained within the file

compareResults(): Takes two sets of results from getResult() as input and compares them to see if there is any overlap. It returns an ArrayList containing the results that did not overlap.

extractResults(): Extracts the String contained in the specified key for the SARIF file

removeLineNumbers(): Removes line numbers from the result strings for comparison. This is used since we only care if the same results are found not the line numbers. This is because when a mutation is introduced we do not expect the line numbers to remain the same

getJson(): gets the JSON contained within a SARIF file or JSON file (nested JSON)

clean(): removes empty values found within an ArrayList

