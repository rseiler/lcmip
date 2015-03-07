package at.rseiler.lcmip.summary;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * This class generates the summary report. To do so it needs the report of "all methods" (generated by the
 * lcmip-jar-analyser) and the report of the "used methods" (generated by the lcmip-javaagent).
 * <p/>
 * The reports contains:
 * <ul>
 * <li>i/j unused classes (i: unused classes, j: total classes)</li>
 * <li>k/l unused methods (k: unused methods, l: total methods)</li>
 * <li>a list of all unused classes.</li>
 * <li>a list of all unused methods.</li>
 * </ul>
 * <p/>
 * Usage: java -jar lcmip-compare-1.0.jar ALL_METHODS_FILE USED_METHODS_FILE
 *
 * @author Reinhard Seiler {@literal <rseiler.developer@gmail.com>}
 */
public class LcmipSummary {

    /**
     * The entry point.
     *
     * @param args the arguments: ALL_METHODS_FILE USED_METHODS_FILE
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        if (args.length == 2) {
            Set<String> usedClasses = new HashSet<>();
            Set<String> usedMethods = new HashSet<>();
            Scanner scanner1 = new Scanner(new File(args[1]));

            // reads: used methods
            while (scanner1.hasNextLine()) {
                String classMethod = scanner1.nextLine().split(" ")[1];
                usedMethods.add(classMethod);
                usedClasses.add(getClassName(classMethod));
            }

            int totalMethodCount = 0;
            List<String> unusedMethods = new ArrayList<>();
            Set<String> unusedClasses = new HashSet<>();
            Set<String> allClasses = new HashSet<>();
            Scanner scanner2 = new Scanner(new File(args[0]));

            // reads: all methods
            while (scanner2.hasNextLine()) {
                totalMethodCount++;
                String line = scanner2.nextLine();

                if (!usedMethods.contains(line)) {
                    unusedMethods.add(line);
                }

                String className = getClassName(line);
                if (!usedClasses.contains(className)) {
                    unusedClasses.add(className);
                }

                allClasses.add(className);
            }

            // output
            System.out.println(String.format("unused classes: %d/%d", unusedClasses.size(), allClasses.size()));
            System.out.println(String.format("unused methods: %d/%d", unusedMethods.size(), totalMethodCount));
            System.out.println();
            System.out.println("unused classes");
            getSortedList(unusedClasses).stream().forEach(System.out::println);
            System.out.println();
            System.out.println("unused methods");
            getSortedList(unusedMethods).stream().forEach(System.out::println);
        } else {
            System.out.println("Usage: java -jar lcmip-compare-1.0.jar ALL_METHODS_FILE USED_METHODS_FILE");
        }
    }

    private static String getClassName(String value) {
        return value.split("#")[0];
    }

    private static List<String> getSortedList(Set<String> set) {
        String[] usedClassArray = set.toArray(new String[set.size()]);
        Arrays.sort(usedClassArray);
        return Arrays.asList(usedClassArray);
    }

    private static List<String> getSortedList(List<String> unusedMethods) {
        Collections.sort(unusedMethods);
        return unusedMethods;
    }

}
