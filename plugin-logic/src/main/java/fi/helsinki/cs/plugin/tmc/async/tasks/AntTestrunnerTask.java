package fi.helsinki.cs.plugin.tmc.async.tasks;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
import java.util.List;

import fi.helsinki.cs.plugin.tmc.Core;
import fi.helsinki.cs.plugin.tmc.async.BackgroundTask;
import fi.helsinki.cs.plugin.tmc.async.TaskFeedback;
import fi.helsinki.cs.plugin.tmc.domain.ClassPath;
import fi.helsinki.cs.plugin.tmc.domain.TestRunResult;
import fi.helsinki.cs.plugin.tmc.utils.TestResultParser;

public class AntTestrunnerTask implements BackgroundTask, TestrunnerTask {
    private List<String> args;
    private ClassPath classpath;

    private String rootPath;
    private String testDirPath;
    private String javaExecutable;
    private Integer memoryLimit;
    private String resultFilePath;
    private TestRunResult result;

    public AntTestrunnerTask(String rootPath, String testDir, String javaExecutable, Integer memoryLimit) {
        this.rootPath = rootPath;
        this.resultFilePath = rootPath + "/results.txt";
        this.testDirPath = testDir;
        this.javaExecutable = javaExecutable;
        this.memoryLimit = memoryLimit;

        this.classpath = new ClassPath(rootPath + "/lib/testrunner/tmc-test-runner.jar");
        classpath.add(rootPath + "/lib/*");
        classpath.add(rootPath + "/lib/testrunner/*");
        classpath.add(rootPath + "/build/classes/");
        classpath.add(rootPath + "/build/test/classes/");
    }

    public TestRunResult get() {
        return this.result;
    }

    @Override
    public int start(TaskFeedback progress) {
        buildTestRunnerArgs();

        ProcessBuilder pb = new ProcessBuilder(args);
        pb.redirectError(Redirect.INHERIT);

        Process p;
        try {
            p = pb.start();
            p.waitFor();

            File resultFile = new File(resultFilePath);
            result = new TestResultParser().parseTestResults(resultFile);
            resultFile.delete();

            return BackgroundTask.RETURN_SUCCESS;
        } catch (IOException e) {
            Core.getErrorHandler().raise("Failed to parse test results.");
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            Core.getErrorHandler().raise("Failed to run tests");
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return BackgroundTask.RETURN_FAILURE;
    }

    @Override
    public void stop() {
        // TODO Auto-generated method stub

    }

    @Override
    public String getDescription() {
        return "TMC Testrunner task";
    }

    private List<String> buildTestScannerArgs(String testPath) {
        List<String> testScannerArgs = new ArrayList<String>();
        testScannerArgs.add(args.get(0)); // JAVA
        testScannerArgs.add("-cp");
        testScannerArgs.add(classpath.toString());
        testScannerArgs.add("fi.helsinki.cs.tmc.testscanner.TestScanner");
        testScannerArgs.add(testPath);
        testScannerArgs.add("--test-runner-format");
        return testScannerArgs;
    }

    private void buildTestRunnerArgs() {
        args = new ArrayList<String>();

        args.add(javaExecutable);

        List<String> testMethods = findProjectTests(testDirPath);

        args.add("-Dtmc.test_class_dir=" + testDirPath);
        args.add("-Dtmc.results_file=" + resultFilePath);
        args.add("-Dfi.helsinki.cs.tmc.edutestutils.defaultLocale=" + Core.getSettings().getErrorMsgLocale().toString());

        if (endorserLibsExists(rootPath)) {
            args.add("-Djava.endorsed.dirs=" + endorsedLibsPath(rootPath));
        }

        if (memoryLimit != null) {
            args.add("-Xmx" + memoryLimit + "M");
        }

        args.add("-cp");
        args.add(classpath.toString());

        args.add("fi.helsinki.cs.tmc.testrunner.Main");

        for (String method : testMethods) {
            args.add(method);
        }
    }

    private boolean endorserLibsExists(String rootPath) {
        File endorsedDir = endorsedLibsPath(rootPath);
        return endorsedDir.exists() && endorsedDir.isDirectory();
    }

    private File endorsedLibsPath(String rootPath) {
        return new File(rootPath + File.separator + "lib" + File.separator + "endorsed");
    }

    private List<String> findProjectTests(String testPath) {
        List<String> testScannerArgs = buildTestScannerArgs(testPath);

        ProcessBuilder pb = new ProcessBuilder(testScannerArgs);
        pb.redirectError(Redirect.INHERIT);
        try {
            Process p = pb.start();
            p.waitFor();

            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            List<String> results = new ArrayList<String>();

            String line;
            while ((line = br.readLine()) != null && !line.equals("")) {
                results.add(line);
            }

            return results;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Core.getErrorHandler().raise("Testrunner failure: failed to find test methods.");
        return null;

    }
}