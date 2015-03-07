package at.rseiler.lcmip.javaagent;


import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.AdviceAdapter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.instrument.Instrumentation;

import static org.objectweb.asm.Opcodes.ASM5;

public class PreMain {

    public static void premain(String args, Instrumentation inst) throws IOException {
        if (args != null && !args.isEmpty()) {
            String[] options = args.split("#");
            MethodLogger.writer = new BufferedWriter(new FileWriter(options[0], true));
            String[] packages = options[1].replace('.', '/').split(";");

            inst.addTransformer((loader, className, classBeingRedefined, protectionDomain, classfileBuffer) -> {
                for (String aPackage : packages) {
                    if (className.startsWith(aPackage)) {
                        ClassReader classReader = new ClassReader(classfileBuffer);
                        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
                        ClassVisitor classVisitor = new LcmipClassVisitor(ASM5, classWriter, className);
                        classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES);
                        return classWriter.toByteArray();
                    }
                }

                return null;
            });
        }
    }

    private static class LcmipClassVisitor extends ClassVisitor {

        private final String className;

        public LcmipClassVisitor(int i, ClassVisitor classVisitor, String className) {
            super(i, classVisitor);
            this.className = className.replaceAll("/", ".");
        }

        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            if (cv == null) {
                return null;
            }

            MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
            return new MethodLogger(mv, access, name, desc);
        }

        class MethodLogger extends AdviceAdapter {

            private final String name;

            protected MethodLogger(MethodVisitor mv, int access, String name, String desc) {
                super(ASM5, mv, access, name, desc);
                this.name = name;
            }

            @Override
            protected void onMethodEnter() {
                super.visitLdcInsn(className + "#" + name);
                super.visitMethodInsn(INVOKESTATIC, "at/rseiler/lcmip/javaagent/MethodLogger", "log", "(Ljava/lang/String;)V", false);
            }
        }

    }

}