package at.rseiler.lcmip.jar.analyser;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static org.objectweb.asm.Opcodes.ASM5;

public class LcmipJarAnalyser {

    public static void main(String[] args) throws IOException {
        for (String arg : args) {
            ZipFile zipFile = new ZipFile(arg);
            zipFile.stream().forEach(zipEntry -> LcmipJarAnalyser.readClass(zipFile, zipEntry));
        }
    }

    private static void readClass(ZipFile zipFile, ZipEntry zipEntry) {
        try {
            if (zipEntry.getName().endsWith(".class")) {
                byte[] classfile = new byte[(int) zipEntry.getSize()];
                DataInputStream dataInputStream = new DataInputStream(zipFile.getInputStream(zipEntry));
                dataInputStream.readFully(classfile);
                logMethods(zipEntry.getName().substring(0, zipEntry.getName().length() - 6), classfile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void logMethods(String className, byte[] classfile) {
        ClassReader classReader = new ClassReader(classfile);
        ClassVisitor classVisitor = new LcmipClassVisitor(ASM5, className);
        classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES);
    }

    private static class LcmipClassVisitor extends ClassVisitor {

        private final String className;

        public LcmipClassVisitor(int i, String className) {
            super(i);
            this.className = className.replaceAll("/", ".");
        }

        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            System.out.println(className + "#" + name);
            return super.visitMethod(access, name, desc, signature, exceptions);
        }

    }

}
