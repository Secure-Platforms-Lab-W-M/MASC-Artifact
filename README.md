# MASC Artifact
Welcome to the artifact of MASC (Paper Accepted in IEEE S&P 2022, to appear)


## Online Appendix

Additional details of our paper can be found in the [Online Appendix](Online_appendix.pdf) PDF File.

## Artifact

We have prepared this artifact that contains the following:

---

## masc

Contains source code of masc.

## minimal_flaws

This directory contains the source code of the minimal versions of each Flaw.
It can also be built using Maven to generate a single jar file that contain each Flaw inside of it.
Some of the tools in our study builds the app as part of analysis process, which is why the maven version was created manually based on minimal flaws.
The subdirectories are broken down by Flaw type and contains the corresponding java files for each. These directories additionally contain all of the tools' analysis reports (i.e., CryptoGuard, CrySL, SpotBugs, QARK, CodeQL (both codescanning and LGTM), ShiftLeft, Xanitizer, and Coverity).
The mapping of each minimal version's folder name and its description (i.e., the Flaw type) is located in the file `tool_logs/minimal_flaws/filename_to_flaw_mapper.md`.

## mutated_apps

The mutated and non-mutated source code of the applications

Similarly to the Flaws folder, some tool logs are also located inside of the subdirectory structures, with their mapping in the Mutation Logs folder.
These applications can be built using gradle or maven (from the original apps) depending on the application.

## tool_logs

Tool log mappers containing relative paths to tool logs are in this directory for all mutated and non-mutated apps as well as minimal flaws. The format of each log is dependent on the tool used. For Xanitizer, these logs will be in PDF format containing an overview of bugs in a particular Flaw (e.g., how many issues detected) along with more specific information per issue instance. Coverity uses CSV format documenting issue types, counts, locations, etc. CryptoGuard, CrySL, and SpotBugs use custom formats which we have placed into markdown files. QARK uses an HTML format containing individual issues detected along with their locations. Lastly, CodeQL (both code-scanning and LGTM) and ShiftLeft Scan use the SARIF format containing individual issues detected and their locations.

## tools_and_scripts

In the `tools_and_scripts` directory, you will find the configurations and some programs/binaries (excluding Xanitizer, Coverity, CodeQL (Codescanner and LGTM) and Shiftleft raw binaries due to their size and/or requirement of licenses) of the different tools we evaluated.

Additionally, for all tools except Xanitizer v4.3.3, Coverity v2020.03, CodeQL (Codescanner and LGTM) (codeql-repo used is included for running the version of codeql queries we performed), and ShiftLeft v1.9.31 (due to their binaries and/or licenses not being provided), we have included a script, `tools_and_scripts/run_tools.sh`, to reproduce our results for all minimal flaws by running the command:

```
bash tools_and_scripts/run_tools.sh minimal_flaws
```

We have also included all the other various scripts we have created to evaluate the other applications (i.e., exhaustive and selective) as well as scripts to summarize certain tools' logs to enable easier interpretability as discussed in our paper. However, due to issues with hard-coded paths, and different build requirements, we do not provide an easy to reproduction script like the one for the minimal Flaws and so these additional scripts are not guaranteed to work out of the box.

*Note*: For Coverity, only the scripts for exhaustive and selective are included since all minimal Flaws were run via Coverity's GUI.
However, the Coverity GUI configurations are included.

`tools_and_scripts/script_descriptions.csv` contains descriptions each of the scripts included.

**Note: Any files/descriptions that refer to "reachability" is synonymous to "exhaustive". Similarly, we used "crysl" internally as synonymous to "cognicrypt".**
