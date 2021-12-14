package edu.wm.cs.mplus.helper;

import java.util.List;

public class ClassMeta {
    private int indexImport = -1;
    private String packageName = null;

    public ClassMeta(List<String> lines) {
        // we only want to check till half the line size
        for (int index = 0; index < lines.size() / 2; index++) {
            String line = lines.get(index);
            if (indexImport == -1 || packageName == null) {
                if (line.contains("package "))
                    packageName = line;
                if (line.contains("import ")) {
                    indexImport = index;
                }

            }
        }
    }

    public int getImportIndex() {
        return indexImport;
    }

    public String getPackageName() {
        return packageName;
    }


}
