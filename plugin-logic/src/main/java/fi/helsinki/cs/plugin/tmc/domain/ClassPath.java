package fi.helsinki.cs.plugin.tmc.domain;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ClassPath {
    List<String> subPaths = new ArrayList<String>();

    public ClassPath(String classpath) {
        subPaths.add(classpath);
    }

    public void add(String classpath) {
        if (!subPaths.contains(classpath)) {
            subPaths.add(classpath);
        }
    }

    public void add(ClassPath classpath) {
        for (String cp : classpath.subPaths) {
            add(cp);
        }
    }

    public List<String> getSubPaths() {
        return subPaths;
    }

    public String toString() {
        String cp = subPaths.get(0);

        for (int i = 1; i < subPaths.size(); i++) {
            cp += System.getProperty("path.separator") + subPaths.get(i);
        }

        return cp;
    }

    public void addDirAndSubDirs(String path) {
        File root = new File(path);
        if (!root.isDirectory()) {
            path = root.getParent();
            root = root.getParentFile();
        }

        if (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }
        String classpath = path + "/*";

        if (!subPaths.contains(classpath)) {
            subPaths.add(classpath);
            for (File child : root.listFiles()) {
                if (child.isDirectory()) {
                    addDirAndSubDirs(child.getAbsolutePath());
                }
            }
        }
    }
}
