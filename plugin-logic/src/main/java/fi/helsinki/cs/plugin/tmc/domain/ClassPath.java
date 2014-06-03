package fi.helsinki.cs.plugin.tmc.domain;

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

    public String toString() {
        if (subPaths.isEmpty()) {
            return "";
        }

        String cp = subPaths.get(0);

        for (int i = 1; i < subPaths.size(); i++) {
            cp += ":" + subPaths.get(i);
        }

        return cp;
    }

}
