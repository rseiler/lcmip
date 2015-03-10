package at.rseiler.lcmip.jar.analyser;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static org.objectweb.asm.Opcodes.ASM5;

/**
 * Analyses the specified jar files and outputs all methods.
 *
 * @author Reinhard Seiler {@literal <rseiler.developer@gmail.com>}
 */
public class LcmipJarAnalyser {

    /**
     * The entry point.
     *
     * @param args the arguments which are the jar files
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        for (String arg : args) {
            ZipFile zipFile = new ZipFile(arg);
            zipFile.stream().forEach(zipEntry -> readClass(zipFile, zipEntry));
        }
    }

    /**
     * Reads the zip entry and if it's a class file extracts the binary of the class file and then outputs the methods.
     *
     * @param zipFile  the zip file
     * @param zipEntry the zip entry
     */
    private static void readClass(ZipFile zipFile, ZipEntry zipEntry) {
        try {
            if (zipEntry.getName().endsWith(".class")) {
                byte[] classFile = new byte[(int) zipEntry.getSize()];
                DataInputStream dataInputStream = new DataInputStream(zipFile.getInputStream(zipEntry));
                dataInputStream.readFully(classFile);
                logMethods(zipEntry.getName().substring(0, zipEntry.getName().length() - 6), classFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads the binary class file and outputs the methods.
     *
     * @param className the class name
     * @param classFile the class file
     */
    public static void logMethods(String className, byte[] classFile) {
        ClassReader classReader = new ClassReader(classFile);
        ClassVisitor classVisitor = new LcmipClassVisitor(ASM5, className);
        classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES);
    }

    /**
     * Visits all methods and outputs the visited methods.
     */
    private static class LcmipClassVisitor extends ClassVisitor {

        private final String className;

        public LcmipClassVisitor(int i, String className) {
            super(i);
            this.className = className.replaceAll("/", ".");
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            System.out.println(className + "#" + name);
            return super.visitMethod(access, name, desc, signature, exceptions);
        }

    }

}
