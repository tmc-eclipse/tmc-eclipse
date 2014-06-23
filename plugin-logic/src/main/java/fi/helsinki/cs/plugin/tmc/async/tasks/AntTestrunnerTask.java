package fi.helsinki.cs.plugin.tmc.async.tasks;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
import java.util.List;

import fi.helsinki.cs.plugin.tmc.async.BackgroundTask;
import fi.helsinki.cs.plugin.tmc.async.TaskFeedback;
import fi.helsinki.cs.plugin.tmc.domain.ClassPath;
import fi.helsinki.cs.plugin.tmc.domain.TestRunResult;
import fi.helsinki.cs.plugin.tmc.services.Settings;
import fi.helsinki.cs.plugin.tmc.ui.IdeUIInvoker;
import fi.helsinki.cs.plugin.tmc.utils.TestResultParser;

/**
 * An abstract background task for buildin and running tmc-junit-runner for an
 * Ant project.
 * 
 * Concrete classes must implement the abstract build method that tells the
 * class how to build and ant project using the argument "compile-test".
 */
public abstract class AntTestrunnerTask extends TestrunnerTask {
    private static final int THREAD_CHECK_INCREMENT_TIME_IN_MILLIS = 100;
    private static final int THREAD_NOT_FINISHED = -1;

    private List<String> args;
    private ClassPath classpath;

    private String rootPath;
    private String testDirPath;
    private String javaExecutable;
    private Integer memoryLimit;
    private String resultFilePath;
    private TestRunResult result;
    private Process process;

    private Settings settings;
    private IdeUIInvoker invoker;

    /**
     * @param rootPath
     *            An absolute path to the root of the project.
     * @param testDir
     *            An absolute path to the root of the test directory.
     * @param javaExecutable
     *            An absolute path to the java executable that should be used.
     *            This means that you should be able to say
     *            "<javaExecutable> --version" in the CLI and it should print
     *            out the JRE version.
     * @param memoryLimit
     *            An optional memory limit in mb.
     * @param settings
     *            An instance of the Settings.
     * @param invoker
     *            An instance of a class that implements IdeUIInvoker.
     */
    public AntTestrunnerTask(String rootPath, String testDir, String javaExecutable, Integer memoryLimit,
            Settings settings, IdeUIInvoker invoker) {

        super("Running tests");

        this.rootPath = rootPath;
        this.resultFilePath = rootPath + "/results.txt";
        this.testDirPath = testDir;
        this.javaExecutable = javaExecutable;
        this.memoryLimit = memoryLimit;
        this.settings = settings;

        this.settings = settings;
        this.invoker = invoker;

        this.classpath = new ClassPath(rootPath);
        classpath.addDirAndSubDirs(rootPath + "/lib");
        classpath.add(rootPath + "/build/classes/");
        classpath.add(rootPath + "/build/test/classes/");
    }

    public abstract void build(String root) throws Exception;

    @Override
    public TestRunResult get() {
        return this.result;
    }

    @Override
    public int start(TaskFeedback progress) {
        progress.startProgress(this.getDescription(), 4);

        try {
            build(rootPath);
        } catch (Exception e) {
            invoker.raiseVisibleException("Unable to run tests: Error when building project.");
            return BackgroundTask.RETURN_FAILURE;
        }

        progress.incrementProgress(1);
        if (shouldStop(progress)) {
            return BackgroundTask.RETURN_INTERRUPTED;
        }

        try {
            buildTestRunnerArgs(progress);
        } catch (InterruptedException e) {
            return BackgroundTask.RETURN_INTERRUPTED;
        }

        progress.incrementProgress(1);
        if (shouldStop(progress)) {
            return BackgroundTask.RETURN_INTERRUPTED;
        }

        ProcessBuilder pb = new ProcessBuilder(args);
        pb.redirectError(Redirect.INHERIT);

        try {
            process = pb.start();

            int status = THREAD_NOT_FINISHED;
            while (status == THREAD_NOT_FINISHED) {
                try {
                    status = process.exitValue();
                } catch (IllegalThreadStateException e) {
                    Thread.sleep(THREAD_CHECK_INCREMENT_TIME_IN_MILLIS);
                }
                if (shouldStop(progress)) {
                    process.destroy();
                    return BackgroundTask.RETURN_INTERRUPTED;
                }
            }

            progress.incrementProgress(1);
            if (shouldStop(progress)) {
                return BackgroundTask.RETURN_INTERRUPTED;
            }

            File resultFile = new File(resultFilePath);
            result = new TestResultParser().parseTestResults(resultFile);
            resultFile.delete();
            progress.incrementProgress(1);

            return BackgroundTask.RETURN_SUCCESS;
        } catch (IOException e) {
            invoker.raiseVisibleException("Failed to parse test results.");
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            invoker.raiseVisibleException("Failed to run tests");
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return BackgroundTask.RETURN_FAILURE;
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

    private boolean buildTestRunnerArgs(TaskFeedback progress) throws InterruptedException {
        args = new ArrayList<String>();

        args.add(javaExecutable);

        List<String> testMethods = findProjectTests(testDirPath, progress);

        args.add("-Dtmc.test_class_dir=" + testDirPath);
        args.add("-Dtmc.results_file=" + resultFilePath);
        args.add("-Dfi.helsinki.cs.tmc.edutestutils.defaultLocale=" + settings.getErrorMsgLocale().toString());

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

        return true;
    }

    private boolean endorserLibsExists(String rootPath) {
        File endorsedDir = endorsedLibsPath(rootPath);
        return endorsedDir.exists() && endorsedDir.isDirectory();
    }

    private File endorsedLibsPath(String rootPath) {
        return new File(rootPath + "/lib/endorsed");
    }

    private List<String> findProjectTests(String testPath, TaskFeedback progress) throws InterruptedException {
        List<String> testScannerArgs = buildTestScannerArgs(testPath);

        ProcessBuilder pb = new ProcessBuilder(testScannerArgs);
        pb.redirectError(Redirect.INHERIT);
        try {
            process = pb.start();
            
            int status = THREAD_NOT_FINISHED;
            while (status == THREAD_NOT_FINISHED) {
                try {
                    status = process.exitValue();
                } catch (IllegalThreadStateException e) {
                    Thread.sleep(THREAD_CHECK_INCREMENT_TIME_IN_MILLIS);
                }
                if (shouldStop(progress)) {
                    process.destroy();
                    throw new InterruptedException();
                }
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
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
        invoker.raiseVisibleException("Testrunner failure: failed to find test methods.");
        return null;
    }
}
