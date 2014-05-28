package tmc.EclipseTestRunner;

//import java.io.File;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.tools.FileObject;
//import javax.tools.JavaCompiler;
//import javax.tools.ToolProvider;
//
//import org.eclipse.core.internal.utils.FileUtil;
//
//import fi.helsinki.cs.plugin.tmc.domain.Project;

public abstract class AbstractJavaExerciseRunner extends AbstractExerciseRunner {
    //
    // protected boolean endorsedLibsExist(final Project projectInfo) {
    // File endorsedDir = new File(projectInfo.getRootPath() +
    // File.separatorChar + "lib" + File.separatorChar
    // + "endorsed");
    // return endorsedDir.exists() && endorsedDir.isDirectory();
    // }
    //
    // protected void runJavaProcessInProject(Project projectInfo, String
    // classPath, String taskName, List<String> args) {
    //
    // JavaPlatform platform = JavaPlatform.getDefault(); // Should probably
    // // use project's
    // // configured
    // // platform instead
    //
    // FileObject javaExe = platform.findTool("java");
    // if (javaExe == null) {
    // throw new IllegalArgumentException();
    // }
    //
    // // TMC server packages this with every exercise for our convenience.
    // // True even for Maven exercises, at least until NB's Maven API is
    // // published.
    // String testRunnerClassPath = getTestRunnerClassPath(projectInfo);
    //
    // if (testRunnerClassPath != null) {
    // classPath = ClassPathSupport.createProxyClassPath(classPath,
    // testRunnerClassPath);
    // }
    //
    // String[] command = new String[3 + args.size()];
    // command[0] = javaExe.toUri().toString();
    // command[1] = "-cp";
    // command[2] = classPath;
    // System.arraycopy(args.toArray(new String[args.size()]), 0, command, 3,
    // args.size());
    //
    // ProcessRunner runner = new ProcessRunner(command, new
    // File(projectInfo.getRootPath()));
    // }
    //
    // protected ClassPath getTestClassPath(Project projectInfo, FileObject
    // testDir) {
    // ClassPathProvider classPathProvider =
    // projectInfo.getProject().getLookup().lookup(ClassPathProvider.class);
    //
    // if (classPathProvider == null) {
    // throw new RuntimeException("Project's class path not (yet) initialized");
    // }
    // ClassPath cp = classPathProvider.findClassPath(testDir,
    // ClassPath.EXECUTE);
    // if (cp == null) {
    // throw new
    // RuntimeException("Failed to get 'execute' classpath for project's tests");
    // }
    // return cp;
    // }
    //
    // protected ClassPath getTestRunnerClassPath(Project projectInfo) {
    // FileObject projectDir = projectInfo.getProjectDir();
    // FileObject testrunnerDir = projectDir.getFileObject("lib/testrunner");
    // if (testrunnerDir != null) {
    // FileObject[] files = testrunnerDir.getChildren();
    // ArrayList<URL> urls = new ArrayList<URL>();
    // for (FileObject file : files) {
    // URL url = FileUtil.urlForArchiveOrDir(FileUtil.toFile(file));
    // if (url != null) {
    // urls.add(url);
    // }
    // }
    // return ClassPathSupport.createClassPath(urls.toArray(new URL[0]));
    // } else {
    // return null;
    // }
    // }
    //
    // protected List<TestMethod> findProjectTests(Project projectInfo,
    // FileObject testDir) {
    // TestScanner scanner = new TestScanner(loadJavaCompiler());
    // scanner.setClassPath(getTestClassPath(projectInfo,
    // testDir).toString(ClassPath.PathConversionMode.WARN));
    // scanner.addSource(FileUtil.toFile(testDir));
    // return scanner.findTests();
    // }
    //
    // private JavaCompiler loadJavaCompiler() {
    // // https://netbeans.org/bugzilla/show_bug.cgi?id=203540
    // ClassLoader orig = Thread.currentThread().getContextClassLoader();
    // try {
    // Thread.currentThread().setContextClassLoader(JavaPlatform.class.getClassLoader());
    // return ToolProvider.getSystemJavaCompiler();
    // } finally {
    // Thread.currentThread().setContextClassLoader(orig);
    // }
    // }
    //
    // protected FileObject findTestDir(Project projectInfo) {
    // // Ideally we'd get these paths from NB, but let's assume the
    // // conventional ones for now.
    // FileObject root = projectInfo.getProjectDir();
    // switch (projectInfo.getProjectType()) {
    // case JAVA_SIMPLE:
    // return root.getFileObject("test");
    // case JAVA_MAVEN:
    // return getSubdir(root, "src", "test", "java");
    // default:
    // throw new IllegalArgumentException("Unknown project type");
    // }
    // }
    //
    // private FileObject getSubdir(FileObject fo, String... subdirs) {
    // for (String s : subdirs) {
    // if (fo == null) {
    // return null;
    // }
    // fo = fo.getFileObject(s);
    // }
    // return fo;
    // }
}
