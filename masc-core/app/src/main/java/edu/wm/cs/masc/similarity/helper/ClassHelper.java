package edu.wm.cs.masc.similarity.helper;

import java.util.List;

public class ClassHelper {
    //scan file for the line "package "
    public static String findPackageName(List<String> lines){
        for(String line: lines){
            if(line.contains("package "))
                return line;
        }
        return null;
    }

    public static int findImportLineIndex(List<String> lines){
        int index = 0;
        for (String line:  lines){
            if (line.contains("import ")){
                return index;
            }else{
                index+=1;
            }
        }
        return -1;
    }

}
