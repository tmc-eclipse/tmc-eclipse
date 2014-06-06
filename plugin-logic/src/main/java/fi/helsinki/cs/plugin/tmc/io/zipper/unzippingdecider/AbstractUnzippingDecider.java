package fi.helsinki.cs.plugin.tmc.io.zipper.unzippingdecider;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import fi.helsinki.cs.plugin.tmc.domain.Project;

public abstract class AbstractUnzippingDecider implements UnzippingDecider {
    protected Project project;
    private List<String> doNotUnzip;

    public AbstractUnzippingDecider(Project project) {
        this.project = project;
        this.doNotUnzip = TmcProjectFile.forProject(new File(project.getRootPath())).getExtraStudentFiles();
    }

    @Override
    public boolean shouldUnzip(String filePath) {
        for (String s : doNotUnzip) {
            s = project.getRootPath() + "/" + s;
            if (filePath.startsWith(s) && (filePath.equals(s) || filePath.charAt(s.length()) == '/')) {
                System.out.println("-false");
                return false;
            }
        }
        return true;
    }
}
