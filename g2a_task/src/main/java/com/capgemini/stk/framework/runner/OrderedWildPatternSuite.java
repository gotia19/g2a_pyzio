package com.capgemini.stk.framework.runner;

import com.googlecode.junittoolbox.util.JUnit4TestChecker;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.AbstractFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.regex.Pattern;

//Copy of com.googlecode.junittoolbox.WildcardPatternSuite plus sorting of classes by RunPriority annotation
@SuppressWarnings("unchecked")
public class OrderedWildPatternSuite extends Suite {
    private static Class<?>[] getSuiteClasses(Class<?> klass) throws InitializationError {
        SuiteClasses annotation1 = klass.getAnnotation(SuiteClasses.class);
        com.googlecode.junittoolbox.SuiteClasses annotation2 = klass
                .getAnnotation(com.googlecode.junittoolbox.SuiteClasses.class);
        if (annotation1 == null && annotation2 == null) {
            throw new InitializationError("class " + klass.getName() + " must have a SuiteClasses annotation");
        }
        else {
            Class<?>[] suiteClasses1 = annotation1 == null ? null : annotation1.value();
            Class<?>[] suiteClasses2 = annotation2 == null ? null : findSuiteClasses(klass, annotation2.value());
            //SORTING
            Class<?>[]     array = union(suiteClasses1, suiteClasses2);
            List<Class<?>> list  = new ArrayList<>(Arrays.asList(array));
            list.sort((f1, f2) -> {
                RunPriority priority1 = f1.getAnnotation(RunPriority.class);
                RunPriority priority2 = f2.getAnnotation(RunPriority.class);

                RunPriorityLevel priorityLevel1 = priority1 == null ? RunPriorityLevel.NORMAL : priority1.value();
                RunPriorityLevel priorityLevel2 = priority2 == null ? RunPriorityLevel.NORMAL : priority2.value();

                return priorityLevel1.compareTo(priorityLevel2);
            });
            Class<?>[] returnArray = new Class<?>[list.size()];
            return list.toArray(returnArray);
        }
    }

    private static Class<?>[] findSuiteClasses(Class<?> klass, String... wildcardPatterns) throws InitializationError {
        File      baseDir    = getBaseDir(klass);
        Set<File> classFiles = findFiles(baseDir, wildcardPatterns);
        if (classFiles.isEmpty()) {
            throw new InitializationError(
                    "Did not find any *.class file using the specified wildcard patterns " + Arrays
                            .toString(wildcardPatterns) + " relative to directory " + baseDir);
        }
        else {
            File testClassesDir = getClassesDir(klass);

            String testClassesPath;
            try {
                testClassesPath = testClassesDir.getCanonicalPath().replace('\\', '/');
            }
            catch (IOException var16) {
                throw new RuntimeException(var16.getMessage());
            }

            List<Class<?>>    testClasses       = new ArrayList<>();
            ClassLoader       classLoader       = klass.getClassLoader();
            JUnit4TestChecker junit4TestChecker = new JUnit4TestChecker(classLoader);
            Iterator          var9              = classFiles.iterator();

            while (var9.hasNext()) {
                File file = (File) var9.next();

                try {
                    String canonicalPath = file.getCanonicalPath().replace('\\', '/');
                    if (canonicalPath.startsWith(testClassesPath)) {
                        String   path      = canonicalPath.substring(testClassesPath.length() + 1);
                        String   className = path.substring(0, path.length() - ".class".length()).replace('/', '.');
                        Class<?> clazz     = classLoader.loadClass(className);
                        if (junit4TestChecker.accept(clazz)) {
                            testClasses.add(clazz);
                        }
                    }
                }
                catch (Exception var15) {
                    throw new InitializationError("Failed to load " + file + " -- " + var15.getMessage());
                }
            }

            if (testClasses.isEmpty()) {
                throw new InitializationError(
                        "Did not find any test classes using the specified wildcard patterns " + Arrays
                                .toString(wildcardPatterns) + " relative to directory " + baseDir);
            }
            else {
                return (Class[]) testClasses.toArray(new Class[testClasses.size()]);
            }
        }
    }

