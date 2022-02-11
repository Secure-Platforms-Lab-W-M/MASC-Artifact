package edu.wm.cs.masc.similarity.helper;

import edu.wm.cs.masc.similarity.detectors.code.visitors.ASTHelper;
import edu.wm.cs.masc.similarity.model.location.MutationLocation;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.dom.CompilationUnit;

import java.io.*;
import java.util.List;

public class PlacementChecker {
    String projectPath;
    String binariesFolder;

    public PlacementChecker(String projectPath, String binariesFolder) {
        this.projectPath = projectPath;
        this.binariesFolder = binariesFolder;
    }

    public void process(List<MutationLocation> locations)
            throws IOException {
        System.out.println("Minimal JDT-AST Location Reachability Check");
        for (MutationLocation mutationLocation : locations) {
            String filePath = mutationLocation.getFilePath();
            System.out.println(
                    "Checking for Code Reachability Issues: " + filePath);
            this.report_jdt(filePath, projectPath, binariesFolder);
        }
    }

    public void report_jdt(String filePath, String projectPath, String
            binariesFolder) throws IOException {
        String unitName = filePath
                .substring(filePath.lastIndexOf(File.separator) + 1)
                .replace(".java", "");

        String source = FileHelper.readSourceFile(filePath).toString();

        CompilationUnit cu = ASTHelper
                .getASTAndBindings(source, projectPath,
                        binariesFolder, unitName);

        for (IProblem problem : cu.getProblems()) {
            if (!problem.isError()) {
                // Ignore anything that's not error severity.
                continue;
            }
            if (!(problem.getID() == IProblem.CodeCannotBeReached))
                continue;

            System.out.println(filePath + ": reachability location problem");
        }

    }
}

//    public static void main(String[] args) throws IOException {
//        PlacementChecker checker = new PlacementChecker();
//        String binariesFolder = "/Users/amitseal/git/muse2source/MDroidPlus" +
//                "/libs4ast/";
//
//        String bad_file = "/Users/amitseal/workspaces/mutationbackyard" +
//                "/mutations/atalk-android-mutant12/aTalk/src/main/java/net" +
//                "/java/sip/communicator/util/NetworkUtils.java";
//        String good_file = "/Users/amitseal/workspaces/mutationbackyard" +
//                "/mutations/atalk-android-mutant9/aTalk/src/main/java/net" +
//                "/java/sip/communicator/impl/provdisc/dhcp/DHCPTransaction" +
//                ".java";
//        String badProjectPath =
//        "/Users/amitseal/workspaces/mutationbackyard" +
//                "/mutations/atalk-android-mutant12/aTalk/src/main/java/";
//        String goodProjectPath =
//        "/Users/amitseal/workspaces/mutationbackyard" +
//                "/mutations/atalk-android-mutant9/aTalk/src/main/java/";
//
//        String testFile = "/Users/amitseal/workspaces/java/TimeTest.java";
//        String testProjectPath = "/Users/amitseal/workspaces/java/";
//        String filePath = bad_file;
//        String projectPath = badProjectPath;
//
//        String unitName = filePath
//                .substring(filePath.lastIndexOf(File.separator) + 1)
//                .replace(".java", "");
//        String source = FileHelper.readSourceFile(filePath).toString();
//
//        CompilationUnit cu = ASTHelper
//                .getASTAndBindings(source, projectPath,
//                        binariesFolder, unitName);
//        int realProblemCount = 0;
//        final StringBuilder errors = new StringBuilder();
//
//
//        for (IProblem problem : cu.getProblems()) {
//            if (!problem.isError()) {
//                // Ignore anything that's not error severity.
//                continue;
//            }
//            if (!(problem.getID() == IProblem.CodeCannotBeReached))
//                continue;
//
////            int problemId = problem.getID();
////            if (problemId > IProblem.Javadoc) {
////                System.out.println("larger than JavaDOC");
////                continue;
////            }
////            else if (problemId < IProblem.Syntax) {
////                System.out.println("Smaller than Syntax");
////                continue;
////            }
////            if (problemId == IProblem.VoidMethodReturnsValue
////                    || problemId == IProblem.NotVisibleMethod
////                    || problemId == IProblem.NotVisibleConstructor
////                    || problemId == IProblem.NotVisibleField
////                    || problemId == IProblem.NotVisibleType
////                    || problemId == IProblem.ImportNotFound
////                    || problemId == IProblem.UndefinedType
////                    || problemId == IProblem.BodyForAbstractMethod) {
////
////                continue;
////            }
//
//            realProblemCount++;
////                System.out.println("%x".format(problemId));
//            int end = problem.getSourceEnd();
//            int start = problem.getSourceStart();
//            int sourceLine = problem.getSourceLineNumber();
//            System.out.println(problem.getMessage());
//            System.out.println(sourceLine);
////            System.out.print(problemId);
////            System.out.print(", ");
////            System.out.print(problem.getMessage());
//////            System.out.print(", ");
//            System.out.println(source.substring(start, end));
////            System.out.println(problem.toString());
//            errors.append(problem.getMessage());
//            errors.append("\n");
//
//        }
////        System.out.println(errors.toString());
////            if (problemId == IProblem.VoidMethodReturnsValue || problemId
////            == IProblem.NotVisibleMethod
////                    || problemId == IProblem.NotVisibleConstructor ||
////                    problemId == IProblem.NotVisibleField
////                    || problemId == IProblem.NotVisibleType || problemId ==
////                    IProblem.ImportNotFound
////                    || problemId == IProblem.UndefinedType || problemId ==
////                    IProblem.BodyForAbstractMethod) {
////
////                continue;
////            }
//
////        realProblemCount++;
//
//
//    }
////        System.out.println(checker.check(new File(bad_file)));

//    }


