package fi.helsinki.cs.plugin.tmc.async.tasks;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import fi.helsinki.cs.plugin.tmc.Core;
import fi.helsinki.cs.plugin.tmc.async.BackgroundTask;
import fi.helsinki.cs.plugin.tmc.async.TaskFeedback;
import fi.helsinki.cs.tmc.testscanner.TestMethod;
import fi.helsinki.cs.tmc.testscanner.TestScanner;

public class TestrunnerTask implements BackgroundTask {
    private List<String> args;
    private String testClasspath;

    public TestrunnerTask(File rootPath, File testDir, File resultFile, String testClasspath, Integer memoryLimit) {
        System.out.println("Constructed with \n" + "rootpath: " + rootPath + "\n" + "testdir: " + testDir + "\n"
                + "resultFile: " + resultFile + "\n" + "testClasspath: " + testClasspath + "\n");

        args = new ArrayList<String>();
        args.add("-Dtmc.test_class_dir=" + testDir.getAbsolutePath());
        args.add("-Dtmc.results_file=" + resultFile.getAbsolutePath());
        args.add("-Dfi.helsinki.cs.tmc.edutestutils.defaultLocale=" + Core.getSettings().getErrorMsgLocale().toString());

        if (endorserLibsExists(rootPath)) {
            args.add("-Djava.endorsed.dirs=" + endorsedLibsPath(rootPath));
        }

        if (memoryLimit != null) {
            args.add("-Xmx" + memoryLimit + "M");
        }

        args.add("fi.helsinki.cs.tmc.testrunner.Main");

        for (TestMethod method : findProjectTests(testDir)) {
            args.add(method.toString());
        }

        this.testClasspath = testClasspath;

        System.out.println("ARGS:");
        for (String arg : args) {
            System.out.println(arg);
        }
    }

    private boolean endorserLibsExists(File rootPath) {
        File endorsedDir = endorsedLibsPath(rootPath);

        return endorsedDir.exists() && endorsedDir.isDirectory();
    }

    private File endorsedLibsPath(File rootPath) {
        return new File(rootPath.getAbsoluteFile() + File.separator + "lib" + File.separator + "endorsed");
    }

    private List<TestMethod> findProjectTests(File testDir) {
        TestScanner scanner = new TestScanner();
        scanner.setClassPath(testClasspath);
        scanner.addSource(testDir);
        return scanner.findTests();
    }

    @Override
    public void start(TaskFeedback progress) {
        String[] argArray = args.toArray(new String[args.size()]);
        fi.helsinki.cs.tmc.testrunner.Main.main(argArray);
    }

    @Override
    public void stop() {
        // TODO Auto-generated method stub

    }

    @Override
    public String getDescription() {
        // TODO Auto-generated method stub
        return null;
    }

}