    private static Set<File> findFiles(File baseDir, String... wildcardPatterns) throws InitializationError {
        try {
            Set<File> included = new HashSet();
            Set<File> excluded = new HashSet();
            String[]  var4     = wildcardPatterns;
            int       var5     = wildcardPatterns.length;

            for (int var6 = 0; var6 < var5; ++var6) {
                String wildcardPattern = var4[var6];
                if (wildcardPattern == null) {
                    throw new InitializationError("wildcard pattern for the SuiteClasses annotation must not be null");
                }

                if (wildcardPattern.startsWith("!")) {
                    excluded.addAll(findFiles(baseDir, wildcardPattern.substring(1)));
                }
                else {
                    if (!wildcardPattern.endsWith(".class")) {
                        throw new InitializationError(
                                "wildcard pattern for the SuiteClasses annotation must end with \".class\"");
                    }

                    included.addAll(findFiles(baseDir, wildcardPattern));
                }
            }

            included.removeAll(excluded);
            return included;
        }
        catch (IOException var8) {
            throw new InitializationError("Failed to scan " + baseDir + " using wildcard patterns " + Arrays
                    .toString(wildcardPatterns) + " -- " + var8);
        }
    }

    private static Collection<File> findFiles(File baseDir, String wildcardPattern) throws InitializationError, IOException {
        if (wildcardPattern.startsWith("/")) {
            throw new InitializationError(
                    "wildcard pattern for the SuiteClasses annotation must not start with a '/' character");
        }
        else {
            while (wildcardPattern.startsWith("../")) {
                baseDir         = baseDir.getParentFile();
                wildcardPattern = wildcardPattern.substring(3);
            }

            final Pattern regex    = convertWildcardPatternToRegex("/" + wildcardPattern);
            final String  basePath = baseDir.getCanonicalPath().replace('\\', '/');
            IOFileFilter fileFilter = new AbstractFileFilter() {
                @Override
                public boolean accept(File file) {
                    try {
                        if (!file.isDirectory() && !file.isHidden() && !file.getName().contains("$")) {
                            String canonicalPath = file.getCanonicalPath().replace('\\', '/');
                            if (canonicalPath.startsWith(basePath)) {
                                String path = canonicalPath.substring(basePath.length());
                                if (regex.matcher(path).matches()) {
                                    return true;
                                }
                            }

                            return false;
                        }
                        else {
                            return false;
                        }
                    }
                    catch (IOException var4) {
                        throw new RuntimeException(var4.getMessage());
                    }
                }
            };
            return FileUtils.listFiles(baseDir, fileFilter, TrueFileFilter.INSTANCE);
        }
    }

    private static File getBaseDir(Class<?> klass) throws InitializationError {
        URL klassUrl = klass.getResource(klass.getSimpleName() + ".class");

        try {
            return (new File(klassUrl.toURI())).getParentFile();
        }
        catch (URISyntaxException var3) {
            throw new InitializationError(
                    "Failed to determine directory of " + klass.getSimpleName() + ".class file: " + var3.getMessage());
        }
    }

    private static File getClassesDir(Class<?> klass) throws InitializationError {
        URL classesDirUrl = klass.getProtectionDomain().getCodeSource().getLocation();

        try {
            return new File(classesDirUrl.toURI());
        }
        catch (URISyntaxException var3) {
            throw new InitializationError(
                    "Failed to determine classes directory of " + klass.getName() + " class: " + var3.getMessage());
        }
    }

    private static Pattern convertWildcardPatternToRegex(String wildCardPattern) throws InitializationError {
        String s;
        for (s = wildCardPattern; s.contains("***"); s = s.replace("***", "**")) {
        }

        String suffix;
        if (s.endsWith("/**")) {
            s      = s.substring(0, s.length() - 3);
            suffix = "(.*)";
        }
        else {
            suffix = "";
        }

        s = s.replace(".", "[.]");
        s = s.replace("/**/", "/::/");
        s = s.replace("*", "([^/]*)");
        s = s.replace("/::/", "((/.*/)|(/))");
        s = s.replace("?", ".");
        if (s.contains("**")) {
            throw new InitializationError("Invalid wildcard pattern \"" + wildCardPattern + "\"");
        }
        else {
            return Pattern.compile(s + suffix);
        }
    }

    private static Class<?>[] union(Class<?>[] suiteClasses1, Class<?>[] suiteClasses2) {
        if (suiteClasses1 == null) {
            return suiteClasses2;
        }
        else if (suiteClasses2 == null) {
            return suiteClasses1;
        }
        else {
            HashSet<Class<?>> temp = new HashSet();
            temp.addAll(Arrays.asList(suiteClasses1));
            temp.addAll(Arrays.asList(suiteClasses2));
            Class<?>[] result = new Class[temp.size()];
            temp.toArray(result);
            return result;
        }
    }

    public OrderedWildPatternSuite(Class<?> klass, RunnerBuilder builder) throws InitializationError {
        super(builder, klass, getSuiteClasses(klass));
        Filter filter = CategoriesFilter.forTestSuite(klass);
        if (filter != null) {
            try {
                this.filter(filter);
            }
            catch (NoTestsRemainException var5) {
                throw new InitializationError(var5);
            }
        }
    }
}


